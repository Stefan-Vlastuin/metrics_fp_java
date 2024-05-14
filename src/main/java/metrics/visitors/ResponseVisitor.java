package metrics.visitors;

import java.util.HashSet;
import java.util.Set;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import metrics.ProgressLogger;

public class ResponseVisitor extends VoidVisitorAdapter<Void> {

    // Storing the ResolvedMethodDeclaration leads to duplicates in the set,
    // so we store the string representation.
    private final Set<String> methods = new HashSet<>();
    private final ProgressLogger logger = ProgressLogger.getInstance();

    @Override
    public void visit(MethodDeclaration method, Void arg) {
        super.visit(method, arg);

        try {
            methods.add(method.resolve().toString());
        } catch (Exception e){
            logger.log(e, "Could not resolve method declaration " + method.getNameAsString() + " at " + method.getRange());
        }
    }

    @Override
    public void visit(MethodCallExpr methodCall, Void arg) {
        super.visit(methodCall, arg);
        
        try {
            methods.add(methodCall.resolve().toString());
        } catch (Exception e){
            logger.log(e, "Could not resolve method call " + methodCall.getNameAsString() + " at " + methodCall.getRange());
        }
    }

    public int getResponse(){
        return methods.size();
    }
    
}
