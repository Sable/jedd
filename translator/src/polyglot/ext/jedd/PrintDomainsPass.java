package polyglot.ext.jedd;

import polyglot.ast.*;
import polyglot.types.*;
import polyglot.frontend.*;
import polyglot.ext.jedd.visit.*;
import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.types.*;
import java.util.*;

/**
 * Extension information for jedd extension.
 */
public class PrintDomainsPass extends GlobalBarrierPass {
    private JeddTypeSystem ts;
    public PrintDomainsPass( Pass.ID id, Job job, TypeSystem ts ) {
        super(id,job);
        this.ts = (JeddTypeSystem) ts;
    }
    public boolean doStuff() throws SemanticException {
        ts.physicalDomains();
        return true;
    }
}
