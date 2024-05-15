package metrics.metrics.baseline;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.ResponseVisitor;

public class ResponseMetric implements Metric {

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
