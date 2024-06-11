package metrics.metrics.streams;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.StreamVisitor;

public class StreamsCountMetric implements Metric {
    private final StreamVisitor streamVisitor;

    public StreamsCountMetric(StreamVisitor streamVisitor){
        this.streamVisitor = streamVisitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return streamVisitor.getUnterminatedStreamsCount();
    }

    @Override
    public String getName() {
        return "StreamsCount";
    }
}
