package polyglot.ext.jedd.ast;

import polyglot.ext.jedd.extension.*;
import polyglot.ext.jedd.types.*;
import polyglot.ext.jl.ast.*;
import polyglot.types.*;
import polyglot.ast.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

public class BDDLitPiece_c extends Node_c implements BDDLitPiece
{
    private Expr e;
    private TypeNode domain;
    private TypeNode phys;
    public Expr e() { return e; }
    public TypeNode domain() { return domain; }
    public TypeNode phys() { return phys; }
    public BDDLitPiece_c(Position pos, Expr e, TypeNode domain, TypeNode phys) {
        super(pos);
        this.e = e;
        this.domain = domain;
        this.phys = phys;
    }
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.begin(0);
        print( e, w, tr );
        w.write(" => ");
        print( domain, w, tr );
        if( phys != null ) {
            w.write(":");
            print( phys, w, tr );
        }
        w.end();
    }
    public Node visitChildren(NodeVisitor v) {
        BDDLitPiece_c ret = (BDDLitPiece_c) copy();
        ret.e = (Expr) visitChild( e, v );
        ret.domain = (TypeNode) visitChild( domain, v );
        ret.phys = (TypeNode) visitChild( phys, v );
        return ret;
    }
}

