/* Jedd - Java extension for decision diagrams
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

public class Jedd {
    // set profiler to null to turn off profiling
    //static final JeddProfiler profiler = null;
    static final JeddProfiler profiler = JeddProfiler.v();

    private static Jedd instance = new Jedd();
    private Jedd() {
        JeddNative.init();
    }
    public static Jedd v() {
        return instance;
    }
    public int replace( Relation r,
        PhysicalDomain[] from, PhysicalDomain[] to ) {
        int ret = replaceImpl( r.bdd, from, to );
        JeddNative.addRef( ret );
        return ret;
    }
    public int replace( int r,
            PhysicalDomain[] from, PhysicalDomain[] to ) {
        int ret = replaceImpl( r, from, to );
        JeddNative.addRef( ret );
        JeddNative.delRef( r );
        return ret;
    }
    public int project( Relation r, PhysicalDomain[] toRemove ) {
        int ret = projectImpl( r.bdd, toRemove );
        JeddNative.addRef( ret );
        return ret;
    }
    public int project( int r, PhysicalDomain[] toRemove ) {
        int ret = projectImpl( r, toRemove );
        JeddNative.addRef( ret );
        JeddNative.delRef( r );
        return ret;
    }
    public int relprod( int r1, int r2,
            PhysicalDomain[] d ) {
        int ret = relprodImpl( r1, r2, d );
        JeddNative.addRef( ret );
        JeddNative.delRef( r1 );
        JeddNative.delRef( r2 );
        return ret;
    }
    public int relprod( int r1, Relation r2,
            PhysicalDomain[] d ) {
        int ret = relprodImpl( r1, r2.bdd, d );
        JeddNative.addRef( ret );
        JeddNative.delRef( r1 );
        return ret;
    }
    public int falseBDD() { return JeddNative.falseBDD(); }
    public int trueBDD() { return JeddNative.trueBDD(); }

    public boolean equals(int r1, int r2) { 
        boolean ret = (r1 == r2);
        JeddNative.delRef(r1);
        JeddNative.delRef(r2);
        return ret;
    }
    public boolean equals(int r1, Relation r2) {
        boolean ret = (r1 == r2.bdd);
        JeddNative.delRef(r1);
        return ret;
    }

    public int union(int r1, int r2) { 
        int ret = JeddNative.or(r1,r2);
        JeddNative.delRef(r1);
        JeddNative.delRef(r2);
        JeddNative.addRef(ret);
        return ret;
    }
    public int union(int r1, Relation r2) {
        int ret = JeddNative.or(r1,r2.bdd);
        JeddNative.delRef(r1);
        JeddNative.addRef(ret);
        return ret;
    }

    public int intersect(int r1, int r2) { 
        int ret = JeddNative.and(r1,r2);
        JeddNative.delRef(r1);
        JeddNative.delRef(r2);
        JeddNative.addRef(ret);
        return ret;
    }
    public int intersect(int r1, Relation r2) {
        int ret = JeddNative.and(r1,r2.bdd);
        JeddNative.delRef(r1);
        JeddNative.addRef(ret);
        return ret;
    }

    public int minus(int r1, int r2) { 
        int ret = JeddNative.minus(r1,r2);
        JeddNative.delRef(r1);
        JeddNative.delRef(r2);
        JeddNative.addRef(ret);
        return ret;
    }
    public int minus(int r1, Relation r2) {
        int ret = JeddNative.minus(r1,r2.bdd);
        JeddNative.delRef(r1);
        JeddNative.addRef(ret);
        return ret;
    }

    public int literal( Object[] exprs, Domain[] domains, PhysicalDomain[] phys ) {
        int[] bits = new int[PhysicalDomain.nextBit];
        Arrays.fill(bits,2);
        for( int i = 0; i < exprs.length; i++ ) {
            phys[i].setBits(bits, domains[i].numberer().get( exprs[i] ) );
        }
        return JeddNative.literal( bits.length, bits );
    }

    public void setOrder( Object[] order, boolean msbAtTop ) {
        List newOrder = new ArrayList();

        for( int i = 0; i < order.length; i++ ) {
            Object o = order[i];
            if( o instanceof PhysicalDomain ) {
                PhysicalDomain pd = (PhysicalDomain) o;
                int[] vars = pd.getBits();
                if( msbAtTop ) reverse( vars );
                for( int k = 0; k < vars.length; k++ ) {
                    newOrder.add( new Integer( vars[k] ) );
                }
            } else if( o instanceof Object[] ) {
                PhysicalDomain[] domains = (PhysicalDomain[]) o;
                int[][] vars = new int[domains.length][];
                for( int j = 0; j < domains.length; j++ ) {
                    vars[j] = domains[j].getBits();
                    if( msbAtTop ) reverse( vars[j] );
                }
                boolean change = true;
                for( int j = 0; change; j++ ) {
                    change = false;
                    for( int k = 0; k < vars.length; k++ ) {
                        if( j < vars[k].length ) {
                            newOrder.add( new Integer( vars[k][j] ) );
                            change = true;
                        }
                    }
                }
            } else throw new RuntimeException();
        }
        int[] buddyOrder = new int[newOrder.size()];
        if( buddyOrder.length != JeddNative.numBits() ) {
            throw new RuntimeException( "Not all domains in variable order" );
        }
        int j = 0;
        for( Iterator iIt = newOrder.iterator(); iIt.hasNext(); ) {
            final Integer i = (Integer) iIt.next();
            buddyOrder[j++] = i.intValue();
        }
        JeddNative.setOrder( buddyOrder.length, buddyOrder );
    }
    public int read( int r ) { return r; }
    public int read( Relation r ) { return r.bdd(); }
    public void info() {
        JeddNative.info();
    }
    public int numNodes( Relation r ) {
        return JeddNative.numNodes(r.bdd);
    }
    public int numNodes( int r ) {
        int ret = JeddNative.numNodes(r);
        JeddNative.delRef(r);
        return ret;
    }

    public int numPaths( Relation r ) {
        return JeddNative.numPaths(r.bdd);
    }
    public int numPaths( int r ) {
        int ret = JeddNative.numPaths(r);
        JeddNative.delRef(r);
        return ret;
    }

    private int[] convertDomains( PhysicalDomain[] d ) {
        int n = 0;
        for( int i = 0; i < d.length; i++ ) n += d[i].bits();
        int[] ret = new int[n];

        int nextbit = 0;
        for( int i = 0; i < d.length; i++ ) {
            int bit = d[i].firstBit();
            for( int j = 0; j < d[i].bits(); j++ ) {
                ret[nextbit++] = bit++;
            }
        }
        return ret;
    }
    private int replaceImpl( int r,
            PhysicalDomain[] from, PhysicalDomain[] to ) {
        int[] cfrom = convertDomains(from);
        int[] cto = convertDomains(to);
        if( cfrom.length != cto.length ) throw new RuntimeException();

        if( profiler != null ) profiler.start( "replace", r );
        int ret = JeddNative.replace( r, cfrom.length, cfrom, cto );
        if( profiler != null ) profiler.finish( "replace", ret );

        return ret;
    }
    private int projectImpl( int r, PhysicalDomain[] toRemove ) {
        int[] ctoRemove = convertDomains(toRemove);

        if( profiler != null ) profiler.start( "project", r );
        int ret = JeddNative.project( r, ctoRemove.length, ctoRemove );
        if( profiler != null ) profiler.finish( "project", ret );

        return ret;
    }
    private int relprodImpl( int r1, int r2,
            PhysicalDomain[] d ) {
        int[] cd = convertDomains(d);

        if( profiler != null ) profiler.start( "relprod", r1, r2 );
        int ret = JeddNative.relprod( r1, r2, cd.length, cd );
        if( profiler != null ) profiler.finish( "relprod", ret );

        return ret;
    }
    private static void reverse( int[] a ) {
        int i = a.length-1;
        int j = 0;
        while( j < i ) {
            int t = a[i];
            a[i] = a[j];
            a[j] = t;
            j++;
            i--;
        }
    }
}
