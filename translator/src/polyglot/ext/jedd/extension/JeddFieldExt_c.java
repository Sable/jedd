package polyglot.ext.jedd.extension;

import polyglot.ext.jl.ast.*;
import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.types.*;
import polyglot.ext.jedd.visit.*;
import polyglot.types.*;
import polyglot.ast.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

public class JeddFieldExt_c extends JeddExt_c implements JeddTypeCheck
{
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        JeddTypeSystem ts = (JeddTypeSystem) tc.typeSystem();
        JeddNodeFactory nf = (JeddNodeFactory) tc.nodeFactory();

        Field n = (Field) node();
        VarInstance vi = n.fieldInstance();
        Type t = vi.type();
        if( !( t instanceof BDDType ) ) return n.typeCheck(tc);
        BDDType bt = (BDDType) t;
        n = (Field) n.type( ts.cloneDomains( bt ) );
        return n;
    }
    public Node physicalDomains( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        Field n = (Field) node();
        VarInstance vi = n.fieldInstance();
        Type t = vi.type();
        if( !( t instanceof BDDType ) ) return n;
        BDDType bt = (BDDType) t;
        for( Iterator domainIt = bt.map().keySet().iterator(); domainIt.hasNext(); ) {
            final Type domain = (Type) domainIt.next();
            ts.addMustEqualEdge( DNode.v( n, domain ), DNode.v( vi, domain ) );
        }
        return n;
    }
}

