package domains;
import jedd.*;

public class IntegerNumberer implements Numberer {
    public void add( Object o ) {
    }
    public Object get( long number ) {
        return new Long(number);
    }
    public long get( Object o ) {
        return ((Long) o).intValue();
    }
    public int size() { return 1000000; }
    public static IntegerNumberer v() { return instance; }
    private static IntegerNumberer instance = new IntegerNumberer();
    private IntegerNumberer() {}
}
