package metrics.metrics;

import com.github.javaparser.ast.CompilationUnit;
import metrics.visitors.LambdaVisitor;

public class LambdaFieldVariableMetric implements Metric{
    LambdaVisitor visitor;

    public LambdaFieldVariableMetric(LambdaVisitor visitor){
        this.visitor = visitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return visitor.getLambdaCountFieldVariable();
    }

    @Override
    public String getName() {
        return "LambdaFieldVariable";
    }
}

