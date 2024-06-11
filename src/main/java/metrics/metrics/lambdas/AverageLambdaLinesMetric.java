package metrics.metrics.lambdas;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.LambdaVisitor;

public class AverageLambdaLinesMetric implements Metric {
    LambdaVisitor visitor;

    public AverageLambdaLinesMetric(LambdaVisitor visitor){
        this.visitor = visitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return visitor.getAverageLambdaLines();
    }

    @Override
    public String getName() {
        return "AverageLambdaLines";
    }
}
