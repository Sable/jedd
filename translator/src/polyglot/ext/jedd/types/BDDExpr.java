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

public abstract class BDDExpr {
    private static class MIExpr extends BDDExpr {
        MethodInstance mi;
        MIExpr( MethodInstance mi ) {
            this.mi = mi;
        }
        public BDDType getType() {
            return (BDDType) mi.returnType();
        }
        protected Object obj() { return mi; }
        public String toString() {
            return "MethodInstance:"+mi.toString();
        }
        public void throwSemanticException( String s ) throws SemanticException {
            throw new SemanticException( s, mi.position() );
        }
        public Position position() {
            return mi.position();
        }
    }
    private static class VIExpr extends BDDExpr {
        VarInstance vi;
        VIExpr( VarInstance vi ) {
            this.vi = vi;
        }
        public BDDType getType() {
            return (BDDType) vi.type();
        }
        protected Object obj() { return vi; }
        public String toString() {
            return "VarInstance:"+vi.toString();
        }
        public void throwSemanticException( String s ) throws SemanticException {
            throw new SemanticException( s, vi.position() );
        }
        public Position position() {
            return vi.position();
        }
    }
    private static class ExprExpr extends BDDExpr {
        Expr ex;
        ExprExpr( Expr ex ) {
            this.ex = ex;
        }
        public BDDType getType() {
            return (BDDType) ex.type();
        }
        protected Object obj() { return ex; }
        public String toString() {
            String className =  ex.getClass().getName();
            className = className.substring( className.lastIndexOf(".")+1 );
            return className+": "+ex.toString();
        }
        public void throwSemanticException( String s ) throws SemanticException {
            throw new SemanticException( s, ex.position() );
        }
        public Position position() {
            return ex.position();
        }
        public boolean isFixPhys() {
            return ex instanceof FixPhys;
        }
    }
    private static BDDExpr v( BDDExpr ret ) {
        BDDExpr ret2 = (BDDExpr) instances.get( ret );
        if( ret2 == null ) instances.put( ret2 = ret, ret );
        return ret2;
    }
    public static BDDExpr v( VarInstance vi ) { return v( new VIExpr(vi) ); }
    public static BDDExpr v( MethodInstance mi ) { return v( new MIExpr(mi) ); }
    public static BDDExpr v( Expr ex ) { return v( new ExprExpr(ex) ); }
    private static Map instances = new HashMap();
    public abstract BDDType getType();
    protected abstract Object obj();
    public abstract Position position();
    public int hashCode() {
        return obj().hashCode();
    }
    public boolean equals( Object other ) {
        if( !(other instanceof BDDExpr) ) return false;
        BDDExpr o = (BDDExpr) other;
        return o.obj() == obj();
    }
    public abstract void throwSemanticException( String s ) throws SemanticException;
    public boolean isFixPhys() { return false; }
}


