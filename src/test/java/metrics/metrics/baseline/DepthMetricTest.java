package metrics.metrics.baseline;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepthMetricTest {
    private static final String path = "src/test/resources/baseline/ChildClass.java";
    private static final String symbolSolverPath = "src/test/resources/baseline";
    private static CompilationUnit cu;

    @BeforeAll
    static void setup() throws FileNotFoundException {
        Setup.setupSymbolSolver(symbolSolverPath);
        File f = new File(path);
        cu = StaticJavaParser.parse(f);
    }

    @Test
    public void basicTest(){
        DepthMetric m = new DepthMetric();
        // Depth = 2: from Object to ParentClass to ChildClass
        assertEquals(2, m.getResult(cu));
    }
}
