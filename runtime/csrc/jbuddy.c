#include "bdd.h"
#include "jbuddy.h"
#include <string.h>

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

// returns -1 if we're already at the last path, 0 if the next path is
// non-accepting, 1 if the next path is accepting
int nextPath( int r, int n, int cube[] ) {
    int stack[n];
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
            for( i = 0; i < n; i++ ) fprintf(stderr, "%d ", cube[i]);
            fprintf( stderr, "internal error %d\n", cube[bdd_level2var(LEVEL(r))] );
            *((int*)0) = 5;
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
    while(1) {
        if( r < 2 ) return r;
        cube[bdd_level2var(LEVEL(r))] = 0;
        r = LOW(r);
    }
}

// returns 1 on success, 0 if we're already on the last cube
extern int nextCube( int r, int n, int cube[] ) {
    while(1) {
        int ret = nextPath( r, n, cube );
        if( ret == 1 ) return 1;
        if( ret == -1 ) return 0;
    }
}

// returns 1 on success, 0 if there are no cubes
extern int firstCube( int r, int n, int cube[] ) {
    int origR = r;
    memset(cube, -1, n*sizeof(*cube) );
    while( r >= 2 ) {
        cube[bdd_level2var(LEVEL(r))] = 0;
        r = LOW(r);
    }
    if( r == 0 ) return nextCube( origR, n, cube );
    else return 1;
}


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

static int bdd_markwidth_rec(int bdd, int level, int seenTerminals[])
{
   BddNode *node;
   
   if (bdd < 2) {
       if(seenTerminals[bdd]) {
           return 0;
       } else {
           seenTerminals[bdd] = 1;
           return 1;
       }
   }

   node = &bddnodes[bdd];
   if (MARKEDp(node)  ||  LOWp(node) == -1)
      return 0;
   
   if(LEVELp(node) > level) {
       // this is a node we want to count
       SETMARKp(node);
       return 1;
   } else {
       SETMARKp(node);
       return bdd_markwidth_rec(LOWp(node), level, seenTerminals)
            + bdd_markwidth_rec(HIGHp(node), level, seenTerminals);
   }
}

extern int bdd_markwidth(int bdd, int var1, int var2)
{
    int level;
    int seenTerminals[2];
    int ret;
    seenTerminals[0] = 0;
    seenTerminals[1] = 0;
    level = bdd_var2level(var1);
    if(bdd_var2level(var2) > level) level = bdd_var2level(var2);
    ret = bdd_markwidth_rec(bdd, level, seenTerminals);
    bdd_unmark(bdd);
    return ret;
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

