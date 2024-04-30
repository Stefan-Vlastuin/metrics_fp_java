package metrics;

import java.util.List;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;

public class NOC extends VoidVisitorAdapter<Void> {
    private int numberOfChildren = 0;
    private List<CompilationUnit> allUnits;

    public NOC (List<CompilationUnit> allUnits){
        this.allUnits = allUnits;
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration classOrInterface, Void arg) {
        super.visit(classOrInterface, arg);

        ResolvedReferenceTypeDeclaration currentClass = classOrInterface.resolve();

        for (CompilationUnit cu : allUnits){
            cu.findAll(ClassOrInterfaceDeclaration.class).forEach(classOrInterfaceDeclaration -> {
                List<ResolvedReferenceTypeDeclaration> ancestors = classOrInterfaceDeclaration.resolve().getAncestors(true).
                                                                    stream().map(a -> a.getTypeDeclaration().get()).toList();
                if (ancestors.contains(currentClass)){
                    // A class has the current class as one of its direct ancestors
                    numberOfChildren++;
                }
            });
        }
    }

    public int getNOC() {
        return numberOfChildren;
    }

}
