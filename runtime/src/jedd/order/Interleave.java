/* Jedd - A language for implementing relations using BDDs
 * Copyright (C) 2004 Ondrej Lhotak
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

package jedd.order;
import java.util.*;

/** Produces a BDD variable ordering in which the variables of the
 * physical domains are interleaved. */
public class Interleave implements Order {
    private List children;
    public Interleave( List children ) {
        this.children = new ArrayList();
        for( Iterator childIt = children.iterator(); childIt.hasNext(); ) {
            final Order child = (Order) childIt.next();
            this.children.add(child);
        }
    }
    public Interleave( Order o1 ) {
        this.children = new ArrayList();
        this.children.add(o1);
    }
    public Interleave( Order o1, Order o2 ) {
        this(o1);
        this.children.add(o2);
    }
    public Interleave( Order o1, Order o2, Order o3 ) {
        this(o1, o2);
        this.children.add(o3);
    }
    public Interleave( Order o1, Order o2, Order o3, Order o4 ) {
        this(o1, o2, o3);
        this.children.add(o4);
    }
    public Interleave( Order o1, Order o2, Order o3, Order o4, Order o5 ) {
        this(o1, o2, o3, o4);
        this.children.add(o5);
    }
    public Interleave( Order o1, Order o2, Order o3, Order o4, Order o5, Order o6 ) {
        this(o1, o2, o3, o4, o5);
        this.children.add(o6);
    }
    public Interleave( Order o1, Order o2, Order o3, Order o4, Order o5, Order o6, Order o7 ) {
        this(o1, o2, o3, o4, o5, o6);
        this.children.add(o7);
    }
    public Interleave( Order o1, Order o2, Order o3, Order o4, Order o5, Order o6, Order o7, Order o8 ) {
        this(o1, o2, o3, o4, o5, o6, o7);
        this.children.add(o8);
    }
    public List listBits() {
        List ret = new ArrayList();
        List iterators = new ArrayList();
        for( Iterator oIt = children.iterator(); oIt.hasNext(); ) {
            final Order o = (Order) oIt.next();
            iterators.add(Rev.reverse(o.listBits()).iterator());
        }
        while(true) {
            boolean change = false;
            for( Iterator itIt = iterators.iterator(); itIt.hasNext(); ) {
                final Iterator it = (Iterator) itIt.next();
                if(it.hasNext()) {
                    change = true;
                    ret.add(it.next());
                }
            }
            if(!change) break;
        }

        return Rev.reverse(ret);
    }
}
