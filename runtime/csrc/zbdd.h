#ifndef ZBDD_H_INCLUDED
#define ZBDD_H_INCLUDED

#include "bdd.h"
#include "kernel.h"

extern BDD zbdd_empty();
extern BDD zbdd_base();
extern BDD zbdd_subset1( BDD p, int var );
extern BDD zbdd_subset0( BDD p, int var );
extern BDD zbdd_change( BDD p, int var );
extern BDD zbdd_union( BDD p, BDD q );
extern BDD zbdd_intsec( BDD p, BDD q );
extern BDD zbdd_diff( BDD p, BDD q );
extern int zbdd_count( BDD p );
extern BDD zbdd_mul( BDD p, BDD q );


BDD getnode( int level, BDD low, BDD high );
#endif
