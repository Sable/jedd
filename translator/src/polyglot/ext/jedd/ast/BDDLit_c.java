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

import polyglot.ext.jedd.extension.*;
import polyglot.ext.jedd.types.*;
import polyglot.ext.jl.ast.*;
import polyglot.types.*;
import polyglot.ast.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

public class BDDLit_c extends Lit_c implements BDDLit, JeddGenerateJava
{
    private List pieces;
    public List pieces() { return pieces; }
    public BDDLit_c(Position pos, List pieces) {
        super(pos);
        this.pieces = pieces;
    }
    public Object objValue() {
        throw new InternalCompilerError( "Jedd should not be treating a BDD as an object." );
    }
    public Node visitChildren(NodeVisitor v) {
        List newPieces = new LinkedList();
        for( Iterator pieceIt = pieces.iterator(); pieceIt.hasNext(); ) {
            final BDDLitPiece piece = (BDDLitPiece) pieceIt.next();
            newPieces.add( visitChild( piece, v ) );
        }

        BDDLit_c ret = (BDDLit_c) copy();
        ret.pieces = newPieces;
        return ret;
    }
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.begin(0);
        w.write("new {");
        for( Iterator pieceIt = pieces.iterator(); pieceIt.hasNext(); ) {
            final BDDLitPiece piece = (BDDLitPiece) pieceIt.next();
            print(piece,w,tr);
            if( pieceIt.hasNext() ) {
                w.write(",");
                w.allowBreak(0, " ");
            }
        }
        w.write("}");
        w.end();
    }
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        JeddTypeSystem ts = (JeddTypeSystem) tc.typeSystem();
        JeddNodeFactory nf = (JeddNodeFactory) tc.nodeFactory();

        Set seenAttrs = new HashSet();
        List pairs = new LinkedList();
        for( Iterator pieceIt = pieces.iterator(); pieceIt.hasNext(); ) {
            final BDDLitPiece piece = (BDDLitPiece) pieceIt.next();
            ClassType[] newPair = { (ClassType) piece.domain().type(),
                                    piece.phys() == null ? null :
                                        (ClassType) piece.phys().type() };
            if( !newPair[0].isSubtype( ts.domain() ) ) 
                throw new SemanticException( ""+newPair[0]+" does not extend jedd.Domain" );
            if( newPair[1] != null && !newPair[1].isSubtype( ts.physicalDomain() ) ) 
                throw new SemanticException( ""+newPair[1]+" does not extend jedd.PhysicalDomain" );
            pairs.add( newPair );
            if( !seenAttrs.add( newPair[0] ) ) throw new SemanticException(
                    "Duplicate attribute "+newPair[0] );
        }
        BDDLit_c ret = (BDDLit_c) type(ts.BDDType(pairs));
        ret.pieces = pieces;
        return ret;
    }
    public Node generateJava( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        Position p = position();

        Map map = ((BDDType)type()).map();

        List exprs = new LinkedList();
        List domains = new LinkedList();
        List phys = new LinkedList();
        for( Iterator pieceIt = pieces().iterator(); pieceIt.hasNext(); ) {
            final BDDLitPiece piece = (BDDLitPiece) pieceIt.next();
            Position pp = piece.domain().position();
            exprs.add( piece.e() );
            domains.add( nf.Call( pp, piece.domain(), "v" ) );
            Type physType = (Type) map.get(piece.domain().type());
            phys.add( nf.Call( pp, nf.CanonicalTypeNode( pp, physType ), "v" ) );
        }

        Call getJedd = nf.Call( p, nf.CanonicalTypeNode( p, ts.jedd() ), "v"  ); 

        return nf.Call( 
                p,
                getJedd,
                "literal",
                nf.NewArray(
                    p,
                    nf.CanonicalTypeNode( p, ts.Object() ),
                    new LinkedList(),
                    1,
                    nf.ArrayInit( p, exprs )
                    ),
                nf.NewArray(
                    p,
                    nf.CanonicalTypeNode( p, ts.domain() ),
                    new LinkedList(),
                    1,
                    nf.ArrayInit( p, domains )
                    ),
                nf.NewArray(
                    p,
                    nf.CanonicalTypeNode( p, ts.physicalDomain() ),
                    new LinkedList(),
                    1,
                    nf.ArrayInit( p, phys )
                    )
                ).type( type() );
    }
}

