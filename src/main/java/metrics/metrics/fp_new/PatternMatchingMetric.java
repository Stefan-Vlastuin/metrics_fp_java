package metrics.metrics.fp_new;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.PatternMatchingVisitor;

public class PatternMatchingMetric implements Metric {
    @Override
    public Number getResult(CompilationUnit cu) {
        PatternMatchingVisitor v = new PatternMatchingVisitor();
        v.visit(cu, null);
        return v.getPatternCount();
    }

    @Override
    public String getName() {
        return "PatternMatching";
    }
}
