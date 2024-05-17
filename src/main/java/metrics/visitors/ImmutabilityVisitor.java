package metrics.visitors;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ImmutabilityVisitor extends VoidVisitorAdapter<Void> {
    private int fieldVariablesCount = 0;
    private int finalFieldVariablesCount = 0;

    @Override
    public void visit(FieldDeclaration fieldDeclaration, Void arg) {
        super.visit(fieldDeclaration, arg);
        fieldVariablesCount += fieldDeclaration.getVariables().size();
        if (fieldDeclaration.isFinal()) {
            finalFieldVariablesCount += fieldDeclaration.getVariables().size();
        }
    }

    @Override
    public void visit(RecordDeclaration recordDeclaration, Void arg) {
        super.visit(recordDeclaration, arg);
        // Parameters of a record are equivalent to final field variables.
        fieldVariablesCount += recordDeclaration.getParameters().size();
        finalFieldVariablesCount += recordDeclaration.getParameters().size();
    }

    public int getNonFinalFieldVariablesCount() {
        return fieldVariablesCount - finalFieldVariablesCount;
    }

    public double getNonFinalFieldVariablesRatio(){
        if (fieldVariablesCount == 0){
            return 0;
        }
        return (double) getNonFinalFieldVariablesCount() / fieldVariablesCount;
    }

}
