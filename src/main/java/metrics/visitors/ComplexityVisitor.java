package metrics.visitors;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ComplexityVisitor extends VoidVisitorAdapter<Void> {

    private int complexity = 1;

    @Override
    public void visit(IfStmt n, Void arg) {
        super.visit(n, arg);
        complexity++;
    }

    @Override
    public void visit(WhileStmt n, Void arg) {
        super.visit(n, arg);
        complexity++;
    }

    @Override
    public void visit(DoStmt n, Void arg) {
        super.visit(n, arg);
        complexity++;
    }

    @Override
    public void visit(ForStmt n, Void arg) {
        super.visit(n, arg);
        complexity++;
    }

    @Override
    public void visit(ForEachStmt n, Void arg) {
        super.visit(n, arg);
        complexity++;
    }

    @Override
    public void visit(TryStmt n, Void arg) {
        super.visit(n, arg);
        complexity++;
    }

    @Override
    // Add one for each case of a switch-statement
    public void visit(SwitchEntry n, Void arg) {
        super.visit(n, arg);
        complexity++;
    }

    @Override
    // Something like x == 0 ? a : b
    public void visit(ConditionalExpr n, Void arg) {
        super.visit(n, arg);
        complexity++;
    }

    @Override
    // Add one for each && or || in a condition
    public void visit(BinaryExpr n, Void arg) {
        super.visit(n, arg);
        if (n.getOperator() == Operator.AND || n.getOperator() == Operator.OR){
            complexity++;
        }
    }

    public int getComplexity(){
        return complexity;
    }
    
}
