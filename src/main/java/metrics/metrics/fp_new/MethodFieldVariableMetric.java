package metrics.metrics.fp_new;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.MethodPurityVisitor;

public class MethodFieldVariableMetric implements Metric {
    private final MethodPurityVisitor visitor;

    public MethodFieldVariableMetric(MethodPurityVisitor visitor){
        this.visitor = visitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return visitor.getCountMethodsFieldVariable();
    }

    @Override
    public String getName() {
        return "MethodFieldVariable";
    }
}

