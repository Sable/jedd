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

import polyglot.ext.jedd.types.*;
import polyglot.ext.jl.ast.*;
import polyglot.types.*;
import polyglot.ast.*;
import polyglot.util.*;
import polyglot.visit.*;
import java.util.*;

public class BDDTypeNode_c extends TypeNode_c implements BDDTypeNode, JeddGenerateJava
{
    List domainPairs;
    public BDDTypeNode_c(Position pos, List domainPairs ) {
        super(pos);
        this.domainPairs = domainPairs;
    }
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        if( type == null ) w.write("<unknown-type>");
        else w.write(type.toString());
    }
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
        JeddTypeSystem ts = (JeddTypeSystem) ar.typeSystem();

        List types = new LinkedList();

        Set seenDomains = new HashSet();
        Set seenPhys = new HashSet();

        for( Iterator pairIt = domainPairs.iterator(); pairIt.hasNext(); ) {

            final TypeNode[] pair = (TypeNode[]) pairIt.next();
            Type[] typePair = new Type[2];
            typePair[0] = pair[0].type();
            if( !typePair[0].isSubtype( ts.attribute() ) ) 
                throw new SemanticException( typePair[0]+" is not a subtype of jedd.Attribute.", pair[0].position() );
            if( seenDomains.contains( typePair[0] ) ) 
                throw new SemanticException( typePair[0]+" is duplicated.", pair[0].position() );
            seenDomains.add( typePair[0] );

            if( pair[1] != null ) {
                typePair[1] = pair[1].type();
                if( !typePair[1].isSubtype( ts.physicalDomain() ) ) 
                    throw new SemanticException( typePair[1]+" is not a subtype of jedd.PhysicalDomain.", pair[1].position() );
                if( seenPhys.contains( typePair[1] ) ) 
                    throw new SemanticException( typePair[1]+" is duplicated.", pair[1].position() );
                seenPhys.add( typePair[1] );
            }

            types.add( typePair );
        }

        return type(ts.BDDType( types ) );
    }
    public Node visitChildren(NodeVisitor v) {
        List newDomains = new LinkedList();
        for( Iterator pairIt = domainPairs.iterator(); pairIt.hasNext(); ) {
            final TypeNode[] pair = (TypeNode[]) pairIt.next();
            TypeNode[] newPair = { (TypeNode) visitChild( pair[0], v ),
                                   (TypeNode) visitChild( pair[1], v ) };
            newDomains.add( newPair );
        }
        BDDTypeNode_c ret = (BDDTypeNode_c) copy();
        ret.domainPairs = newDomains;
        return ret;
    }
    public Node generateJava( JeddTypeSystem ts, JeddNodeFactory nf ) throws SemanticException {
        return this;
    }
    public void translate(CodeWriter w, Translator tr) {
        tr.nodeFactory().CanonicalTypeNode( position(),
                ((JeddTypeSystem) tr.typeSystem()).relation()).translate(w, tr);
    }
}

