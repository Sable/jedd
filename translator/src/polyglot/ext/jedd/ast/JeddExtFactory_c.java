package polyglot.ext.jedd.ast;

import polyglot.ast.*;
import polyglot.ext.jl.ast.*;
import polyglot.ext.jedd.extension.*;
import polyglot.util.*;
import java.util.*;

/**
 * ExtFactory for jedd extension.
 */
public class JeddExtFactory_c extends AbstractExtFactory_c {
    public Ext extAssignImpl() {
        return new JeddAssignExt_c();
    }
    public Ext extBinaryImpl() {
        return new JeddBinaryExt_c();
    }
    public Ext extLocalImpl() {
        return new JeddLocalExt_c();
    }
    public Ext extFieldImpl() {
        return new JeddFieldExt_c();
    }
    public Ext extLocalDeclImpl() {
        return new JeddLocalDeclExt_c();
    }
    public Ext extFieldDeclImpl() {
        return new JeddFieldDeclExt_c();
    }
    public Ext extFormalImpl() {
        return new JeddFormalExt_c();
    }
    public Ext extCallImpl() {
        return new JeddCallExt_c();
    }
}
