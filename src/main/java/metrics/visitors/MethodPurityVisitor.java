package metrics.visitors;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodPurityVisitor extends VoidVisitorAdapter<Void> {
    private int countMethods = 0;
    private int countMethodsSideEffect = 0;
    private int countMethodsFieldVariable = 0;
    private int countMethodsImpure = 0;

    @Override
    public void visit(MethodDeclaration method, Void arg){
        super.visit(method, arg);

        countMethods++;

        SideEffectVisitor sideEffectVisitor = new SideEffectVisitor();
        sideEffectVisitor.visit(method, method.getRange().orElseThrow());
        if (sideEffectVisitor.hasSideEffect()){
            countMethodsSideEffect++;
        }

        FieldVariableVisitor fieldVariableVisitor = new FieldVariableVisitor();
        fieldVariableVisitor.visit(method, null);
        if (fieldVariableVisitor.usesNonFinalFieldVariable()){
            countMethodsFieldVariable++;
        }

        if (sideEffectVisitor.hasSideEffect() || fieldVariableVisitor.usesNonFinalFieldVariable()){
            countMethodsImpure++;
        }
    }

    public int getCountMethodsSideEffect(){
        return countMethodsSideEffect;
    }

    public int getCountMethodsFieldVariable(){
        return countMethodsFieldVariable;
    }

    public int getCountMethodsImpure(){
        return countMethodsImpure;
    }

    public double getRatioMethodsSideEffect(){
        if (countMethods == 0){
            return 0;
        }
        return (double) countMethodsSideEffect / countMethods;
    }

    public double getRatioMethodsFieldVariable(){
        if (countMethods == 0){
            return 0;
        }
        return (double) countMethodsFieldVariable / countMethods;
    }

    public double getRatioMethodsImpure(){
        if (countMethods == 0){
            return 0;
        }
        return (double) countMethodsImpure / countMethods;
    }

}
