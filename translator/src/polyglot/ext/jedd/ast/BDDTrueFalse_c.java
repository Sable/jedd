package polyglot.ext.jedd.ast;

import polyglot.ext.jedd.extension.*;
import polyglot.ext.jedd.types.*;
import polyglot.ext.jl.ast.*;
import polyglot.types.*;
import polyglot.ast.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

public class BDDTrueFalse_c extends Lit_c implements BDDTrueFalse, JeddGenerateJava
{
    private boolean value;
    public boolean value() { return value; }
    public BDDTrueFalse_c(Position pos, boolean value ) {
        super(pos);
        this.value = value;
    }
    public Object objValue() {
        throw new InternalCompilerError( "Jedd should not be treating a BDD as an object." );
    }
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.begin(0);
        w.write(value ? "1B" : "0B");
        w.end();
    }
    public Node buildTypes(TypeBuilder tb) {
        JeddTypeSystem ts = (JeddTypeSystem) tb.typeSystem();
        BDDTrueFalse_c ret = (BDDTrueFalse_c) type(ts.BDDType(new LinkedList()));
        ret.value = value;
        return ret;
    }
    public Node generateJava( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        BDDTrueFalse n = this;

        Call getJedd = nf.Call( n.position(), nf.CanonicalTypeNode( n.position(), ts.jedd() ), "v"  ); 

        return nf.Call( n.position(), getJedd, n.value() ? "trueBDD" : "falseBDD" )
            .type( n.type() );
    }
}

