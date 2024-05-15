package metrics.metrics.baseline;

import com.github.javaparser.ast.CompilationUnit;
import metrics.calculators.LinesOfCodeCalculator;
import metrics.metrics.Metric;

public class LinesOfCodeMetric implements Metric {
    @Override
    public Number getResult(CompilationUnit cu) {
        return LinesOfCodeCalculator.countSLOC(cu);
    }

    @Override
    public String getName() {
        return "LOC";
    }
}
