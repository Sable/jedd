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

import polyglot.util.*;
import polyglot.ast.*;
import polyglot.types.*;
import polyglot.ext.jl.ast.*;
import polyglot.ext.jedd.ast.*;
import java.util.*;

public class DNode {
    BDDExpr expr;
    Type dom;
    int domNum;
    Type phys;
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
            nodeSet.add(ret2);
            exprSet.add(ret2.expr);
        }
        Group.v( ret2 );
        return ret2;
    }

    public static Set nodes() {
        return nodeSet;
    }
    public static Set exprs() {
        return exprSet;
    }

    private static Set exprSet = new HashSet();
    private static Set nodeSet = new HashSet();
    private static Map nodes = new HashMap();
    private DNode( BDDExpr expr, Type dom ) {
        this.expr = expr;
        this.dom = dom;
        BDDType t = expr.getType();
        Map map = t.map();
        this.phys = (Type) map.get(dom);
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

    private DNode rep = this;
    public DNode rep() {
        if( rep == this ) return this;
        rep = rep.rep();
        return rep;
    }

    public void merge( DNode other ) {
        if( rep != this ) {
            rep.merge(other);
        } else {
            DNode otherRep = other.rep();

            // prefer the node that's not a FixPhys for the rep
            if( !expr.isFixPhys() && otherRep.expr.isFixPhys() ) {
                otherRep.merge(this);
                return;
            }

            rep = otherRep;

            if( phys != null ) {
                if( rep.phys != null && rep.phys != phys ) {
                    throw new InternalCompilerError( "trying to merge node "+this+" with phys "+phys+" with node "+rep+" with phys "+rep.phys, rep.expr.position() );
                }
                rep.phys = phys;
            }
        }
    }
}


