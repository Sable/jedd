package polyglot.ext.jedd.extension;

import polyglot.ext.jl.ast.*;
import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.types.*;
import polyglot.types.*;
import polyglot.ast.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

public class JeddDel_c extends JL_c
{
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        return ((JeddTypeCheck) node().ext() ).typeCheck( tc );
    }
}

