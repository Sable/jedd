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
 * physical domains are interleaved, but asummetrically (taking k1
 * bits from one child, then k2 bits from the next child, etc.). */
public class AsymInterleave implements Order {
    private List children;
    private List counts;
    public AsymInterleave( List children, List counts ) {
        this.children = new ArrayList();
        for( Iterator childIt = children.iterator(); childIt.hasNext(); ) {
            final Order child = (Order) childIt.next();
            this.children.add(child);
        }
        this.counts = new ArrayList();
        for( Iterator countIt = counts.iterator(); countIt.hasNext(); ) {
            final Integer count = (Integer) countIt.next();
            this.counts.add(count);
        }
    }
    public AsymInterleave( Order o1, int k1 ) {
        this.children = new ArrayList();
        this.children.add(o1);
        this.counts = new ArrayList();
        this.counts.add(new Integer(k1));
    }
    public AsymInterleave( Order o1, int k1, Order o2, int k2 ) {
        this(o1, k1);
        this.children.add(o2);
        this.counts.add(new Integer(k2));
    }
    public AsymInterleave( Order o1, int k1, Order o2, int k2, Order o3, int k3 ) {
        this(o1, k1, o2, k2);
        this.children.add(o3);
        this.counts.add(new Integer(k3));
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
            Iterator countIt = counts.iterator();
            for( Iterator itIt = iterators.iterator(); itIt.hasNext(); ) {
                final Iterator it = (Iterator) itIt.next();
                Integer count = (Integer) countIt.next();
                int k = count.intValue();
                for( int i = 0; i < k; i++ ) {
                    if(it.hasNext()) {
                        change = true;
                        ret.add(it.next());
                    }
                }
            }
            if(!change) break;
        }

        return Rev.reverse(ret);
    }
}
