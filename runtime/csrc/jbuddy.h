extern int      bdd_setpairs(bddPair*, int[], int[], int);
extern int      bdd_makeset(int[], int);
extern void     bdd_setvarorder(int[]);


extern void     allCubes( int r, int cubes[] );
extern int      nextCube( int r, int n, int cube[] );
extern int      firstCube( int r, int n, int cube[] );
extern void     getShape( int bdd, int shape[] );

extern const char* bdd_errno;
extern void setuperrorhandler();
extern void verbose_gc();

extern int      bdd_markwidth(int bdd, int var1, int var2);

