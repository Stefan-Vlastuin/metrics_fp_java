package metrics.metrics.fp_new;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.LambdaVisitor;
import metrics.visitors.MethodReferenceVisitor;

public class HigherOrderSideEffectMetric implements Metric {
    LambdaVisitor lambdaVisitor;

    public HigherOrderSideEffectMetric(LambdaVisitor lambdaVisitor){
        this.lambdaVisitor = lambdaVisitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        MethodReferenceVisitor methodReferenceVisitor = new MethodReferenceVisitor();
        methodReferenceVisitor.visit(cu, null);
        return methodReferenceVisitor.getMethodReferenceCountSideEffect() + lambdaVisitor.getLambdaCountSideEffect();
    }

    @Override
    public String getName() {
        return "HigherOrderSideEffect";
    }
}
