package metrics.metrics.baseline;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseMetricTest {
    private static final String path = "src/test/resources/baseline/ClassA.java";
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
        ResponseMetric metric = new ResponseMetric();
        // Two method declarations, and three external calls (println for strings, println for ints, and compute)
        assertEquals(5, metric.getResult(cu));
    }

}
