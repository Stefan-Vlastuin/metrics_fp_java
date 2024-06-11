import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class StreamClass {
    private List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    private Stream<String> s;

    public void f(){
        Stream<Integer> even = numbers.stream().filter(n -> n % 2 == 0);
        even.forEach(System.out::println);
    }

    public int g(){
        return numbers.stream().map(x -> x + 1).filter(x -> x % 2 == 0).mapToInt(x -> x).sum();
    }

    public int h(){
        return numbers.stream().filter(x -> x > 5).reduce((x, y) -> x - y).orElseThrow();
    }
}