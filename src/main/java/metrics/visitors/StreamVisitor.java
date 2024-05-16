package metrics.visitors;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import metrics.calculators.StreamOperationsCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StreamVisitor extends VoidVisitorAdapter<Void> {
    private int unterminatedStreamsCount = 0;
    private final List<Integer> operationsPerStream = new ArrayList<>();

    @Override
    public void visit(VariableDeclarator var, Void arg) {
        super.visit(var, arg);

        if (var.getType() instanceof ClassOrInterfaceType classOrInterfaceType) {
            if (classOrInterfaceType.getNameAsString().equals("Stream")) {
                unterminatedStreamsCount++;
            }
        }
    }

    @Override
    public void visit(MethodCallExpr methodCall, Void arg){
        super.visit(methodCall, arg);

        if (methodCall.getNameAsString().equals("stream")) {
            operationsPerStream.add(countOperations(methodCall));
        }
    }

    private int countOperations(MethodCallExpr methodCall) {
        int count = 0;
        Optional<Node> parent = methodCall.getParentNode();
        while (parent.isPresent() && isStreamOperation(parent.get())) {
            count++;
            parent = parent.get().getParentNode();
        }
        return count;
    }

    private boolean isStreamOperation(Node node) {
        return node instanceof MethodCallExpr && StreamOperationsCalculator.isStreamOperation((MethodCallExpr) node);
    }

    public int getUnterminatedStreamsCount() {
        return unterminatedStreamsCount;
    }

    public int getTotalStreamOperations() {
        return operationsPerStream.stream().mapToInt(Integer::intValue).sum();
    }

    public double getAverageStreamOperations() {
        if (operationsPerStream.isEmpty()) {
            return 0;
        }
        return (double) getTotalStreamOperations() / operationsPerStream.size();
    }

    public int getMaxStreamOperations() {
        return operationsPerStream.stream().mapToInt(Integer::intValue).max().orElse(0);
    }
}

