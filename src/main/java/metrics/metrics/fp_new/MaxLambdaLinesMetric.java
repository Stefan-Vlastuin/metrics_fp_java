package metrics.metrics.fp_new;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.LambdaVisitor;

public class MaxLambdaLinesMetric implements Metric {
    LambdaVisitor visitor;

    public MaxLambdaLinesMetric(LambdaVisitor visitor){
        this.visitor = visitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return visitor.getMaxLambdaLines();
    }

    @Override
    public String getName() {
        return "MaxLambdaLines";
    }
}
