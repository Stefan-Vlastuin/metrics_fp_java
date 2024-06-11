public class ComplexityClass {
    int x = 0;

    public void f(){
        while(true){
            System.out.println("Hello World");
            if (x > 0){
                System.out.println("Hi");
            }
        }
    }

    public void g(){
        if (x > 0 && x < 10){
            System.out.println("Hello World");
        }
    }
}