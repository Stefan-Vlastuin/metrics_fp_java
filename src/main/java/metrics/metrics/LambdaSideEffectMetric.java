package metrics.metrics;

import com.github.javaparser.ast.CompilationUnit;
import metrics.visitors.LambdaVisitor;

public class LambdaSideEffectMetric implements Metric{
    LambdaVisitor visitor;

    public LambdaSideEffectMetric(LambdaVisitor visitor){
        this.visitor = visitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return visitor.getLambdaCountSideEffect();
    }

    @Override
    public String getName() {
        return "LambdaSideEffect";
    }
}

