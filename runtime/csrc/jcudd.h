extern DdNode* Cudd_bddNot( DdNode* in );
extern DdNode * Cudd_CubeArrayToBdd (DdManager *dd, int array[]);
extern DdNode * Cudd_IndicesToCube (DdManager *dd, int array[], int n);
extern int Cudd_ShuffleHeap (DdManager *table, int permutation[]);



typedef struct {
    int size;
    DdNode** from;
    DdNode** to;
} bddPair;

extern bddPair newPair( int size );
extern void setPairs( DdManager* manager, bddPair pair, int from[], int to[] );
extern DdNode* swapVariables( DdManager* manager, DdNode* bdd, bddPair pair );
extern int equals( DdNode* n1, DdNode* n2 );
extern void allCubes( DdManager* manager, int totalBits, DdNode* r, int cubes[] );
extern DdGen* firstCube( DdManager* manager, DdNode* r, int n, int cube[] );
extern int nextCube( DdGen* iterator, int n, int cube[] );
extern void freeIterator( DdGen* iterator );
extern int isNull( DdGen* iterator );
