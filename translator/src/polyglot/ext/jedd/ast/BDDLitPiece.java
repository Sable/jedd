package polyglot.ext.jedd.ast;

import polyglot.ext.jl.ast.*;
import polyglot.ast.*;
import java.util.*;

public interface BDDLitPiece extends Node 
{
    public Expr e();
    public TypeNode domain();
    public TypeNode phys();
}

