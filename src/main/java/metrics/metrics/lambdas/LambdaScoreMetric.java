package metrics.metrics.lambdas;

import com.github.javaparser.ast.CompilationUnit;
import metrics.calculators.LinesOfCodeCalculator;
import metrics.metrics.Metric;
import metrics.visitors.LambdaVisitor;

public class LambdaScoreMetric implements Metric {
    LambdaVisitor visitor;

    public LambdaScoreMetric(LambdaVisitor visitor){
        this.visitor = visitor;
    }

    @Override
    public Number getResult(CompilationUnit cu) {
        return (double) visitor.getLambdaLines() / LinesOfCodeCalculator.countSLOC(cu);
    }

    @Override
    public String getName() {
        return "LambdaScore";
    }
}

