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

/** Interleaves the BDD variables of a single physical domain with
 * themselves. The period specifies the difference between successive
 * bits in the ordering. For example, if the physical domain has 20 bits
 * and the period is set to 5, the ordering will be:
 * 0, 5, 10, 15, 1, 6, 11, 16, 2, 7, 12, 17, 3, 8, 13, 18, 4, 9, 14, 19.
 */
public class Permute implements Order {
    private int period;
    private Order child;
    public Permute( int period, Order child ) {
        this.period = period;
        this.child = child;
    }
    public List listBits() {
        List ret = new ArrayList();
        List childBits = Rev.reverse(child.listBits());
        for( int i = 0; i < period; i++ ) {
            for( int j = i; j < childBits.size(); j += period ) {
                ret.add(childBits.get(j));
            }
        }
        return Rev.reverse(ret);
    }
}
