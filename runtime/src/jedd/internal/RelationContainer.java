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
import jedd.*;

public class RelationContainer implements Relation {
    RelationInstance bdd;
    private Attribute[] attributes;
    private PhysicalDomain[] phys;
    private String desc;

    public RelationInstance bdd() {
        Backend.v().addRef(bdd);
        return bdd;
    }

    public RelationContainer( Attribute[] attributes, PhysicalDomain[] phys, String desc ) {
        this.attributes = attributes;
        this.phys = phys;
        this.desc = desc;
        bdd = Backend.v().falseBDD();
    }
    public RelationContainer( Attribute[] attributes, PhysicalDomain[] phys, String desc, RelationContainer r ) {
        this.attributes = attributes;
        this.phys = phys;
        this.desc = desc;
        bdd = r.bdd;
        Backend.v().addRef(bdd);
    }
    public RelationContainer( Attribute[] attributes, PhysicalDomain[] phys, String desc, RelationInstance r ) {
        this.attributes = attributes;
        this.phys = phys;
        this.desc = desc;
        bdd = r;
        //Backend.v().addRef(bdd);
        //Backend.v().delRef(r);
    }

    public RelationContainer eq( RelationInstance rhs ) {
        if( bdd != null ) Backend.v().delRef(bdd);
        bdd = rhs;
        //Backend.v().addRef(bdd);
        //Backend.v().delRef(rhs);
        return this;
    }
    public RelationContainer eq( RelationContainer r ) {
        if( bdd != null ) Backend.v().delRef(bdd);
        bdd = r.bdd();
        return this;
    }
    public void kill() {
        eq((RelationInstance) null);
    }

    public RelationContainer eqUnion( RelationInstance rhs ) {
        //if( Profiler.enabled() ) Profiler.v().start( "eqUnion", bdd, rhs );
        RelationInstance newBdd = Backend.v().or( bdd, rhs );
        Backend.v().addRef(newBdd);
        Backend.v().delRef(bdd);
        Backend.v().delRef(rhs);
        bdd = newBdd;
        //if( Profiler.enabled() ) Profiler.v().finish( "eqUnion", bdd );
        return this;
    }

    public RelationContainer eqUnion( RelationContainer r ) {
        if( Profiler.enabled() ) Profiler.v().start( "eqUnion", bdd, r.bdd );
        RelationInstance newBdd = Backend.v().or( bdd, r.bdd );
        Backend.v().addRef(newBdd);
        Backend.v().delRef(bdd);
        bdd = newBdd;
        if( Profiler.enabled() ) Profiler.v().finish( "eqUnion", bdd );
        return this;
    }

    public RelationContainer eqIntersect( RelationInstance rhs ) {
        if( Profiler.enabled() ) Profiler.v().start( "eqIntersect", bdd, rhs );
        RelationInstance newBdd;
        newBdd = Backend.v().and( bdd, rhs );
        Backend.v().addRef(newBdd);
        Backend.v().delRef(bdd);
        Backend.v().delRef(rhs);
        bdd = newBdd;
        if( Profiler.enabled() ) Profiler.v().finish( "eqIntersect", bdd );
        return this;
    }

    public RelationContainer eqIntersect( RelationContainer r ) {
        if( Profiler.enabled() ) Profiler.v().start( "eqIntersect", bdd, r.bdd );
        RelationInstance newBdd;
        newBdd = Backend.v().and( bdd, r.bdd );
        Backend.v().addRef(newBdd);
        Backend.v().delRef(bdd);
        bdd = newBdd;
        if( Profiler.enabled() ) Profiler.v().finish( "eqIntersect", bdd );
        return this;
    }

    public RelationContainer eqMinus( RelationInstance rhs ) {
        if( Profiler.enabled() ) Profiler.v().start( "eqMinus", bdd, rhs );
        RelationInstance newBdd = Backend.v().minus( bdd, rhs );
        Backend.v().addRef(newBdd);
        Backend.v().delRef(bdd);
        Backend.v().delRef(rhs);
        bdd = newBdd;
        if( Profiler.enabled() ) Profiler.v().finish( "eqMinus", bdd );
        return this;
    }

    public RelationContainer eqMinus( RelationContainer r ) {
        if( Profiler.enabled() ) Profiler.v().start( "eqMinus", bdd, r.bdd );
        RelationInstance newBdd = Backend.v().minus( bdd, r.bdd );
        Backend.v().addRef(newBdd);
        Backend.v().delRef(bdd);
        bdd = newBdd;
        if( Profiler.enabled() ) Profiler.v().finish( "eqMinus", bdd );
        return this;
    }

    public void finalize() {
        Backend.v().delRef(bdd);
    }

    public long size() {
        int vars = 0;
        for( int i = 0; i < phys.length; i++ ) vars += phys[i].bits();
        return Backend.v().satCount(bdd, vars);
    }
    public int numNodes() {
        return Backend.v().numNodes( bdd );
    }

    public Iterator iterator(Attribute[] wanted) {
        if( attributes.length != wanted.length ) {
            throw new RuntimeException( "Attribute count doesn't match" );
        }
        PhysicalDomain[] physWanted = new PhysicalDomain[wanted.length];
        for( int i=0; i < wanted.length; i++ ) {
            for( int j = 0; j < attributes.length; j++ ) {
                if( attributes[j] == wanted[i] )
                    physWanted[i] = phys[j];
            }
            if( physWanted[i] == null ) {
                throw new RuntimeException( "Request for iterator with attribute "+wanted[i]+" on a relation of type "+typeToString() );
            }
        }
        return new MultiRelationIterator( bdd, physWanted, wanted );
    }
    
    class MultiRelationIterator implements Iterator {
        MultiRelationIterator( RelationInstance bdd, PhysicalDomain[] phys, Attribute[] attribute ) {
            this.phys = phys;
            this.attribute = attribute;
            this.ret = new Object[attribute.length];
            this.ret2 = new Object[attribute.length];
            ncubes = Backend.v().numPaths( bdd );
            nbits = Backend.v().numBits();
            cubes = new int[ ncubes * nbits ];
            Backend.v().allCubes( bdd, cubes );
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
        private Attribute[] attribute;

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
            for( int i = 0; i < attribute.length; i++ )
                ret[i] = attribute[i].numberer().get( phys[i].readBits( curcube ) );
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
            for( int i = 0; i < attribute.length; i++ ) ret2[i] = ret[i];
            //debug();
            advance();
            return ret2;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator iterator() {
        if( attributes.length != 1 ) {
            throw new RuntimeException( "Can only get iterator over single-attribute BDD." );
        }
        return new RelationIterator( bdd, phys[0], attributes[0] );
    }
    
    class RelationIterator implements Iterator {
        RelationIterator( RelationInstance bdd, PhysicalDomain phys, Attribute attribute ) {
            this.phys = phys;
            this.attribute = attribute;
            ncubes = Backend.v().numPaths( bdd );
            nbits = Backend.v().numBits();
            cubes = new int[ ncubes * nbits ];
            Backend.v().allCubes( bdd, cubes );
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
        private Attribute attribute;

        public boolean hasNext() { return current < ncubes*nbits; }
        private void newCube() {
            
            for( int i = phys.firstBit(); i < phys.bitAfterLast(); i++ ) {
                if( cubes[i+current] == 1 ) curcube[i] = 1;
                else curcube[i] = 0;
            }
            curCubeToObject();
        }
        private void curCubeToObject() {
            ret = attribute.numberer().get( phys.readBits( curcube ) );
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
            System.out.println( "object is: "+attribute.numberer().get( phys.readBits( curcube ) ) );
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

    private void toString( String prefix, StringBuffer b, int attribute, RelationInstance bdd ) {
        if( attribute >= attributes.length ) {
            b.append( prefix+"\n" );
            return;
        }

        // Get an array of attributes other than attribute
        PhysicalDomain[] otherDomains = new PhysicalDomain[attributes.length-1];
        int i = 0;
        int j = 0;
        for( ; i < attributes.length; i++ ) {
            if( i == attribute ) continue;
            otherDomains[j++] = phys[i];
        }

        // Project down to attribute
        Backend.v().addRef(bdd);
        RelationContainer attributeBDD = new RelationContainer(
                new Attribute[] { attributes[attribute] },
                new PhysicalDomain[] { phys[attribute] },
                "toString",
                Jedd.v().project( bdd, otherDomains ) );

        // Iterate over all the values of attribute
        for( Iterator oIt = attributeBDD.iterator(); oIt.hasNext(); ) {
            final Object o = (Object) oIt.next();
            RelationInstance literal = Jedd.v().literal(
                    new Object[] { o },
                    new Attribute[] { attributes[attribute] },
                    new PhysicalDomain[] { phys[attribute] } );
            Backend.v().addRef(bdd);
            RelationInstance restrictedBdd = Jedd.v().compose( bdd, literal, new PhysicalDomain[] { phys[attribute] } );
            toString( prefix + o + ((attribute == attributes.length - 1) ? "]" : ", "),
                    b,
                    attribute + 1,
                    restrictedBdd );
            Backend.v().delRef( restrictedBdd );
        }

        // Free up attributeBDD
        attributeBDD.eq(Backend.v().falseBDD());
    }
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append( "[" );
        for( int i=0; i < attributes.length; i++ ) {
            b.append( attributes[i].name() );
            if( i == attributes.length-1 ) b.append("]\n");
            else b.append(", ");
        }
        toString("[", b, 0, bdd);
        return b.toString();
    }
    public jedd.Relation applyShifter( jedd.Jedd.Shifter s ) {
        Jedd.Shifter shifter = (Jedd.Shifter) s;

        RelationInstance projected = Backend.v().project( bdd, shifter.p );
        Backend.v().addRef(projected);

        RelationInstance copied = Backend.v().copy( projected, shifter.c );
        Backend.v().addRef(copied);
        Backend.v().delRef(projected);

        return new RelationContainer( attributes, phys, "applyShifter", copied );
    }
    RelationInstance cast( Attribute[] newAttributes, PhysicalDomain[] newPhys ) {
        do {
            if( newAttributes.length != attributes.length ) break;
            if( newPhys.length != phys.length ) break;

            Set oldSet = new HashSet();
            Map newMap = new HashMap();
            
            for( int i = 0; i < attributes.length; i++ ) {
                oldSet.add(attributes[i]);
                newMap.put(newAttributes[i], newPhys[i]);
            }
            if( !oldSet.equals(newMap.keySet()) ) break;

            List from = new ArrayList();
            List to = new ArrayList();

            for( int i = 0; i < attributes.length; i++ ) {
                PhysicalDomain newD = (PhysicalDomain) newMap.get(attributes[i]);
                if( newD.equals(phys[i]) ) continue;
                from.add( phys[i] );
                to.add(newD);
            }

            if( from.isEmpty() ) return Jedd.v().read(this);
            return Jedd.v().replace( this,
                (PhysicalDomain[]) from.toArray(new PhysicalDomain[from.size()]),
                (PhysicalDomain[]) to.toArray(new PhysicalDomain[to.size()]));
        } while(false);
        throw new ClassCastException( typeToString() );
    }
    private String typeToString() {
        StringBuffer ret = new StringBuffer();
        ret.append("<");
        for( int i = 0; i < attributes.length; i++ ) {
            if( i > 0 ) ret.append( ", " );
            ret.append( attributes[i]+":"+phys[i] );
        }
        ret.append(">");
        return ret.toString();
    }
}
