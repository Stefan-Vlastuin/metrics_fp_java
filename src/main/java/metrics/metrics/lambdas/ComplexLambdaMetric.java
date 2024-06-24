package metrics.metrics.lambdas;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.LambdaVisitor;

public class ComplexLambdaMetric implements Metric {
    LambdaVisitor visitor;

    public ComplexLambdaMetric(LambdaVisitor visitor){
        this.visitor = visitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return visitor.getComplexLambdaCount();
    }

    @Override
    public String getName() {
        return "ComplexLambda";
    }
}
