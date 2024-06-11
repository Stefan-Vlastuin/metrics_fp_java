package metrics.metrics.referential_transparency;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.MethodPurityVisitor;

public class MethodRatioSideEffectMetric implements Metric {
    private final MethodPurityVisitor visitor;

    public MethodRatioSideEffectMetric(MethodPurityVisitor visitor){
        this.visitor = visitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return visitor.getRatioMethodsSideEffect();
    }

    @Override
    public String getName() {
        return "MethodRatioSideEffect";
    }
}

