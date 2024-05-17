package metrics.metrics.fp_new;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.LambdaVisitor;

public class LambdaComplexityMetric implements Metric {
    LambdaVisitor visitor;

    public LambdaComplexityMetric(LambdaVisitor visitor){
        this.visitor = visitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return visitor.getLambdaComplexity();
    }

    @Override
    public String getName() {
        return "LambdaComplexity";
    }
}
