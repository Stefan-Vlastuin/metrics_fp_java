package metrics.metrics.referential_transparency;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.MethodPurityVisitor;

public class MethodImpureMetric implements Metric {
    private final MethodPurityVisitor visitor;

    public MethodImpureMetric(MethodPurityVisitor visitor){
        this.visitor = visitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return visitor.getCountMethodsImpure();
    }

    @Override
    public String getName() {
        return "MethodImpure";
    }
}

