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

package jedd.internal;
import java.util.*;

public abstract class PhysicalDomain {
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

    public void setBits( int[] bits, long value ) {
        int bit = firstBit;
        for( int i = 0; i < bits(); i++ ) {
            bits[bit] = (int) (value & 1L);
            bit++;
            value >>>= 1;
        }
        if( value != 0 ) throw new RuntimeException( "Value was too large in domain "+name()+"!" );
    }
    public long readBits( int[] bits ) {
        long ret = 0;
        int bit = firstBit+bits()-1;
        for( int i = 0; i < bits(); i++ ) {
            ret <<= 1;
            ret = ret | bits[bit];
            bit--;
        }
        return ret;
    }

    private int firstBit;
    public int firstBit() { return firstBit; }
    public int bitAfterLast() { return firstBit + bits(); }

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
}
