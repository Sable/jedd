#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <time.h>
#include <assert.h>

#include "kernel.h"
#include "cache.h"

#include "zbdd.h"
#include "zbddext.h"

/*
 * Extensions to ZBDDs for relational operations.
 * Copyright (C) 2004 Ondrej Lhotak
 *
 */

void make_physdom( int var, int* physdoms, int length, physdom* out ) {
    int i;
    for( i = 0; i < length; i+=2 ) {
        if( var >= physdoms[i] && var <= physdoms[i+1] ) {
            out->start = physdoms[i];
            out->end = physdoms[i+1];
            return;
        }
    }
    fprintf( stderr, "Ran off the end of physdoms array\n" );
    exit(1);
}

BDD zbdd_ithvar( int val ) {
    return zbdd_change(zbdd_base(), val);
}

BDD zfbdd_ithvar( physdom* dom, int val ) {
    if(dom->start + val > dom->end) fprintf(stderr, "physdom too small\n");
    return zbdd_ithvar(dom->start + val);
}

static int in_dom( int var, physdom* physdoms ) {
    if( var >= physdoms->start && var <= physdoms->end ) return -1;
    return 0;
}

static int in_physdom( int var, physdom* physdoms ) {
    while( physdoms != NULL ) {
        if( in_dom(var, physdoms) ) return -1;
        physdoms = physdoms->next;
    }
    return 0;
}

BDD zbdd_rel_mul( BDD p, BDD q, physdom* physdoms ) {
    int pd;
    BDD p0, p1, q0, q1;

    if( LEVEL(p) > LEVEL(q) ) return zbdd_rel_mul(q, p, physdoms);
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
    if(in_physdom(bddlevel2var[LEVEL(p)], physdoms))
        return getnode( LEVEL(p), 
                zbdd_rel_mul(p0, q0, physdoms),
                zbdd_rel_mul(p1, q1, physdoms) );

    return getnode( LEVEL(p), 
            zbdd_rel_mul(p0, q0, physdoms),
            zbdd_union(zbdd_union(
                    zbdd_rel_mul(p1, q1, physdoms), zbdd_rel_mul(p1, q0, physdoms)), zbdd_rel_mul(p0, q1, physdoms)));
}


BDD zbdd_replace( BDD b, physdom* from, physdom* to ) {
    physdom* fi;
    physdom* ti;
    for( fi = from, ti = to; fi && ti; fi = fi->next, ti = ti->next ) {
        int var = bddlevel2var[LEVEL(b)];
        if(in_dom(var, fi)) {
            int newvar = to->start + var - from->start;
            return zbdd_union(
                    zbdd_replace(LOW(b), from, to),
                    zbdd_change(zbdd_replace(HIGH(b), from, to), newvar));
        }
    }
    return getnode(LEVEL(b), zbdd_replace(LOW(b), from, to),
            zbdd_replace(HIGH(b), from, to));
}
