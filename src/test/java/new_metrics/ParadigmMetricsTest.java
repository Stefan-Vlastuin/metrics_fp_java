package new_metrics;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.baseline.Setup;
import metrics.metrics.paradigm.ParadigmAlternativeMetric;
import metrics.metrics.paradigm.ParadigmMetric;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParadigmMetricsTest {
    private static final String path = "src/test/resources/new_metrics/ParadigmClass.java";
    private static final String symbolSolverPath = "src/test/resources/new_metrics";
    private static CompilationUnit cu;

    @BeforeAll
    static void setup() throws FileNotFoundException {
        Setup.setupSymbolSolver(symbolSolverPath);
        File f = new File(path);
        cu = StaticJavaParser.parse(f);
    }

    @Test
    public void paradigmScoreTest() {
        ParadigmMetric metric = new ParadigmMetric();
        // 4 FP and 2 IP elements
        assertEquals((double) 2/6, metric.getResult(cu));
    }

    @Test
    public void paradigmScoreAltTest() {
        ParadigmAlternativeMetric metric = new ParadigmAlternativeMetric();
        // 4 FP and 2 IP elements
        assertEquals((double) 4/6, metric.getResult(cu));
    }

}
