package metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import metrics.visitors.*;

public class Main {
    private static final String RESULT_PATH = "output/output.csv";
    private final static ProgressLogger LOGGER = new ProgressLogger(true);
    
    public static void main(String[] args) throws IOException {
        // TODO: each Java file is a compilation unit, so we now compute the metrics per file; do we need to do it per class?

        if (args.length != 1) {
            System.err.println("Usage: java Main <path>");
            System.exit(1);
        }
        String path = args[0];

        ReflectionTypeSolver typeSolver = new ReflectionTypeSolver();
        JavaParserTypeSolver javaParserTypeSolver = new JavaParserTypeSolver(new File(path));
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver(typeSolver, javaParserTypeSolver);

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(combinedTypeSolver);
        StaticJavaParser.getParserConfiguration().setSymbolResolver(symbolSolver).setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_18);

        List<CompilationUnit> compilationUnits = getCompilationUnits(new File(path));

        ResultWriter resultWriter = new ResultWriter(RESULT_PATH);
        for (CompilationUnit cu : compilationUnits){
            LOGGER.log("Working on file " + cu.getStorage().orElseThrow().getFileName());
            ResultCompilationUnit result = getResults(cu, compilationUnits);
            resultWriter.writeResult(result);
        }
        resultWriter.close();
    }

    private static List<CompilationUnit> getCompilationUnits(File path) throws FileNotFoundException{
        List<CompilationUnit> result = new ArrayList<>();

        for (File f : Objects.requireNonNull(path.listFiles())){
            if (f.isDirectory()){
                result.addAll(getCompilationUnits(f));
            } else if (f.getName().endsWith(".java")) {
                CompilationUnit cu = StaticJavaParser.parse(f);
                result.add(cu);
            }
        }

        return result;
    }

    private static ResultCompilationUnit getResults(CompilationUnit cu, List<CompilationUnit> allUnits){
        LexicalPreservingPrinter.setup(cu);
        SLOC SLOC = new SLOC();
        int linesOfCode = SLOC.countSLOC(LexicalPreservingPrinter.print(cu));

        Complexity complexity = new Complexity();
        complexity.visit(cu, null);
        int compl = complexity.getComplexity();

        DIT DIT = new DIT();
        DIT.visit(cu, null);
        int depth = DIT.getDepth();

        NOC NOC = new NOC(allUnits);
        NOC.visit(cu, null);
        int numberOfChildren = NOC.getNOC();

        Response response = new Response();
        response.visit(cu, null);
        int resp = response.getResponse();

        LackOfCohesion lackOfCohesion = new LackOfCohesion();
        lackOfCohesion.visit(cu, null);
        int cohesion = lackOfCohesion.getLackOfCohesion();

        Coupling coupling = new Coupling(cu);
        coupling.visit(cu, null);
        int coupl = coupling.getCoupling();

        LambdaCount lambdaCount = new LambdaCount();
        lambdaCount.visit(cu, null);
        int count = lambdaCount.getCount();

        LambdaLinesCount lambdaLinesCount = new LambdaLinesCount();
        lambdaLinesCount.visit(cu, null);
        int lambdaLines = lambdaLinesCount.getCount();

        double ratio = (double) lambdaLines / linesOfCode;

        LambdaCountField lambdaCountField = new LambdaCountField();
        lambdaCountField.visit(cu, null);
        int countField = lambdaCountField.getCount();

        LambdaCountSideEffect lambdaCountSideEffect = new LambdaCountSideEffect();
        lambdaCountSideEffect.visit(cu, null);
        int countSideEffect = lambdaCountSideEffect.getCount();

        StreamsCount streamsCount = new StreamsCount();
        streamsCount.visit(cu, null);
        int nrStreams = streamsCount.getCount();

        ParadigmScore paradigmScore = new ParadigmScore();
        paradigmScore.visit(cu, null);
        double parScore = paradigmScore.getScore();

        return new ResultCompilationUnit(cu.getStorage().orElseThrow().getFileName(), linesOfCode, compl, depth, numberOfChildren, resp, cohesion, coupl, count, lambdaLines, ratio, countField, countSideEffect, nrStreams, parScore);
    }
}