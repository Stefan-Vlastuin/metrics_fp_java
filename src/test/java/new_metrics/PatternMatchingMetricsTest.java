package new_metrics;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.baseline.Setup;
import metrics.metrics.fp_new.InstanceofMetric;
import metrics.metrics.fp_new.PatternMatchingMetric;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatternMatchingMetricsTest {
    private static final String path = "src/test/resources/new_metrics/PatternMatchingClass.java";
    private static final String symbolSolverPath = "src/test/resources/new_metrics";
    private static CompilationUnit cu;

    @BeforeAll
    static void setup() throws FileNotFoundException {
        Setup.setupSymbolSolver(symbolSolverPath);
        File f = new File(path);
        cu = StaticJavaParser.parse(f);
    }

    @Test
    public void instanceofTest() {
        InstanceofMetric metric = new InstanceofMetric();
        assertEquals(2, metric.getResult(cu));
    }

    @Test
    public void patternMatchingTest() {
        PatternMatchingMetric metric = new PatternMatchingMetric();
        assertEquals(1, metric.getResult(cu));
    }
}
