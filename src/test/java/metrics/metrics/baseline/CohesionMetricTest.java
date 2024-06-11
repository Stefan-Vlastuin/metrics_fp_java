package metrics.metrics.baseline;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CohesionMetricTest {
    private static final String path = "src/test/resources/baseline/CohesionClass.java";
    private static final String symbolSolverPath = "src/test/resources/baseline";
    private static CompilationUnit cu;

    @BeforeAll
    static void setup() throws FileNotFoundException {
        Setup.setupSymbolSolver(symbolSolverPath);
        File f = new File(path);
        cu = StaticJavaParser.parse(f);
    }

    @Test
    public void basicTest() {
        CohesionMetric metric = new CohesionMetric();
        // There are 4 unrelated, and 2 related method pairs, so that gives 4-2=2
        assertEquals(2, metric.getResult(cu));
    }
}
