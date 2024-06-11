package new_metrics;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.baseline.Setup;
import metrics.metrics.fp_existing.StreamsCountMetric;
import metrics.metrics.fp_new.AverageStreamOperationsMetric;
import metrics.metrics.fp_new.MaxStreamOperationsMetric;
import metrics.metrics.fp_new.TotalStreamOperationsMetric;
import metrics.visitors.StreamVisitor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamMetricsTest {
    private static final String path = "src/test/resources/new_metrics/StreamClass.java";
    private static final String symbolSolverPath = "src/test/resources/new_metrics";
    private static CompilationUnit cu;
    private static StreamVisitor v;

    @BeforeAll
    static void setup() throws FileNotFoundException {
        Setup.setupSymbolSolver(symbolSolverPath);
        File f = new File(path);
        cu = StaticJavaParser.parse(f);
        v = new StreamVisitor();
        v.visit(cu, null);
    }

    @Test
    public void unterminatedStreamsTest() {
        StreamsCountMetric metric = new StreamsCountMetric(v);
        assertEquals(2, metric.getResult(cu));
    }

    @Test
    public void totalOperationsTest() {
        TotalStreamOperationsMetric metric = new TotalStreamOperationsMetric(v);
        assertEquals(6, metric.getResult(cu));
    }

    @Test
    public void avgOperationsTest() {
        AverageStreamOperationsMetric metric = new AverageStreamOperationsMetric(v);
        // 6 operations in 3 chains
        assertEquals(2.0, metric.getResult(cu));
    }

    @Test
    public void maxOperationsTest() {
        MaxStreamOperationsMetric metric = new MaxStreamOperationsMetric(v);
        assertEquals(3, metric.getResult(cu));
    }

}
