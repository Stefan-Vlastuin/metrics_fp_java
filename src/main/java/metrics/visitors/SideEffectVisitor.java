package metrics.visitors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import java.util.Arrays;

public class SideEffectVisitor extends VoidVisitorAdapter<Range> {
    private boolean hasSideEffect = false;

    @Override
    public void visit(AssignExpr assignExpr, Range scope) {
        super.visit(assignExpr, scope);

        if (hasSideEffect(assignExpr.getTarget(), scope)){
            hasSideEffect = true;
        }
    }

    @Override
    public void visit(UnaryExpr unaryExpr, Range scope) {
        super.visit(unaryExpr, scope);

        if (Arrays.asList(UnaryExpr.Operator.PREFIX_INCREMENT, UnaryExpr.Operator.PREFIX_DECREMENT, UnaryExpr.Operator.POSTFIX_INCREMENT, UnaryExpr.Operator.POSTFIX_DECREMENT).contains(unaryExpr.getOperator())){
            if (hasSideEffect(unaryExpr.getExpression(), scope)){
                hasSideEffect = true;
            }
        }
    }

    private boolean hasSideEffect(Expression e, Range scope){
        if (e instanceof NameExpr nameExpr){
            ResolvedValueDeclaration decl = nameExpr.resolve();
            // If the variable is declared inside this scope, it is not a side effect.
            return !scope.contains(decl.toAst().orElseThrow().getRange().orElseThrow());
        }
        return false;
    }

    public boolean hasSideEffect() {
        return hasSideEffect;
    }



}
