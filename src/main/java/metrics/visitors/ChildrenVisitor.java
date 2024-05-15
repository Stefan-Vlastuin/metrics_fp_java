package metrics.visitors;

import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import metrics.calculators.ChildrenCalculator;

public class ChildrenVisitor extends VoidVisitorAdapter<Void> {
    private int numberOfChildren = 0;
    private final List<CompilationUnit> allUnits;

    public ChildrenVisitor(List<CompilationUnit> allUnits){
        this.allUnits = allUnits;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration classOrInterface, Void arg) {
        super.visit(classOrInterface, arg);

        List<CompilationUnit> children = ChildrenCalculator.getInstance(allUnits).getChildren(classOrInterface);
        numberOfChildren += children.size();
    }

    public int getNOC() {
        return numberOfChildren;
    }

}
