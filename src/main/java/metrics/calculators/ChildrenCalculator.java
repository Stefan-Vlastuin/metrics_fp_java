package metrics.calculators;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import metrics.ProgressLogger;

import java.util.*;

public class ChildrenCalculator {
    private static ChildrenCalculator instance;
    private final List<CompilationUnit> allUnits;
    private Map<ResolvedReferenceTypeDeclaration, List<CompilationUnit>> children;

    private ChildrenCalculator(List<CompilationUnit> allUnits){
        this.allUnits = allUnits;
    }

    public static ChildrenCalculator getInstance(List<CompilationUnit> allUnits){
        if (instance == null){
            instance = new ChildrenCalculator(allUnits);
        }
        return instance;
    }

    public List<CompilationUnit> getChildren(ClassOrInterfaceDeclaration classOrInterfaceDeclaration){
        if (children == null){
            calculateChildren();
        }

        ResolvedReferenceTypeDeclaration decl = classOrInterfaceDeclaration.resolve();
        if (children.containsKey(decl)){
            return children.get(decl);
        } else {
            return new ArrayList<>();
        }
    }

    private void calculateChildren(){
        children = new HashMap<>();
        for (CompilationUnit cu : allUnits){
            AncestorVisitor v = new AncestorVisitor();
            v.visit(cu, null);
            List<ResolvedReferenceTypeDeclaration> ancestors = v.getAncestors();

            for (ResolvedReferenceTypeDeclaration ancestor : ancestors){
                if (children.containsKey(ancestor)){
                    children.get(ancestor).add(cu);
                } else {
                    List<CompilationUnit> l = new ArrayList<>();
                    l.add(cu);
                    children.put(ancestor, l);
                }
            }
        }

    }
}

class AncestorVisitor extends VoidVisitorAdapter<Void> {
    private final List<ResolvedReferenceTypeDeclaration> ancestors = new ArrayList<>();
    private final ProgressLogger logger = ProgressLogger.getInstance();

    @Override
    public void visit(ClassOrInterfaceDeclaration classOrInterface, Void arg) {
        super.visit(classOrInterface, arg);
        try {
            ancestors.addAll(classOrInterface.resolve().getAncestors(true).
                    stream().map(a -> a.getTypeDeclaration().orElseThrow()).toList());
        } catch (Exception e) {
            logger.log(e, "Could not resolve ancestors of " + classOrInterface.getNameAsString() + " at " + classOrInterface.getRange());
        }
    }

    public List<ResolvedReferenceTypeDeclaration> getAncestors() {
        return ancestors;
    }
}
