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

import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.extension.*;
import polyglot.ast.*;
import polyglot.types.*;
import polyglot.util.*;
import polyglot.frontend.*;
import polyglot.visit.*;
import java.util.*;

public class Group {
    public Set dnodes = new HashSet();
    public static Set groups = new HashSet();
    public static Map dnodeToGroup = new HashMap();

    public static Group v( DNode dnode ) {
        Group ret = (Group) dnodeToGroup.get(dnode);
        if( ret == null ) {
            ret = new Group();
            ret.dnodes.add(dnode);
            groups.add(ret);
            dnodeToGroup.put( dnode, ret );
        }
        return ret;
    }

    public void merge( Group other ) {
        for( Iterator dnodeIt = other.dnodes.iterator(); dnodeIt.hasNext(); ) {
            final DNode dnode = (DNode) dnodeIt.next();
            dnodeToGroup.put( dnode, this );
            dnodes.add( dnode );
        }
        groups.remove( other );
    }
}

