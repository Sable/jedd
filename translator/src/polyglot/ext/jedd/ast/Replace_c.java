package polyglot.ext.jedd.ast;

import polyglot.ext.jedd.extension.*;
import polyglot.ext.jedd.types.*;
import polyglot.ext.jl.ast.*;
import polyglot.types.*;
import polyglot.ast.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

public class Replace_c extends FixPhys_c implements Replace, JeddGenerateJava, JeddPhysicalDomains
{
    private List domainPairs;
    public List domainPairs() { return domainPairs; }
    public Replace_c(Position pos, Expr expr, List domainPairs ) {
        super( pos, expr );
        this.domainPairs = domainPairs;
    }
    public Node visitChildren(NodeVisitor v) {
        Replace_c ret = (Replace_c) copy();

        ret.expr = (Expr) visitChild( expr, v );

        List newDomains = new LinkedList();
        for( Iterator pairIt = domainPairs.iterator(); pairIt.hasNext(); ) {
            final TypeNode[] pair = (TypeNode[]) pairIt.next();
            TypeNode[] newPair = { (TypeNode) visitChild( pair[0], v ),
                                   (TypeNode) visitChild( pair[1], v ) };
            newDomains.add( newPair );
        }
        ret.domainPairs = newDomains;
        return ret;
    }
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.begin(0);
        w.write("(");
        for( Iterator pairIt = domainPairs.iterator(); pairIt.hasNext(); ) {
            final TypeNode[] pair = (TypeNode[]) pairIt.next();
            print(pair[0],w,tr);
            w.write(" => ");
            print(pair[1],w,tr);
            if( pairIt.hasNext() ) {
                w.write(",");
                w.allowBreak(0, " ");
            }
        }
        w.write(")");
        w.allowBreak(2, " ");
        printSubExpr(expr, w, tr);
        w.end();
    }
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        JeddTypeSystem ts = (JeddTypeSystem) tc.typeSystem();
        JeddNodeFactory nf = (JeddNodeFactory) tc.nodeFactory();

        if( !( expr.type() instanceof BDDType) ) {
            throw new SemanticException( "Argument of replace must be a BDD.",
                    expr.position() );
        }

        expr = (Expr) nf.FixPhys( expr.position(), expr ).typeCheck(tc);

        BDDType exprType = (BDDType) expr.type();
        Map exprMap = exprType.map();
        for( Iterator pairIt = domainPairs.iterator(); pairIt.hasNext(); ) {
            final TypeNode[] pair = (TypeNode[]) pairIt.next();
            if( !pair[0].type().isSubtype( ts.domain() ) ) {
                throw new SemanticException( "Attempt to replace a non-domain" );
            }
            if( !exprMap.containsKey( pair[0].type() ) ) {
                throw new SemanticException( "Argument of replace doesn't have domain "+pair[0].type() );
            }
        }

        Set seenAlready = new HashSet();
        List newDomains = new LinkedList();
        for( Iterator exprPairIt = exprType.domainPairs().iterator(); exprPairIt.hasNext(); ) {
            final Type[] exprPair = (Type[]) exprPairIt.next();
            Type[] newDomain = new Type[2];
            for( Iterator pairIt = domainPairs.iterator(); pairIt.hasNext(); ) {
                final TypeNode[] pair = (TypeNode[]) pairIt.next();
                if( !pair[0].type().equals( exprPair[0] ) ) continue;
                if( pair[1].type().isSubtype( ts.physicalDomain() ) ) {
                    if( newDomain[1] != null ) {
                        throw new SemanticException( "Multiple physical domains specified for domain "+pair[0].type() );
                    }
                    newDomain[1] = pair[1].type();
                } else if( pair[1].type().isSubtype( ts.domain() ) ) {
                    if( newDomain[0] != null ) {
                        throw new SemanticException( "Multiple physical domains specified for domain "+pair[0].type() );
                    }
                    newDomain[0] = pair[1].type();
                } else throw new SemanticException( "Attempt to replace to a non-domain" );
            }
            if( newDomain[0] == null ) newDomain[0] = exprPair[0];
            newDomains.add( newDomain );
            if( !seenAlready.add( newDomain[0] ) ) {
                throw new SemanticException( "Resulting type has duplicate domain "+newDomain[0] );
            }
            if( newDomain[1] != null ) {
                if( !seenAlready.add( newDomain[1] ) ) {
                    throw new SemanticException( "Resulting type has duplicate physical domain "+newDomain[0] );
                }
            }
        }
        return type( ts.BDDType( newDomains ) );
    }
    public Node physicalDomains( JeddTypeSystem ts, JeddNodeFactory nf ) {
        BDDType exprType = (BDDType) expr.type();
        Map exprMap = exprType.map();

        for( Iterator exprPairIt = exprType.domainPairs().iterator(); exprPairIt.hasNext(); ) {

            final Type[] exprPair = (Type[]) exprPairIt.next();
            Type[] newDomain = new Type[2];
            for( Iterator pairIt = domainPairs.iterator(); pairIt.hasNext(); ) {
                final TypeNode[] pair = (TypeNode[]) pairIt.next();
                if( !pair[0].type().equals( exprPair[0] ) ) continue;
                if( pair[1].type().isSubtype( ts.physicalDomain() ) ) {
                    newDomain[1] = pair[1].type();
                } else {
                    newDomain[0] = pair[1].type();
                }
            }
            if( newDomain[0] == null ) newDomain[0] = exprPair[0];
            if( newDomain[1] != null ) {
                ts.addAssignEdge( DNode.v( this, newDomain[0] ),
                        DNode.v( expr, exprPair[0] ) );
            } else {
                ts.addMustEqualEdge( DNode.v( this, newDomain[0] ),
                        DNode.v( expr, exprPair[0] ) );
            }
        }
        return this;
    }
    public Node generateJava( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        Position p = position();

        BDDType type = (BDDType) type();
        BDDType exprType = (BDDType) expr().type();
        Map map = type.map();

        List from = new LinkedList();
        List to = new LinkedList();
        for( Iterator pairIt = exprType.domainPairs().iterator(); pairIt.hasNext(); ) {
            final Type[] pair = (Type[]) pairIt.next();
            Type phys = pair[1];
            for( Iterator repPairIt = domainPairs().iterator(); repPairIt.hasNext(); ) {
                final TypeNode[] repPair = (TypeNode[]) repPairIt.next();
                if( repPair[0].type().equals( pair[0] ) ) {
                    if( repPair[1].type().isSubtype( ts.domain() ) ) {
                        phys = (Type) map.get( repPair[1].type() );
                    } else {
                        phys = repPair[1].type();
                    }
                }
            }
            if( phys.equals( pair[1] ) ) continue;
            from.add( nf.Call( p, nf.CanonicalTypeNode( p, pair[1] ), "v" ) );
            to.add( nf.Call( p, nf.CanonicalTypeNode( p, phys ), "v" ) );
        }
        if( from.isEmpty() ) return expr().type(type);

        Call getJedd = nf.Call( p, nf.CanonicalTypeNode( p, ts.jedd() ), "v"  ); 

        return nf.Call( 
                p,
                getJedd,
                "replace",
                expr(),
                nf.NewArray(
                    p,
                    nf.CanonicalTypeNode( p, ts.physicalDomain() ),
                    new LinkedList(),
                    1,
                    nf.ArrayInit( p, from )
                    ),
                nf.NewArray(
                    p,
                    nf.CanonicalTypeNode( p, ts.physicalDomain() ),
                    new LinkedList(),
                    1,
                    nf.ArrayInit( p, to )
                    )
                ).type( type );
    }
}

