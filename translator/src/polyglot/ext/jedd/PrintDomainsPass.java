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
import polyglot.util.*;
import java.util.*;

/**
 * Extension information for jedd extension.
 */
public class PrintDomainsPass extends AbstractPass {
    private JeddNodeFactory nf;
    private JeddTypeSystem ts;
    private Job job;
    public PrintDomainsPass( Pass.ID id, Job job, TypeSystem ts, NodeFactory nf ) {
        super(id);
        this.job = job;
        this.ts = (JeddTypeSystem) ts;
        this.nf = (JeddNodeFactory) nf;
    }
    static boolean runAlready = false;
    public boolean run() {
        if( runAlready ) return true;
        runAlready = true;
        try {
            ts.physicalDomains(nf, ((ExtensionInfo) job.extensionInfo()).jobs());
        } catch( SemanticException e ) {
            job.compiler().errorQueue().enqueue(ErrorInfo.SEMANTIC_ERROR,
                                e.getMessage(), e.position());
            return false;
        }

        return true;
    }
}
