package polyglot.ext.jedd.types;
import polyglot.types.*;
import polyglot.ext.jl.types.*;
import java.util.*;

public interface BDDType extends ReferenceType {
    public Map map();
    public List domainPairs();
}

