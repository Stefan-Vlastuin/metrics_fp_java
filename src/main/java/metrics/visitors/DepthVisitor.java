package metrics.visitors;

import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedReferenceTypeDeclaration;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import metrics.ProgressLogger;

public class DepthVisitor extends VoidVisitorAdapter<Void> {
    private int depth = 1;
    private final ProgressLogger logger = ProgressLogger.getInstance();

    @Override
    public void visit(ClassOrInterfaceDeclaration classOrInterface, Void arg) {
        super.visit(classOrInterface, arg);
        depth = computeDepth(classOrInterface.resolve());
    }

    private int computeDepth (ResolvedReferenceTypeDeclaration resolvedReferenceTypeDeclaration){
        try {
            List<ResolvedReferenceType> ancestors = resolvedReferenceTypeDeclaration.getAncestors(true);
            // Compute maximum depth of all ancestors
            Optional<Integer> depth = ancestors.stream()
                    .map(a -> computeDepth(a.getTypeDeclaration().orElseThrow()))
                    .max(Integer::compareTo);
            return depth.map(d -> d + 1).orElse(0);
        } catch (Exception e) {
            logger.log(e, "Could not resolve ancestors of " + resolvedReferenceTypeDeclaration.getName() + " at " + resolvedReferenceTypeDeclaration.getClassName());
            return 0;
        }
    }

    public int getDepth() {
        return depth;
    }

}
