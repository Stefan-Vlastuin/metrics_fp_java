package metrics.metrics;

import com.github.javaparser.ast.CompilationUnit;
import metrics.visitors.ChildrenVisitor;

import java.util.List;

public class ChildrenMetric implements Metric{
    List<CompilationUnit> allUnits;

    public ChildrenMetric(List<CompilationUnit> allUnits){
        this.allUnits = allUnits;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        ChildrenVisitor childrenVisitor = new ChildrenVisitor(allUnits);
        childrenVisitor.visit(cu, null);
        return childrenVisitor.getNOC();
    }

    @Override
    public String getName() {
        return "Children";
    }
}
