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

/** Reverses the bits in the ordering. This can be used, for example,
 * to encode a physical domain with the MSB at the bottom of the BDD
 * (the default is the MSB at the top of the BDD).
 */
public class Rev implements Order {
    private Order child;
    public Rev( Order child ) {
        this.child = child;
    }
    public List listBits() {
        return reverse(child.listBits());
    }
    public static List reverse( List l ) {
        LinkedList ret = new LinkedList();
        for( Iterator oIt = l.iterator(); oIt.hasNext(); ) {
            final Object o = (Object) oIt.next();
            ret.addFirst(o);
        }
        return ret;
    }
}
