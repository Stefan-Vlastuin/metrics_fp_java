package metrics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import metrics.metrics.baseline.*;
import metrics.metrics.lambdas.*;
import metrics.metrics.paradigm.ParadigmAlternativeMetric;
import metrics.metrics.paradigm.ParadigmMetric;
import metrics.metrics.paradigm.UsesFP;
import metrics.metrics.pattern_matching.InstanceofMetric;
import metrics.metrics.pattern_matching.PatternMatchingMetric;
import metrics.metrics.referential_transparency.*;
import metrics.metrics.streams.AverageStreamOperationsMetric;
import metrics.metrics.streams.MaxStreamOperationsMetric;
import metrics.metrics.streams.StreamsCountMetric;
import metrics.metrics.streams.TotalStreamOperationsMetric;
import metrics.visitors.*;
import metrics.metrics.*;

public class Main {
    private static final String RESULT_PATH = "output/output.csv";
    private final static ProgressLogger logger = ProgressLogger.getInstance();
    
    public static void main(String[] args) {
        List<String> srcPaths = new ArrayList<>();
        String basePath = "";
        List<String> filesToIgnore = new ArrayList<>();
        if (args.length == 2) {
            srcPaths = Arrays.asList(args[0].split(","));
            basePath = args[1];
        } else if (args.length == 3){
            srcPaths = Arrays.asList(args[0].split(","));
            basePath = args[1];
            filesToIgnore = Arrays.asList(args[2].split(","));
        } else {
            System.err.println("Usage: java Main <srcPaths> <basePath> <ignore>");
            System.exit(1);
        }

        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        ParserConfiguration parserConfiguration = new ParserConfiguration()
                .setSymbolResolver(new JavaSymbolSolver(typeSolver)).setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_18);
        for (String srcPath : srcPaths){
            typeSolver.add(new JavaParserTypeSolver(new File(srcPath), parserConfiguration));
        }
        typeSolver.add(new ReflectionTypeSolver(false));
        StaticJavaParser.setConfiguration(parserConfiguration);

        ResultWriter resultWriter = null;
        int totalLOC = 0;
        try {
            List<CompilationUnit> compilationUnits = getCompilationUnits(srcPaths);
            compilationUnits = removeFiles(compilationUnits, filesToIgnore);
            LambdaVisitor lambdaVisitor = new LambdaVisitor();
            StreamVisitor streamVisitor = new StreamVisitor();
            MethodPurityVisitor methodPurityVisitor = new MethodPurityVisitor();

            List<String> names = new ArrayList<>(getMetrics(compilationUnits, lambdaVisitor, streamVisitor, methodPurityVisitor).stream().map(Metric::getName).toList());
            names.add(0, "FileName");
            resultWriter = new ResultWriter(RESULT_PATH, names);

            for (CompilationUnit cu : compilationUnits){
                logger.log("Working on file " + cu.getStorage().orElseThrow().getFileName());
                lambdaVisitor = new LambdaVisitor();
                lambdaVisitor.visit(cu, null); // We do this here, so that it is done only once for all lambda metrics.
                streamVisitor = new StreamVisitor();
                streamVisitor.visit(cu, null);
                methodPurityVisitor = new MethodPurityVisitor();
                methodPurityVisitor.visit(cu, null);
                ResultCompilationUnit result = getResults(cu, getMetrics(compilationUnits, lambdaVisitor, streamVisitor, methodPurityVisitor), Paths.get(basePath));
                totalLOC += (int) result.numbers().get(0); // We assume the first number is the LOC
                resultWriter.writeResult(result);
            }
        } catch (IOException e){
            logger.log(e);
        } finally {
            if (resultWriter != null){
                resultWriter.close();
            }
            logger.close();
            System.out.println("Total LOC: " + totalLOC);
        }
    }

    private static List<CompilationUnit> getCompilationUnits(List<String> srcPaths) throws FileNotFoundException {
        List<CompilationUnit> result = new ArrayList<>();
        for (String srcPath : srcPaths){
            result.addAll(getCompilationUnits(new File(srcPath)));
        }
        return result;
    }

    public static List<CompilationUnit> getCompilationUnits(File path) throws FileNotFoundException{
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

    private static List<Metric> getMetrics(List<CompilationUnit> compilationUnits, LambdaVisitor lambdaVisitor, StreamVisitor streamVisitor, MethodPurityVisitor methodPurityVisitor){
        return List.of(
                new LinesOfCodeMetric(), // Needs to be the first (for the computation of total LOC of the project)!
                new ComplexityMetric(),
                new DepthMetric(),
                new ChildrenMetric(compilationUnits),
                new ResponseMetric(),
                new CohesionMetric(),
                new CouplingMetric(compilationUnits),
                new LambdaCountMetric(lambdaVisitor),
                new LambdaLinesMetric(lambdaVisitor),
                new AverageLambdaLinesMetric(lambdaVisitor),
                new MaxLambdaLinesMetric(lambdaVisitor),
                new LambdaScoreMetric(lambdaVisitor),
                new LambdaComplexityMetric(lambdaVisitor),
                new LambdaFieldVariableMetric(lambdaVisitor),
                new HigherOrderFieldVariableMetric(lambdaVisitor),
                new LambdaSideEffectMetric(lambdaVisitor),
                new HigherOrderSideEffectMetric(lambdaVisitor),
                new StreamsCountMetric(streamVisitor),
                new TotalStreamOperationsMetric(streamVisitor),
                new AverageStreamOperationsMetric(streamVisitor),
                new MaxStreamOperationsMetric(streamVisitor),
                new ParadigmMetric(),
                new ParadigmAlternativeMetric(),
                new PatternMatchingMetric(),
                new InstanceofMetric(),
                new MethodSideEffectMetric(methodPurityVisitor),
                new MethodFieldVariableMetric(methodPurityVisitor),
                new MethodImpureMetric(methodPurityVisitor),
                new MethodRatioSideEffectMetric(methodPurityVisitor),
                new MethodRatioFieldVariableMetric(methodPurityVisitor),
                new MethodRatioImpureMetric(methodPurityVisitor),
                new FieldVariableNonFinalMetric(),
                new FieldVariableRatioNonFinalMetric(),
                new ComplexLambdaMetric(lambdaVisitor),
                new UsesFP()
        );
    }

    private static ResultCompilationUnit getResults(CompilationUnit cu, List<Metric> metrics, Path basePath){
        List<Number> numbers = metrics.stream().map(m -> m.getResult(cu)).toList();
        Path relativePath = basePath.relativize(cu.getStorage().orElseThrow().getPath());
        return new ResultCompilationUnit(relativePath.toString(), numbers);
    }

    private static List<CompilationUnit> removeFiles(List<CompilationUnit> units, List<String> toIgnore){
        return units.stream().filter(cu -> !toIgnore.contains(cu.getStorage().orElseThrow().getFileName())).toList();
    }

}