package metrics.visitors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import metrics.ProgressLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class LambdaVisitor extends VoidVisitorAdapter<Void> {
    private int count = 0;
    private int linesCount = 0;
    private int fieldVariableCount = 0;
    private int sideEffectCount = 0;
    private final ProgressLogger logger = ProgressLogger.getInstance();

    @Override
    public void visit(LambdaExpr lambda, Void arg) {
        super.visit(lambda, arg);
        count++;

        Range range = lambda.getRange().orElseThrow();
        int lines = range.end.line - range.begin.line + 1;
        linesCount += lines;

        if (usesFieldVariable(lambda)){
            fieldVariableCount++;
        }

        if (hasSideEffect(lambda)){
            sideEffectCount++;
        }
    }

    private boolean usesFieldVariable(Node n){
        ArrayList<NameExpr> fieldVariables = new ArrayList<>(); // All field variables used

        // Go over each variable used
        n.findAll(NameExpr.class).forEach(nameExpr -> {
            try {
                // Get declaration
                ResolvedValueDeclaration decl = nameExpr.resolve();
                Optional<Node> declNode = decl.toAst();

                if (declNode.isPresent() && declNode.get() instanceof FieldDeclaration fieldDeclaration){ // Declaration is a declaration of a field variable

                    // If it is not final, add to list of used field variables
                    if (!fieldDeclaration.isFinal()){
                        fieldVariables.add(nameExpr);
                    }
                }
            } catch (UnsolvedSymbolException e){
                logger.log(e, "Could not resolve variable " + nameExpr.getNameAsString() + " at " + nameExpr.getRange());
            }
        });

        return !fieldVariables.isEmpty();
    }

    private boolean hasSideEffect(Node n){
        ArrayList<Node> assignments = new ArrayList<>(); // All assignments done

        Range range = n.getRange().orElseThrow();

        // Go over each assignment expression
        n.findAll(AssignExpr.class).forEach(assignExpr -> {
            // Get declaration
            // We only look at assignments to field variables of the class itself; assignments with field accesses or array accesses are not counted
            Expression target = assignExpr.getTarget();
            if (target instanceof NameExpr nameExpr){
                ResolvedValueDeclaration decl = nameExpr.resolve();

                // If the variable is not declared in the lambda
                if (!range.contains(decl.toAst().orElseThrow().getRange().orElseThrow())){
                    assignments.add(assignExpr);
                }
            }
        });

        // Go over each unary expression
        n.findAll(UnaryExpr.class).forEach(unaryExpr -> {
            // Check if it is ++ or --
            if (Arrays.asList(UnaryExpr.Operator.PREFIX_INCREMENT, UnaryExpr.Operator.PREFIX_DECREMENT, UnaryExpr.Operator.POSTFIX_INCREMENT, UnaryExpr.Operator.POSTFIX_DECREMENT).contains(unaryExpr.getOperator())){
                // Get declaration
                Expression expr = unaryExpr.getExpression();
                if (expr instanceof NameExpr nameExpr){
                    ResolvedValueDeclaration decl = nameExpr.resolve();

                    // If the variable is not declared in the lambda
                    if (!range.contains(decl.toAst().orElseThrow().getRange().orElseThrow())){
                        assignments.add(unaryExpr);
                    }
                }
            }
        });

        return !assignments.isEmpty();
    }

    public int getLambdaCount(){
        return count;
    }

    public int getLambdaLines(){
        return linesCount;
    }

    public int getLambdaCountFieldVariable(){
        return fieldVariableCount;
    }

    public int getLambdaCountSideEffect(){
        return sideEffectCount;
    }

}
