package metrics.visitors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class LambdaLinesCount extends VoidVisitorAdapter<Void> {
    private int count = 0;

    @Override
    public void visit(LambdaExpr lambda, Void arg) {
        super.visit(lambda, arg);
        Range range = lambda.getRange().get();
        int lines = range.end.line - range.begin.line + 1;
        count += lines;
    }

    public int getCount() {
        return count;
    }
}
