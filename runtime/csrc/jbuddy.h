extern int      bdd_setpairs(bddPair*, int[], int[], int);
extern int      bdd_makeset(int[], int);
extern void     bdd_setvarorder(int[]);


extern void     allCubes( int r, int cubes[] );
extern void     getShape( int bdd, int shape[] );

extern const char* bdd_errno;
extern void setuperrorhandler();
