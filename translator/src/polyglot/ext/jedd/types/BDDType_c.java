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

package polyglot.ext.jedd.types;
import polyglot.types.*;
import polyglot.ext.jl.types.*;
import polyglot.util.*;
import java.util.*;

public class BDDType_c extends ReferenceType_c implements BDDType {
    private boolean isLitType;
    public boolean isLitType() { return isLitType; }
    private List domainPairs = new LinkedList();
    public List domainPairs() { return domainPairs; }
    BDDType_c( TypeSystem ts, List domainPairs, boolean isLitType ) {
        super(ts);
        this.isLitType = isLitType;
        this.domainPairs = domainPairs;
        if( isLitType && !domainPairs.isEmpty() ) 
            throw new InternalCompilerError("Attempting to create literal BDD type with attributes.");
        HashSet seen = new HashSet();
        for( Iterator domainsIt = domainPairs.iterator(); domainsIt.hasNext(); ) {
            final Type[] domains = (Type[]) domainsIt.next();
            if( domains[0] != null ) {
                if( !seen.add(domains[0]) ) {
                    throw new RuntimeException( "bad type: "+domainPairs );
                }
            }
            if( domains[1] != null ) {
                if( !seen.add(domains[1]) ) {
                    throw new RuntimeException( "bad type: "+domainPairs );
                }
            }
        }
    }
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("<");
        for( Iterator pairIt = domainPairs.iterator(); pairIt.hasNext(); ) {
            final Type[] pair = (Type[]) pairIt.next();
            if( pair[0] == null ) 
                ret.append( "<unknown>" );
            else
                ret.append( pair[0].toString() );
            if( pair[1] != null ) {
                ret.append(":");
                ret.append( pair[1].toString() );
            }
            if( pairIt.hasNext() ) ret.append(", ");
        }
        ret.append(">");
        return ret.toString();
    }
    public String translate(Resolver c) {
        return toString();
    }
    public boolean isCastValidImpl(Type toType) {
        if( isImplicitCastValidImpl(toType) ) return true;
        if( toType instanceof BDDType && map().keySet().isEmpty() ) return true;
        return false;
    }
    public boolean isImplicitCastValidImpl(Type toType) {
        if( toType instanceof NullType ) return false;
        if( descendsFromImpl(toType) ) return true;
        if( !( toType instanceof BDDType ) ) return false;
        BDDType to = (BDDType) toType;
        if( map().keySet().equals( to.map().keySet() ) ) return true;
        if( isLitType() ) return true;
        if( to.map().keySet().isEmpty() ) return true;
        return false;
    }
    public Map map() {
        HashMap ret = new HashMap();
        for( Iterator pairIt = domainPairs.iterator(); pairIt.hasNext(); ) {
            final Type[] pair = (Type[]) pairIt.next();
            ret.put( pair[0], pair[1] );
        }
        return ret;
    }
    public boolean isCanonical() {
        return true;
    }
    public List interfaces() { return new LinkedList(); }
    public List fields() { return new LinkedList(); }
    public List methods() { return new LinkedList(); }
    private JeddTypeSystem jeddts() { return (JeddTypeSystem) ts; }
    public Type superType() { 
        return jeddts().jeddRelation();
    }
    public boolean descendsFromImpl(Type ancestor) {
        return ts.equals(ancestor, jeddts().jeddRelation() ) || ts.equals(ancestor, ts.Object());
    }
    public FieldInstance fieldNamed( String name ) { return null; }
}

