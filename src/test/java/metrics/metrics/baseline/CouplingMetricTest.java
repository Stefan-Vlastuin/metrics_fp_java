package metrics.metrics.baseline;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

public class CouplingMetricTest {
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
        CouplingMetric metric = new CouplingMetric();
        // ClassA is coupled to ClassB, ClassC, and the class containing the println method
        //assertEquals(3, metric.getResult(cu));
    }
}
