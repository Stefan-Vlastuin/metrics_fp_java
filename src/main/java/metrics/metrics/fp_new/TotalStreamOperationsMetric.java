package metrics.metrics.fp_new;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.StreamVisitor;

public class TotalStreamOperationsMetric implements Metric {
    StreamVisitor streamVisitor;

    public TotalStreamOperationsMetric(StreamVisitor streamVisitor){
        this.streamVisitor = streamVisitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return streamVisitor.getTotalStreamOperations();
    }

    @Override
    public String getName() {
        return "TotalStreamOperations";
    }
}
