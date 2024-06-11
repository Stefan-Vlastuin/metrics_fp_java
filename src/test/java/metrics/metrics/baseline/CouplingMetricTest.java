package metrics.metrics.baseline;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static metrics.Main.getCompilationUnits;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CouplingMetricTest {
    private static final String path = "src/test/resources/baseline/ClassA.java";
    private static final String symbolSolverPath = "src/test/resources/baseline";
    private static CompilationUnit cu;
    private static List<CompilationUnit> allUnits;

    @BeforeAll
    static void setup() throws FileNotFoundException {
        Setup.setupSymbolSolver(symbolSolverPath);
        allUnits = getCompilationUnits(new File(symbolSolverPath));
        File f = new File(path);
        cu = StaticJavaParser.parse(f);
    }

    @Test
    public void basicTest() {
        CouplingMetric metric = new CouplingMetric(allUnits);
        // ClassA is coupled to ClassB and ClassC
        assertEquals(2, metric.getResult(cu));
    }
}
