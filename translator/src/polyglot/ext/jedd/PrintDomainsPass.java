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
