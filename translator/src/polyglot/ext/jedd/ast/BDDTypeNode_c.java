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
            if( !typePair[0].isSubtype( ts.domain() ) ) 
                throw new SemanticException( typePair[0]+" is not a subtype of jedd.Domain.", pair[0].position() );
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
        return nf.CanonicalTypeNode( position(), ts.relation()).type(type);
    }
}

