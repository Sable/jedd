package polyglot.ext.jedd.visit;

import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.extension.*;
import polyglot.ext.jedd.types.*;
import polyglot.ast.*;
import polyglot.types.*;
import polyglot.util.*;
import polyglot.frontend.*;
import polyglot.visit.*;
import java.util.*;

public class PhysicalDomains extends ContextVisitor
{
    public PhysicalDomains( Job job, TypeSystem ts, NodeFactory nf ) {
        super(job, ts, nf);
    }
    public Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
        n = super.leaveCall(old, n, v);

        if (n.ext() instanceof JeddExt) {
            return ((JeddExt) n.ext()).physicalDomains((JeddTypeSystem) typeSystem(),
                                              (JeddNodeFactory) nodeFactory());
        } else if (n instanceof JeddPhysicalDomains) {
            return ((JeddPhysicalDomains) n).physicalDomains((JeddTypeSystem) typeSystem(),
                                              (JeddNodeFactory) nodeFactory());
        }

        return n;
    }
}

