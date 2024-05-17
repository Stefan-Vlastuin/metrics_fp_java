package metrics.metrics.fp_new;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.ImmutabilityVisitor;

public class FieldVariableRatioNonFinalMetric implements Metric{

    @Override
    public Number getResult(CompilationUnit cu) {
        ImmutabilityVisitor v = new ImmutabilityVisitor();
        v.visit(cu, null);
        return v.getNonFinalFieldVariablesRatio();
    }

    @Override
    public String getName() {
        return "FieldVariableRatioNonFinal";
    }
}
