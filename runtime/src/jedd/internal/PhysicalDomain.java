/* Jedd - A language for implementing relations using BDDs
 * Copyright (C) 2003, 2004, 2005 Ondrej Lhotak
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

package jedd.internal;
import java.util.*;

public abstract class PhysicalDomain implements jedd.order.Order {
    public abstract int bits();

    public String name() {
        return this.getClass().toString();
    }

    public PhysicalDomain() {
        firstBit = nextBit;
        nextBit += bits();
        Backend.v().addBits(bits());
        domNum = nextDomNum++;
        Jedd.v().physicalDomains.add(this);
        minPhysPos = firstBit;
        maxPhysPos = firstBit+bits()-1;
    }

    private int firstBit;
    public int firstBit() { return firstBit; }
    public int bitAfterLast() { return firstBit + bits(); }
    public List listBits() {
        LinkedList ret = new LinkedList();
        for( int i = 0; i < bits(); i++ ) {
            ret.addFirst(new Integer(i+firstBit));
        }
        return ret;
    }

    public int[] getBits() {
        int[] ret = new int[bits()];
        for( int i = 0; i < bits(); i++ ) ret[i] = i+firstBit;
        return ret;
    }

    static int nextBit = 0;
    
    static int nextDomNum = 0;
    int domNum;
    int maxPhysPos = 0;
    int minPhysPos = Integer.MAX_VALUE;
    public void clearPhysPos() {
        maxPhysPos = 0;
        minPhysPos = Integer.MAX_VALUE;
    }
    public void setPhysPos(int pos) { 
        if( pos < minPhysPos ) minPhysPos = pos;
        if( pos > maxPhysPos ) maxPhysPos = pos;
    }
    public boolean hasBit(int bit) {
        if(bit < firstBit) return false;
        if(bit >= firstBit + bits()) return false;
        return true;
    }
}
