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

    void init() {
        System.loadLibrary("jeddcudd");
        manager = Cudd.Cudd_Init(0,0,Cudd.CUDD_UNIQUE_SLOTS*4,Cudd.CUDD_CACHE_SLOTS,0);
    }
    protected int totalBits = 0;
    int numBits() {
        return totalBits;
    }
    void addBits( int bits ) {
        totalBits += bits;
        while(bits-- > 0) Cudd.Cudd_bddNewVar(manager);
    }

    private int refs = 0;
    void addRef( RelationInstance bdd ) {
        refs++;
        Cudd.Cudd_Ref(bdd(bdd));
    }
    void delRef( RelationInstance bdd ) {
        refs--;
        //System.out.println( "refs = "+refs+":"+Cudd.Cudd_CheckZeroRef(manager) );
        //Cudd.Cudd_RecursiveDeref(manager, bdd(bdd));
        Cudd.Cudd_IterDerefBdd(manager, bdd(bdd));
    }

    // return value of following functions *is* refed
    RelationInstance falseBDD() {
        RelationInstance ret = bdd( Cudd.Cudd_ReadLogicZero(manager) );
        addRef( ret );
        return ret;
    }
    RelationInstance trueBDD() {
        RelationInstance ret = bdd( Cudd.Cudd_ReadOne(manager) );
        addRef( ret );
        return ret;
    }
    RelationInstance literal( int bits[] ) {
        RelationInstance ret = bdd(Cudd.Cudd_CubeArrayToBdd(manager, bits));
        addRef(ret);
        return ret;
    }

    protected RelationInstance ithVar( int i ) {
        RelationInstance ret = bdd( Cudd.Cudd_bddIthVar(manager,i) );
        addRef( ret );
        return ret;
    }
    protected RelationInstance nithVar( int i ) {
        RelationInstance ithvar = ithVar(i);
        RelationInstance ret = bdd( Cudd.Cudd_bddNot(bdd(ithvar)) );
        delRef( ithvar );
        addRef( ret );
        return ret;
    }

    // return value of following functions is *not* refed
    Replacer makeReplacer( int from[], int to[] ) {
        bddPair pair = Cudd.newPair(from.length);
        Cudd.setPairs(manager, pair, from, to);
        return pair( pair );
    }
    RelationInstance replace( RelationInstance r, Replacer pair ) {
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

    Projector makeProjector( int domains[] ) {
        RelationInstance cube = bdd( Cudd.Cudd_IndicesToCube( manager, domains, domains.length ) );
        addRef(cube);
        return relpc( cube );
    }
    RelationInstance relprod( RelationInstance r1, RelationInstance r2, Projector proj ) {
        return bdd( Cudd.Cudd_bddAndAbstract(manager,
                    bdd(r1), bdd(r2), bdd(relpc(proj)) ) );

    }
    RelationInstance project( RelationInstance r, Projector proj ) {
        return bdd( Cudd.Cudd_bddExistAbstract(manager,
                    bdd(r), bdd(relpc(proj)) ) );
    }
    RelationInstance or( RelationInstance r1, RelationInstance r2 ) {
        return bdd( Cudd.Cudd_bddOr( manager, bdd(r1), bdd(r2) ) );
    }
    RelationInstance and( RelationInstance r1, RelationInstance r2 ) {
        return bdd( Cudd.Cudd_bddAnd( manager, bdd(r1), bdd(r2) ) );
    }
    RelationInstance biimp( RelationInstance r1, RelationInstance r2 ) {
        return bdd( Cudd.Cudd_bddXnor( manager, bdd(r1), bdd(r2) ) );
    }
    RelationInstance minus( RelationInstance r1, RelationInstance r2 ) {
        RelationInstance notr2 = bdd( Cudd.Cudd_bddNot( bdd(r2) ) );
        addRef( notr2 );
        RelationInstance ret = bdd(
                Cudd.Cudd_bddAnd(manager, bdd(r1), bdd(notr2) ) );
        delRef( notr2 );
        return ret;
    }

    boolean equals( RelationInstance r1, RelationInstance r2 ) {
        return Cudd.equals( bdd(r1), bdd(r2) ) != 0;
    }


    void setOrder( int level2var[] ) {
        Cudd.Cudd_ShuffleHeap( manager, level2var );
    }

    void allCubes( RelationInstance r, int cubes[] ) {
        Cudd.allCubes( manager, totalBits, bdd(r), cubes );
    }

    int numNodes( RelationInstance r ) {
        return Cudd.Cudd_DagSize(bdd(r));
    }
    int numPaths( RelationInstance r ) {
        return (int) Cudd.Cudd_CountPathsToNonZero(bdd(r));
    }

    int satCount( RelationInstance r, int vars ) {
        double s = Cudd.Cudd_CountMinterm( manager, bdd(r), vars );
        return (int) s;
    }

    void gbc() {
    }

    void getShape( RelationInstance bdd, int shape[] ) {
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
}
