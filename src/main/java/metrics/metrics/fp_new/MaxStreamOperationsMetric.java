package metrics.metrics.fp_new;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.StreamVisitor;

public class MaxStreamOperationsMetric implements Metric {
    StreamVisitor streamVisitor;

    public MaxStreamOperationsMetric(StreamVisitor streamVisitor){
        this.streamVisitor = streamVisitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return streamVisitor.getMaxStreamOperations();
    }

    @Override
    public String getName() {
        return "MaxStreamOperations";
    }
}