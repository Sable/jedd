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

package polyglot.ext.jedd.types;

import polyglot.ast.*;
import polyglot.types.*;
import polyglot.ext.jedd.ast.*;
import polyglot.ext.jl.ast.*;
import polyglot.ext.jl.types.*;
import polyglot.util.*;
import java.util.*;
import java.io.*;
//import polyglot.ext.jedd.cudd.*;


public class PhysDom {
    private static PhysDom instance = new PhysDom();
    public static PhysDom v() { return instance; }

    public Map domainAssignment = new HashMap();

    public Set assignEdges = new HashSet();
    public Set mustEqualEdges = new HashSet();
    public Set allPhys = new HashSet();

    private String satSolver = System.getProperty("user.home")+System.getProperty("file.separator")+"sat";
    public void setSatSolver( String s ) { satSolver = s; }

    static class SetLit {
        Set set;
        private SetLit( Set set ) {
            this.set = set;
        }
        public static SetLit v( Set set ) {
            SetLit ret = new SetLit(set);
            SetLit ret2 = (SetLit) setMap.get( ret );
            if( ret2 == null ) setMap.put( ret2 = ret, ret );
            return ret2;
        }
        public boolean equals( Object other ) {
            if( !(other instanceof SetLit) ) return false;
            SetLit o = (SetLit) other;
            if( !o.set.equals(set) ) return false;
            return true;
        }
        public int hashCode() { return set.hashCode(); }
        public String toString() {
            Integer i = (Integer) litNumMap.get(this);
            if( i == null ) litNumMap.put( this, i = new Integer(++nextInt) );
            return i.toString();
        }
    }
    static class NegSetLit {
        SetLit set;
        private NegSetLit( SetLit set ) {
            this.set = set;
        }
        private NegSetLit( Set set ) {
            this( SetLit.v( set ) );
        }
        public static NegSetLit v( Set set ) {
            NegSetLit ret = new NegSetLit( set );
            NegSetLit ret2 = (NegSetLit) setMap.get( ret );
            if( ret2 == null ) setMap.put( ret2 = ret, ret );
            return ret2;
        }
        public static NegSetLit v( SetLit e ) {
            return v(e.set);
        }
        public boolean equals( Object other ) {
            if( !(other instanceof NegSetLit) ) return false;
            NegSetLit o = (NegSetLit) other;
            if( !o.set.equals(set) ) return false;
            return true;
        }
        public int hashCode() { return set.hashCode() + 1; }
        public String toString() {
            return "-"+set;
        }
    }
    public static Map setMap = new HashMap();

    static class Literal {
        DNode dnode;
        Type phys;
        private Literal( DNode dnode, Type phys ) {
            this.dnode = dnode; this.phys = phys;
        }
        public static Literal v( DNode dnode, Type phys ) {
            Literal ret = new Literal( dnode, phys );
            Literal ret2 = (Literal) litMap.get( ret );
            if( ret2 == null ) litMap.put( ret2 = ret, ret );
            return ret2;
        }
        public boolean equals( Object other ) {
            if( !(other instanceof Literal) ) return false;
            Literal o = (Literal) other;
            if( o.dnode != dnode ) return false;
            if( o.phys != phys ) return false;
            return true;
        }
        public int hashCode() { return dnode.hashCode() + phys.hashCode(); }
        public String toString() {
            Integer i = (Integer) litNumMap.get(this);
            if( i == null ) litNumMap.put( this, i = new Integer(++nextInt) );
            return i.toString();
        }
    }
    static int nextInt = 0;
    public static Map litMap = new HashMap();
    public static Map litNumMap = new HashMap();
    
    static class NegLiteral {
        Literal lit;
        private NegLiteral( Literal lit ) {
            this.lit = lit;
        }
        private NegLiteral( DNode dnode, Type phys ) {
            this( Literal.v( dnode, phys ) );
        }
        public static NegLiteral v( DNode dnode, Type phys ) {
            NegLiteral ret = new NegLiteral( dnode, phys );
            NegLiteral ret2 = (NegLiteral) litMap.get( ret );
            if( ret2 == null ) litMap.put( ret2 = ret, ret );
            return ret2;
        }
        public boolean equals( Object other ) {
            if( !(other instanceof NegLiteral) ) return false;
            NegLiteral o = (NegLiteral) other;
            if( !o.lit.equals(lit) ) return false;
            return true;
        }
        public int hashCode() { return lit.hashCode() + 1; }
        public String toString() {
            return "-"+lit;
        }
    }

    Set cnf = new HashSet();


    public void findAssignment() throws SemanticException {
        printDomainsDot();

        createLiterals();

        createDnodeConstraints();
        setupSpecifiedAssignment();

        addAssignEdges();

        addMustEqualEdges();
        addConflictEdges();

        //tryWithBDDsJustForKicks();
        runSat();

        recordPhys();

        printDomainsDot();
    }

    Set solution = new HashSet();
    public void runSat() throws SemanticException {
        int numvars = (setMap.size()+litMap.size())/2;
        if( numvars == 0 ) return;
        try {
            File tmpFile = File.createTempFile("domainassign",".cnf");
            PrintWriter file = new PrintWriter(
                    new FileOutputStream( tmpFile ) );
            file.println( "p cnf "+numvars+" "+cnf.size() );
            boolean first = true;
            for( Iterator clauseIt = cnf.iterator(); clauseIt.hasNext(); ) {
                final Set clause = (Set) clauseIt.next();
                if( !first ) file.println( "0" );
                first = false;
                for( Iterator litIt = clause.iterator(); litIt.hasNext(); ) {
                    final Object lit = (Object) litIt.next();
                    file.println( lit.toString() );
                }
            }
            file.close();
            Process p = Runtime.getRuntime().exec(satSolver+" "+tmpFile.getAbsolutePath());
            BufferedReader br = new BufferedReader(new InputStreamReader( p.getInputStream() ) );
            String str;
            String soln = null;
            while ((str = br.readLine()) != null) {
                System.out.println( str );
                boolean hasNum = false;
                boolean hasBad = false;
                for( int i = 0; i < str.length(); i++ ) {
                    if( str.charAt(i) == ' ' ) continue;
                    if( str.charAt(i) == '\t' ) continue;
                    if( str.charAt(i) == '-' ) continue;
                    if( str.charAt(i) >= '0' && str.charAt(i) <= '9' ) {
                        hasNum = true;
                        continue;
                    }
                    hasBad = true;
                }
                if( hasNum && !hasBad ) {
                    // this looks like the solution
                    if( soln  != null ) throw new RuntimeException( "old solution was "+soln+"; now we got "+str );
                    soln = str;
                }
            }
            boolean pos[] = new boolean[numvars+1];
            boolean neg[] = new boolean[numvars+1];
            if( soln == null ) throw new SemanticException( "SAT solver couldn't assign physical domains." );
            StringTokenizer st = new StringTokenizer( soln );
            while( st.hasMoreTokens() ) {
                String tok = st.nextToken();
                int i = Integer.parseInt( tok );
                if( i < 0 ) neg[-i] = true;
                else pos[i] = true;
            }
            for( int i = 1; i <= numvars; i++ ) {
                if( neg[i] && pos[i] ) throw new RuntimeException("both for "+i);
                if( !neg[i] && !pos[i] ) throw new RuntimeException("neither for "+i);
            }
            for( Iterator litIt = litNumMap.keySet().iterator(); litIt.hasNext(); ) {
                final Object lit = (Object) litIt.next();
                Integer i = (Integer) litNumMap.get( lit );
                if( pos[i.intValue()] ) solution.add( lit );
            }
            tmpFile.delete();
        } catch( IOException e ) {
            throw new RuntimeException( e.toString() );
        }
    }


    /*
    public void tryWithBDDsJustForKicks() {
        int numvars = (setMap.size()+litMap.size())/2;
        System.loadLibrary("jcudd");
        SWIGTYPE_p_DdManager manager = Cudd.Cudd_Init(numvars+1,0,Cudd.CUDD_UNIQUE_SLOTS,Cudd.CUDD_CACHE_SLOTS,0);

        SWIGTYPE_p_DdNode bdd = Cudd.Cudd_ReadOne(manager);
        Cudd.Cudd_Ref(bdd);

        for( Iterator clauseIt = cnf.iterator(); clauseIt.hasNext(); ) {

            final Set clause = (Set) clauseIt.next();

            SWIGTYPE_p_DdNode clauseBdd = Cudd.Cudd_ReadLogicZero(manager);
            Cudd.Cudd_Ref(clauseBdd);

            for( Iterator oIt = clause.iterator(); oIt.hasNext(); ) {

                final Object o = (Object) oIt.next();
                int i = Integer.parseInt( o.toString() );

                SWIGTYPE_p_DdNode literal = Cudd.Cudd_bddIthVar(manager, i<0?-i:i);
                if( i < 0 ) literal = Cudd.Cudd_bddNot( literal );
                Cudd.Cudd_Ref( literal );

                SWIGTYPE_p_DdNode oldClause = clauseBdd;
                clauseBdd = Cudd.Cudd_bddOr( manager, clauseBdd, literal );
                Cudd.Cudd_Ref( clauseBdd );
                Cudd.Cudd_RecursiveDeref( manager, oldClause );
                Cudd.Cudd_RecursiveDeref( manager, literal );
            }
            SWIGTYPE_p_DdNode oldBdd = bdd;
            bdd = Cudd.Cudd_bddAnd( manager, bdd, clauseBdd );
            Cudd.Cudd_Ref( bdd );
            Cudd.Cudd_RecursiveDeref( manager, oldBdd );
            Cudd.Cudd_RecursiveDeref( manager, clauseBdd );
            System.out.println( "BDD size: "+Cudd.Cudd_DagSize(bdd) );
        }
    }
    */
    

    private void createLiterals() {
        for( Iterator exprIt = DNode.exprs().iterator(); exprIt.hasNext(); ) {
            final BDDExpr expr = (BDDExpr) exprIt.next();
            BDDType t = expr.getType();
            Map map = t.map();
            for( Iterator domainIt = map.keySet().iterator(); domainIt.hasNext(); ) {
                final Type domain = (Type) domainIt.next();
                Type phys = (Type) map.get(domain);
                if( phys != null ) allPhys.add(phys);
            }
        }
    }

    public void createDnodeConstraints() {
        // Each dnode must be assigned to at least one phys
        for( Iterator dnodeIt = DNode.nodes().iterator(); dnodeIt.hasNext(); ) {
            final DNode dnode = (DNode) dnodeIt.next();
            Set clause = new HashSet();
            for( Iterator physIt = allPhys.iterator(); physIt.hasNext(); ) {
                final Type phys = (Type) physIt.next();
                clause.add( Literal.v( dnode, phys ) );
            }
            cnf.add( clause );
        }

        // Each dnode must be assigned to at most one phys
        for( Iterator dnodeIt = DNode.nodes().iterator(); dnodeIt.hasNext(); ) {
            final DNode dnode = (DNode) dnodeIt.next();
            for( Iterator physIt = allPhys.iterator(); physIt.hasNext(); ) {
                final Type phys = (Type) physIt.next();
                for( Iterator phys2It = allPhys.iterator(); phys2It.hasNext(); ) {
                    final Type phys2 = (Type) phys2It.next();
                    if( phys == phys2 ) continue;
                    Set clause = new HashSet();
                    clause.add( NegLiteral.v( dnode, phys ) );
                    clause.add( NegLiteral.v( dnode, phys2 ) );
                    cnf.add( clause );
                }
            }
        }
    }

    public void addMustEqualEdges() {
        for( Iterator edgeIt = mustEqualEdges.iterator(); edgeIt.hasNext(); ) {
            final DNode[] edge = (DNode[]) edgeIt.next();
            addMustEqualEdge( edge[0], edge[1] );
        }
    }
    private void addMustEqualEdge( DNode node1, DNode node2 ) {
        // (xa ==> ya) /\ (ya ==> xa) = (ya \/ ~xa) /\ (xa \/ ~ya)
        for( Iterator physIt = allPhys.iterator(); physIt.hasNext(); ) {
            final Type phys = (Type) physIt.next();
            Set clause = new HashSet();
            clause.add( Literal.v( node1, phys ) );
            clause.add( NegLiteral.v( node2, phys ) );
            cnf.add( clause );

            clause = new HashSet();
            clause.add( NegLiteral.v( node1, phys ) );
            clause.add( Literal.v( node2, phys ) );
            cnf.add( clause );
        }
    }

    public void addConflictEdges() {
        for( Iterator exprIt = DNode.exprs().iterator(); exprIt.hasNext(); ) {
            final BDDExpr expr = (BDDExpr) exprIt.next();
            BDDType t = expr.getType();
            for( Iterator domainIt = t.map().keySet().iterator(); domainIt.hasNext(); ) {
                final Type domain = (Type) domainIt.next();
                for( Iterator domain2It = t.map().keySet().iterator(); domain2It.hasNext(); ) {
                    final Type domain2 = (Type) domain2It.next();
                    if( domain.equals(domain2) ) continue;
                    addConflictEdge( DNode.v(expr,domain), DNode.v(expr,domain2) );
                }
            }
        }
    }

    private void addConflictEdge( DNode node1, DNode node2 ) {
        // (xa ==> ~ya) /\ (ya ==> ~xa) = (~ya \/ ~xa)
        for( Iterator physIt = allPhys.iterator(); physIt.hasNext(); ) {
            final Type phys = (Type) physIt.next();
            Set clause = new HashSet();
            clause.add( NegLiteral.v( node1, phys ) );
            clause.add( NegLiteral.v( node2, phys ) );
            cnf.add( clause );
        }
    }

    public Type phys(DNode d) {
        BDDExpr expr = d.expr;
        BDDType t = expr.getType();
        Map map = t.map();
        return (Type) map.get(d.dom);
    }

    public Type phys(Set s) {
        for( Iterator nodeIt = s.iterator(); nodeIt.hasNext(); ) {
            final DNode node = (DNode) nodeIt.next();
            Type ret = phys(node);
            if(ret != null) return ret;
        }
        return null;
    }

    public Collection adjacent(DNode d) {
        Set ret = new HashSet();
        for( Iterator edgeIt = assignEdges.iterator(); edgeIt.hasNext(); ) {
            final DNode[] edge = (DNode[]) edgeIt.next();
            if( edge[0] == d ) ret.add(edge[1]);
            if( edge[1] == d ) ret.add(edge[0]);
        }
        for( Iterator edgeIt = mustEqualEdges.iterator(); edgeIt.hasNext(); ) {
            final DNode[] edge = (DNode[]) edgeIt.next();
            if( edge[0] == d ) ret.add(edge[1]);
            if( edge[1] == d ) ret.add(edge[0]);
        }
        return ret;
    }
    
    public void addAssignEdges() throws SemanticException {
        LinkedList worklist = new LinkedList();
        Map pathMap = new HashMap();

        // initialize all nodes that have a phys with a path of length 1
        for( Iterator nodeIt = DNode.nodes().iterator(); nodeIt.hasNext(); ) {
            final DNode node = (DNode) nodeIt.next();
            Set paths = new HashSet();
            pathMap.put( node, paths );
            if( phys(node) == null ) continue;
            Set path = new HashSet();
            path.add(node);
            paths.add( path );
            worklist.addLast(node);
        }

        // do a BFS, extending paths through the graph
        while(!worklist.isEmpty()) {
            DNode node = (DNode) worklist.removeFirst();
            for( Iterator onodeIt = adjacent(node).iterator(); onodeIt.hasNext(); ) {
                final DNode onode = (DNode) onodeIt.next();
                if( phys(onode) != null ) continue;
                boolean changed = false;
outer:          for( Iterator newPathIt = ((Set)pathMap.get(node)).iterator(); newPathIt.hasNext(); ) {              final Set newPath = (Set) newPathIt.next();
                    Set newPath2 = new HashSet(newPath);
                    newPath2.add( onode );
                    for( Iterator oldPathIt = ((Set)pathMap.get(onode)).iterator(); oldPathIt.hasNext(); ) {
                        final Set oldPath = (Set) oldPathIt.next();
                        if( newPath2.containsAll(oldPath) ) continue outer;
                    }
                    ((Set) pathMap.get(onode)).add( newPath2 );
                    changed = true;
                }
                if( changed ) worklist.addLast(onode);
            }
        }

        // now encode paths as constraints
        for( Iterator nodeIt = pathMap.keySet().iterator(); nodeIt.hasNext(); ) {
            final DNode node = (DNode) nodeIt.next();
            Set paths = (Set) pathMap.get(node);

            {
                // at least one path must be active for each node
                Set clause = new HashSet();
                for( Iterator pathIt = paths.iterator(); pathIt.hasNext(); ) {
                    final Set path = (Set) pathIt.next();
                    clause.add( SetLit.v( path ) );
                }
                if( clause.size() == 0 ) {
                    node.expr.throwSemanticException( "No physical domains reaching domain "+node.dom );
                }
                cnf.add( clause );
            }

            {
                for( Iterator pathIt = paths.iterator(); pathIt.hasNext(); ) {
                    final Set path = (Set) pathIt.next();
                    // all the nodes in the path must have correct phys
                    Type phys = phys(path);
                    for( Iterator nodeOnPathIt = path.iterator(); nodeOnPathIt.hasNext(); ) {
                        final DNode nodeOnPath = (DNode) nodeOnPathIt.next();
                        // a ==> b /\ c /\ d = (b \/ ~a) /\ (c \/ ~a) /\ (d \/ ~a)
                        Set clause = new HashSet();
                        clause.add( NegSetLit.v(path) );
                        clause.add( Literal.v( nodeOnPath, phys ) );
                        cnf.add(clause);
                    }
                }
            }
        }
    }

    public void setupSpecifiedAssignment() {
        for( Iterator exprIt = DNode.exprs().iterator(); exprIt.hasNext(); ) {
            final BDDExpr expr = (BDDExpr) exprIt.next();
            BDDType t = expr.getType();
            Map map = t.map();
            for( Iterator domainIt = map.keySet().iterator(); domainIt.hasNext(); ) {
                final Type domain = (Type) domainIt.next();
                Type phys = (Type) map.get(domain);
                DNode dnode = DNode.v(expr,domain);
                if( phys != null ) {
                    Set clause = new HashSet();
                    clause.add( Literal.v( dnode, phys ) );
                    cnf.add( clause );
                }
            }
        }
    }

    public void recordPhys() {
        for( Iterator exprIt = DNode.exprs().iterator(); exprIt.hasNext(); ) {
            final BDDExpr expr = (BDDExpr) exprIt.next();
            BDDType t = expr.getType();
            for( Iterator pairIt = t.domainPairs().iterator(); pairIt.hasNext(); ) {
                final Type[] pair = (Type[]) pairIt.next();
                if( pair[1] != null ) continue;
                for( Iterator physIt = allPhys.iterator(); physIt.hasNext(); ) {
                    final Type phys = (Type) physIt.next();
                    if( solution.contains( Literal.v(DNode.v(expr,pair[0]),phys) ) ) {
                        pair[1] = phys;
                    }
                }
            }
        }
    }

    public void printDomainsDot() {
        try {
            PrintWriter file = new PrintWriter(
                    new FileOutputStream( new File("domainassign.dot") ) );
            int snum = 1;
            file.println( "digraph G {" );
            file.println( "  size=\"100,75\";" );
            file.println( "  nodesep=0.2;" );
            file.println( "  ranksep=1.5;" );
            file.println( "  mclimit=10;" );
            file.println( "  nslimit=10;" );
            for( Iterator exprIt = DNode.exprs().iterator(); exprIt.hasNext(); ) {
                final BDDExpr expr = (BDDExpr) exprIt.next();
                file.println( " subgraph cluster"+ snum++ +" {" );
                file.println( "  label=\""+expr+"\";" );
                BDDType t = expr.getType();
                Map map = t.map();
                for( Iterator dIt = DNode.nodes().iterator(); dIt.hasNext(); ) {
                    final DNode d = (DNode) dIt.next();
                    if( d.expr != expr ) continue;
                    Type phys = (Type) map.get(d.dom);
                    String phs = phys == null ? "null":phys.toString();
                    file.println( "  "+d.domNum+" [label=\""+d.toShortString()+":"+phs+"\"];" );
                }
                file.println( " }" );
            }
            for( Iterator eIt = mustEqualEdges.iterator(); eIt.hasNext(); ) {
                final DNode[] e = (DNode[]) eIt.next();
                file.println( "  "+e[0].domNum+" -> "+
                        e[1].domNum+";" );
            }
            for( Iterator eIt = assignEdges.iterator(); eIt.hasNext(); ) {
                final DNode[] e = (DNode[]) eIt.next();
                file.println( "  "+e[0].domNum+" -> "+
                        e[1].domNum+" [dir=none];" );
            }
            file.println( "}" );
            file.close();
        } catch( IOException e ) {
            throw new RuntimeException( e.toString() );
        }
    }
}
