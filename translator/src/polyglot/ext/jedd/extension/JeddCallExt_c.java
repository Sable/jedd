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

public class JeddCallExt_c extends JeddExt_c implements JeddPhysicalDomains
{
    public Node generateJava( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        Call n = (Call) node();

        Receiver target = n.target();
        if( target instanceof Expr ) {
            Expr tgt = (Expr) target;
            if( tgt.type() instanceof BDDType ) {
                n = n.target( newRelation( ts, nf, (BDDType) tgt.type(), tgt ) );
            }
        }
        List newArgs = new LinkedList();
        for( Iterator argIt = n.arguments().iterator(); argIt.hasNext(); ) {
            final Expr arg = (Expr) argIt.next();
            if( arg.type() instanceof BDDType ) {
                newArgs.add( newRelation( ts, nf, (BDDType) arg.type(), arg ) );
            } else {
                newArgs.add( arg );
            }
        }

        return n.arguments( newArgs );
    }

    public Node physicalDomains( PhysicalDomains pd ) throws SemanticException {
        JeddTypeSystem ts = pd.jeddTypeSystem();

        Call n = (Call) node();
        MethodInstance mi = n.methodInstance();

// We want to return if none of the arguments or return type is a BDD type.
// Now wouldn't it have been easier to just leave goto in the language?
found_bdd: 
        {
            for( Iterator argIt = n.arguments().iterator(); argIt.hasNext(); ) {
                final Expr arg = (Expr) argIt.next();
                if( arg.type() instanceof BDDType ) break found_bdd;
            }
            if( mi.returnType() instanceof BDDType ) break found_bdd;
            return n;
        }

        MethodDecl md = (MethodDecl) ts.instance2Decl().get(mi);
        if( md == null ) {
            throw new SemanticException( "Call to "+mi.container()+":"+mi+" but I don't have its code to analyze." );
        }

        Iterator formalIt = md.formals().iterator();
        for( Iterator argIt = n.arguments().iterator(); argIt.hasNext(); ) {
            final Expr arg = (Expr) argIt.next();
            Formal formal = (Formal) formalIt.next();

            Type t = arg.type();
            if( !(t instanceof BDDType) ) continue;
            BDDType type = (BDDType) t;
            for( Iterator domainIt = type.map().keySet().iterator(); domainIt.hasNext(); ) {
                final Type domain = (Type) domainIt.next();
                ts.addAssignEdge( DNode.v( arg, domain ),
                        DNode.v( formal.localInstance(), domain ) );
            }
        }
        if( md.returnType().type() instanceof BDDType ) {
            BDDType type = (BDDType) md.returnType().type();
            for( Iterator domainIt = type.map().keySet().iterator(); domainIt.hasNext(); ) {
                final Type domain = (Type) domainIt.next();
                ts.addAssignEdge( DNode.v( mi, domain ), DNode.v( n, domain ) );
            }
        }
        return n;
    }
}

