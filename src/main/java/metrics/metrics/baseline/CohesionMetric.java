package metrics.metrics.baseline;

import metrics.metrics.Metric;
import metrics.visitors.CohesionVisitor;
import com.github.javaparser.ast.CompilationUnit;

public class CohesionMetric implements Metric {

    @Override
    public Number getResult(CompilationUnit cu) {
        CohesionVisitor cohesion = new CohesionVisitor();
        cohesion.visit(cu, null);
        return cohesion.getLackOfCohesion();
    }

    @Override
    public String getName() {
        return "Cohesion";
    }
}
