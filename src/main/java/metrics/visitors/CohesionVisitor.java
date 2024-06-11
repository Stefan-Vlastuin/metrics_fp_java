package metrics.visitors;

import java.util.*;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CohesionVisitor extends VoidVisitorAdapter<Void> {

    // TODO: currently, when field variables are declared together (int x, y, z;), they are seen as the same

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
        FieldVariableVisitor v = new FieldVariableVisitor();
        v.visit(m, null);
        return v.getFieldVariables();
    }

    public int getLackOfCohesion(){
        int result = nrUnrelatedMethodPairs - nrRelatedMethodPairs;
        return Math.max(result, 0);
    }
    
}
