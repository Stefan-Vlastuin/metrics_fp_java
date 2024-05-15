package metrics.visitors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.*;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.resolution.types.ResolvedType;

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

        List<String> functionalCalls =
                Arrays.asList("allMatch", "anyMatch", "count", "filter", "findAny", "findFirst",
                        "flatMap", "flatMapToDouble", "flatMapToInt", "flatMapToLong", "map",
                        "mapToDouble", "mapToInt", "mapToLong", "noneMatch", "reduce");

        if (functionalCalls.contains(n.getNameAsString())){
            functionalCount++;
        }
    }

    @Override
    public void visit(MethodDeclaration n, Void arg){
        super.visit(n, arg);

        if (isRecursive(n) || isHigherOrder(n)){
            functionalCount++;
        }
    }

    private boolean isRecursive(MethodDeclaration method){
        if (method.getBody().isEmpty()){
            return false;
        }

        RecursionVisitor v = new RecursionVisitor();
        v.visit(method, method);
        return v.isRecursive();
    }

    private boolean isHigherOrder(MethodDeclaration method){
        List<Type> types = new ArrayList<>();
        types.add(method.getType());
        types.addAll(method.getParameters().stream().map(Parameter::getType).toList());

        for (Type t : types){
            ResolvedType resolvedType = t.resolve();
            if (resolvedType.isReferenceType()){
                ResolvedReferenceType referenceType = resolvedType.asReferenceType();
                if (referenceType.getTypeDeclaration().isPresent()){
                    if (referenceType.getTypeDeclaration().get().isFunctionalInterface()){
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public double getScore(){
        if (functionalCount + imperativeCount == 0){
            return 0.0;
        }
        return (double) (functionalCount - imperativeCount) / (functionalCount + imperativeCount);
    }
    
}

class RecursionVisitor extends VoidVisitorAdapter<MethodDeclaration> {
    private boolean isRecursive = false;

    @Override
    public void visit(MethodCallExpr methodCall, MethodDeclaration methodDeclaration) {
        super.visit(methodCall, methodDeclaration);

        String name = methodDeclaration.getNameAsString();
        int nrParameters = methodDeclaration.getParameters().size();

        if (methodCall.getNameAsString().equals(name) && methodCall.getArguments().size() == nrParameters){
            if (methodCall.resolve().toDescriptor().equals(methodDeclaration.resolve().toDescriptor())){
                isRecursive = true;
            }
        }

    }

    public boolean isRecursive(){
        return isRecursive;
    }

}

