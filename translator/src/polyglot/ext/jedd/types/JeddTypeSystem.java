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

import polyglot.ast.*;
import polyglot.types.*;
import java.util.*;

public interface JeddTypeSystem extends TypeSystem {
    // TODO: declare any new methods needed
    public BDDType BDDType( List domainPairs );
    public void addAssignEdge( DNode n1, DNode n2 );
    public void addMustEqualEdge( DNode n1, DNode n2 );
    public void physicalDomains() throws SemanticException;
    public ClassType jedd();
    public ClassType attribute();
    public ClassType physicalDomain();
    public ClassType relation();
    public BDDType sameDomains( BDDType t );
    public BDDType cloneDomains( BDDType t );
    public Map instance2Decl();
    public void makeBDDFormalsConform(MethodInstance mi, MethodInstance mj) throws SemanticException;
}
