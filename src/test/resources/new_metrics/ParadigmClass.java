import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

class ParadigmScoreClass {
    private int x = 3;

    public void f(){
        while(x < 10){ // IP: loop
            x = x+1; // IP: assignment
        }
    }

    public void g(){
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println(numbers.stream().filter(n -> n > 5).count()); // FP: 2 stream operations
    }

    public int rec(int a){ // FP: recursive function
        if (a <= 0){
            return 0;
        }
        return rec(a-1);
    }

    public int h(Function<Integer, Integer> f){ // FP: higher-order function
        return f.apply(0);
    }
}
