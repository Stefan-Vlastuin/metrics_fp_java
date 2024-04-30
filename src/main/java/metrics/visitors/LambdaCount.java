package metrics.visitors;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class LambdaCount extends VoidVisitorAdapter<Void> {
    private int count = 0;

    @Override
    public void visit(LambdaExpr lambda, Void arg) {
        super.visit(lambda, arg);
        count++;
    }

    public int getCount() {
        return count;
    }
}
