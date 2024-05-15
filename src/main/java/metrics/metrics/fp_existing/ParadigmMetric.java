package metrics.metrics.fp_existing;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.ParadigmVisitor;

public class ParadigmMetric implements Metric {
    @Override
    public Number getResult(CompilationUnit cu) {
        ParadigmVisitor paradigmVisitor = new ParadigmVisitor();
        paradigmVisitor.visit(cu, null);
        return paradigmVisitor.getScore();
    }

    @Override
    public String getName() {
        return "ParadigmScore";
    }
}
