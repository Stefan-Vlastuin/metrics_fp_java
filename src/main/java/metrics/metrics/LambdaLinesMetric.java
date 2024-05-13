package metrics.metrics;

import com.github.javaparser.ast.CompilationUnit;
import metrics.visitors.LambdaVisitor;

public class LambdaLinesMetric implements Metric{
    LambdaVisitor visitor;

    public LambdaLinesMetric(LambdaVisitor visitor){
        this.visitor = visitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return visitor.getLambdaLines();
    }

    @Override
    public String getName() {
        return "LambdaLines";
    }
}

