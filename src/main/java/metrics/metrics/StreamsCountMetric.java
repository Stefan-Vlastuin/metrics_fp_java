package metrics.metrics;

import com.github.javaparser.ast.CompilationUnit;
import metrics.visitors.StreamsCountVisitor;

public class StreamsCountMetric implements Metric{
    @Override
    public Number getResult(CompilationUnit cu) {
        StreamsCountVisitor streamsCountVisitor = new StreamsCountVisitor();
        streamsCountVisitor.visit(cu, null);
        return streamsCountVisitor.getCount();
    }

    @Override
    public String getName() {
        return "StreamsCount";
    }
}
