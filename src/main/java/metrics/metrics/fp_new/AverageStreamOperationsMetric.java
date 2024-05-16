package metrics.metrics.fp_new;
import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.StreamVisitor;

public class AverageStreamOperationsMetric implements Metric {
    StreamVisitor streamVisitor;

    public AverageStreamOperationsMetric(StreamVisitor streamVisitor){
        this.streamVisitor = streamVisitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return streamVisitor.getAverageStreamOperations();
    }

    @Override
    public String getName() {
        return "AverageStreamOperations";
    }
}
