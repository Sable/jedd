package polyglot.ext.jedd.types;

import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.extension.*;
import polyglot.ast.*;
import polyglot.types.*;
import polyglot.util.*;
import polyglot.frontend.*;
import polyglot.visit.*;
import java.util.*;

public class Edge {
    public Object n1, n2;
    public Type d1, d2;
    Edge( Node n1, Type d1, Node n2, Type d2 ) {
        this.n1 = canon(n1);
        this.n2 = canon(n2);
        this.d1 = d1;
        this.d2 = d2;
    }
    private Object canon(Node n) {
        if( n instanceof Local ) return ((Local) n).localInstance();
        if( n instanceof Field ) return ((Field) n).fieldInstance();
        return n;
    }
    public boolean equals( Object o ) {
        if( !( o instanceof Edge ) ) return false;
        Edge other = (Edge) o;
        if( !n1.equals( other.n1 ) ) return false;
        if( !n2.equals( other.n2 ) ) return false;
        if( !d1.equals( other.d1 ) ) return false;
        if( !d2.equals( other.d2 ) ) return false;
        return true;
    }
    public int hashCode() {
        return n1.hashCode()+n2.hashCode()+d1.hashCode()+d2.hashCode();
    }
}

