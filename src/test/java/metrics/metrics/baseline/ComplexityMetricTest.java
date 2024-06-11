package metrics.metrics.baseline;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComplexityMetricTest {
    private static final String path = "src/test/resources/baseline/ComplexityClass.java";
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
        ComplexityMetric metric = new ComplexityMetric();
        // Complexity = 5: start with 1, add a loop, an if-statement, and an if-statement with 2 conditions
        assertEquals(5, metric.getResult(cu));
    }
}
