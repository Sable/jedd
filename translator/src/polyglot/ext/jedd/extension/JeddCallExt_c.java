package polyglot.ext.jedd.extension;

import polyglot.ext.jl.ast.*;
import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.types.*;
import polyglot.types.*;
import polyglot.ast.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

public class JeddCallExt_c extends JeddExt_c
{
    public Node generateJava( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        Call n = (Call) node();

        CanonicalTypeNode jeddRelation =  nf.CanonicalTypeNode( n.position(), ts.relation() );

        List newArgs = new LinkedList();
        for( Iterator argIt = n.arguments().iterator(); argIt.hasNext(); ) {
            final Expr arg = (Expr) argIt.next();
            if( arg.type() instanceof BDDType ) {
                newArgs.add( newRelation( ts, nf, (BDDType) arg.type(), arg ) );
            } else {
                newArgs.add( arg );
            }
        }

        return n.arguments( newArgs );
    }
}

