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

public class JeddFieldExt_c extends JeddExt_c implements JeddTypeCheck, JeddPhysicalDomains
{
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        JeddTypeSystem ts = (JeddTypeSystem) tc.typeSystem();
        JeddNodeFactory nf = (JeddNodeFactory) tc.nodeFactory();

        Field n = (Field) node().typeCheck(tc);
        VarInstance vi = n.fieldInstance();
        Type t = vi.type();
        if( !( t instanceof BDDType ) ) return n;
        BDDType bt = (BDDType) t;
        n = (Field) n.type( ts.cloneDomains( bt ) );
        return n;
    }
    public Node physicalDomains( PhysicalDomains pd ) throws SemanticException {
        JeddTypeSystem ts = pd.jeddTypeSystem();

        Field n = (Field) node();
        VarInstance vi = n.fieldInstance();
        Type t = vi.type();
        if( !( t instanceof BDDType ) ) return n;
        BDDType bt = (BDDType) t;
        for( Iterator domainIt = bt.map().keySet().iterator(); domainIt.hasNext(); ) {
            final Type domain = (Type) domainIt.next();
            ts.addMustEqualEdge( DNode.v( n, domain ), DNode.v( vi, domain ) );
        }
        return n;
    }
}

