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

public class JeddAssignExt_c extends JeddExt_c implements JeddTypeCheck, JeddPhysicalDomains
{
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        JeddTypeSystem ts = (JeddTypeSystem) tc.typeSystem();
        JeddNodeFactory nf = (JeddNodeFactory) tc.nodeFactory();

        Assign n = (Assign) node();
        Type ltype = n.left().type();
        Type rtype = n.right().type();
        if( !( ltype instanceof BDDType ) ) return n.typeCheck( tc );
        if( !( rtype instanceof BDDType ) ) return n.typeCheck( tc );

        n = n.right( (Expr) nf.FixPhys( n.right().position(), n.right() ).typeCheck(tc) );


        BDDType lt = (BDDType) n.left().type();
        BDDType rt = (BDDType) n.right().type();

        Assign.Operator op = n.operator();

        if( op == Assign.ASSIGN
        ||  op == Assign.BIT_AND_ASSIGN 
        ||  op == Assign.BIT_OR_ASSIGN
        ||  op == Assign.SUB_ASSIGN ) {
            if( lt.map().keySet().equals( rt.map().keySet() )
            ||  rt.isLitType() ) {
                return n.type( ts.sameDomains( lt ) );
            }
            throw new SemanticException( "Incompatible types for "+op+
                " : lhs has type "+lt+" while rhs has type "+rt+"." );
        }

        throw new SemanticException( "Operator "+op+" cannot be used with BDD types." );
    }
    public Node physicalDomains( PhysicalDomains pd ) throws SemanticException {
        JeddTypeSystem ts = pd.jeddTypeSystem();

        Assign n = (Assign) super.physicalDomains(pd);
        if( !( n.left().type() instanceof BDDType ) ) return n;
        if( !( n.right().type() instanceof BDDType ) ) return n;
        if( !( n.type() instanceof BDDType ) ) return n;
        BDDType lt = (BDDType) n.left().type();
        BDDType rt = (BDDType) n.right().type();
        BDDType t = (BDDType) n.type();
        for( Iterator domainIt = t.map().keySet().iterator(); domainIt.hasNext(); ) {
            final Type domain = (Type) domainIt.next();
            ts.addMustEqualEdge(
                    DNode.v( n, domain ), DNode.v( n.left(), domain ) );
            ts.addMustEqualEdge(
                    DNode.v( n.left(), domain ), DNode.v( n.right(), domain ) );
        }
        return n;
    }
    public Node generateJava( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        Assign n = (Assign) node();
        if( !( n.left().type() instanceof BDDType ) 
        ||  !( n.right().type() instanceof BDDType ) )
        {
            return super.generateJava( ts, nf );
        }

        Assign.Operator op = n.operator();

        String method;
        if( op == Assign.ASSIGN ) method = "eq";
        else if( op == Assign.BIT_AND_ASSIGN ) method = "eqIntersect";
        else if( op == Assign.BIT_OR_ASSIGN ) method = "eqUnion";
        else if( op == Assign.SUB_ASSIGN ) method = "eqMinus";
        else throw new InternalCompilerError( 
                             "The type checker should have picked this up." );

        return nf.Call( n.position(), n.left(), method, n.right() ).type(n.type());
    }
}

