#include "bdd.h"
#include "jbuddy.h"

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

#include "kernel.h"

void bdd_markshape(int i, int shape[]);

extern void getShape( int bdd, int shape[] ) {
    bdd_markshape(bdd, shape);
    bdd_unmark(bdd);
}

void bdd_markshape(int i, int shape[])
{
   BddNode *node;
   
   if (i < 2)
      return;

   node = &bddnodes[i];
   if (MARKEDp(node)  ||  LOWp(node) == -1)
      return;
   
   shape[LEVELp(node)]++;
   SETMARKp(node);
   
   bdd_markshape(LOWp(node), shape);
   bdd_markshape(HIGHp(node), shape);
}

extern int bdd_varlevel(int x) {
    fprintf( stderr, "missing function bdd_varlevel called\n" );
    *((int*)0) = 5;
    return x;
}


static void errorhandler(int errorCode) {
    bdd_errno = bdd_errstring(errorCode);
    bdd_clear_error();
}

const char* bdd_errno = NULL;
extern void setuperrorhandler() {
    bdd_error_hook(errorhandler);
    bdd_gbc_hook(NULL);
}

