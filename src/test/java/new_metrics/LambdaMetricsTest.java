package new_metrics;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.baseline.Setup;
import metrics.metrics.fp_existing.*;
import metrics.metrics.fp_new.*;
import metrics.visitors.LambdaVisitor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LambdaMetricsTest {
    private static final String path = "src/test/resources/new_metrics/LambdaClass.java";
    private static final String symbolSolverPath = "src/test/resources/new_metrics";
    private static CompilationUnit cu;
    private static LambdaVisitor v;

    @BeforeAll
    static void setup() throws FileNotFoundException {
        Setup.setupSymbolSolver(symbolSolverPath);
        File f = new File(path);
        cu = StaticJavaParser.parse(f);
        v = new LambdaVisitor();
        v.visit(cu, null);
    }

    @Test
    public void lambdaCountTest() {
        LambdaCountMetric metric = new LambdaCountMetric(v);
        assertEquals(4, metric.getResult(cu));
    }

    @Test
    public void lambdaSideEffectTest() {
        LambdaSideEffectMetric metric = new LambdaSideEffectMetric(v);
        assertEquals(1, metric.getResult(cu));
    }

    @Test
    public void higherOrderSideEffectTest() {
        HigherOrderSideEffectMetric metric = new HigherOrderSideEffectMetric(v);
        assertEquals(2, metric.getResult(cu));
    }

    @Test
    public void lambdaFieldVariableTest() {
        LambdaFieldVariableMetric metric = new LambdaFieldVariableMetric(v);
        assertEquals(2, metric.getResult(cu));
    }

    @Test
    public void higherOrderFieldVariableTest() {
        HigherOrderFieldVariableMetric metric = new HigherOrderFieldVariableMetric(v);
        assertEquals(4, metric.getResult(cu));
    }

    @Test
    public void lambdaLinesTest() {
        LambdaLinesMetric metric = new LambdaLinesMetric(v);
        assertEquals(13, metric.getResult(cu));
    }

    @Test
    public void averageLambdaLinesTest() {
        AverageLambdaLinesMetric metric = new AverageLambdaLinesMetric(v);
        // 13 lines over 4 expressions
        assertEquals((double)13/4, metric.getResult(cu));
    }

    @Test
    public void maxLambdaLinesTest() {
        MaxLambdaLinesMetric metric = new MaxLambdaLinesMetric(v);
        assertEquals(7, metric.getResult(cu));
    }

    @Test
    public void lambdaScoreTest() {
        LambdaScoreMetric metric = new LambdaScoreMetric(v);
        // 50 lines with 13 lambda lines
        assertEquals((double) 13/50, metric.getResult(cu));
    }

}
