package metrics.visitors;

import java.util.HashSet;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import metrics.ProgressLogger;

public class CouplingVisitor extends VoidVisitorAdapter<Void> {

    private final Set<CompilationUnit> classes = new HashSet<>();
    private final ProgressLogger logger = ProgressLogger.getInstance();

    @Override
    public void visit(MethodCallExpr methodCall, Void arg) {
        super.visit(methodCall, arg);

        try {
            CompilationUnit cu = methodCall.resolve().toAst().orElseThrow().findCompilationUnit().orElseThrow();
            classes.add(cu);
        } catch (Exception e){
            logger.log(e, "Could not resolve method call " + methodCall.getNameAsString() + " at " + methodCall.getRange());
        }
    }

    @Override
    public void visit(FieldAccessExpr fieldAccessExpr, Void arg) {
        super.visit(fieldAccessExpr, arg);

        try {
            ResolvedValueDeclaration resolvedValueDeclaration = fieldAccessExpr.resolve();
            if (resolvedValueDeclaration.isField()){
                CompilationUnit cu = resolvedValueDeclaration.toAst().orElseThrow().findCompilationUnit().orElseThrow();
                classes.add(cu);
            }
        } catch (Exception e){
            logger.log(e, "Could not resolve name expression " + fieldAccessExpr.getNameAsString() + " at " + fieldAccessExpr.getRange());
        }
    }

    public Set<CompilationUnit> getUsedClasses(){
        return classes;
    }

}
