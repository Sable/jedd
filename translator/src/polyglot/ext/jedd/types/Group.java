package polyglot.ext.jedd.types;

import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.extension.*;
import polyglot.ast.*;
import polyglot.types.*;
import polyglot.util.*;
import polyglot.frontend.*;
import polyglot.visit.*;
import java.util.*;

public class Group {
    public Set dnodes = new HashSet();
    public static Set groups = new HashSet();
    public static Map dnodeToGroup = new HashMap();

    public static Group v( DNode dnode ) {
        Group ret = (Group) dnodeToGroup.get(dnode);
        if( ret == null ) {
            ret = new Group();
            ret.dnodes.add(dnode);
            groups.add(ret);
            dnodeToGroup.put( dnode, ret );
        }
        return ret;
    }

    public void merge( Group other ) {
        for( Iterator dnodeIt = other.dnodes.iterator(); dnodeIt.hasNext(); ) {
            final DNode dnode = (DNode) dnodeIt.next();
            dnodeToGroup.put( dnode, this );
            dnodes.add( dnode );
        }
        groups.remove( other );
    }
}

