package metrics.visitors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import metrics.ProgressLogger;

public class MethodReferenceVisitor extends VoidVisitorAdapter<Void> {
    private int fieldVariableCount = 0;
    private int sideEffectCount = 0;
    private final ProgressLogger logger = ProgressLogger.getInstance();

    @Override
    public void visit (MethodReferenceExpr methodReference, Void arg){
        super.visit(methodReference, arg);

        try {
            ResolvedMethodDeclaration resolvedMethodDeclaration = methodReference.resolve();
            if (resolvedMethodDeclaration.toAst().isPresent()){
                Node n = resolvedMethodDeclaration.toAst().get();
                if (usesFieldVariable(n)){
                    logger.log("HOF using field variable: " + methodReference.getIdentifier());
                    fieldVariableCount++;
                }

                if (hasSideEffect(n)){
                    sideEffectCount++;
                }
            }
        } catch (UnsolvedSymbolException e){
            logger.log(e, "Could not resolve method reference " + methodReference.getIdentifier() + " at " + methodReference.getRange());
        }
    }

    private boolean usesFieldVariable(Node n){
        FieldVariableVisitor v = new FieldVariableVisitor();
        n.accept(v, null);
        return v.usesNonFinalFieldVariable();
    }

    private boolean hasSideEffect(Node n){
        SideEffectVisitor v = new SideEffectVisitor();
        n.accept(v, n.getRange().orElseThrow());
        return v.hasSideEffect();
    }

    public int getMethodReferenceCountFieldVariable(){
        return fieldVariableCount;
    }

    public int getMethodReferenceCountSideEffect(){
        return sideEffectCount;
    }

}
