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

package polyglot.ext.jedd.extension;

import polyglot.ext.jl.ast.*;
import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.types.*;
import polyglot.ext.jedd.visit.*;
import polyglot.types.*;
import polyglot.ast.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

public class JeddBinaryExt_c extends JeddExt_c implements JeddTypeCheck
{
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        JeddTypeSystem ts = (JeddTypeSystem) tc.typeSystem();
        JeddNodeFactory nf = (JeddNodeFactory) tc.nodeFactory();

        Binary n = (Binary) node();
        Type ltype = n.left().type();
        Type rtype = n.right().type();
        if( !( ltype instanceof BDDType ) ) return n.typeCheck( tc );
        if( !( rtype instanceof BDDType ) ) return n.typeCheck( tc );

        n = n.right( (Expr) nf.FixPhys( n.right().position(), n.right() ).typeCheck(tc) );
        n = n.left( (Expr) nf.FixPhys( n.left().position(), n.left() ).typeCheck(tc) );

        BDDType lt = (BDDType) n.left().type();
        BDDType rt = (BDDType) n.right().type();

        Binary.Operator op = n.operator();
        if( op == Binary.BIT_AND
        ||  op == Binary.BIT_OR
        ||  op == Binary.SUB ) {
            if( lt.map().keySet().equals( rt.map().keySet() ) )
                return n.type( ts.sameDomains( lt ) );
            if( lt.map().keySet().isEmpty() )
                return n.type( ts.sameDomains( rt ) );
            if( rt.map().keySet().isEmpty() )
                return n.type( ts.sameDomains( lt ) );
            if( op == Binary.BIT_AND ) {
                if( rt.map().keySet().containsAll( lt.map().keySet() ) ) {
                    return n.type( ts.sameDomains( rt ) );
                }
                if( lt.map().keySet().containsAll( rt.map().keySet() ) ) {
                    return n.type( ts.sameDomains( lt ) );
                }
            }
            throw new SemanticException( "Incompatible types for "+op+
                " : lhs has type "+lt+" while rhs has type "+rt+"." );
        }

        if( op == Binary.EQ || op == Binary.NE ) {
            return n.typeCheck( tc );
        }

        throw new SemanticException( "Operator "+op+" cannot be used with BDD types." );
    }
    public Node physicalDomains( PhysicalDomains pd ) throws SemanticException {
        JeddTypeSystem ts = pd.jeddTypeSystem();

        Binary n = (Binary) super.physicalDomains(pd);
        if( !( n.left().type() instanceof BDDType ) ) return n;
        if( !( n.right().type() instanceof BDDType ) ) return n;

        BDDType lt = (BDDType) n.left().type();
        BDDType rt = (BDDType) n.right().type();
        for( Iterator domainIt = lt.map().keySet().iterator(); domainIt.hasNext(); ) {
            final Type domain = (Type) domainIt.next();
            if( rt.map().containsKey( domain ) )
                ts.addMustEqualEdge( DNode.v( n.left(), domain ),
                        DNode.v( n.right(), domain ) );
        }
        if( n.type() instanceof BDDType ) 
            for( Iterator domainIt = ((BDDType)n.type()).map().keySet().iterator(); domainIt.hasNext(); ) { 
                final Type domain = (Type) domainIt.next();
                if( lt.map().containsKey( domain ) )
                    ts.addMustEqualEdge( DNode.v( n.left(), domain ),
                        DNode.v( n, domain ) );
                if( rt.map().containsKey( domain ) )
                    ts.addMustEqualEdge( DNode.v( n.right(), domain ),
                        DNode.v( n, domain ) );
        }
        return n;
    }
    public Node generateJava( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        Binary n = (Binary) node();
        if( !( n.left().type() instanceof BDDType ) 
        ||  !( n.right().type() instanceof BDDType ) )
        {
            return super.generateJava( ts, nf );
        }

        Binary.Operator op = n.operator();

        String method;
        if( op == Binary.EQ || op == Binary.NE ) method = "equals";
        else if( op == Binary.BIT_OR ) method = "union";
        else if( op == Binary.BIT_AND ) method = "intersect";
        else if( op == Binary.SUB ) method = "minus";
        else throw new InternalCompilerError( 
                             "The type checker should have picked this up." );

        Call getJedd = nf.Call( n.position(), nf.CanonicalTypeNode( n.position(), ts.jedd() ), "v"  ); 

        Expr ret = nf.Call(
                n.position(),
                getJedd,
                method,
                nf.Call( n.position(), getJedd, "read", n.left() ),
                n.right() ).type(n.type());

        if( op == Binary.NE ) {
            ret = nf.Unary( n.position(), Unary.NOT, ret ).type(n.type());
        }
        return ret;
    }
}

