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
 * DelFactory for jedd extension.
 */
public class JeddDelFactory_c extends AbstractDelFactory_c {
    public JL delAssignImpl() {
        return new JeddDel_c();
    }
    public JL delBinaryImpl() {
        return new JeddDel_c();
    }
    public JL delCallImpl() {
        return new JeddDel_c();
    }
    public JL delConstructorCallImpl() {
        return new JeddDel_c();
    }
    public JL delConstructorDeclImpl() {
        return new JeddDel_c();
    }
    public JL delFieldDeclImpl() {
        return new JeddDel_c();
    }
    public JL delFieldImpl() {
        return new JeddDel_c();
    }
    public JL delLocalDeclImpl() {
        return new JeddDel_c();
    }
    public JL delLocalImpl() {
        return new JeddDel_c();
    }
    public JL delMethodDeclImpl() {
        return new JeddDel_c();
    }
    public JL delNewImpl() {
        return new JeddDel_c();
    }
    public JL delReturnImpl() {
        return new JeddDel_c();
    }
}
