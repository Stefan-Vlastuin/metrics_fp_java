public class PatternMatchingClass {
    private Object o;

    public void f(){
        if (o instanceof String){
            String s = (String) o;
            System.out.println(s);
        }
    }

    public void g(){
        if (o instanceof String s){
            System.out.println(s);
        }
    }
}