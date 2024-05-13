package metrics.metrics;

import com.github.javaparser.ast.CompilationUnit;

public interface Metric {
    Number getResult(CompilationUnit cu);
    String getName();
}
