package metrics.metrics.fp_existing;

import com.github.javaparser.ast.CompilationUnit;
import metrics.metrics.Metric;
import metrics.visitors.StreamsCountVisitor;

public class StreamsCountMetric implements Metric {
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
