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

public class Edge {
    public Object n1, n2;
    public Type d1, d2;
    Edge( Node n1, Type d1, Node n2, Type d2 ) {
        this.n1 = canon(n1);
        this.n2 = canon(n2);
        this.d1 = d1;
        this.d2 = d2;
    }
    private Object canon(Node n) {
        if( n instanceof Local ) return ((Local) n).localInstance();
        if( n instanceof Field ) return ((Field) n).fieldInstance();
        return n;
    }
    public boolean equals( Object o ) {
        if( !( o instanceof Edge ) ) return false;
        Edge other = (Edge) o;
        if( !n1.equals( other.n1 ) ) return false;
        if( !n2.equals( other.n2 ) ) return false;
        if( !d1.equals( other.d1 ) ) return false;
        if( !d2.equals( other.d2 ) ) return false;
        return true;
    }
    public int hashCode() {
        return n1.hashCode()+n2.hashCode()+d1.hashCode()+d2.hashCode();
    }
}

