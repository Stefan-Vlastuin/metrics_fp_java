package metrics.metrics;

import metrics.visitors.DepthVisitor;
import com.github.javaparser.ast.CompilationUnit;

public class DepthMetric implements Metric{
    @Override
    public Number getResult(CompilationUnit cu) {
        DepthVisitor depthVisitor = new DepthVisitor();
        depthVisitor.visit(cu, null);
        return depthVisitor.getDepth();
    }

    @Override
    public String getName() {
        return "Depth";
    }
}
