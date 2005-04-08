/* Jedd - A language for implementing relations using BDDs
 * Copyright (C) 2005 Ondrej Lhotak
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

package org.sf.javabdd;

/**
 * Implements additional BDD operations missing from JavaBDD's JavaFactory.
 */

public class JeddJavaFactory extends JavaFactory {
    public static BDDFactory init(int nodenum, int cachesize) {
        JeddJavaFactory INSTANCE = new JeddJavaFactory();
        INSTANCE.initialize(nodenum, cachesize);
        return INSTANCE;
    }

    int bdd_level2var(int level) { return bddlevel2var[level]; }

    // returns -1 if we're already at the last path, 0 if the next path is
    // non-accepting, 1 if the next path is accepting
    public int nextPath( int r, int n, int cube[] ) {
        int[] stack = new int[n];
        int top = 0;
        int top0 = 0;
        int i;
        while( r >= 2 ) {
            stack[top++] = r;
            if( cube[bdd_level2var(LEVEL(r))] == 0 ) {
                top0 = top;
                r = LOW(r);
            } else if( cube[bdd_level2var(LEVEL(r))] == 1 ) {
                r = HIGH(r);
            } else {
                for( i = 0; i < n; i++ ) System.err.println( cube[i]);
                throw new RuntimeException("internal error "+cube[bdd_level2var(LEVEL(r))] );
            }
        }
        // No zeros => we're already at the last path.
        if( top0 == 0 ) return -1;

        // Cut off the end of the path.
        for( i = top0; i < top; i++ ) {
            cube[bdd_level2var(LEVEL(stack[i]))] = -1;
        }

        // Switch the zero to a one.
        r = stack[top0-1];
        cube[bdd_level2var(LEVEL(r))] = 1;
        r = HIGH(r);

        // Fill in the other zeros.
        while(true) {
            if( r < 2 ) return r;
            cube[bdd_level2var(LEVEL(r))] = 0;
            r = LOW(r);
        }
    }

    public int nextCube( BDD r, int n, int cube[] ) {
        return nextCube(((bdd) r)._index, n, cube);
    }
    // returns 1 on success, 0 if we're already on the last cube
    public int nextCube( int r, int n, int cube[] ) {
        while(true) {
            int ret = nextPath( r, n, cube );
            if( ret == 1 ) return 1;
            if( ret == -1 ) return 0;
        }
    }

    public int firstCube( BDD r, int n, int cube[] ) {
        return firstCube(((bdd) r)._index, n, cube);
    }
    // returns 1 on success, 0 if there are no cubes
    public int firstCube( int r, int n, int cube[] ) {
        int origR = r;
        for(int i = 0; i < cube.length; i++) cube[i] = -1;
        while( r >= 2 ) {
            cube[bdd_level2var(LEVEL(r))] = 0;
            r = LOW(r);
        }
        if( r == 0 ) return nextCube( origR, n, cube );
        else return 1;
    }
}
