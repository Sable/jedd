#include "util.h"
#include "cudd.h"
#include "jcudd.h"

extern DdNode* Cudd_bddNot( DdNode* in ) {
    return Cudd_Not( in );
}

extern bddPair newPair( int size ) {
    bddPair ret;
    ret.size = size;
    ret.from = malloc(size*sizeof(DdNode*));
    ret.to = malloc(size*sizeof(DdNode*));
    return ret;
}

extern void setPairs( DdManager* manager, bddPair pair, int from[], int to[] ) {
    int i;
    for( i = 0; i < pair.size; i++ ) {
        pair.from[i] = Cudd_bddIthVar( manager, from[i] );
        pair.to[i] = Cudd_bddIthVar( manager, to[i] );
    }
}

extern DdNode* swapVariables( DdManager* manager, DdNode* bdd, bddPair pair ) {
    return Cudd_bddSwapVariables( manager, bdd, pair.from, pair.to, pair.size );
}

extern int equals( DdNode* n1, DdNode* n2 ) {
    return n1 == n2;
}

extern void allCubes( DdManager* manager, int totalBits, DdNode* r, int cubes[] ) {
    int* cube;
    CUDD_VALUE_TYPE dummy;
    DdGen* iterator = Cudd_FirstCube( manager, r, &cube, &dummy );
    if( iterator == NULL ) return;
    do {
        memcpy( cubes, cube, totalBits*sizeof(int) );
        cubes += totalBits;
    } while( Cudd_NextCube( iterator, &cube, &dummy ) );
    Cudd_GenFree( iterator );
}

extern DdGen* firstCube( DdManager* manager, DdNode* r, int n, int cube[] ) {
    CUDD_VALUE_TYPE dummy;
    int* cubein;
    DdGen* iterator = Cudd_FirstCube( manager, r, &cubein, &dummy );
    if( iterator == NULL ) return NULL;
    memcpy( cube, cubein, n*sizeof(*cube) );
    return iterator;
}

extern int nextCube( DdGen* iterator, int n, int cube[] ) {
    CUDD_VALUE_TYPE dummy;
    int* cubein;
    int ret = Cudd_NextCube( iterator, &cubein, &dummy );
    if( !ret ) {
        Cudd_GenFree( iterator );
        return 0;
    }
    memcpy( cube, cubein, n*sizeof(*cube) );
    return 1;
}

extern int isNull( DdGen* iterator ) {
    return iterator == NULL;
}

extern void freeIterator( DdGen* iterator ) {
    Cudd_GenFree( iterator );
}

