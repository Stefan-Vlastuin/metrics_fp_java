package metrics.metrics.fp_new;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.MethodPurityVisitor;

public class MethodRatioFieldVariableMetric implements Metric {
    private final MethodPurityVisitor visitor;

    public MethodRatioFieldVariableMetric(MethodPurityVisitor visitor){
        this.visitor = visitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return visitor.getRatioMethodsFieldVariable();
    }

    @Override
    public String getName() {
        return "MethodRatioFieldVariable";
    }
}

