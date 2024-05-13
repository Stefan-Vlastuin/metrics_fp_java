package metrics;

import java.util.List;
import java.util.stream.Collectors;

public record ResultCompilationUnit(String className, List<Number> numbers) {

    @Override
    public String toString() {
        return className + ";" + numbers.stream().map(Number::toString).collect(Collectors.joining(";"));
    }

}
