package polyglot.ext.jedd.ast;

import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.extension.*;
import polyglot.ext.jedd.types.*;
import polyglot.ast.*;
import polyglot.types.*;
import polyglot.util.*;
import polyglot.frontend.*;
import polyglot.visit.*;
import java.util.*;

public interface JeddGenerateJava
{
    public Node generateJava( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException;
}

