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

package polyglot.ext.jedd.visit;

import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.extension.*;
import polyglot.ext.jedd.types.*;
import polyglot.ast.*;
import polyglot.types.*;
import polyglot.util.*;
import polyglot.frontend.*;
import polyglot.visit.*;
import java.util.*;

public class LiveVariables extends DataFlow
{
    public LiveVariables( Job job, TypeSystem ts, NodeFactory nf ) {
        super(job, ts, nf, false /*backward analysis*/, true /*on entry*/ );
    }
    public Item createInitialItem(FlowGraph graph) {
        return new DataFlowItem();
    }
    protected static class DataFlowItem extends Item {
        private Set liveVars = new HashSet();
        public Set liveVars() { return liveVars; }
        public void add( LocalInstance li ) {
            liveVars.add(new IdentityKey(li));
        }
        public void addAll( DataFlowItem item ) {
            liveVars.addAll( item.liveVars );
        }
        public void remove( LocalInstance li ) {
            liveVars.remove(new IdentityKey(li));
        }
        public boolean equals( Object o ) {
            if( !(o instanceof DataFlowItem) ) return false;
            DataFlowItem other = (DataFlowItem) o;
            return other.liveVars.equals( liveVars );
        }
        public int hashCode() {
            return liveVars.hashCode();
        }
        public DataFlowItem copy() {
            DataFlowItem ret = new DataFlowItem();
            ret.liveVars.addAll( liveVars );
            return ret;
        }
        public String toString() {
            return liveVars.toString();
        }
    }
    public Map flow(Item inItem, FlowGraph graph, Term n, Set succEdgeKeys) {
        DataFlowItem in = (DataFlowItem) inItem;
        DataFlowItem out = in;

        if( n instanceof LocalAssign ) {
            LocalAssign la = (LocalAssign) n;
            Local l = (Local) la.left();
            LocalInstance li = l.localInstance();
            if( li.type() instanceof BDDType ) {
                out = out.copy();
                if( la.operator() == Assign.ASSIGN ) {
                    out.remove( li );
                } else {
                    out.add( li );
                }
            }
        } else if( n instanceof Local ) {
            Local l = (Local) n;
            LocalInstance li = l.localInstance();
            if( li.type() instanceof BDDType ) {
                out = out.copy();
                out.add( li );
            }
        } else if( n instanceof LocalDecl ) {
            LocalDecl ld = (LocalDecl) n;
            LocalInstance li = ld.localInstance();
            if( li.type() instanceof BDDType ) {
                out = out.copy();
                out.remove( li );
            }
        }
        if( out == null ) throw new RuntimeException();
        return itemToMap( out, succEdgeKeys );
    }
    public Item confluence(List inItems, Term node) {
        DataFlowItem out = new DataFlowItem();
        for( Iterator itemIt = inItems.iterator(); itemIt.hasNext(); ) {
            final DataFlowItem item = (DataFlowItem) itemIt.next();
            out.addAll( item );
        }
        return out;
    }
    public void check(FlowGraph graph, Term n, Item inItem, Map outItems) throws SemanticException {
    }
    protected Node leaveCall(Node old, Node root, NodeVisitor outerV) throws SemanticException {
        if( !(root instanceof CodeDecl) ) return super.leaveCall(old, root, outerV);
        final Map toKills = new HashMap();
        final Map toKillsBefore = new HashMap();
        final FlowGraph graph = currentFlowGraph();

        dumpFlowGraph( graph, (Term) root );
        //if(true) return super.leaveCall(old, root, outerV);

        NodeVisitor v = new NodeVisitor() {
            private FlowGraph.Peer entry( FlowGraph.Peer p ) {
                FlowGraph.Peer pred = p;
                FlowGraph.Peer prePred;
                do {
                    prePred = pred;
                    if( pred.succs().isEmpty() ) {
                        break;
                    }
                    FlowGraph.Edge e = (FlowGraph.Edge) 
                        pred.succs().iterator().next();
                    pred = e.getTarget();
                } while( !( pred.node() instanceof Stmt ) );
                return prePred;
            }
            private DataFlowItem itemBefore( FlowGraph.Peer p ) {
                return (DataFlowItem) entry(p).outItem( (FlowGraph.EdgeKey)
                        entry(p).succEdgeKeys().iterator().next() );
            }
            public Node leave(Node parent, Node old, Node n, NodeVisitor v) {
                if( old != n ) throw new RuntimeException( 
                        "We shouldn't be modifying anything" );
                if( n instanceof Stmt ) {
                    Stmt s = (Stmt) n;
                    Set globalToKill = null;
                    Set globalToKillBefore = null;
                    Collection peers = graph.peers(s);
                    if( peers.isEmpty() ) {
                        Collection allPeers = graph.peers();
                        System.out.println( "all peers: "+allPeers );
                        throw new RuntimeException( "no peers for "+s );
                    }
                    for( Iterator pIt = peers.iterator(); pIt.hasNext(); ) {
                        final FlowGraph.Peer p = (FlowGraph.Peer) pIt.next();
                        Set toKill = new HashSet();
                        Set notToKill = new HashSet();
                        
                        toKill.addAll( itemBefore( p ).liveVars() );
                        // Argh! Since it's a backwards analysis, Polyglot
                        // switches the preds and succs. We want the successors
                        // (in program flow order), which are the predecessors
                        // in dataflow order, and Polyglot calls them preds.
                        for( Iterator edgeIt = p.preds().iterator(); edgeIt.hasNext(); ) {
                            final FlowGraph.Edge edge = (FlowGraph.Edge) edgeIt.next();
                            FlowGraph.Peer succ = edge.getTarget();
                            notToKill.addAll( itemBefore( succ ).liveVars() );
                        }
                        if( p.preds().isEmpty() ) {
                            // no successors: better not insert any kills
                            // since they will be unreachable
                            toKill = new HashSet();
                        }
                        toKill.removeAll( notToKill );
                        if( globalToKill == null ) {
                            globalToKill = toKill;
                        } else {
                            globalToKill.retainAll( toKill );
                        }

                        Set toKillBefore = new HashSet();
                        Set notToKillBefore = new HashSet();
                        for( Iterator edgeIt = entry(p).succs().iterator(); edgeIt.hasNext(); ) {
                            final FlowGraph.Edge edge = (FlowGraph.Edge) edgeIt.next();
                            FlowGraph.Peer pred = edge.getTarget();
                            toKillBefore.addAll( itemBefore( pred ).liveVars() );
                        }
                        notToKillBefore.addAll( itemBefore( p ).liveVars() );
                        toKillBefore.removeAll( notToKillBefore );
                        if( globalToKillBefore == null ) {
                            globalToKillBefore = toKillBefore;
                        } else {
                            globalToKillBefore.retainAll( toKillBefore );
                        }
                    }
                    if( !globalToKill.isEmpty() ) {
                        toKills.put( new IdentityKey(s), globalToKill );
                    }
                    if( !globalToKillBefore.isEmpty() ) {
                        toKillsBefore.put( new IdentityKey(s), globalToKillBefore );
                    }
                }
                return super.leave( parent, old, n, v );
            }
        };
        v.begin();
        root = root.visit(v);
        v.finish();

        v = new ContextVisitor(job, ts, nf) {
            public Node leave(Node parent, Node old, Node n, NodeVisitor v) {
                if( !( n instanceof LocalDecl ) 
                    &&  ( toKills.containsKey(new IdentityKey(old))
                        || toKillsBefore.containsKey(new IdentityKey(old)))) {
                    Stmt s = (Stmt) n;
                    Block bl = nf.Block( s.position(), s );
                    Collection globalToKill = (Collection)
                        toKills.get(new IdentityKey(old));
                    if( globalToKill != null ) {
                        for( Iterator idkIt = globalToKill.iterator(); idkIt.hasNext(); ) {
                            final IdentityKey idk = (IdentityKey) idkIt.next();
                            LocalInstance li = (LocalInstance) idk.object();
                            if( context().findVariableSilent(li.name()) != li ) continue; 
                            bl = bl.append(
                                    nf.Eval( s.position(),
                                        nf.Call(
                                            s.position(),
                                            nf.Local( s.position(), li.name() )
                                                .localInstance( li ),
                                            "kill"
                                            ) ) );
                        }
                    }
                    Collection globalToKillBefore = (Collection)
                        toKillsBefore.get(new IdentityKey(old));
                    if( globalToKillBefore != null ) {
                        for( Iterator idkIt = globalToKillBefore.iterator(); idkIt.hasNext(); ) {
                            final IdentityKey idk = (IdentityKey) idkIt.next();
                            LocalInstance li = (LocalInstance) idk.object();
                            if( context().findVariableSilent(li.name()) != li ) continue; 
                            bl = bl.prepend(
                                    nf.Eval( s.position(),
                                        nf.Call(
                                            s.position(),
                                            nf.Local( s.position(), li.name() )
                                                .localInstance( li ),
                                            "kill"
                                            ) ) );
                        }
                    }
                    n = bl;
                }
                return super.leave( parent, old, n, v );
            }
        };
        v.begin();
        root = root.visit(v);
        v.finish();

        return super.leaveCall(old, root, outerV);
    }
}

