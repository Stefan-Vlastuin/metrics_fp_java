package metrics.metrics.baseline;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class LinesOfCodeMetricTest {
    private static final String path = "src/test/resources/baseline/ClassWithComments.java";
    private static CompilationUnit cu;

    @BeforeAll
    static void setup() throws FileNotFoundException {
        File f = new File(path);
        cu = StaticJavaParser.parse(f);
    }

    @Test
    public void basicTest() {
        LinesOfCodeMetric metric = new LinesOfCodeMetric();
        assertEquals(9, metric.getResult(cu));
    }

}