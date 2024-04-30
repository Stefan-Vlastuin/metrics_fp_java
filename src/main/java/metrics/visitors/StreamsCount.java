package metrics.visitors;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class StreamsCount extends VoidVisitorAdapter<Void> {
    private int count = 0;

    @Override
    public void visit(VariableDeclarator var, Void arg) {
        super.visit(var, arg);

        if (var.getType() instanceof ClassOrInterfaceType) {
            ClassOrInterfaceType classOrInterfaceType = (ClassOrInterfaceType) var.getType();
            if (classOrInterfaceType.getNameAsString().equals("Stream")) {
                count++;
            }
        }
    }

    public int getCount() {
        return count;
    }
}

