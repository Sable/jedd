extern const char* bdd_errno;

extern void init();
extern void addBits( int bits );
extern int numBits();

extern void addRef( int bdd );
extern void delRef( int bdd );

// return value of following functions *is* refed
extern int literal( int n, int bits[] );
extern int falseBDD();
extern int trueBDD();

// return value of following functions is *not* refed
extern int replace( int r, int n, int from[], int to[] );
extern int relprod( int r1, int r2, int n, int domains[] );
extern int project( int r, int n, int toRemove[] );
extern int or( int r1, int r2 );
extern int and( int r1, int r2 );
extern int minus( int r1, int r2 );

extern void setOrder( int n, int level2var[] );

extern void allCubes( int r, int cubes[] );

extern int numNodes( int r );
extern int numPaths( int r );

extern void dump( int r, int n, int bits[] );
extern void dumpdot( int r );
extern void info();

extern void reportOrdering( int n, int vars[] );

extern void gbc();
