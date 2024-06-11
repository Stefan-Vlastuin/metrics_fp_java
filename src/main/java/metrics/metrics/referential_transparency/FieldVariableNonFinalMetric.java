package metrics.metrics.referential_transparency;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.ImmutabilityVisitor;

public class FieldVariableNonFinalMetric implements Metric {
    @Override
    public Number getResult(CompilationUnit cu) {
        ImmutabilityVisitor v = new ImmutabilityVisitor();
        v.visit(cu, null);
        return v.getNonFinalFieldVariablesCount();
    }

    @Override
    public String getName() {
        return "FieldVariableNonFinal";
    }
}
