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
import SableJBDD.bdd.*;

public class SableBackend extends Backend {
    protected JBDD bdd( RelationInstance in ) {
        return ((SableInstance)in).bdd;
    }
    protected RelationInstance bdd( JBDD in ) {
        return new SableInstance( in );
    }

    private JBddManager manager;
    synchronized void init() {
        manager = new JBddManager();
    }

    protected int totalBits = 0;
    int numBits() {
        return totalBits;
    }
    synchronized void addBits( int bits ) {
        while( bits-- > 0 ) {
            manager.newVariable( ""+(totalBits++) );
        }
    }

    void addRef( RelationInstance bdd ) {
    }
    void delRef( RelationInstance bdd ) {
    }

    // return value of following functions *is* refed
    synchronized RelationInstance falseBDD() {
        return bdd( manager.ZERO() );
    }
    synchronized RelationInstance trueBDD() {
        return bdd( manager.ONE() );
    }
    synchronized protected RelationInstance ithVar( int i ) {
        return bdd( manager.posBddOf(manager.getIthVariable(i)) );
    }
    synchronized protected RelationInstance nithVar( int i ) {
        return bdd( manager.negBddOf(manager.getIthVariable(i)) );
    }
    synchronized RelationInstance literal( int bits[] ) {
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
    synchronized RelationInstance replace( RelationInstance r, Replacer pair ) {
        return bdd( bdd(r).replace(pair(pair)) );
    }

    synchronized RelationInstance copy( RelationInstance r, Copier copier ) {
        return and( r, relpc( copier ) );
    }
    synchronized Copier makeCopier( int from[], int to[] ) {
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

    synchronized RelationInstance relprod( RelationInstance r1, RelationInstance r2, Projector proj ) {
        // TODO: implement combined and-exist in SableJBDD
        return bdd( bdd(r1).and(bdd(r2)).exist(bdd(relpc(proj))) );
    }
    synchronized RelationInstance project( RelationInstance r, Projector proj ) {
        return bdd( bdd(r).exist(bdd(relpc(proj))) );
    }
    synchronized RelationInstance or( RelationInstance r1, RelationInstance r2 ) {
        return bdd( bdd(r1).or( bdd(r2) ) );
    }
    synchronized RelationInstance and( RelationInstance r1, RelationInstance r2 ) {
        return bdd( bdd(r1).and( bdd(r2) ) );
    }
    synchronized RelationInstance biimp( RelationInstance r1, RelationInstance r2 ) {
        return bdd( bdd(r1).biimply( bdd(r2) ) );
    }
    synchronized RelationInstance minus( RelationInstance r1, RelationInstance r2 ) {
        return bdd( bdd(r1).diff( bdd(r2) ) );
    }

    synchronized boolean equals( RelationInstance r1, RelationInstance r2 ) {
        return bdd(r1).equals( bdd(r2) );
    }


    synchronized void setOrder( int level2var[] ) {
        // TODO: implement in SableJBDD
    }

    synchronized Iterator cubeIterator( RelationInstance r ) {
        // TODO: implement in SableJBDD
        throw new RuntimeException("NYI");
    }
    synchronized void allCubes( RelationInstance r, int cubes[] ) {
        // TODO: implement in SableJBDD
    }

    synchronized int numNodes( RelationInstance r ) {
        return bdd(r).size();
    }
    synchronized int numPaths( RelationInstance r ) {
        // TODO: implement in SableJBDD
        return 0;
    }

    synchronized long satCount( RelationInstance r, int vars ) {
        // TODO: implement in SableJBDD
        return 0;
    }

    synchronized double fSatCount( RelationInstance r, int vars ) {
        // TODO: implement in SableJBDD
        return 0;
    }

    synchronized void gbc() {
        // TODO: implement in SableJBDD
    }

    synchronized void getShape( RelationInstance bdd, int shape[] ) {
        // TODO: implement in SableJBDD
    }

    synchronized Projector makeProjector( int domains[] ) {
        RelationInstance ret = trueBDD();
        for( int i = 0; i < domains.length; i++ ) {
            ret = and( ret, ithVar( domains[i] ) );
        }
        return relpc( ret );
    }
    synchronized Replacer makeReplacer( int from[], int to[] ) {
        Map map = new HashMap();
        for( int i = 0; i < from.length; i++ ) {
            map.put( manager.getIthVariable( from[i] ),
                    manager.getIthVariable( to[i] ) );
        }
        return pair(map);
    }
    private static class SableReplacer implements Replacer {
        final Map pair;
        SableReplacer( Map pair ) {
            this.pair = pair;
        }
    }
    private Map pair( Replacer in ) {
        return ((SableReplacer)in).pair;
    }
    private Replacer pair( Map in ) {
        return new SableReplacer( in );
    }
    synchronized public Adder makeAdder(int[] from, int[] to) {
        throw new RuntimeException("NYI");
    }
    synchronized public RelationInstance add(RelationInstance ri, Adder adder, long offset) {
        throw new RuntimeException("NYI");
    }
}
