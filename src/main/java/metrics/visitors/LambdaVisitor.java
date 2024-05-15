package metrics.visitors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class LambdaVisitor extends VoidVisitorAdapter<Void> {
    private int count = 0;
    private int linesCount = 0;
    private int fieldVariableCount = 0;
    private int sideEffectCount = 0;

    @Override
    public void visit(LambdaExpr lambda, Void arg) {
        super.visit(lambda, arg);
        count++;

        Range range = lambda.getRange().orElseThrow();
        int lines = range.end.line - range.begin.line + 1;
        linesCount += lines;

        if (usesFieldVariable(lambda)){
            fieldVariableCount++;
        }

        if (hasSideEffect(lambda)){
            sideEffectCount++;
        }
    }

    private boolean usesFieldVariable(LambdaExpr l){
        FieldVariableVisitor v = new FieldVariableVisitor();
        v.visit(l, null);
        return v.getFieldVariables().stream().anyMatch(var -> !var.isFinal());
    }

    private boolean hasSideEffect(LambdaExpr l){
        SideEffectVisitor v = new SideEffectVisitor();
        v.visit(l, l.getRange().orElseThrow());
        return v.hasSideEffect();
    }

    public int getLambdaCount(){
        return count;
    }

    public int getLambdaLines(){
        return linesCount;
    }

    public int getLambdaCountFieldVariable(){
        return fieldVariableCount;
    }

    public int getLambdaCountSideEffect(){
        return sideEffectCount;
    }

}
