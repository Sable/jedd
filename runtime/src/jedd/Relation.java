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

public class Relation {
    static {
        // make sure Jedd is initialized before doing anything
        Jedd jedd = Jedd.v();
    }
    int bdd;
    Attribute[] domains;
    PhysicalDomain[] phys;
    public int bdd() {
        JeddNative.addRef(bdd);
        return bdd;
    }

    public Relation( Attribute[] domains, PhysicalDomain[] phys ) {
        this.domains = domains;
        this.phys = phys;
        bdd = JeddNative.falseBDD();
    }
    public Relation( Attribute[] domains, PhysicalDomain[] phys, Relation r ) {
        this.domains = domains;
        this.phys = phys;
        bdd = r.bdd;
        JeddNative.addRef(bdd);
    }
    public Relation( Attribute[] domains, PhysicalDomain[] phys, int r ) {
        this.domains = domains;
        this.phys = phys;
        bdd = r;
        //JeddNative.addRef(bdd);
        //JeddNative.delRef(r);
    }

    public Relation eq( int rhs ) {
        JeddNative.delRef(bdd);
        bdd = rhs;
        //JeddNative.addRef(bdd);
        //JeddNative.delRef(rhs);
        return this;
    }
    public Relation eq( Relation r ) {
        JeddNative.delRef(bdd);
        bdd = r.bdd();
        return this;
    }

    public Relation eqUnion( int rhs ) {
        //if( Jedd.profiler != null ) Jedd.profiler.start( "eqUnion", bdd, rhs );
        int newBdd = JeddNative.or( bdd, rhs );
        JeddNative.addRef(newBdd);
        JeddNative.delRef(bdd);
        JeddNative.delRef(rhs);
        bdd = newBdd;
        //if( Jedd.profiler != null ) Jedd.profiler.finish( "eqUnion", bdd );
        return this;
    }

    public Relation eqUnion( Relation r ) {
        if( Jedd.profiler != null ) Jedd.profiler.start( "eqUnion", bdd, r.bdd );
        int newBdd = JeddNative.or( bdd, r.bdd );
        JeddNative.addRef(newBdd);
        JeddNative.delRef(bdd);
        bdd = newBdd;
        if( Jedd.profiler != null ) Jedd.profiler.finish( "eqUnion", bdd );
        return this;
    }

    public Relation eqIntersect( int rhs ) {
        if( Jedd.profiler != null ) Jedd.profiler.start( "eqIntersect", bdd, rhs );
        int newBdd = JeddNative.and( bdd, rhs );
        JeddNative.addRef(newBdd);
        JeddNative.delRef(bdd);
        JeddNative.delRef(rhs);
        bdd = newBdd;
        if( Jedd.profiler != null ) Jedd.profiler.finish( "eqIntersect", bdd );
        return this;
    }

    public Relation eqIntersect( Relation r ) {
        if( Jedd.profiler != null ) Jedd.profiler.start( "eqIntersect", bdd, r.bdd );
        int newBdd = JeddNative.and( bdd, r.bdd );
        JeddNative.addRef(newBdd);
        JeddNative.delRef(bdd);
        bdd = newBdd;
        if( Jedd.profiler != null ) Jedd.profiler.finish( "eqIntersect", bdd );
        return this;
    }

    public Relation eqMinus( int rhs ) {
        if( Jedd.profiler != null ) Jedd.profiler.start( "eqMinus", bdd, rhs );
        int newBdd = JeddNative.minus( bdd, rhs );
        JeddNative.addRef(newBdd);
        JeddNative.delRef(bdd);
        JeddNative.delRef(rhs);
        bdd = newBdd;
        if( Jedd.profiler != null ) Jedd.profiler.finish( "eqMinus", bdd );
        return this;
    }

    public Relation eqMinus( Relation r ) {
        if( Jedd.profiler != null ) Jedd.profiler.start( "eqMinus", bdd, r.bdd );
        int newBdd = JeddNative.minus( bdd, r.bdd );
        JeddNative.addRef(newBdd);
        JeddNative.delRef(bdd);
        bdd = newBdd;
        if( Jedd.profiler != null ) Jedd.profiler.finish( "eqMinus", bdd );
        return this;
    }

    public void finalize() {
        JeddNative.delRef(bdd);
    }

    public int size() {
        int vars = 0;
        for( int i = 0; i < phys.length; i++ ) vars += phys[i].bits();
        return JeddNative.satCount(bdd, vars);
    }
    public int numNodes() {
        return JeddNative.numNodes( bdd );
    }

    public Iterator iterator(Attribute[] wanted) {
        if( domains.length != wanted.length ) {
            throw new RuntimeException( "Attribute count doesn't match" );
        }
        PhysicalDomain[] physWanted = new PhysicalDomain[wanted.length];
        for( int i=0; i < wanted.length; i++ ) {
            for( int j = 0; j < domains.length; j++ ) {
                if( domains[j] == wanted[i] )
                    physWanted[i] = phys[j];
            }
        }
        return new MultiRelationIterator( bdd, physWanted, wanted );
    }
    
    class MultiRelationIterator implements Iterator {
        MultiRelationIterator( int bdd, PhysicalDomain[] phys, Attribute[] domain ) {
            this.phys = phys;
            this.domain = domain;
            this.ret = new Object[domain.length];
            this.ret2 = new Object[domain.length];
            ncubes = JeddNative.numPaths( bdd );
            nbits = JeddNative.numBits();
            cubes = new int[ ncubes * nbits ];
            JeddNative.allCubes( bdd, cubes );
            curcube = new int[nbits];
            if( ncubes > 0 ) newCube();
        }
        private int[] cubes;
        private int ncubes;
        private int nbits;
        private int current = 0;
        private int[] curcube;
        private Object[] ret;
        private Object[] ret2;
        private PhysicalDomain[] phys;
        private Attribute[] domain;

        public boolean hasNext() { return current < ncubes*nbits; }
        private void newCube() {
            
            for( int j = 0; j < phys.length; j++ ) {
                for( int i = phys[j].firstBit(); i < phys[j].bitAfterLast(); i++ ) {
                    if( cubes[i+current] == 1 ) curcube[i] = 1;
                    else curcube[i] = 0;
                }
            }
            curCubeToObject();
        }
        private void curCubeToObject() {
            for( int i = 0; i < domain.length; i++ )
                ret[i] = domain[i].numberer().get( phys[i].readBits( curcube ) );
        }
        private void advance() {
            if( current >= ncubes*nbits ) throw new RuntimeException( "advancing past end of iterator" );

            for( int j = 0; j < phys.length; j++ ) {
                for( int i = phys[j].bitAfterLast()-1; i >= phys[j].firstBit(); i-- ) {
                    if( cubes[current+i] == 0 ) continue;
                    if( cubes[current+i] == 1 ) continue;
                    if( curcube[i] == 1 ) curcube[i] = 0;
                    else {
                        curcube[i] = 1;
                        curCubeToObject();
                        return;
                    }
                }
            }
            current += nbits;
            if( current < ncubes*nbits ) newCube();
        }
        public Object next() {
            for( int i = 0; i < domain.length; i++ ) ret2[i] = ret[i];
            //debug();
            advance();
            return ret2;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator iterator() {
        if( domains.length != 1 ) {
            throw new RuntimeException( "Can only get iterator over single-domain BDD." );
        }
        return new RelationIterator( bdd, phys[0], domains[0] );
    }
    
    class RelationIterator implements Iterator {
        RelationIterator( int bdd, PhysicalDomain phys, Attribute domain ) {
            this.phys = phys;
            this.domain = domain;
            ncubes = JeddNative.numPaths( bdd );
            nbits = JeddNative.numBits();
            cubes = new int[ ncubes * nbits ];
            JeddNative.allCubes( bdd, cubes );
            curcube = new int[nbits];
            if( ncubes > 0 ) newCube();
        }
        private int[] cubes;
        private int ncubes;
        private int nbits;
        private int current = 0;
        private int[] curcube;
        private Object ret;
        private PhysicalDomain phys;
        private Attribute domain;

        public boolean hasNext() { return current < ncubes*nbits; }
        private void newCube() {
            
            for( int i = phys.firstBit(); i < phys.bitAfterLast(); i++ ) {
                if( cubes[i+current] == 1 ) curcube[i] = 1;
                else curcube[i] = 0;
            }
            curCubeToObject();
        }
        private void curCubeToObject() {
            ret = domain.numberer().get( phys.readBits( curcube ) );
        }
        private void advance() {
            if( current >= ncubes*nbits ) throw new RuntimeException( "advancing past end of iterator" );

            for( int i = phys.bitAfterLast()-1; i >= phys.firstBit(); i-- ) {
                if( cubes[current+i] == 0 ) continue;
                if( cubes[current+i] == 1 ) continue;
                if( curcube[i] == 1 ) curcube[i] = 0;
                else {
                    curcube[i] = 1;
                    curCubeToObject();
                    return;
                }
            }
            current += nbits;
            if( current < ncubes*nbits ) newCube();
        }
        public void debug() {
            System.out.println( "ncubes = "+ncubes+" nbits = "+nbits+" current = "+current );
            System.out.print( "cube is: " );
            for( int i = phys.firstBit(); i < phys.bitAfterLast(); i++ ) System.out.print( cubes[current+i] );
            System.out.println("");
            System.out.print( "full cube is: " );
            for( int i = 0; i < nbits; i++ ) System.out.print( cubes[current+i] );
            System.out.println("");
            System.out.println( "all cubes are: " );
            for( int i = 0; i < ncubes; i++ ) {
                for( int j = 0; j < nbits; j++ ) {
                    System.out.print( cubes[i*nbits+j] );
                }
                System.out.println("");
            }
            System.out.print( "curcube is: " );
            for( int i = phys.firstBit(); i < phys.bitAfterLast(); i++ ) System.out.print( curcube[i] );
            System.out.println("");
            System.out.println( "index is: "+phys.readBits( curcube ) );
            System.out.println( "object is: "+domain.numberer().get( phys.readBits( curcube ) ) );
        }
        public Object next() {
            Object r = ret;
            //debug();
            advance();
            return r;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void toString( String prefix, StringBuffer b, int domain, int bdd ) {
        if( domain >= domains.length ) {
            b.append( prefix+"\n" );
            return;
        }

        // Get an array of domains other than domain
        PhysicalDomain[] otherDomains = new PhysicalDomain[domains.length-1];
        int i = 0;
        int j = 0;
        for( ; i < domains.length; i++ ) {
            if( i == domain ) continue;
            otherDomains[j++] = phys[i];
        }

        // Project down to domain
        JeddNative.addRef(bdd);
        Relation domainBDD = new Relation(
                new Attribute[] { domains[domain] },
                new PhysicalDomain[] { phys[domain] },
                Jedd.v().project( bdd, otherDomains ) );

        // Iterate over all the values of domain
        for( Iterator oIt = domainBDD.iterator(); oIt.hasNext(); ) {
            final Object o = (Object) oIt.next();
            int literal = Jedd.v().literal(
                    new Object[] { o },
                    new Attribute[] { domains[domain] },
                    new PhysicalDomain[] { phys[domain] } );
            JeddNative.addRef(bdd);
            int restrictedBdd = Jedd.v().compose( bdd, literal, new PhysicalDomain[] { phys[domain] } );
            toString( prefix + o + ((domain == domains.length - 1) ? "]" : ", "),
                    b,
                    domain + 1,
                    restrictedBdd );
            JeddNative.delRef( restrictedBdd );
        }

        // Free up domainBDD
        domainBDD.eq(JeddNative.falseBDD());
    }
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append( "[" );
        for( int i=0; i < domains.length; i++ ) {
            b.append( domains[i].name() );
            if( i == domains.length-1 ) b.append("]\n");
            else b.append(", ");
        }
        toString("[", b, 0, bdd);
        return b.toString();
    }

}
