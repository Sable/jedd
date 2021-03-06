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
import jedd.internal.cudd.*;

public class CuddBackend extends Backend {
    private SWIGTYPE_p_DdManager manager;
    protected SWIGTYPE_p_DdNode bdd( RelationInstance in ) {
        return ((CuddInstance)in).bdd;
    }
    protected RelationInstance bdd( SWIGTYPE_p_DdNode in ) {
        return new CuddInstance( in );
    }

    synchronized void init() {
        System.loadLibrary("jeddcudd");
        manager = Cudd.Cudd_Init(0,0,Cudd.CUDD_UNIQUE_SLOTS*4,Cudd.CUDD_CACHE_SLOTS,0);
    }
    protected int totalBits = 0;
    int numBits() {
        return totalBits;
    }
    synchronized void addBits( int bits ) {
        totalBits += bits;
        while(bits-- > 0) Cudd.Cudd_bddNewVar(manager);
    }

    private int refs = 0;
    synchronized void addRef( RelationInstance bdd ) {
        refs++;
        Cudd.Cudd_Ref(bdd(bdd));
    }
    synchronized void delRef( RelationInstance bdd ) {
        refs--;
        //System.out.println( "refs = "+refs+":"+Cudd.Cudd_CheckZeroRef(manager) );
        //Cudd.Cudd_RecursiveDeref(manager, bdd(bdd));
        Cudd.Cudd_IterDerefBdd(manager, bdd(bdd));
    }

    // return value of following functions *is* refed
    synchronized RelationInstance falseBDD() {
        RelationInstance ret = bdd( Cudd.Cudd_ReadLogicZero(manager) );
        addRef( ret );
        return ret;
    }
    synchronized RelationInstance trueBDD() {
        RelationInstance ret = bdd( Cudd.Cudd_ReadOne(manager) );
        addRef( ret );
        return ret;
    }
    synchronized RelationInstance literal( int bits[] ) {
        RelationInstance ret = bdd(Cudd.Cudd_CubeArrayToBdd(manager, bits));
        addRef(ret);
        return ret;
    }

    synchronized protected RelationInstance ithVar( int i ) {
        RelationInstance ret = bdd( Cudd.Cudd_bddIthVar(manager,i) );
        addRef( ret );
        return ret;
    }
    synchronized protected RelationInstance nithVar( int i ) {
        RelationInstance ithvar = ithVar(i);
        RelationInstance ret = bdd( Cudd.Cudd_bddNot(bdd(ithvar)) );
        delRef( ithvar );
        addRef( ret );
        return ret;
    }

    // return value of following functions is *not* refed
    synchronized Replacer makeReplacer( int from[], int to[] ) {
        bddPair pair = Cudd.newPair(from.length);
        Cudd.setPairs(manager, pair, from, to);
        return pair( pair );
    }
    synchronized RelationInstance replace( RelationInstance r, Replacer pair ) {
        return bdd( Cudd.swapVariables( manager, bdd(r), pair(pair) ) );
    }
    RelationInstance copy( RelationInstance r, Copier copier ) {
        return and( r, relpc( copier ) );
    }
    Copier makeCopier( int from[], int to[] ) {
        int i;
        RelationInstance ret = trueBDD();
        for( i=0; i < from.length; i++ ) {
            RelationInstance b1, b2;
            RelationInstance ivf = ithVar(from[i]);
            RelationInstance ivt = ithVar(to[i]);
            b1 = biimp( ivf, ivt );
            addRef( b1 );
            delRef( ivf );
            delRef( ivt );
            b2 = and( b1, ret );
            addRef( b2 );
            delRef( b1 );
            delRef( ret );
            ret = b2;
        }
        return relpc(ret);
    }

    synchronized Projector makeProjector( int domains[] ) {
        RelationInstance cube = bdd( Cudd.Cudd_IndicesToCube( manager, domains, domains.length ) );
        addRef(cube);
        return relpc( cube );
    }
    synchronized RelationInstance relprod( RelationInstance r1, RelationInstance r2, Projector proj ) {
        return bdd( Cudd.Cudd_bddAndAbstract(manager,
                    bdd(r1), bdd(r2), bdd(relpc(proj)) ) );

    }
    synchronized RelationInstance project( RelationInstance r, Projector proj ) {
        return bdd( Cudd.Cudd_bddExistAbstract(manager,
                    bdd(r), bdd(relpc(proj)) ) );
    }
    synchronized RelationInstance or( RelationInstance r1, RelationInstance r2 ) {
        return bdd( Cudd.Cudd_bddOr( manager, bdd(r1), bdd(r2) ) );
    }
    synchronized RelationInstance and( RelationInstance r1, RelationInstance r2 ) {
        return bdd( Cudd.Cudd_bddAnd( manager, bdd(r1), bdd(r2) ) );
    }
    synchronized RelationInstance biimp( RelationInstance r1, RelationInstance r2 ) {
        return bdd( Cudd.Cudd_bddXnor( manager, bdd(r1), bdd(r2) ) );
    }
    synchronized RelationInstance minus( RelationInstance r1, RelationInstance r2 ) {
        RelationInstance notr2 = bdd( Cudd.Cudd_bddNot( bdd(r2) ) );
        addRef( notr2 );
        RelationInstance ret = bdd(
                Cudd.Cudd_bddAnd(manager, bdd(r1), bdd(notr2) ) );
        delRef( notr2 );
        return ret;
    }

    synchronized boolean equals( RelationInstance r1, RelationInstance r2 ) {
        return Cudd.equals( bdd(r1), bdd(r2) ) != 0;
    }


    synchronized void setOrder( int level2var[] ) {
        Cudd.Cudd_ShuffleHeap( manager, level2var );
    }

    synchronized Iterator cubeIterator( final RelationInstance r ) {
        return new Iterator() {
            int[] cubes = new int[totalBits];
            SWIGTYPE_p_DdGen iterator = Cudd.firstCube(manager, bdd(r), cubes.length, cubes);
            synchronized public boolean hasNext() { return iterator != null && Cudd.isNull(iterator) == 0; }
            synchronized public Object next() {
                int[] ret = new int[totalBits];
                System.arraycopy( cubes, 0, ret, 0, totalBits );
                boolean done = (0 == Cudd.nextCube(iterator, cubes.length, cubes));
                if( done ) iterator = null;
                return ret;
            }
            public void remove() {
                throw new UnsupportedOperationException();
            }
            synchronized public void finalize() {
                if( iterator != null && Cudd.isNull(iterator) == 0 ) Cudd.freeIterator(iterator);
            }
        };
    }

    /*
    void allCubes( RelationInstance r, int cubes[] ) {
        Cudd.allCubes( manager, totalBits, bdd(r), cubes );
    }
    */

    synchronized int numNodes( RelationInstance r ) {
        return Cudd.Cudd_DagSize(bdd(r));
    }
    synchronized int numPaths( RelationInstance r ) {
        return (int) Cudd.Cudd_CountPathsToNonZero(bdd(r));
    }

    synchronized double fSatCount( RelationInstance r, int vars ) {
        double s = Cudd.Cudd_CountMinterm( manager, bdd(r), vars );
        return s;
    }

    synchronized long satCount( RelationInstance r, int vars ) {
        return (long) fSatCount(r, vars);
    }

    synchronized void gbc() {
    }

    synchronized void getShape( RelationInstance bdd, int shape[] ) {
    }

    private static class CuddReplacer implements Replacer {
        final bddPair pair;
        CuddReplacer( bddPair pair ) {
            this.pair = pair;
        }
    }
    private bddPair pair( Replacer in ) {
        return ((CuddReplacer)in).pair;
    }
    private Replacer pair( bddPair in ) {
        return new CuddReplacer( in );
    }
    public Adder makeAdder(int[] from, int[] to) {
        throw new RuntimeException("NYI");
    }
    public RelationInstance add(RelationInstance ri, Adder adder, long offset) {
        throw new RuntimeException("NYI");
    }
}
