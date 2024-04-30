package metrics;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class Response extends VoidVisitorAdapter<Void> {

    // Storing the ResolvedMethodDeclaration leads to duplicates in the set,
    // so we store the string representation.
    private Set<String> methods = new HashSet<>();

    // TODO: check why some methods cannot be resolved; is it okay to just ignore them?

    @Override
    public void visit(MethodDeclaration method, Void arg) {
        super.visit(method, arg);

        try {
            methods.add(method.resolve().toString());
        } catch (Exception e){
            // Just ignore this method
        }
    }

    @Override
    public void visit(MethodCallExpr methodCall, Void arg) {
        super.visit(methodCall, arg);
        
        try {
            methods.add(methodCall.resolve().toString());
        } catch (Exception e){
            // Just ignore this method
        }
    }

    public int getResponse(){
        return methods.size();
    }
    
}
