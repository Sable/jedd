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
import org.sf.javabdd.*;

public class JavabddBackend extends Backend {
    protected BDD bdd( RelationInstance in ) {
        return ((JavabddInstance)in).bdd;
    }
    protected RelationInstance bdd( BDD in ) {
        return new JavabddInstance( in );
    }

    private BDDFactory manager;
    void init() {
        manager = BDDFactory.init("java", 1*1000*1000, 100*1000 );
    }

    protected int totalBits = 0;
    int numBits() {
        return totalBits;
    }
    void addBits( int bits ) {
        manager.extVarNum(bits);
        totalBits += bits;
    }

    void addRef( RelationInstance bdd ) {
        ((JavabddInstance)bdd).refcount++;
    }
    void delRef( RelationInstance bdd ) {
        if( --((JavabddInstance)bdd).refcount == 0 ) {
            bdd(bdd).free();
        }
    }

    // return value of following functions *is* refed
    RelationInstance falseBDD() {
        RelationInstance ret = bdd( manager.zero() );
        addRef( ret );
        return ret;
    }
    RelationInstance trueBDD() {
        RelationInstance ret = bdd( manager.one() );
        addRef( ret );
        return ret;
    }
    protected RelationInstance ithVar( int i ) {
        RelationInstance ret = bdd( manager.ithVar(i) );
        addRef( ret );
        return ret;
    }
    protected RelationInstance nithVar( int i ) {
        RelationInstance ret = bdd( manager.nithVar(i) );
        addRef( ret );
        return ret;
    }
    RelationInstance literal( int bits[] ) {
        int i = 0;
        RelationInstance ret = trueBDD();
        RelationInstance tmp;
        RelationInstance var;
        for( i = 0; i < bits.length; i++ ) {
            if( bits[i] == 0 ) {
                tmp = ret;
                ret = and( tmp, var = nithVar(i) );
                addRef(ret);
                delRef(var);
                delRef(tmp);
            } else if( bits[i] == 1 ) {
                tmp = ret;
                ret = and( tmp, var = ithVar(i) );
                addRef(ret);
                delRef(var);
                delRef(tmp);
            }
        }
        return ret;
    }


    // return value of following functions is *not* refed
    RelationInstance replace( RelationInstance r, Replacer pair ) {
        return bdd( bdd(r).replace( pair(pair) ) );
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

    RelationInstance relprod( RelationInstance r1, RelationInstance r2, Projector proj ) {
        RelationInstance ret = bdd( bdd(r1).relprod(bdd(r2), bdd(relpc(proj))) );
        return ret;
    }
    RelationInstance project( RelationInstance r, Projector proj ) {
        RelationInstance ret = bdd( bdd(r).exist( bdd(relpc(proj)) ) );
        return ret;
    }
    RelationInstance or( RelationInstance r1, RelationInstance r2 ) {
        return bdd( bdd(r1).or( bdd(r2) ) );
    }
    RelationInstance and( RelationInstance r1, RelationInstance r2 ) {
        return bdd( bdd(r1).and( bdd(r2) ) );
    }
    RelationInstance biimp( RelationInstance r1, RelationInstance r2 ) {
        return bdd( bdd(r1).biimp( bdd(r2) ) );
    }
    RelationInstance minus( RelationInstance r1, RelationInstance r2 ) {
        return bdd( bdd(r1).apply( bdd(r2), BDDFactory.diff ) );
    }

    boolean equals( RelationInstance r1, RelationInstance r2 ) {
        return bdd(r1).equals( bdd(r2) );
    }


    void setOrder( int level2var[] ) {
        manager.setVarOrder( level2var );
    }

    Iterator cubeIterator( RelationInstance r ) {
        throw new RuntimeException("NYI");
    }

    int numNodes( RelationInstance r ) {
        return bdd(r).nodeCount();
    }
    int numPaths( RelationInstance r ) {
        return (int) bdd(r).pathCount();
    }

    long satCount( RelationInstance r, int vars ) {
        double s = bdd(r).satCount();
        s /= Math.pow(2,totalBits-vars);
        return (long) s;
    }

    void gbc() {
        // TODO: implement in javabdd
    }

    void getShape( RelationInstance bdd, int shape[] ) {
        // TODO: implement in javabdd
    }

    Projector makeProjector( int domains[] ) {
        RelationInstance ret = trueBDD();
        for( int i = 0; i < domains.length; i++ ) {
            RelationInstance oldret = ret;
            RelationInstance ithVar = ithVar(domains[i]);
            ret = and( oldret, ithVar( domains[i] ) );
            addRef(ret);
            delRef(oldret);
            delRef(ithVar);
        }
        return relpc( ret );
    }
    Replacer makeReplacer( int from[], int to[] ) {
        BDDPairing pair = manager.makePair();
        pair.set( from, to );
        return pair( pair );
    }
    private static class JavabddReplacer implements Replacer {
        final BDDPairing pair;
        JavabddReplacer( BDDPairing pair ) {
            this.pair = pair;
        }
    }
    private BDDPairing pair( Replacer in ) {
        return ((JavabddReplacer)in).pair;
    }
    private Replacer pair( BDDPairing in ) {
        return new JavabddReplacer( in );
    }
}
