package polyglot.ext.jedd.extension;

import polyglot.ext.jl.ast.*;
import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.types.*;
import polyglot.types.*;
import polyglot.ast.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

public class JeddFormalExt_c extends JeddExt_c
{
    public Node generateJava( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        Formal n = (Formal) node();

        CanonicalTypeNode jeddRelation =  nf.CanonicalTypeNode( n.position(), ts.relation() );

        if( n.declType() instanceof BDDType ) {
            n = n.type( jeddRelation );
            n = n.flags( n.flags().set( Flags.FINAL ) );
        }
        return n;
    }
}

