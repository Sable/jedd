/* Jedd - A language for implementing relations using BDDs
 * Copyright (C) 2003 Ondrej Lhotak
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

package polyglot.ext.jedd.types;

import polyglot.ast.*;
import polyglot.types.*;
import polyglot.ext.jl.ast.*;
import polyglot.ext.jedd.ast.*;
import java.util.*;

public class DNode {
    BDDExpr expr;
    Type dom;
    int domNum;
    static int nextDomNum = 0;
    public static DNode v( VarInstance expr, Type dom ) {
        return v( BDDExpr.v(expr), dom );
    }
    public static DNode v( MethodInstance expr, Type dom ) {
        return v( BDDExpr.v(expr), dom );
    }
    public static DNode v( Expr expr, Type dom ) {
        return v( BDDExpr.v(expr), dom );
    }
    public static DNode v( BDDExpr expr, Type dom ) {
        BDDType t = expr.getType();
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
    private DNode( BDDExpr expr, Type dom ) {
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
    public String toShortString() {
        return dom.toString();
    }
    public String toString() {
        String className = expr.toString();
        if( dom instanceof ClassType ) 
            return "\""+className+":"+((ClassType)dom).name()+"\"";
        return "\""+className+":"+dom.toString()+"\"";
    }
    public String toLongString() {
        return toString()+" at "+expr.position();
    }
}


