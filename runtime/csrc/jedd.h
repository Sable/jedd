/* Jedd - A language for implementing relations using BDDs
 * Copyright (C) 2003 Ondrej Lhotak
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

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
extern int replacepair( int r, int pair );
extern int relprod( int r1, int r2, int n, int domains[] );
extern int project( int r, int n, int toRemove[] );
extern int or( int r1, int r2 );
extern int and( int r1, int r2 );
extern int minus( int r1, int r2 );

extern void setOrder( int n, int level2var[] );

extern void allCubes( int r, int cubes[] );

extern int numNodes( int r );
extern int numPaths( int r );

extern int satCount( int r, int vars );

extern void dump( int r, int n, int bits[] );
extern void dumpdot( int r );
extern void info();

extern void reportOrdering( int n, int vars[] );

extern void gbc();

extern void getShape( int bdd, int shape[] );

extern int makecube( int n, int domains[] );
extern int relprodcube( int r1, int r2, int cube );
extern int makepair( int fn, int from[], int tn, int to[] );

