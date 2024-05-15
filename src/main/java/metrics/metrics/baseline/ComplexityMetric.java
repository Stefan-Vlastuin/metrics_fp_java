package metrics.metrics.baseline;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.ComplexityVisitor;

public class ComplexityMetric implements Metric {
    @Override
    public Number getResult(CompilationUnit cu) {
        ComplexityVisitor complexityVisitor = new ComplexityVisitor();
        complexityVisitor.visit(cu, null);
        return complexityVisitor.getComplexity();
    }

    @Override
    public String getName() {
        return "Complexity";
    }
}
