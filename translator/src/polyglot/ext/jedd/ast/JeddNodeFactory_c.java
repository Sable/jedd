package polyglot.ext.jedd.ast;

import polyglot.ast.*;
import polyglot.ext.jl.ast.*;
import polyglot.ext.jedd.extension.*;
import polyglot.util.*;
import java.util.*;

/**
 * NodeFactory for jedd extension.
 */
public class JeddNodeFactory_c extends NodeFactory_c implements JeddNodeFactory {
    public JeddNodeFactory_c() {
        super(new JeddExtFactory_c(), new JeddDelFactory_c());
    }
    protected JeddNodeFactory_c(ExtFactory extFact) {
        super(extFact);
    }

    public BDDTypeNode BDDTypeNode( Position pos, List domainPairs ) {
        return new BDDTypeNode_c( pos, domainPairs );
    }
    public Replace Replace( Position pos, Expr expr, List domainPairs ) {
        Node ret = new Replace_c( pos, expr, domainPairs );
        return (Replace) ret;
    }
    public FixPhys FixPhys( Position pos, Expr expr ) {
        Node ret = new FixPhys_c( pos, expr );
        return (FixPhys) ret;
    }
    public Relprod Relprod( Position pos, Expr lhs, Expr rhs, List ldomains, List rdomains ) {
        Node ret = new Relprod_c( pos, lhs, rhs, ldomains, rdomains );
        return (Relprod) ret;
    }
    public BDDLit BDDLit( Position pos, List pieces ) {
        Node ret = new BDDLit_c(pos, pieces);
        return (BDDLit) ret;
    }
    public BDDLitPiece BDDLitPiece( Position pos, Expr e, TypeNode domain, TypeNode phys ) {
        return new BDDLitPiece_c(pos, e, domain, phys);
    }
    public BDDTrueFalse BDDTrueFalse( Position pos, boolean value ) {
        Node ret = new BDDTrueFalse_c(pos, value );
        return (BDDTrueFalse) ret;
    }
}
