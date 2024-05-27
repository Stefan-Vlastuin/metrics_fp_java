package metrics.visitors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import metrics.ProgressLogger;

import java.util.Arrays;

public class SideEffectVisitor extends VoidVisitorAdapter<Range> {
    private boolean hasSideEffect = false;
    private final ProgressLogger logger = ProgressLogger.getInstance();

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

    private boolean hasSideEffect(Expression expr, Range scope){
        if (expr instanceof NameExpr nameExpr){
            try {
                ResolvedValueDeclaration decl = nameExpr.resolve();
                // If the variable is declared inside this scope, it is not a side effect.
                return !scope.contains(decl.toAst().orElseThrow().getRange().orElseThrow());
            } catch (Exception e){
                logger.log(e, "Could not resolve variable " + nameExpr.getNameAsString() + " at " + nameExpr.getRange());
            }
        }
        return false;
    }

    public boolean hasSideEffect() {
        return hasSideEffect;
    }

}
