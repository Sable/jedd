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
import polyglot.types.Flags;
import polyglot.types.Package;
import polyglot.types.Type;
import polyglot.types.Qualifier;
import polyglot.util.*;
import java.util.*;

/**
 * NodeFactory for jedd extension.
 */
public interface JeddNodeFactory extends NodeFactory {
    // TODO: Declare any factory methods for new AST nodes.
    public BDDTypeNode BDDTypeNode( Position pos, List domainPairs );
    public Replace Replace( Position pos, Expr expr, List domainPairs );
    public FixPhys FixPhys( Position pos, Expr expr );
    public Relprod Relprod( Position pos, Expr lhs, Expr rhs, List ldomains, List rdomains );
    public BDDLit BDDLit( Position pos, List pieces );
    public BDDLitPiece BDDLitPiece( Position pos, Expr e, TypeNode domain, TypeNode phys );
    public BDDTrueFalse BDDTrueFalse( Position pos, boolean value );
}
