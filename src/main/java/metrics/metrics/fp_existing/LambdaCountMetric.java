package metrics.metrics.fp_existing;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.LambdaVisitor;

public class LambdaCountMetric implements Metric {
    LambdaVisitor visitor;

    public LambdaCountMetric(LambdaVisitor visitor){
        this.visitor = visitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return visitor.getLambdaCount();
    }

    @Override
    public String getName() {
        return "LambdaCount";
    }
}
