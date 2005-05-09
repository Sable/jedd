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
import jedd.*;
import jedd.order.*;

public class Jedd {
    private static boolean VERBOSE = false;
    private static Jedd instance = new Jedd();
    private Jedd() {
    }
    public static Jedd v() {
        return instance;
    }
    public void setBackend( String type ) {
        Backend.init( type );
    }
    public RelationInstance copy( RelationContainer r,
        PhysicalDomain[] from, Attribute[] fromAttr, PhysicalDomain[] to ) {
        RelationInstance ret = copyImpl( r.bdd, from, fromAttr, to );
        return ret;
    }
    public RelationInstance copy( RelationInstance r,
            PhysicalDomain[] from, Attribute[] fromAttr, PhysicalDomain[] to ) {
        RelationInstance ret = copyImpl( r, from, fromAttr, to );
        Backend.v().delRef( r );
        return ret;
    }
    public RelationInstance replace( RelationContainer r,
        PhysicalDomain[] from, PhysicalDomain[] to ) {
        RelationInstance ret = replaceImpl( r.bdd, from, to );
        return ret;
    }
    public RelationInstance replace( RelationInstance r,
            PhysicalDomain[] from, PhysicalDomain[] to ) {
        RelationInstance ret = replaceImpl( r, from, to );
        Backend.v().delRef( r );
        return ret;
    }
    public RelationInstance project( RelationContainer r, PhysicalDomain[] toRemove ) {
        RelationInstance ret = projectImpl( r.bdd, toRemove );
        return ret;
    }
    public RelationInstance project( RelationInstance r, PhysicalDomain[] toRemove ) {
        RelationInstance ret = projectImpl( r, toRemove );
        Backend.v().delRef( r );
        return ret;
    }
    public RelationInstance compose( RelationInstance r1, RelationInstance r2,
            PhysicalDomain[] d ) {
        RelationInstance ret = composeImpl( r1, r2, d );
        Backend.v().delRef( r1 );
        Backend.v().delRef( r2 );
        return ret;
    }
    public RelationInstance compose( RelationInstance r1, RelationContainer r2,
            PhysicalDomain[] d ) {
        RelationInstance ret = composeImpl( r1, r2.bdd, d );
        Backend.v().delRef( r1 );
        return ret;
    }
    public RelationInstance join( RelationInstance r1, RelationInstance r2,
            PhysicalDomain[] d ) {
        RelationInstance ret = joinImpl( r1, r2 );
        Backend.v().delRef( r1 );
        Backend.v().delRef( r2 );
        return ret;
    }
    public RelationInstance join( RelationInstance r1, RelationContainer r2,
            PhysicalDomain[] d ) {
        RelationInstance ret = joinImpl( r1, r2.bdd );
        Backend.v().delRef( r1 );
        return ret;
    }
    public RelationInstance falseBDD() { return Backend.v().falseBDD(); }
    public RelationInstance trueBDD() { return Backend.v().trueBDD(); }

    public boolean equals(RelationInstance r1, RelationInstance r2) { 
        boolean ret = Backend.v().equals(r1, r2);
        Backend.v().delRef(r1);
        Backend.v().delRef(r2);
        return ret;
    }
    public boolean equals(RelationInstance r1, RelationContainer r2) {
        boolean ret = Backend.v().equals(r1, r2.bdd);
        Backend.v().delRef(r1);
        return ret;
    }

    public RelationInstance union(RelationInstance r1, RelationInstance r2) { 
        RelationInstance ret = Backend.v().or(r1,r2);
        Backend.v().addRef(ret);
        Backend.v().delRef(r1);
        Backend.v().delRef(r2);
        return ret;
    }
    public RelationInstance union(RelationInstance r1, RelationContainer r2) {
        RelationInstance ret = Backend.v().or(r1,r2.bdd);
        Backend.v().addRef(ret);
        Backend.v().delRef(r1);
        return ret;
    }

    public RelationInstance intersect(RelationInstance r1, RelationInstance r2) { 
        RelationInstance ret = Backend.v().and(r1,r2);
        Backend.v().addRef(ret);
        Backend.v().delRef(r1);
        Backend.v().delRef(r2);
        return ret;
    }
    public RelationInstance intersect(RelationInstance r1, RelationContainer r2) {
        RelationInstance ret = Backend.v().and(r1,r2.bdd);
        Backend.v().addRef(ret);
        Backend.v().delRef(r1);
        return ret;
    }

    public RelationInstance minus(RelationInstance r1, RelationInstance r2) { 
        RelationInstance ret = Backend.v().minus(r1,r2);
        Backend.v().addRef(ret);
        Backend.v().delRef(r1);
        Backend.v().delRef(r2);
        return ret;
    }
    public RelationInstance minus(RelationInstance r1, RelationContainer r2) {
        RelationInstance ret;
        ret = Backend.v().minus(r1,r2.bdd);
        Backend.v().addRef(ret);
        Backend.v().delRef(r1);
        return ret;
    }

    public RelationInstance literal( Object[] exprs, Attribute[] attrs, PhysicalDomain[] phys ) {
        int[] bits = new int[PhysicalDomain.nextBit];
        Arrays.fill(bits,2);
        for( int i = 0; i < exprs.length; i++ ) {
            attrs[i].domain().setBits(
                    phys[i], bits, attrs[i].numberer().get(exprs[i]));
        }
        return Backend.v().literal( bits );
    }

    public void gbc() {
        Backend.v().gbc();
    }

    public void setOrder( Order order ) {
        for( Iterator pdIt = physicalDomains.iterator(); pdIt.hasNext(); ) {
            final PhysicalDomain pd = (PhysicalDomain) pdIt.next();
            pd.clearPhysPos();
        }
        List newOrder = order.listBits();
        int[] buddyOrder = new int[newOrder.size()];
        if( buddyOrder.length != Backend.v().numBits() ) {
            throw new RuntimeException( "Not all domains in variable order" );
        }
        int j = 0;
        for( Iterator iIt = newOrder.iterator(); iIt.hasNext(); ) {
            final Integer i = (Integer) iIt.next();
            for( Iterator pdIt = physicalDomains.iterator(); pdIt.hasNext(); ) {
                final PhysicalDomain pd = (PhysicalDomain) pdIt.next();
                if( pd.hasBit(i.intValue()) ) {
                    pd.setPhysPos(j);
                    if(VERBOSE) {
                        System.out.println( "Bit "+(i.intValue()-pd.firstBit())+" of "+pd );
                    }
                }
            }
            buddyOrder[j++] = i.intValue();
        }
        Backend.v().setOrder( buddyOrder );
    }
    public void allowReorder(boolean setting) {
        Backend.v().allowReorder(setting);
    }
    public RelationInstance read( RelationInstance r ) { return r; }
    public RelationInstance read( RelationContainer r ) { return r.bdd(); }
    public int numNodes( RelationContainer r ) {
        return Backend.v().numNodes(r.bdd);
    }
    public int numNodes( RelationInstance r ) {
        int ret = Backend.v().numNodes(r);
        Backend.v().delRef(r);
        return ret;
    }

    public int numPaths( RelationContainer r ) {
        return Backend.v().numPaths(r.bdd);
    }
    public int numPaths( RelationInstance r ) {
        int ret = Backend.v().numPaths(r);
        Backend.v().delRef(r);
        return ret;
    }
    class Shifter implements jedd.Jedd.Shifter {
        Backend.Projector p;
        Backend.Copier c;
    }
    public Shifter makeShifter( int[] fromBits, int[] toBits ) {
        Shifter ret = new Shifter();
        if( fromBits.length != toBits.length ) throw new RuntimeException();
        ret.p = Backend.v().makeProjector( toBits );
        ret.c = Backend.v().makeCopier( fromBits, toBits );
        return ret;
    }
    public RelationInstance cast( jedd.Relation r, Attribute[] attrs,
            PhysicalDomain[] phys ) {
        return ((RelationContainer) r).cast(attrs, phys);
    }
    int[] convertDomains( PhysicalDomain[] d ) {
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
    int[][] convertDomains( PhysicalDomain[] d1, PhysicalDomain[] d2 ) {
        int n = 0;
        if( d1.length != d2.length ) throw new RuntimeException();
        for( int i = 0; i < d1.length; i++ ) {
            int bits1 = d1[i].bits();
            int bits2 = d2[i].bits();
            if( bits1 < bits2 ) n += bits1;
            else n += bits2;
        }
        int[][] ret = new int[2][n];

        int nextbit = 0;
        for( int i = 0; i < d1.length; i++ ) {
            int bit1 = d1[i].firstBit();
            int bit2 = d2[i].firstBit();
            int bits1 = d1[i].bits();
            int bits2 = d2[i].bits();
            int bits = bits1;
            if( bits1 > bits2 ) bits = bits2;
            for( int j = 0; j < bits; j++ ) {
                ret[0][nextbit] = bit1++;
                ret[1][nextbit] = bit2++;
                nextbit++;
            }
        }
        return ret;
    }
    int[][] convertDomains( PhysicalDomain[] d1, Attribute[] a1, PhysicalDomain[] d2 ) {
        int n = 0;
        if( d1.length != d2.length ) throw new RuntimeException();
        if( d1.length != a1.length ) throw new RuntimeException();
        for( int i = 0; i < d1.length; i++ ) {
            n += a1[i].domain().numUsefulBits();
        }
        int[][] ret = new int[2][n];

        int nextbit = 0;
        for( int i = 0; i < d1.length; i++ ) {
            boolean[] usefulBits = a1[i].domain().usefulBits();
            for( int j = 0; j < usefulBits.length; j++ ) {
                if(usefulBits[j]) {
                    ret[0][nextbit] = d1[i].firstBit()+j;
                    ret[1][nextbit] = d2[i].firstBit()+j;
                    nextbit++;
                }
            }
        }
        return ret;
    }
    private List toList( PhysicalDomain from, PhysicalDomain to ) {
        return toList( new PhysicalDomain[] {from}, new PhysicalDomain[] {to} );
    }
    private List toList( PhysicalDomain[] from, Attribute[] fromAttr, PhysicalDomain[] to ) {
        return Arrays.asList( new List[] {
            Arrays.asList(from), Arrays.asList(fromAttr), Arrays.asList(to) } );
    }
    private List toList( PhysicalDomain[] from, PhysicalDomain[] to ) {
        return Arrays.asList( new List[] {
            Arrays.asList(from), Arrays.asList(to) } );
    }
    private RelationInstance copyImpl( RelationInstance r,
            PhysicalDomain[] from, Attribute[] fromAttr, PhysicalDomain[] to ) {
        RelationInstance ret;

        if( Profiler.enabled() ) Profiler.v().start( "copy", r );
        ret = Backend.v().copy( r,
                (Backend.Copier) copyCache.get( toList( from, fromAttr, to ) ) );
        Backend.v().addRef( ret );
        if( Profiler.enabled() ) Profiler.v().finish( "copy", ret );

        return ret;
    }
    private abstract static class Cache {
        private Map map = new HashMap();
        public Object get( Object key ) {
            Object ret = map.get(key);
            if( ret == null ) {
                map.put( key, ret = make(key) );
            }
            return ret;
        }
        public abstract Object make( Object key );
    }

    private Cache replCache = new Cache() {
        public Object make( Object key ) {
            List l = (List) key;
            PhysicalDomain[] from = new PhysicalDomain[0];
            from = (PhysicalDomain[]) ((List) l.get(0)).toArray(from);
            PhysicalDomain[] to = new PhysicalDomain[0];
            to = (PhysicalDomain[]) ((List) l.get(1)).toArray(to);
            int[][] converted = convertDomains(from, to);
            return Backend.v().makeReplacer( converted[0], converted[1] );
        }
    };

    private Cache copyCache = new Cache() {
        public Object make( Object key ) {
            List l = (List) key;
            PhysicalDomain[] from = new PhysicalDomain[0];
            from = (PhysicalDomain[]) ((List) l.get(0)).toArray(from);
            Attribute[] fromAttr = new Attribute[0];
            fromAttr = (Attribute[]) ((List) l.get(1)).toArray(fromAttr);
            PhysicalDomain[] to = new PhysicalDomain[0];
            to = (PhysicalDomain[]) ((List) l.get(2)).toArray(to);
            int[][] converted = convertDomains(from, fromAttr, to);
            return Backend.v().makeCopier( converted[0], converted[1] );
        }
    };

    private Cache projectCache = new Cache() {
        public Object make( Object key ) {
            PhysicalDomain[] proj = new PhysicalDomain[0];
            proj = (PhysicalDomain[]) ((List) key).toArray(proj);
            return Backend.v().makeProjector( convertDomains(proj) );
        }
    };

    private Cache addCache = new Cache() {
        public Object make( Object key ) {
            List l = (List) key;
            PhysicalDomain[] from = new PhysicalDomain[0];
            from = (PhysicalDomain[]) ((List) l.get(0)).toArray(from);
            PhysicalDomain[] to = new PhysicalDomain[0];
            to = (PhysicalDomain[]) ((List) l.get(1)).toArray(to);
            int[][] converted = convertDomains(from, to);
            return Backend.v().makeAdder( converted[0], converted[1] );
        }
    };


    private RelationInstance replaceImpl( RelationInstance r,
            PhysicalDomain[] from, PhysicalDomain[] to ) {
        RelationInstance ret;

        if( Profiler.enabled() ) Profiler.v().start( "replace", r );
        ret = Backend.v().replace( r, 
                (Backend.Replacer) replCache.get( toList( from, to ) ) );
        Backend.v().addRef( ret );
        if( Profiler.enabled() ) Profiler.v().finish( "replace", ret );

        return ret;
    }
    private RelationInstance projectImpl( RelationInstance r, PhysicalDomain[] toRemove ) {
        if( Profiler.enabled() ) Profiler.v().start( "project", r );
        RelationInstance ret = Backend.v().project( r, 
                (Backend.Projector) projectCache.get( Arrays.asList(toRemove) ) );
        Backend.v().addRef( ret );
        if( Profiler.enabled() ) Profiler.v().finish( "project", ret );

        return ret;
    }
    private RelationInstance composeImpl( RelationInstance r1, RelationInstance r2,
            PhysicalDomain[] d ) {
        RelationInstance ret;

        if( Profiler.enabled() ) Profiler.v().start( "compose", r1, r2 );
        ret = Backend.v().relprod( r1, r2,
                (Backend.Projector) projectCache.get( Arrays.asList(d) ) );
        Backend.v().addRef( ret );
        if( Profiler.enabled() ) Profiler.v().finish( "compose", ret );

        return ret;
    }
    private RelationInstance joinImpl( RelationInstance r1, RelationInstance r2 ) {
        if( Profiler.enabled() ) Profiler.v().start( "join", r1, r2 );
        RelationInstance ret = Backend.v().and( r1, r2 );
        Backend.v().addRef( ret );
        if( Profiler.enabled() ) Profiler.v().finish( "join", ret );

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
    public final List physicalDomains = new ArrayList();
    public RelationInstance add( RelationContainer r,
        PhysicalDomain from, PhysicalDomain to, long offset ) {
        RelationInstance ret = addImpl( from, to, offset, r.bdd );
        return ret;
    }
    private RelationInstance addImpl(PhysicalDomain from,
            PhysicalDomain to, long offset, RelationInstance r) {
        RelationInstance ret;

        if( Profiler.enabled() ) Profiler.v().start( "add", r );
        ret = Backend.v().add( r,
                (Backend.Adder) addCache.get( toList( from, to ) ), offset );
        Backend.v().addRef( ret );
        if( Profiler.enabled() ) Profiler.v().finish( "add", ret );

        return ret;
    }
}
