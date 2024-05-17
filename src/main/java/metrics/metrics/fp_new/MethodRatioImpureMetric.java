package metrics.metrics.fp_new;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.MethodPurityVisitor;

public class MethodRatioImpureMetric implements Metric {
    private final MethodPurityVisitor visitor;

    public MethodRatioImpureMetric(MethodPurityVisitor visitor){
        this.visitor = visitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return visitor.getRatioMethodsImpure();
    }

    @Override
    public String getName() {
        return "MethodRatioImpure";
    }
}

