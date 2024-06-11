import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LambdaClass {
    private List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    private int table = 4;

    public List<Integer> square(){
        return numbers.stream()
                .map(x -> x * x)
                .collect(Collectors.toList());
    }

    public void printMultTable(){
        numbers.stream()
                .map(x -> x * table) // Uses field variable
                .forEach(System.out::println);
    }

    public void printMultTableAlt(){
        numbers.stream()
                .map(this::mult) // Uses field variable
                .forEach(System.out::println);
    }

    public int mult(int x){
        return x * table;
    }

    public void sideEffects(){
        numbers.stream()
                .map(this::addOne) // Has side effect, and uses field variable
                .map(x -> {
                    table++; // Side effect, and uses field variable
                    return x+1;
                })
                .forEach(System.out::println);
    }

    public int addOne(int x){
        table = 0;
        return x + 1;
    }

    public void complexLambda(){
        numbers.stream()
                .map(x -> {
                    int y = 1;
                    while (x > 0){
                        y = y * x;
                    }
                    return x;
                })
                .forEach(System.out::println);
    }

}
