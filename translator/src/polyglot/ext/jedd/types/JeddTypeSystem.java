package polyglot.ext.jedd.types;

import polyglot.ast.*;
import polyglot.types.*;
import java.util.*;

public interface JeddTypeSystem extends TypeSystem {
    // TODO: declare any new methods needed
    public BDDType BDDType( List domainPairs );
    public void addAssignEdge( DNode n1, DNode n2 );
    public void addMustEqualEdge( DNode n1, DNode n2 );
    public void physicalDomains() throws SemanticException;
    public ClassType jedd();
    public ClassType domain();
    public ClassType physicalDomain();
    public ClassType relation();
    public BDDType sameDomains( BDDType t );
    public BDDType cloneDomains( BDDType t );
}
