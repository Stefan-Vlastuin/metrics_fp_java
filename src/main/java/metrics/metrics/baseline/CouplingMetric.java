package metrics.metrics.baseline;

import com.github.javaparser.ast.CompilationUnit;
import metrics.calculators.CouplingCalculator;
import metrics.metrics.Metric;

import java.util.List;

public class CouplingMetric implements Metric {
    private final List<CompilationUnit> allUnits;

    public CouplingMetric(List<CompilationUnit> allUnits){
        this.allUnits = allUnits;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return CouplingCalculator.getInstance(allUnits).getCoupling(cu);
    }

    @Override
    public String getName() {
        return "Coupling";
    }
}
