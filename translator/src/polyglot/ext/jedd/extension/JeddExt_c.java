package polyglot.ext.jedd.extension;

import polyglot.ast.*;
import polyglot.ext.jl.ast.*;
import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.types.*;
import polyglot.ext.jedd.visit.*;
import polyglot.types.*;
import polyglot.frontend.*;
import polyglot.visit.*;
import polyglot.util.*;
import java.util.*;

public class JeddExt_c extends Ext_c implements JeddExt {
    public Node physicalDomains(JeddTypeSystem ts, JeddNodeFactory nf) throws SemanticException {
        Node n = node();
        if( n instanceof JeddPhysicalDomains ) {
            return ((JeddPhysicalDomains) n).physicalDomains( ts, nf );
        }
        return n;
    }
    public Node generateJava(JeddTypeSystem ts, JeddNodeFactory nf) throws SemanticException {
        Node n = node();
        if( n instanceof JeddGenerateJava ) {
            return ((JeddGenerateJava) n).generateJava( ts, nf );
        }
        return n;
    }
    protected Expr newRelation( JeddTypeSystem ts, JeddNodeFactory nf, BDDType type, Node init ) throws SemanticException {
        Position p = node().position();
        
        List domains = new LinkedList();
        List phys = new LinkedList();
        for( Iterator pairIt = type.domainPairs().iterator(); pairIt.hasNext(); ) {
            final Type[] pair = (Type[]) pairIt.next();
            domains.add( nf.Call( p, nf.CanonicalTypeNode( p, pair[0] ), "v" ) );
            phys.add( nf.Call( p, nf.CanonicalTypeNode( p, pair[1] ), "v" ) );
        }

        List args = new LinkedList();
        args.add( nf.NewArray(
                    p,
                    nf.CanonicalTypeNode( p, ts.domain() ),
                    new LinkedList(),
                    1,
                    nf.ArrayInit( p, domains ) ) );
        args.add( nf.NewArray(
                    p,
                    nf.CanonicalTypeNode( p, ts.physicalDomain() ),
                    new LinkedList(),
                    1,
                    nf.ArrayInit( p, phys ) ) );
        if( init != null ) args.add( init );

        return nf.New( p, nf.CanonicalTypeNode( p, ts.relation() ), args ).type(type);
    }
}
