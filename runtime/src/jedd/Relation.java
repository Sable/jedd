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

package jedd;

import java.util.*;

public interface Relation {
    /** Returns the number of tuples in the relation. */
    public long size();
    /** Returns the number of tuples in the relation. */
    public double fsize();
    /** Returns the number of BDD nodes used to represent the relation. */
    public int numNodes();
    /** Returns an iterator over the tuples in the relation. Each tuple
     * is returned as an Object array of its components. The argument to this
     * method must be an array containing the attributes of this relation.
     * In each tuple array returned by the iterator, the components will
     * appear in the same order as the order of the attributes in the
     * wanted argument to this method.
     *
     * When a relation only has a single attribute, it is easier to iterate
     * over it using the other iterator method. The present iterator is more
     * general in that it works for relations with arbitrary numbers of
     * attributes.
     */
    public Iterator iterator(Attribute[] wanted);
    /** Returns an iterator over the components in the relation. This method
     * may only be used on relations having exactly one attribute. The
     * iterator returns the single component of each tuple, one tuple at a
     * time.
     *
     * This iterator is easier to use than the iterator above, but works only
     * on relations with a single attribute.
     */
    public Iterator iterator();
    /** Returns a string representation listing all the tuples in the relation. */
    public String toString();
    /** Shift the bits in the relation using the provided shifter. */
    public Relation applyShifter( Jedd.Shifter s );
    /** Undocumented */
    public Relation add( Attribute srca, PhysicalDomain srcpd, Attribute dsta, PhysicalDomain dstpd, long offset );
    /** Undocumented */
    public int width( PhysicalDomain pd );
}
