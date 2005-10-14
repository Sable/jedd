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

package jedd;
import soot.*;
import soot.util.*;
import java.util.*;
import soot.jimple.*;
import soot.toolkits.scalar.*;
import soot.toolkits.graph.*;

/**
 * Jedd dead variable optimizer - at the point where a relation variable
 * becomes dead, inserts a call to jedd.Relation.kill to ensure that the
 * corresponding BDD is garbage collected.
 */

public class DeadVarOptimizer extends BodyTransformer {
    public static final void main( String[] args ) { 
        PackManager.v().getPack("jtp").add(
	    new Transform("jtp.dvo", new DeadVarOptimizer()));

	soot.Main.main(args);
    }

    private RefType relationType = RefType.v( "jedd.Relation" );
    SimpleLocalDefs defs;
    CompleteUnitGraph g;
    protected void internalTransform(Body body, String phaseName, Map options) {
        g = new CompleteUnitGraph(body);
        defs = new SimpleLocalDefs(g);

        FlowSet escapes = new ArraySparseSet();

        // now do an escape analysis
        for( Iterator sIt = body.getUnits().iterator(); sIt.hasNext(); ) {
            final Stmt s = (Stmt) sIt.next();
            if( s.containsInvokeExpr() ) {
                InvokeExpr ie = s.getInvokeExpr();
                SootClass cl = ie.getMethod().getDeclaringClass();
                if( cl.getName().equals( "jedd.Relation" ) ) continue;
                if( cl.getName().equals( "jedd.internal.RelationContainer" ) ) continue;
                if( cl.getName().equals( "jedd.Jedd" ) ) continue;
                if( cl.getName().equals( "jedd.internal.Jedd" ) ) continue;
                for( Iterator vbIt = s.getUseBoxes().iterator(); vbIt.hasNext(); ) {
                    final ValueBox vb = (ValueBox) vbIt.next();
                    Value v = vb.getValue();
                    if( !( v instanceof Local ) ) continue;
                    if( !isRelation( v.getType() ) ) continue;
                    escapes.add( getTarget( (Local) v, s ) );
                }
            } else if( s instanceof AssignStmt ) {
                AssignStmt as = (AssignStmt) s;
                Value lhs = as.getLeftOp();
                if( lhs instanceof ConcreteRef ) {
                    Value rhs = as.getRightOp();
                    if( rhs instanceof Local ) {
                        Local rhsLocal = (Local) rhs;
                        if( isRelation( rhsLocal.getType() ) ) {
                            escapes.add( getTarget( rhsLocal, s ) );
                        }
                    }
                }
            }
        }
        
        LiveRelationVariables lrv = new LiveRelationVariables();
        Chain units = body.getUnits();
        for( Iterator sIt = (new LinkedList( units )).iterator(); sIt.hasNext(); ) {
            final Stmt s = (Stmt) sIt.next();
            FlowSet toBeKilled = new ArraySparseSet();
            for( Iterator predIt = g.getPredsOf(s).iterator(); predIt.hasNext(); ) {
                final Stmt pred = (Stmt) predIt.next();
                toBeKilled.union( (FlowSet) lrv.getFlowBefore(pred) );
            }
            toBeKilled.difference( (FlowSet) lrv.getFlowBefore(s) );
            toBeKilled.difference( escapes );
            for( Iterator vbIt = toBeKilled.iterator(); vbIt.hasNext(); ) { 
                final ValueBox vb = (ValueBox) vbIt.next();
locals:
                for( Iterator lIt = body.getLocals().iterator(); lIt.hasNext(); ) {
                    final Local l = (Local) lIt.next();
                    if( !isRelation( l.getType() ) ) continue;
                    for( Iterator predIt = g.getPredsOf(s).iterator(); predIt.hasNext(); ) {
                        final Stmt pred = (Stmt) predIt.next();
                        if( !defs.hasDefsAt( l, pred ) ) continue;
                        if( getTarget( l, pred ) != vb ) continue;
                        units.insertBefore(
                                Jimple.v().newInvokeStmt(
                                    Jimple.v().newVirtualInvokeExpr(
                                        l,
                                        Scene.v().makeMethodRef(
                                            relationType.getSootClass(),
                                            "kill",
                                            new ArrayList(),
                                            VoidType.v(),
                                            false)
                                    )
                                )
                            ,s );
                        break locals;
                    }
                }
            }
        }
    }

    private ValueBox getTarget( Local l, Stmt s ) {
        List localDefs = defs.getDefsOfAt( l, s );
        if( localDefs.size() > 1 ) {
            throw new RuntimeException( "Multiple defs of "+l+" in "
                    +g.getBody().getMethod()+"\n"+localDefs.toString() );
        }
        DefinitionStmt def = (DefinitionStmt) localDefs.iterator().next();
        ValueBox rhsbox = def.getRightOpBox();
        Value rhs = rhsbox.getValue();
        if( rhs instanceof Local ) return getTarget( (Local) rhs, def );
        return rhsbox;
    }
    private boolean isRelation( Type t ) {
        if( !(t instanceof RefType) ) return false;
        RefType rt = (RefType) t;
        while(true) {
            if( rt.equals( relationType ) ) return true;
            SootClass cl = rt.getSootClass();
            if( !cl.hasSuperclass() ) return false;
            rt = cl.getSuperclass().getType();
        }
    }


    class LiveRelationVariables extends BackwardFlowAnalysis {
        LiveRelationVariables()
        {
            super(g);

            doAnalysis();
            
        }

        // merge operator is union
        protected void merge(Object in1, Object in2, Object out)
        {
            FlowSet inSet1 = (FlowSet) in1;
            FlowSet inSet2 = (FlowSet) in2;
            FlowSet outSet = (FlowSet) out;

            inSet1.union(inSet2, outSet);
        }
        
        // in(s) = ( out(s) minus defs(s) ) union uses(s)
        protected void flowThrough(Object outValue, Object unit,
                Object inValue)
        {
            FlowSet in  = (FlowSet) inValue;
            FlowSet out = (FlowSet) outValue;
            Stmt    s   = (Stmt)    unit;

            // Copy out to in
            out.copy( in );

            // Take out kill set
            Iterator boxIt = killSet(s).iterator();
            while( boxIt.hasNext() ) {
                final ValueBox box = (ValueBox) boxIt.next();
                Value value = box.getValue();
                if( value instanceof Local )
                    in.remove( getTarget((Local) value, s) );
            }

            // Add gen set
            boxIt = genSet(s).iterator();
            while( boxIt.hasNext() ) {
                final ValueBox box = (ValueBox) boxIt.next();
                Value value = box.getValue();
                if( value instanceof Local ) {
                    ValueBox target = getTarget((Local) value, s);
                    Value tgtVal = target.getValue();
                    if( tgtVal instanceof NewExpr
                    ||  tgtVal instanceof ParameterRef 
                    ||  tgtVal instanceof InvokeExpr ) {
                        in.add( target );
                    } else if( tgtVal instanceof FieldRef 
                            || tgtVal instanceof ArrayRef );
                    else throw new RuntimeException( "unanticipated tgtVal: "+tgtVal );
                }
            }
        }

        protected void copy(Object source, Object dest)
        {
            FlowSet sourceSet = (FlowSet) source;
            FlowSet destSet   = (FlowSet) dest;
                
            sourceSet.copy(destSet);
        }

        // STEP 6: Determine value for start/end node, and
        // initial approximation.
        //
        // end node:              empty set
        // initial approximation: empty set
        protected Object entryInitialFlow()
        {
            return new ArraySparseSet();
        }
            
        protected Object newInitialFlow()
        {
            return new ArraySparseSet();
        }

        private boolean isDef( SootMethod m ) {
            String subSig = m.getSubSignature();
            String[] eqs = { "eq", "eqUnion", "eqIntersect", "eqMinus" };
            for( int i = 0; i < eqs.length; i++ ) {
                if( subSig.equals( "jedd.internal.RelationContainer "+eqs[i]+"(jedd.internal.RelationContainer)" ) ) return true;
                if( subSig.equals( "jedd.internal.RelationContainer "+eqs[i]+"(jedd.internal.RelationInstance)" ) )
                    return true;
            }
            return false;
        }
        private VirtualInvokeExpr getDef( Stmt s ) {
            if( s.containsInvokeExpr() ) {
                InvokeExpr ie = s.getInvokeExpr();
                if( isDef( ie.getMethod() ) ) {
                    return (VirtualInvokeExpr) ie;
                }
            }
            return null;
        }
        private List genSet(Stmt s) {
            List ret = new ArrayList();
            List boxes;
            VirtualInvokeExpr def = getDef(s);
            if( def != null ) {
                boxes = new ArrayList();
                boxes.add( def.getArgBox(0) );
                if( !def.getMethod().getName().equals("eq") ) {
                    boxes.add( def.getBaseBox() );
                }
            } else {
                boxes = s.getUseBoxes();
            }
            for( Iterator vbIt = boxes.iterator(); vbIt.hasNext(); ) {
                final ValueBox vb = (ValueBox) vbIt.next();
                if( vb.getValue() instanceof Local ) {
                    Local l = (Local) vb.getValue();
                    if( isRelation( l.getType() ) ) {
                        ret.add( vb );
                    }
                }
            }
            return ret;
        }
        private List killSet(Stmt s) {
            VirtualInvokeExpr def = getDef(s);
            List ret = new ArrayList();
            if( def != null ) {
                ret.add( def.getBaseBox() );
            }
            return ret;
        }
    }
}

