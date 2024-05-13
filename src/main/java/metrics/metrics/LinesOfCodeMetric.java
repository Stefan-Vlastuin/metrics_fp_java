package metrics.metrics;

import com.github.javaparser.ast.CompilationUnit;
import metrics.calculators.LinesOfCodeCalculator;

public class LinesOfCodeMetric implements Metric{
    @Override
    public Number getResult(CompilationUnit cu) {
        return LinesOfCodeCalculator.countSLOC(cu);
    }

    @Override
    public String getName() {
        return "LOC";
    }
}
