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
import polyglot.types.*;
import polyglot.ast.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

public class JeddMethodDeclExt_c extends JeddExt_c implements JeddTypeCheck, MethodDeclMap
{
    public Node typeCheck( TypeChecker tc ) throws SemanticException {
        JeddTypeSystem ts = (JeddTypeSystem) tc.typeSystem();

        MethodDecl n = (MethodDecl) node();

        n = (MethodDecl) n.typeCheck(tc);
        for( Iterator mjIt = ts.overrides( n.methodInstance() ).iterator(); mjIt.hasNext(); ) {
            final MethodInstance mj = (MethodInstance) mjIt.next();
            ts.makeBDDFormalsConform(n.methodInstance(), mj);
        }
        for( Iterator mjIt = ts.implemented( n.methodInstance() ).iterator(); mjIt.hasNext(); ) {
            final MethodInstance mj = (MethodInstance) mjIt.next();
            ts.makeBDDFormalsConform(n.methodInstance(), mj);
        }
        return n;
    }
    public Node methodDeclMap( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        MethodDecl n = (MethodDecl) node();

        ts.instance2Decl().put( n.methodInstance(), n );
        return n;
    }
}

