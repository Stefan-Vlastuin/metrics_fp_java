package metrics.metrics.referential_transparency;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.MethodPurityVisitor;

public class MethodSideEffectMetric implements Metric {
    private final MethodPurityVisitor visitor;

    public MethodSideEffectMetric(MethodPurityVisitor visitor){
        this.visitor = visitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return visitor.getCountMethodsSideEffect();
    }

    @Override
    public String getName() {
        return "MethodSideEffect";
    }
}
