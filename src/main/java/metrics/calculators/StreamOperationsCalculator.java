package metrics.calculators;

import com.github.javaparser.ast.expr.MethodCallExpr;

import java.util.Arrays;
import java.util.List;

public class StreamOperationsCalculator {
    private static final List<String> streamOperations =
            Arrays.asList("allMatch", "anyMatch", "count", "filter", "findAny", "findFirst",
                    "flatMap", "flatMapToDouble", "flatMapToInt", "flatMapToLong", "map",
                    "mapToDouble", "mapToInt", "mapToLong", "noneMatch", "reduce");

    public static boolean isStreamOperation(String s){
        return streamOperations.contains(s);
    }

    public static boolean isStreamOperation(MethodCallExpr m){
        return isStreamOperation(m.getNameAsString());
    }

}
