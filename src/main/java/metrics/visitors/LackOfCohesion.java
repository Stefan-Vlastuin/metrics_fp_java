package metrics.visitors;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedValueDeclaration;

public class LackOfCohesion extends VoidVisitorAdapter<Void> {

    private int nrRelatedMethodPairs = 0;
    private int nrUnrelatedMethodPairs = 0;

    @Override
    public void visit(ClassOrInterfaceDeclaration classOrInterface, Void arg) {
        super.visit(classOrInterface, arg);

        List<MethodDeclaration> methods = classOrInterface.getMethods();

        // Consider each method pair
        for (int i = 0; i < methods.size(); i++){
            for (int j = i+1; j < methods.size(); j++){               
                if (shareInstanceVariable(methods.get(i), methods.get(j))){
                    nrRelatedMethodPairs++;
                } else {
                    nrUnrelatedMethodPairs++;
                }
            }
        }
    }

    // Checks whether two methods have at least 1 shared instance variable
    private boolean shareInstanceVariable(MethodDeclaration m1, MethodDeclaration m2){
        Set<FieldDeclaration> instanceVariables1 = getUsedInstanceVariables(m1);
        Set<FieldDeclaration> instanceVariables2 = getUsedInstanceVariables(m2);
        return instanceVariables1.stream().anyMatch(instanceVariables2::contains);
    }

    // Gets all used instance variables of a method
    private Set<FieldDeclaration> getUsedInstanceVariables(MethodDeclaration m){
        Set<FieldDeclaration> variables = new HashSet<>();

        // Go over each used variable
        m.findAll(NameExpr.class).forEach(nameExpr -> {
            try {
                // Get declaration
                ResolvedValueDeclaration decl = nameExpr.resolve();
                Optional<Node> declNode = decl.toAst();

                if (declNode.isPresent() && declNode.get() instanceof FieldDeclaration fieldDeclaration){ // Declaration is a declaration of a field variable
                    variables.add(fieldDeclaration);
                }
            } catch (UnsolvedSymbolException e){
                // Apparently not a field variable: we do not have to do anything
            }
        });

        return variables;
    }

    public int getLackOfCohesion(){
        int result = nrUnrelatedMethodPairs - nrRelatedMethodPairs;
        return Math.max(result, 0);
    }
    
}
