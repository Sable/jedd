package polyglot.ext.jedd.ast;

import polyglot.ext.jl.ast.*;
import polyglot.ast.*;
import java.util.*;

public interface Relprod extends Expr
{
    public Expr lhs();
    public Expr rhs();
    public List ldomains();
    public List rdomains();
}

