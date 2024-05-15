package metrics.metrics.fp_new;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.LambdaVisitor;
import metrics.visitors.MethodReferenceVisitor;

public class HigherOrderFieldVariableMetric implements Metric {
    LambdaVisitor lambdaVisitor;

    public HigherOrderFieldVariableMetric(LambdaVisitor lambdaVisitor){
        this.lambdaVisitor = lambdaVisitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        MethodReferenceVisitor methodReferenceVisitor = new MethodReferenceVisitor();
        methodReferenceVisitor.visit(cu, null);
        return methodReferenceVisitor.getMethodReferenceCountFieldVariable() + lambdaVisitor.getLambdaCountFieldVariable();
    }

    @Override
    public String getName() {
        return "HigherOrderFieldVariable";
    }
}
