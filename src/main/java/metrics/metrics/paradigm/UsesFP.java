package metrics.metrics.paradigm;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.UsesFPVisitor;

public class UsesFP implements Metric {
    @Override
    public Number getResult(CompilationUnit cu) {
        UsesFPVisitor usesFPVisitor = new UsesFPVisitor();
        usesFPVisitor.visit(cu, null);
        return usesFPVisitor.usesFP() ? 1 : 0;
    }

    @Override
    public String getName() {
        return "UsesFP";
    }
}
