package metrics.visitors;

import java.util.ArrayList;
import java.util.Optional;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;

public class LambdaCountField extends VoidVisitorAdapter<Void> {
    private int count = 0;

    @Override
    public void visit(LambdaExpr lambda, Void arg) {
        super.visit(lambda, arg);

        ArrayList<NameExpr> fieldVariables = new ArrayList<>(); // All field variables used in the lambda

        // Go over each variable used in the lambda
        lambda.findAll(NameExpr.class).forEach(nameExpr -> {
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
                // Apparently not a field variable: we do not have to do anything
            }
        });

        if (!fieldVariables.isEmpty()){
            // At least one mutable field variable is used in the lambda
            count++;
        }
    }

    public int getCount() {
        return count;
    }
}

