package metrics.visitors;

import com.github.javaparser.Range;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class LambdaVisitor extends VoidVisitorAdapter<Void> {
    private int count = 0;
    private int fieldVariableCount = 0;
    private int sideEffectCount = 0;
    private final List<Integer> lambdaLengths = new ArrayList<>();
    private int lambdaComplexity = 0;

    @Override
    public void visit(LambdaExpr lambda, Void arg) {
        super.visit(lambda, arg);
        count++;

        Range range = lambda.getRange().orElseThrow();
        int lines = range.end.line - range.begin.line + 1;
        lambdaLengths.add(lines);

        if (usesFieldVariable(lambda)){
            fieldVariableCount++;
        }

        if (hasSideEffect(lambda)){
            sideEffectCount++;
        }

        ComplexityVisitor complexityVisitor = new ComplexityVisitor();
        complexityVisitor.visit(lambda, null);
        lambdaComplexity += complexityVisitor.getComplexity();
    }

    private boolean usesFieldVariable(LambdaExpr l){
        FieldVariableVisitor v = new FieldVariableVisitor();
        v.visit(l, null);
        return v.usesNonFinalFieldVariable();
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
        return lambdaLengths.stream().mapToInt(Integer::intValue).sum();
    }

    public double getAverageLambdaLines(){
        if (lambdaLengths.isEmpty()){
            return 0;
        }
        return (double) getLambdaLines() / lambdaLengths.size();
    }

    public int getMaxLambdaLines(){
        return lambdaLengths.stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    public int getLambdaCountFieldVariable(){
        return fieldVariableCount;
    }

    public int getLambdaCountSideEffect(){
        return sideEffectCount;
    }

    public int getLambdaComplexity(){
        return lambdaComplexity;
    }

}
