package metrics.metrics;

import com.github.javaparser.ast.CompilationUnit;
import metrics.visitors.CouplingVisitor;

public class CouplingMetric implements Metric {

    @Override
    public Number getResult(CompilationUnit cu) {
        CouplingVisitor couplingVisitor = new CouplingVisitor(cu);
        couplingVisitor.visit(cu, null);
        return couplingVisitor.getCoupling();
    }

    @Override
    public String getName() {
        return "Coupling";
    }
}
