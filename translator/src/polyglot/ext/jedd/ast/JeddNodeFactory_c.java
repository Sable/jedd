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
 * NodeFactory for jedd extension.
 */
public class JeddNodeFactory_c extends NodeFactory_c implements JeddNodeFactory {
    public JeddNodeFactory_c() {
        super(new JeddExtFactory_c(), new JeddDelFactory_c());
    }
    protected JeddNodeFactory_c(ExtFactory extFact) {
        super(extFact);
    }

    public BDDTypeNode BDDTypeNode( Position pos, List domainPairs ) {
        return new BDDTypeNode_c( pos, domainPairs );
    }
    public Replace Replace( Position pos, Expr expr, List domainPairs ) {
        Node ret = new Replace_c( pos, expr, domainPairs );
        return (Replace) ret;
    }
    public FixPhys FixPhys( Position pos, Expr expr ) {
        Node ret = new FixPhys_c( pos, expr );
        return (FixPhys) ret;
    }
    public Relprod Relprod( Position pos, Expr lhs, Expr rhs, List ldomains, List rdomains ) {
        Node ret = new Relprod_c( pos, lhs, rhs, ldomains, rdomains );
        return (Relprod) ret;
    }
    public BDDLit BDDLit( Position pos, List pieces ) {
        Node ret = new BDDLit_c(pos, pieces);
        return (BDDLit) ret;
    }
    public BDDLitPiece BDDLitPiece( Position pos, Expr e, TypeNode domain, TypeNode phys ) {
        return new BDDLitPiece_c(pos, e, domain, phys);
    }
    public BDDTrueFalse BDDTrueFalse( Position pos, boolean value ) {
        Node ret = new BDDTrueFalse_c(pos, value );
        return (BDDTrueFalse) ret;
    }
}
