class RefTransClass {
    private int x = 5;
    private int y, z;
    private final int a, b;

    private void f(){
        System.out.println(x); // Uses field variable
    }

    private void g(){
        y++; // Uses field variable and has side effect
    }

    private void h(){
        System.out.println(a); // Not counted because a is final
    }

}

record R (int a, String s) {}