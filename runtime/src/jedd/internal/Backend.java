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

public abstract class Backend {
    static private Backend instance;
    static Backend v() { return instance; }
    static public void init( String type ) {
        if( type.equals( "buddy" ) ) instance = new BuddyBackend();
        if( type.equals( "cudd" ) ) instance = new CuddBackend();
        if( type.equals( "sablejbdd" ) ) instance = new SableBackend();
        if( type.equals( "javabdd" ) ) instance = new JavabddBackend();
        v().init();
    }

    abstract void init();
    abstract void addBits( int bits );
    abstract int numBits();

    abstract void addRef( RelationInstance bdd );
    abstract void delRef( RelationInstance bdd );

    // return value of following functions *is* refed
    abstract RelationInstance literal( int bits[] );
    abstract RelationInstance falseBDD();
    abstract RelationInstance trueBDD();

    // return value of following functions is *not* refed
    abstract RelationInstance replace( RelationInstance r, Replacer repl );
    abstract RelationInstance copy( RelationInstance r, Copier copyer );
    abstract RelationInstance relprod( RelationInstance r1, RelationInstance r2, Projector proj );
    abstract RelationInstance project( RelationInstance r, Projector proj );

    abstract RelationInstance or( RelationInstance r1, RelationInstance r2 );
    abstract RelationInstance and( RelationInstance r1, RelationInstance r2 );
    abstract RelationInstance minus( RelationInstance r1, RelationInstance r2 );
    abstract RelationInstance biimp( RelationInstance r1, RelationInstance r2 );

    abstract boolean equals( RelationInstance r1, RelationInstance r2 );

    abstract void setOrder( int level2var[] );

    abstract void allCubes( RelationInstance r, int cubes[] );

    abstract int numNodes( RelationInstance r );
    abstract int numPaths( RelationInstance r );

    abstract long satCount( RelationInstance r, int vars );

    abstract void gbc();

    abstract void getShape( RelationInstance bdd, int shape[] );

    abstract Projector makeProjector( int domains[] );
    abstract Replacer makeReplacer( int from[], int to[] );
    abstract Copier makeCopier( int from[], int to[] );

    protected interface Replacer {
    }

    protected interface Copier {
    }

    protected interface Projector {
    }

    protected class RelationProjectorCopier implements Projector, Copier {
        final RelationInstance relpc;
        RelationProjectorCopier( RelationInstance relpc ) {
            this.relpc = relpc;
        }
    }
    protected RelationInstance relpc( Projector in ) {
        return ((RelationProjectorCopier)in).relpc;
    }
    protected RelationInstance relpc( Copier in ) {
        return ((RelationProjectorCopier)in).relpc;
    }
    protected RelationProjectorCopier relpc( RelationInstance in ) {
        return new RelationProjectorCopier( in );
    }
}
