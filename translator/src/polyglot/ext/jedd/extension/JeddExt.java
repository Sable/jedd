package polyglot.ext.jedd.extension;

import polyglot.ast.*;
import polyglot.ext.jl.ast.*;
import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.types.*;
import polyglot.types.*;
import polyglot.frontend.*;
import polyglot.visit.*;
import polyglot.util.*;

public interface JeddExt extends Ext {
    public Node physicalDomains(JeddTypeSystem ts, JeddNodeFactory nf) throws SemanticException;
    public Node generateJava(JeddTypeSystem ts, JeddNodeFactory nf) throws SemanticException;
}
