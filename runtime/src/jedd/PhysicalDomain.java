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

public abstract class PhysicalDomain {
    public abstract int bits();

    public String name() {
        return this.getClass().toString();
    }

    public PhysicalDomain() {
        firstBit = nextBit;
        nextBit += bits();
        JeddNative.addBits(bits());
        domNum = nextDomNum++;
    }

    public void setBits( int[] bits, int value ) {
        int bit = firstBit;
        for( int i = 0; i < bits(); i++ ) {
            bits[bit] = value & 1;
            bit++;
            value >>= 1;
        }
        if( value != 0 ) throw new RuntimeException( "Value was too large in domain "+name()+"!" );
    }
    public int readBits( int[] bits ) {
        int ret = 0;
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

    public int cube() {
        if( cube == 0 ) {
            int[] cd = Jedd.v().convertDomains( new PhysicalDomain[] { this } );
            cube = JeddNative.makecube( cd.length, cd );
        }
        return cube;
    }

    public int repl( PhysicalDomain to ) {
        if( pairs[domNum][to.domNum] == 0 ) {
            int[] cd = Jedd.v().convertDomains( new PhysicalDomain[] { this } );
            int[] tcd = Jedd.v().convertDomains( new PhysicalDomain[] { to } );
            pairs[domNum][to.domNum] = 
                JeddNative.makepair( cd.length, cd, tcd.length, tcd );
        }
        return pairs[domNum][to.domNum];
    }

    int cube = 0;

    static int nextBit = 0;
    
    static int nextDomNum = 0;
    int domNum;
    
    static int[][] pairs = new int[32][32];
}
