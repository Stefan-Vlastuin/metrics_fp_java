package metrics.visitors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import metrics.ProgressLogger;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class FieldVariableVisitor extends VoidVisitorAdapter<Void> {
    private final ProgressLogger logger = ProgressLogger.getInstance();
    private final Set<FieldDeclaration> fieldVariables = new HashSet<>();

    @Override
    public void visit(NameExpr nameExpr, Void arg) {
        super.visit(nameExpr, arg);

        try {
            ResolvedValueDeclaration decl = nameExpr.resolve();
            Optional<Node> declNode = decl.toAst();

            if (declNode.isPresent() && declNode.get() instanceof FieldDeclaration fieldDeclaration){
                fieldVariables.add(fieldDeclaration);
            }
        } catch (Exception e){
            logger.log(e, "Could not resolve variable " + nameExpr.getNameAsString() + " at " + nameExpr.getRange());
        }

    }

    public Set<FieldDeclaration> getFieldVariables() {
        return fieldVariables;
    }

    public Set<FieldDeclaration> getNonFinalFieldVariables(){
        return fieldVariables.stream().filter(v -> !v.isFinal()).collect(Collectors.toSet());
    }

    public boolean usesNonFinalFieldVariable(){
        return !getNonFinalFieldVariables().isEmpty();
    }
}
