package metrics.metrics;

import com.github.javaparser.ast.CompilationUnit;
import metrics.visitors.ResponseVisitor;

public class ResponseMetric implements Metric{

    @Override
    public Number getResult(CompilationUnit cu) {
        ResponseVisitor responseVisitor = new ResponseVisitor();
        responseVisitor.visit(cu, null);
        return responseVisitor.getResponse();
    }

    @Override
    public String getName() {
        return "Response";
    }
}
