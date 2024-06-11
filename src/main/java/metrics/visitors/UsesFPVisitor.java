package metrics.visitors;

import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class UsesFPVisitor extends VoidVisitorAdapter<Void> {
    private boolean usesFP = false;

    @Override
    public void visit(LambdaExpr lambdaExpr, Void arg) {
        super.visit(lambdaExpr, arg);
        usesFP = true;
    }

    @Override
    public void visit(MethodCallExpr methodCall, Void arg){
        super.visit(methodCall, arg);

        if (methodCall.getNameAsString().equals("stream")) {
            usesFP = true;
        }
    }

    public boolean usesFP(){
        return usesFP;
    }

}
