package metrics.visitors;

import java.util.ArrayList;
import java.util.Arrays;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.UnaryExpr.Operator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;

public class LambdaCountSideEffect extends VoidVisitorAdapter<Void> {
    private int count = 0;

    @Override
    public void visit(LambdaExpr lambda, Void arg) {
        super.visit(lambda, arg);

        ArrayList<Node> assignments = new ArrayList<>(); // All assignments done in the lambda
        Range lambdaRange = lambda.getRange().get();

        // Go over each assignment expression in the lambda
        lambda.findAll(AssignExpr.class).forEach(assignExpr -> {
            // Get declaration
            // We only look at assignments to field variables of the class itself; assignments with field accesses or array accesses are not counted
            Expression target = assignExpr.getTarget();
            if (target instanceof NameExpr){
                ResolvedValueDeclaration decl = ((NameExpr) target).resolve();

                // If the variable is not declared in the lambda
                if (!lambdaRange.contains(decl.toAst().get().getRange().get())){
                    assignments.add(assignExpr);
                }
            }
        });

        // Go over each unary expression in the lambda
        lambda.findAll(UnaryExpr.class).forEach(unaryExpr -> {
            // Check if it is ++ or --            
            if (Arrays.asList(Operator.PREFIX_INCREMENT, Operator.PREFIX_DECREMENT, Operator.POSTFIX_INCREMENT, Operator.POSTFIX_DECREMENT).contains(unaryExpr.getOperator())){
                // Get declaration
                Expression expr = unaryExpr.getExpression();
                if (expr instanceof NameExpr){
                    ResolvedValueDeclaration decl = ((NameExpr) expr).resolve();

                    // If the variable is not declared in the lambda
                    if (!lambdaRange.contains(decl.toAst().get().getRange().get())){
                        assignments.add(unaryExpr);
                    }
                }
            }
        });

        if (!assignments.isEmpty()){
            // At least one variable is assigned to in the lambda
            count++;
        }
    }

    public int getCount() {
        return count;
    }
}


