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

package polyglot.ext.jedd.ast;

import polyglot.ext.jedd.extension.*;
import polyglot.ext.jedd.types.*;
import polyglot.ext.jl.ast.*;
import polyglot.types.*;
import polyglot.ast.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

public class FixPhys_c extends Expr_c implements FixPhys, JeddGenerateJava, JeddPhysicalDomains
{
    protected Expr expr;
    public Expr expr() { return expr; }
    public FixPhys_c(Position pos, Expr expr ) {
        super( pos );
        this.expr = expr;
    }
    public Node visitChildren(NodeVisitor v) {
        FixPhys_c ret = (FixPhys_c) copy();

        ret.expr = (Expr) visitChild( expr, v );

        return ret.type( type );
    }
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.begin(0);
        printSubExpr(expr, w, tr);
        w.end();
    }
    public Node typeCheck( TypeChecker tc ) throws SemanticException {
        JeddTypeSystem ts = (JeddTypeSystem) tc.typeSystem();
        JeddNodeFactory nf = (JeddNodeFactory) tc.nodeFactory();
        
        BDDType exprType = (BDDType) expr.type();
        List newPairs = new LinkedList();
        for( Iterator pairIt = exprType.domainPairs().iterator(); pairIt.hasNext(); ) {
            final Type[] pair = (Type[]) pairIt.next();
            Type[] newPair = new Type[] { pair[0], null };
            newPairs.add( newPair );
        }
        Expr ret = type( ts.BDDType( newPairs ) );
        return ret;
    }
    public Node physicalDomains( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        BDDType type = (BDDType) type();
        BDDType exprType = (BDDType) expr().type();
        for( Iterator domainIt = exprType.map().keySet().iterator(); domainIt.hasNext(); ) {
            final Type domain = (Type) domainIt.next();
            ts.addAssignEdge( DNode.v( expr(), domain ), DNode.v( this, domain ) );
        }
        return this;
    }
    public Node generateJava( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        if( this instanceof Replace ) throw new RuntimeException();
        Position p = position();

        BDDType type = (BDDType) type();
        BDDType exprType = (BDDType) expr().type();
        Map map = type.map();

        List from = new LinkedList();
        List to = new LinkedList();
        for( Iterator pairIt = exprType.domainPairs().iterator(); pairIt.hasNext(); ) {
            final Type[] pair = (Type[]) pairIt.next();
            Type phys = (Type) map.get( pair[0] );
            if( phys.equals( pair[1] ) ) continue;
            from.add( nf.Call( p, nf.CanonicalTypeNode( p, pair[1] ), "v" ) );
            to.add( nf.Call( p, nf.CanonicalTypeNode( p, phys ), "v" ) );
        }
        if( from.isEmpty() ) return expr();

        Call getJedd = nf.Call( p, nf.CanonicalTypeNode( p, ts.jedd() ), "v"  ); 

        return nf.Call( 
                p,
                getJedd,
                "replace",
                expr(),
                nf.NewArray(
                    p,
                    nf.CanonicalTypeNode( p, ts.physicalDomain() ),
                    new LinkedList(),
                    1,
                    nf.ArrayInit( p, from )
                    ),
                nf.NewArray(
                    p,
                    nf.CanonicalTypeNode( p, ts.physicalDomain() ),
                    new LinkedList(),
                    1,
                    nf.ArrayInit( p, to )
                    )
                ).type( type() );
    }
}
