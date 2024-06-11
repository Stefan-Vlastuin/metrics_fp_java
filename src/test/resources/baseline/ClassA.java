public class ClassA {
    public int x = 5;

    public ClassA() {}

    public void f(){

        System.out.println("BasicClass2.f()");
        ClassB b = new ClassB();
        System.out.println(b.compute());
        System.out.println(add(1,2));

    }

    public int add(int a, int b){
        return a+b;
    }
}