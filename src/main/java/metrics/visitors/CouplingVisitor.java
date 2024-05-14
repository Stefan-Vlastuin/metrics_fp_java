package metrics.visitors;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;
import metrics.ProgressLogger;

public class CouplingVisitor extends VoidVisitorAdapter<Void> {

    private String currentClass = "";
    private final Set<String> classes = new HashSet<>();
    private final ProgressLogger logger = ProgressLogger.getInstance();

    public CouplingVisitor(CompilationUnit cu){
        Optional<ClassOrInterfaceDeclaration> classOrInterfaceDeclaration = cu.findFirst(ClassOrInterfaceDeclaration.class);
        classOrInterfaceDeclaration.ifPresent(decl -> this.currentClass = decl.getNameAsString());
    }

    @Override
    public void visit(MethodCallExpr methodCall, Void arg) {
        super.visit(methodCall, arg);

        try {
            String declClass = methodCall.resolve().getClassName();
            if (!currentClass.equals(declClass)){
                classes.add(declClass);
            }
        } catch (Exception e){
            logger.log(e, "Could not resolve method call " + methodCall.getNameAsString() + " at " + methodCall.getRange());
        }
    }

    @Override
    public void visit(NameExpr nameExpr, Void arg) {
        super.visit(nameExpr, arg);

        try {
            ResolvedValueDeclaration resolvedValueDeclaration = nameExpr.resolve();
            if (resolvedValueDeclaration.isField()){
                String declClass = resolvedValueDeclaration.asField().declaringType().getClassName();
                if (!currentClass.equals(declClass)){
                    classes.add(declClass);
                }
            }
        } catch (Exception e){
            logger.log(e, "Could not resolve name expression " + nameExpr.getNameAsString() + " at " + nameExpr.getRange());
        }
    }

    public int getCoupling(){
        return classes.size();
    }
    
}
