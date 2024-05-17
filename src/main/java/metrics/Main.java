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
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import metrics.metrics.baseline.*;
import metrics.metrics.fp_existing.*;
import metrics.metrics.fp_new.*;
import metrics.visitors.*;
import metrics.metrics.*;

public class Main {
    private static final String RESULT_PATH = "output/output.csv";
    private final static ProgressLogger logger = ProgressLogger.getInstance();
    
    public static void main(String[] args) {
        // TODO: each Java file is a compilation unit, so we now compute the metrics per file; do we need to do it per class?

        if (args.length != 1) {
            System.err.println("Usage: java Main <path>");
            System.exit(1);
        }
        String path = args[0];


        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        ParserConfiguration parserConfiguration = new ParserConfiguration()
                .setSymbolResolver(new JavaSymbolSolver(typeSolver)).setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_18);
        typeSolver.add(new JavaParserTypeSolver(new File(path), parserConfiguration));
        typeSolver.add(new ReflectionTypeSolver(false));
        StaticJavaParser.setConfiguration(parserConfiguration);

        ResultWriter resultWriter = null;
        try {
            List<CompilationUnit> compilationUnits = getCompilationUnits(new File(path));
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
                ResultCompilationUnit result = getResults(cu, getMetrics(compilationUnits, lambdaVisitor, streamVisitor, methodPurityVisitor));
                resultWriter.writeResult(result);
            }
        } catch (IOException e){
            logger.log(e);
        } finally {
            if (resultWriter != null){
                resultWriter.close();
            }
            logger.close();
        }
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

    private static List<Metric> getMetrics(List<CompilationUnit> compilationUnits, LambdaVisitor lambdaVisitor, StreamVisitor streamVisitor, MethodPurityVisitor methodPurityVisitor){
        return List.of(
                new LinesOfCodeMetric(),
                new ComplexityMetric(),
                new DepthMetric(),
                new ChildrenMetric(compilationUnits),
                new ResponseMetric(),
                new CohesionMetric(),
                new CouplingMetric(),
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
                new FieldVariableRatioNonFinalMetric()
        );
    }

    private static ResultCompilationUnit getResults(CompilationUnit cu, List<Metric> metrics){
        List<Number> numbers = metrics.stream().map(m -> m.getResult(cu)).toList();
        return new ResultCompilationUnit(cu.getStorage().orElseThrow().getFileName(), numbers);
    }

}