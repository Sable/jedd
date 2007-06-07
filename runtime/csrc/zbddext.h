#ifndef ZBDDEXT_H_INCLUDED
#define ZBDDEXT_H_INCLUDED


typedef struct _ {
    int start;
    int end;
    struct _* next;
} physdom;

BDD zbdd_rel_mul( BDD p, BDD q, physdom* physdoms );
BDD zbdd_ithvar( int val );
BDD zfbdd_ithvar( physdom* dom, int val );

#endif
