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

import polyglot.ast.*;
import polyglot.ext.jl.ast.*;
import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.types.*;
import polyglot.ext.jedd.visit.*;
import polyglot.types.*;
import polyglot.frontend.*;
import polyglot.visit.*;
import polyglot.util.*;
import java.util.*;

public class JeddExt_c extends Ext_c implements JeddExt {
    public Node physicalDomains(PhysicalDomains pd) throws SemanticException {
        Node n = node();
        if( n instanceof JeddPhysicalDomains ) {
            return ((JeddPhysicalDomains) n).physicalDomains( pd );
        }
        return n;
    }
    public Node generateJava(JeddTypeSystem ts, JeddNodeFactory nf) throws SemanticException {
        Node n = node();
        if( n instanceof JeddGenerateJava ) {
            return ((JeddGenerateJava) n).generateJava( ts, nf );
        }
        return n;
    }
    public Node methodDeclMap( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        Node n = node();
        if( n instanceof MethodDeclMap ) {
            return ((MethodDeclMap) n).methodDeclMap( ts, nf );
        }
        return n;
    }
    protected Expr newRelation( JeddTypeSystem ts, JeddNodeFactory nf, BDDType type, Node init ) throws SemanticException {
        Position p = node().position();
        
        List domains = new LinkedList();
        List phys = new LinkedList();
        for( Iterator pairIt = type.domainPairs().iterator(); pairIt.hasNext(); ) {
            final Type[] pair = (Type[]) pairIt.next();
            domains.add( nf.Call( p, nf.CanonicalTypeNode( p, pair[0] ), "v" ) );
            phys.add( nf.Call( p, nf.CanonicalTypeNode( p, pair[1] ), "v" ) );
        }

        List args = new LinkedList();
        args.add( nf.NewArray(
                    p,
                    nf.CanonicalTypeNode( p, ts.attribute() ),
                    new LinkedList(),
                    1,
                    nf.ArrayInit( p, domains ) ) );
        args.add( nf.NewArray(
                    p,
                    nf.CanonicalTypeNode( p, ts.physicalDomain() ),
                    new LinkedList(),
                    1,
                    nf.ArrayInit( p, phys ) ) );
        args.add( nf.StringLit( p, node.toString()+" at "+p.toString() ) );
        if( init != null ) args.add( init );

        return nf.New( p, nf.CanonicalTypeNode( p, ts.relation() ), args ).type(type);
    }
}
