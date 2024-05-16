package metrics.visitors;

import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.PatternExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class PatternMatchingVisitor extends VoidVisitorAdapter<Void> {
    private int patternCount = 0;
    private int instanceofCount = 0;

    @Override
    public void visit(PatternExpr patternExpr, Void arg) {
        super.visit(patternExpr, arg);
        patternCount++;
    }

    @Override
    public void visit(InstanceOfExpr instanceOfExpr, Void arg) {
        super.visit(instanceOfExpr, arg);
        instanceofCount++;
    }

    public int getPatternCount(){
        return patternCount;
    }

    public int getInstanceofCount(){
        return instanceofCount;
    }

}
