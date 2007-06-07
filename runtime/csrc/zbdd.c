#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>
#include <assert.h>

#include "kernel.h"
#include "cache.h"

/*
 * The functions in this file are directly based on those given in Ch. 6 of
 * Minato. _Binary Decision Diagrams and Applications for VLSI CAD_
 *
 * WARNING: Minato numbers BDD levels starting from the bottom (constant nodes),
 * while BuDDy numbers them starting from the top. Therefore, all comparisons
 * of levels have been reversed.
 */

BDD getnode( int level, BDD low, BDD high ) {
    BDD ret;

    PUSHREF(low);
    PUSHREF(high);
    ret = zbdd_makenode( level, READREF(2), READREF(1) );
    POPREF(2);
    return ret;
}

BDD zbdd_empty() {
    return bdd_false();
}

BDD zbdd_base() {
    return bdd_true();
}

static BDD zbdd_subset1_rec( BDD p, BDD var );

BDD zbdd_subset1( BDD p, int var ) {
    BDD varbdd = bdd_ithvar(var);
    return zbdd_subset1_rec( p, varbdd );
}

static BDD zbdd_subset1_rec( BDD p, BDD var ) {
    if( LEVEL(p) > LEVEL(var) ) return zbdd_empty();
    if( LEVEL(p) == LEVEL(var) ) return HIGH(p);
    return getnode( LEVEL(p),
            zbdd_subset1_rec(LOW(p), var),
            zbdd_subset1_rec(HIGH(p), var) );
}

static BDD zbdd_subset0_rec( BDD p, BDD var );

BDD zbdd_subset0( BDD p, int var ) {
    BDD varbdd = bdd_ithvar(var);
    return zbdd_subset0_rec( p, varbdd );
}

static BDD zbdd_subset0_rec( BDD p, BDD var ) {
    if( LEVEL(p) > LEVEL(var) ) return p;
    if( LEVEL(p) == LEVEL(var) ) return LOW(p);
    return getnode( LEVEL(p),
            zbdd_subset0_rec(LOW(p), var),
            zbdd_subset0_rec(HIGH(p), var) );
}

static BDD zbdd_change_rec( BDD p, BDD var );

BDD zbdd_change( BDD p, int var ) {
    BDD varbdd = bdd_ithvar(var);
    return zbdd_change_rec( p, varbdd );
}

static BDD zbdd_change_rec( BDD p, BDD var ) {
    if( LEVEL(p) > LEVEL(var) ) 
        return getnode( LEVEL(var), zbdd_empty(), p );
    if( LEVEL(p) == LEVEL(var) )
        return getnode( LEVEL(var), HIGH(p), LOW(p) );
    return getnode( LEVEL(p),
        zbdd_change_rec( LOW(p), var ),
        zbdd_change_rec( HIGH(p), var ) );
}

BDD zbdd_union( BDD p, BDD q ) {
    if( p == 0 ) return q;
    if( q == 0 ) return p;
    if( p == q ) return p;
    if( LEVEL(p) < LEVEL(q) )
        return getnode( LEVEL(p), zbdd_union(LOW(p), q), HIGH(p) );
    if( LEVEL(p) > LEVEL(q) )
        return getnode( LEVEL(q), zbdd_union(p, LOW(q)), HIGH(q) );
    return getnode( LEVEL(p),
            zbdd_union(LOW(p), LOW(q)),
            zbdd_union(HIGH(p), HIGH(q)));
}

BDD zbdd_intsec( BDD p, BDD q ) {
    if( p == 0 ) return 0;
    if( q == 0 ) return 0;
    if( p == q ) return p;
    if( LEVEL(p) < LEVEL(q) ) return zbdd_intsec(LOW(p), q);
    if( LEVEL(p) > LEVEL(q) ) return zbdd_intsec(p, LOW(q));
    return getnode( LEVEL(p),
            zbdd_intsec(LOW(p), LOW(q)),
            zbdd_intsec(HIGH(p), HIGH(q)));
}

BDD zbdd_diff( BDD p, BDD q ) {
    if( p == 0 ) return 0;
    if( q == 0 ) return p;
    if( p == q ) return 0;
    if( LEVEL(p) < LEVEL(q) )
        return getnode( LEVEL(p), zbdd_diff(LOW(p), q), HIGH(p));
    if( LEVEL(p) > LEVEL(q) ) return zbdd_diff(p, LOW(q));
    return getnode( LEVEL(p),
            zbdd_intsec(LOW(p), LOW(q)),
            zbdd_intsec(HIGH(p), HIGH(q)));
}

BDD zbdd_mul( BDD p, BDD q ) {
    BDD p0, p1, q0, q1;

    if( LEVEL(p) > LEVEL(q) ) return zbdd_mul(q, p);
    if( q == 0 ) return 0;
    if( q == 1 ) return p;
    // lookup in cache
    p0 = LOW(p);
    p1 = HIGH(p);
    if( LEVEL(p) == LEVEL(q) ) {
        q0 = LOW(q);
        q1 = HIGH(q);
    } else {
        q0 = q;
        q1 = 0;
    }
    // save it in cache
    return getnode( LEVEL(p), 
            zbdd_mul(p0, q0),
            zbdd_union(zbdd_union(
                    zbdd_mul(p1, q1), zbdd_mul(p1, q0)), zbdd_mul(p0, q1)));
}

BDD zbdd_div( BDD p, BDD q ) {
    BDD p0, p1, q0, q1, r;
    int x;

    if( q == 1 ) return p;
    if( q == 0 ) return 0;
    if( p == 0 ) return 0;
    if( p == q ) return 1;
    // lookup in cache
    x = bddlevel2var[LEVEL(q)];
    p0 = zbdd_subset0( p, x );
    p1 = zbdd_subset1( p, x );
    q0 = LOW(q);
    q1 = HIGH(q);
    r = zbdd_div( p1, q1 );
    if( r != 0 && q0 != 0 ) r = zbdd_intsec(r, zbdd_div(p0, q0));
    // save in cache
    return r;
}

BDD zbdd_rem( BDD p, BDD q ) {
    return zbdd_diff(p, zbdd_mul(p, zbdd_div(p, q)));
}

int zbdd_count( BDD p ) {
    if( p < 2 ) return p;
    return zbdd_count(LOW(p))+zbdd_count(HIGH(p));
}
