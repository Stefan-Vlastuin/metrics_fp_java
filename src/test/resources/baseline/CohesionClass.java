public class CohesionClass {
    private int x;
    private int y;
    private int z;

    public void f(){
        System.out.println(x);
    }

    public void g(){
        System.out.println(y);
    }

    public void h(){
        System.out.println(x + y);
    }

    public void s(){
        System.out.println("Hello");
    }
}