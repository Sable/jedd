#include "jedd.h"
#include "util.h"
#include "cudd.h"

const char* bdd_errno = NULL;

static DdManager* manager;

extern void init() {
    //manager = Cudd_Init(0,0,CUDD_UNIQUE_SLOTS,CUDD_CACHE_SLOTS,0);
    manager = Cudd_Init(0,0,CUDD_UNIQUE_SLOTS*4,CUDD_CACHE_SLOTS,0);
}

static int totalBits = 0;
extern void addBits( int bits ) {
    totalBits += bits;
    while(bits--) Cudd_bddNewVar(manager);
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
    if( refs > 15 ) printf( "refs = %u:%u\n", refs, Cudd_CheckZeroRef(manager) );
#endif
    Cudd_Ref( (DdNode*) bdd );
}

extern void delRef( int bdd ) {
#ifdef REF_DEBUG
    refs--;
#endif
    Cudd_RecursiveDeref( manager, (DdNode*) bdd );
    //Cudd_IterDerefBdd( manager, (DdNode*) bdd );
}


// return value of following functions *is* refed
extern int falseBDD() {
    int ret = (int) Cudd_ReadLogicZero(manager);
    addRef(ret);
    return ret;
}

extern int trueBDD() {
    int ret = (int) Cudd_ReadOne(manager);
    addRef(ret);
    return ret;
}

extern int literal( int n, int bits[] ) {
    //Cudd_PrintInfo( manager, stdout );
    int ret = (int) Cudd_CubeArrayToBdd(manager, bits);
    addRef(ret);
    return ret;
}

// return value of following functions is *not* refed
extern int replace( int r, int n, int from[], int to[] ) {
    DdNode* fromNodes[n];
    DdNode* toNodes[n];
    int i;
    for( i = 0; i < n; i++ ) {
        fromNodes[i] = Cudd_bddIthVar(manager, from[i]);
        toNodes[i] = Cudd_bddIthVar(manager, to[i]);
    }
    return (int) Cudd_bddSwapVariables(manager, (DdNode*)r, fromNodes, toNodes, n);
}

extern int relprod( int r1, int r2, int n, int domains[] ) {
    int ret;
    DdNode* cube = Cudd_IndicesToCube( manager, domains, n );
    addRef((int)cube);
    ret = (int) Cudd_bddAndAbstract(manager, (DdNode*)r1, (DdNode*)r2, cube );
    delRef((int)cube);
    return ret;
}

extern int project( int r, int n, int toRemove[] ) {
    int ret;
    DdNode* cube = Cudd_IndicesToCube( manager, toRemove, n );
    addRef((int)cube);
    ret = (int) Cudd_bddExistAbstract(manager, (DdNode*)r, cube );
    delRef((int)cube);
    return ret;
}

extern int or( int r1, int r2 ) {
    return (int) Cudd_bddOr(manager, (DdNode*) r1, (DdNode*) r2);
}

extern int and( int r1, int r2 ) {
    return (int) Cudd_bddAnd(manager, (DdNode*) r1, (DdNode*) r2);
}

extern int minus( int r1, int r2 ) {
    DdNode* notr2 = Cudd_Not( (DdNode*) r2 );
    DdNode* ret;
    addRef((int)notr2);
    ret = Cudd_bddAnd(manager, (DdNode*) r1, notr2);
    delRef((int)notr2);
    return (int) ret;
}

extern void setOrder( int n, int level2var[] ) {
    Cudd_ShuffleHeap( manager, level2var );
}

extern void allCubes( int r, int cubes[] ) {
    int* cube;
    CUDD_VALUE_TYPE dummy;
    DdGen* iterator = Cudd_FirstCube( manager, (DdNode*) r, &cube, &dummy );
    if( iterator == NULL ) return;
    do {
        memcpy( cubes, cube, totalBits*sizeof(int) );
        cubes += totalBits;
    } while( Cudd_NextCube( iterator, &cube, &dummy ) );
    Cudd_GenFree( iterator );
}

extern int numNodes( int r ) {
    return Cudd_DagSize( (DdNode*) r );
}

extern int numPaths( int r ) {
    return (int) Cudd_CountPathsToNonZero((DdNode*) r);
}

extern void dump( int r1, int n, int bits[] ) {
    Cudd_PrintDebug( manager, (DdNode*) r1, n, 2 );
}

extern void dumpdot( int r ) {
    DdNode* node = (DdNode*) r;
    Cudd_DumpDot( manager, 1, &node, NULL, NULL, stdout );
}

extern void info() {
    printf("ZeroRef %u\n",  Cudd_CheckZeroRef(manager) );
    Cudd_PrintInfo( manager, stdout );
}

extern void reportOrdering( int n, int vars[] ) {
}

extern void gbc() {}
