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

public class NumberOfChildrenMetricTest {
    private static final String path = "src/test/resources/baseline/ParentClass.java";
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
    public void basicTest(){
        ChildrenMetric m = new ChildrenMetric(allUnits);
        assertEquals(1, m.getResult(cu));
    }
}
