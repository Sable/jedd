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

public class JeddLocalDeclExt_c extends JeddExt_c implements JeddTypeCheck
{
    public Node typeCheck( TypeChecker tc ) throws SemanticException {
        JeddNodeFactory nf = (JeddNodeFactory) tc.nodeFactory();
        JeddTypeSystem ts = (JeddTypeSystem) tc.typeSystem();

        LocalDecl n = (LocalDecl) node();
        if( n.init() == null ) return n;
        if( !( n.init().type() instanceof BDDType ) ) {
            if( n.localInstance().type() instanceof BDDType )
                throw new SemanticException( "Attempt to initialize BDD with non-BDD." );
            return n.typeCheck(tc);
        } else {
            if( !(n.localInstance().type() instanceof BDDType) ) 
                throw new SemanticException( "Attempt to initialize non-BDD with BDD." );
            if( !n.init().type().isImplicitCastValid( n.localInstance().type() ) )
                throw new SemanticException( "Incompatible type of BDD initializer." );
            return n.init( (Expr) nf.FixPhys( n.init().position(), n.init() ).typeCheck( tc ) );
        }
    }
    public Node physicalDomains( PhysicalDomains pd ) throws SemanticException {
        JeddTypeSystem ts = pd.jeddTypeSystem();

        LocalDecl n = (LocalDecl) super.physicalDomains(pd);
        if( n.init() == null ) return n;
        if( !( n.declType() instanceof BDDType ) ) return n;
        BDDType t = (BDDType) n.declType();
        for( Iterator domainIt = t.map().keySet().iterator(); domainIt.hasNext(); ) {
            final Type domain = (Type) domainIt.next();
            ts.addMustEqualEdge( DNode.v( n.init(), domain ),
                    DNode.v( n.localInstance(), domain ) );
        }
        return n;
    }


    
    public Node generateJava( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        LocalDecl n = (LocalDecl) node();
        if( !( n.declType() instanceof BDDType ) ) {
            return super.generateJava( ts, nf );
        }
        BDDType declType = (BDDType) n.localInstance().type();

        CanonicalTypeNode jeddRelation = 
            nf.CanonicalTypeNode( n.position(), ts.relation() );

        n = n.flags( n.flags().set( Flags.FINAL ) );
        n = n.type( jeddRelation );

        n = n.init( newRelation( ts, nf, declType, n.init() ) );

        return n;
    }
}

