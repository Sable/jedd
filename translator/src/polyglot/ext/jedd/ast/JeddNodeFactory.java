package polyglot.ext.jedd.ast;

import polyglot.ast.*;
import polyglot.ext.jl.ast.*;
import polyglot.types.Flags;
import polyglot.types.Package;
import polyglot.types.Type;
import polyglot.types.Qualifier;
import polyglot.util.*;
import java.util.*;

/**
 * NodeFactory for jedd extension.
 */
public interface JeddNodeFactory extends NodeFactory {
    // TODO: Declare any factory methods for new AST nodes.
    public BDDTypeNode BDDTypeNode( Position pos, List domainPairs );
    public Replace Replace( Position pos, Expr expr, List domainPairs );
    public FixPhys FixPhys( Position pos, Expr expr );
    public Relprod Relprod( Position pos, Expr lhs, Expr rhs, List ldomains, List rdomains );
    public BDDLit BDDLit( Position pos, List pieces );
    public BDDLitPiece BDDLitPiece( Position pos, Expr e, TypeNode domain, TypeNode phys );
    public BDDTrueFalse BDDTrueFalse( Position pos, boolean value );
}
