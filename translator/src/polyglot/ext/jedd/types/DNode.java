package polyglot.ext.jedd.types;

import polyglot.ast.*;
import polyglot.types.*;
import polyglot.ext.jl.ast.*;
import polyglot.ext.jedd.ast.*;
import java.util.*;

public class DNode {
    Object expr;
    Type dom;
    int domNum;
    static int nextDomNum = 0;
    public static DNode v( Object expr, Type dom ) {
        BDDType t = (BDDType) PhysDom.getType(expr);
        if( !t.map().keySet().contains(dom) && !t.map().keySet().isEmpty() ) throw new RuntimeException( "expression "+expr+" doesn't have domain "+dom+"; it has domains "+t.map().keySet());

        DNode ret = new DNode( expr, dom );
        DNode ret2 = (DNode) nodes.get(ret);
        if( ret2 == null ) {
            nodes.put( ret2 = ret, ret );
            ret2.domNum = ++nextDomNum;
        }
        Group.v( ret2 );
        return ret2;
    }

    public static Set nodes() {
        return new HashSet(nodes.values());
    }
    public static Set exprs() {
        HashSet ret = new HashSet();
        for( Iterator dnodeIt = nodes.values().iterator(); dnodeIt.hasNext(); ) {
            final DNode dnode = (DNode) dnodeIt.next();
            ret.add( dnode.expr );
        }
        return ret;
    }

    private static Map nodes = new HashMap();
    private DNode( Object expr, Type dom ) {
        this.expr = expr;
        this.dom = dom;
    }
    public int hashCode() {
        return expr.hashCode()+dom.hashCode();
    }
    public boolean equals( Object o ) {
        if( !(o instanceof DNode) ) return false;
        DNode other = (DNode) o;
        if( !expr.equals( other.expr ) ) return false;
        if( !dom.equals( other.dom ) ) return false;
        return true;
    }
    public static String toString(Object n ) {
        String className = n.getClass().getName();
        className = className.substring( className.lastIndexOf(".") );
        if( !(n instanceof Node) ) className = n.toString();
        if( n instanceof FixPhys ) className = "FixPhys "+((FixPhys) n).expr();
        if( n instanceof Replace ) className = "Replace "+((Replace) n).expr();
        className = className+":"+System.identityHashCode(n);
        return className;
    }
    public String toShortString() {
        return dom.toString();
    }
    public String toString() {
        String className = toString(expr);
        return "\""+className+":"+dom.toString()+"\"";
    }
}


