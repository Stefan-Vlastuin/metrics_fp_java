package metrics.visitors;

import java.util.Arrays;
import java.util.List;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class ParadigmVisitor extends VoidVisitorAdapter<Void> {

    private int functionalCount = 0;
    private int imperativeCount = 0;

    @Override
    public void visit(WhileStmt n, Void arg) {
        super.visit(n, arg);
        imperativeCount++;
    }

    @Override
    public void visit(DoStmt n, Void arg) {
        super.visit(n, arg);
        imperativeCount++;
    }

    @Override
    public void visit(ForStmt n, Void arg) {
        super.visit(n, arg);
        imperativeCount++;
    }

    @Override
    public void visit(ForEachStmt n, Void arg) {
        super.visit(n, arg);
        imperativeCount++;
    }

    @Override
    public void visit(AssignExpr n, Void arg) {
        super.visit(n, arg);
        imperativeCount++;
    }

    @Override
    public void visit(MethodCallExpr n, Void arg) {
        super.visit(n, arg);

        // TODO: check this list of functions
        List<String> functionalCalls = Arrays.asList("fold", "map", "filter", "count", "exists", "find");
        if (functionalCalls.contains(n.getNameAsString())){
            functionalCount++;
        }
    }

    public double getScore(){
        if (functionalCount + imperativeCount == 0){
            return 0.0;
        }
        return (double) (functionalCount - imperativeCount) / (functionalCount + imperativeCount);
    }
    
}

