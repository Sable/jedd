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

#include "jedd.h"
#include "bdd.h"
#include <math.h>

const char* bdd_errno = NULL;
static void errorhandler(int errorCode) {
    bdd_errno = bdd_errstring(errorCode);
    bdd_clear_error();
}

extern void init() {
    bdd_init( 1000000, 100000 );
    bdd_error_hook(errorhandler);
    bdd_disable_reorder();
    bdd_setcacheratio(4);
    bdd_setmaxincrease(100000);
}

static int totalBits = 0;
extern void addBits( int bits ) {
    bdd_extvarnum(bits);
    totalBits += bits;
}

extern int numBits() {
    return totalBits;
}

//#define REF_DEBUG

#ifdef REF_DEBUG
static int refs = 0;
#endif

extern void addRef( int bdd ) {
#ifdef REF_DEBUG
    refs++;
    if( refs > 15 ) printf( "refs = %u\n", refs );
#endif
    bdd_addref( bdd );
}

extern void delRef( int bdd ) {
#ifdef REF_DEBUG
    refs--;
#endif
    bdd_delref( bdd );
}


// return value of following functions *is* refed
extern int falseBDD() {
    int ret = bdd_false();
    addRef(ret);
    return ret;
}

extern int trueBDD() {
    int ret = bdd_true();
    addRef(ret);
    return ret;
}

extern int literal( int n, int bits[] ) {
    int i = 0;
    int ret = trueBDD();
    int tmp;
    for( i = 0; i < n; i++ ) {
        if( bits[i] == 0 ) {
            tmp = ret;
            ret = bdd_and( tmp, bdd_nithvar(i) );
            delRef(tmp);
            addRef(ret);
        } else if( bits[i] == 1 ) {
            tmp = ret;
            ret = bdd_and( tmp, bdd_ithvar(i) );
            delRef(tmp);
            addRef(ret);
        }
    }
    return ret;
}

// return value of following functions is *not* refed
extern int replace( int r, int n, int from[], int to[] ) {
    int i;
    bddPair* pair = bdd_newpair();
    bdd_setpairs( pair, from, to, n );
    return bdd_replace( r, pair );
}

extern int relprod( int r1, int r2, int n, int domains[] ) {
    int ret;
    int cube = bdd_makeset( domains, n );
    addRef(cube);
    ret = bdd_relprod( r1, r2, cube );
    delRef(cube);
    return ret;
}

extern int project( int r, int n, int toRemove[] ) {
    int ret;
    int cube = bdd_makeset( toRemove, n );
    addRef(cube);
    ret = bdd_exist( r, cube );
    delRef(cube);
    return ret;
}

extern int or( int r1, int r2 ) {
    return bdd_or( r1, r2 );
}

extern int and( int r1, int r2 ) {
    return bdd_and( r1, r2 );
}

extern int minus( int r1, int r2 ) {
    return bdd_apply( r1, r2, bddop_diff );
}

extern void setOrder( int n, int level2var[] ) {
    bdd_setvarorder( level2var );
}

static int* allsatCubes;
static void allsatHandler( char* cube, int size ) {
    int i;
    for( i=0; i < size; i++ ) {
        *allsatCubes = cube[i];
        allsatCubes++;
    }
}

extern void allCubes( int r, int cubes[] ) {
    allsatCubes = cubes;
    bdd_allsat(r, allsatHandler);
}

extern int numNodes( int r ) {
    return bdd_nodecount(r);
}

extern int numPaths( int r ) {
    return (int) bdd_pathcount(r);
}

double myexp2( int i ) {
    double ret = 1;
    while(i--) ret *=2;
    return ret;
}
extern int satCount( int r, int vars ) {
    double s = bdd_satcount(r);
    s /= myexp2(totalBits-vars);
    return (int) s;
}

extern void dumpdot( int r ) {
    bdd_printdot(r);
}

extern void dump( int r1, int n, int bits[] ) {
    bdd_printset(r1);
}

extern void info() {
    bdd_printstat();
}

extern void reportOrdering( int n, int vars[] ) {
    int i;
    for( i = 0; i<n; i++ ) {
        printf( "%u ", bdd_var2level(vars[i]) );
    }
    printf("\n");
}

extern void gbc() {
    bdd_gbc();
}
