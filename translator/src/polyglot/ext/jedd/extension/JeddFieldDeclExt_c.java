package polyglot.ext.jedd.extension;

import polyglot.ext.jl.ast.*;
import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.types.*;
import polyglot.types.*;
import polyglot.ast.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

public class JeddFieldDeclExt_c extends JeddExt_c implements JeddTypeCheck
{
    public Node typeCheck( TypeChecker tc ) throws SemanticException {
        JeddNodeFactory nf = (JeddNodeFactory) tc.nodeFactory();
        JeddTypeSystem ts = (JeddTypeSystem) tc.typeSystem();

        FieldDecl n = (FieldDecl) node();
        if( n.init() == null ) return n;
        if( !( n.init().type() instanceof BDDType ) ) {
            if( n.fieldInstance().type() instanceof BDDType )
                throw new SemanticException( "Attempt to initialize BDD with non-BDD." );
            return n.typeCheck(tc);
        } else {
            if( !(n.fieldInstance().type() instanceof BDDType) ) 
                throw new SemanticException( "Attempt to initialize non-BDD with BDD." );
            if( !n.init().type().isImplicitCastValid( n.fieldInstance().type() ) )
                throw new SemanticException( "Incompatible type of BDD initializer." );
            return n.init( (Expr) nf.FixPhys( n.init().position(), n.init() ).typeCheck( tc ) );
        }
    }
    public Node physicalDomains( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        FieldDecl n = (FieldDecl) node();
        if( n.init() == null ) return n;
        if( !( n.declType() instanceof BDDType ) ) return n;
        BDDType t = (BDDType) n.declType();
        for( Iterator domainIt = t.map().keySet().iterator(); domainIt.hasNext(); ) {
            final Type domain = (Type) domainIt.next();
            ts.addMustEqualEdge( DNode.v( n.init(), domain ),
                    DNode.v( n.fieldInstance(), domain ) );
        }
        return n;
    }
    public Node generateJava( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        FieldDecl n = (FieldDecl) node();
        if( !( n.declType() instanceof BDDType ) ) {
            return super.generateJava( ts, nf );
        }
        BDDType declType = (BDDType) n.fieldInstance().type();

        CanonicalTypeNode jeddRelation =  nf.CanonicalTypeNode( n.position(), ts.relation() );

        n = n.flags( n.flags().set( Flags.FINAL ) );
        n = n.type( jeddRelation );

        n = n.init( newRelation( ts, nf, declType, n.init() ) );

        return n;
    }
}

