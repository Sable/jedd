package polyglot.ext.jedd.ast;

import polyglot.ast.*;
import polyglot.ext.jl.ast.*;
import polyglot.ext.jedd.extension.*;
import polyglot.util.*;
import java.util.*;

/**
 * DelFactory for jedd extension.
 */
public class JeddDelFactory_c extends AbstractDelFactory_c {
    public JL delBinaryImpl() {
        return new JeddDel_c();
    }
    public JL delAssignImpl() {
        return new JeddDel_c();
    }
    public JL delLocalDeclImpl() {
        return new JeddDel_c();
    }
    public JL delFieldDeclImpl() {
        return new JeddDel_c();
    }
    public JL delLocalImpl() {
        return new JeddDel_c();
    }
    public JL delFieldImpl() {
        return new JeddDel_c();
    }
}
