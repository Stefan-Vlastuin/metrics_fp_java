package metrics;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class Coupling extends VoidVisitorAdapter<Void> {

    private String currentClass = "";
    private Set<String> classes = new HashSet<>();

    public Coupling (CompilationUnit cu){
        Optional<ClassOrInterfaceDeclaration> classOrInterfaceDeclaration = cu.findFirst(ClassOrInterfaceDeclaration.class);
        if (classOrInterfaceDeclaration.isPresent()){
            this.currentClass = classOrInterfaceDeclaration.get().getNameAsString();
        }
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
            // Just ignore this method
        }
    }

    @Override
    public void visit(NameExpr nameExpr, Void arg) {
        super.visit(nameExpr, arg);

        try {
            String declClass = nameExpr.resolve().asField().declaringType().getClassName();
            if (!currentClass.equals(declClass)){
                classes.add(declClass);
            }
        } catch (Exception e){
            // Just ignore this expression
        }
    }

    public int getCoupling(){
        return classes.size();
    }
    
}
