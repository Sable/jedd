package domains;
import jedd.*;

public class IntegerNumberer implements Numberer {
    public void add( Object o ) {
    }
    public Object get( int number ) {
        return new Integer(number);
    }
    public int get( Object o ) {
        return ((Integer) o).intValue();
    }
    public int size() { return 1000000; }
    public static IntegerNumberer v() { return instance; }
    private static IntegerNumberer instance = new IntegerNumberer();
    private IntegerNumberer() {}
}
