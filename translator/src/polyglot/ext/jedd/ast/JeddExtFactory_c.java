/* Jedd - A language for implementing relations using BDDs
 * Copyright (C) 2003 Ondrej Lhotak
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

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
