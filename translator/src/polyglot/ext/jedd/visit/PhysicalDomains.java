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

package polyglot.ext.jedd.visit;

import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.extension.*;
import polyglot.ext.jedd.types.*;
import polyglot.ast.*;
import polyglot.types.*;
import polyglot.util.*;
import polyglot.frontend.*;
import polyglot.visit.*;
import java.util.*;

public class PhysicalDomains extends ContextVisitor
{
    public PhysicalDomains( Job job, TypeSystem ts, NodeFactory nf ) {
        super(job, ts, nf);
    }
    public Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
        n = super.leaveCall(old, n, v);

        if (n.ext() instanceof JeddExt) {
            return ((JeddExt) n.ext()).physicalDomains(this);
        } else if (n instanceof JeddPhysicalDomains) {
            return ((JeddPhysicalDomains) n).physicalDomains( this );
        }

        return n;
    }
    public JeddTypeSystem jeddTypeSystem() {
        return (JeddTypeSystem) typeSystem();
    }
    public JeddNodeFactory jeddNodeFactory() {
        return (JeddNodeFactory) nodeFactory();
    }
}

