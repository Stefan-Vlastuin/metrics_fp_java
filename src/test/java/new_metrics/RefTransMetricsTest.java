package new_metrics;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.baseline.Setup;
import metrics.metrics.referential_transparency.*;
import metrics.visitors.MethodPurityVisitor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RefTransMetricsTest {
    private static final String path = "src/test/resources/new_metrics/RefTransClass.java";
    private static final String symbolSolverPath = "src/test/resources/new_metrics";
    private static CompilationUnit cu;
    private static MethodPurityVisitor v;

    @BeforeAll
    static void setup() throws FileNotFoundException {
        Setup.setupSymbolSolver(symbolSolverPath);
        File f = new File(path);
        cu = StaticJavaParser.parse(f);
        v = new MethodPurityVisitor();
        v.visit(cu, null);
    }

    @Test
    public void methodSideEffectTest() {
        MethodSideEffectMetric metric = new MethodSideEffectMetric(v);
        assertEquals(1, metric.getResult(cu));
    }

    @Test
    public void methodRatioSideEffectTest() {
        MethodRatioSideEffectMetric metric = new MethodRatioSideEffectMetric(v);
        assertEquals((double) 1/3, metric.getResult(cu));
    }

    @Test
    public void methodFieldVariableTest() {
        MethodFieldVariableMetric metric = new MethodFieldVariableMetric(v);
        assertEquals(2, metric.getResult(cu));
    }

    @Test
    public void methodRatioFieldVariableTest() {
        MethodRatioFieldVariableMetric metric = new MethodRatioFieldVariableMetric(v);
        assertEquals((double) 2/3, metric.getResult(cu));
    }

    @Test
    public void methodImpureTest() {
        MethodImpureMetric metric = new MethodImpureMetric(v);
        assertEquals(2, metric.getResult(cu));
    }

    @Test
    public void methodRatioImpureTest() {
        MethodRatioImpureMetric metric = new MethodRatioImpureMetric(v);
        assertEquals((double) 2/3, metric.getResult(cu));
    }

    @Test
    public void fieldVariableNonFinalTest() {
        FieldVariableNonFinalMetric metric = new FieldVariableNonFinalMetric();
        assertEquals(3, metric.getResult(cu));
    }

    @Test
    public void fieldVariableRatioNonFinalTest() {
        FieldVariableRatioNonFinalMetric metric = new FieldVariableRatioNonFinalMetric();
        assertEquals((double) 3/7, metric.getResult(cu));
    }

}
