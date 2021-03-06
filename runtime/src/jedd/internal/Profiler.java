/* Jedd - A language for implementing relations using BDDs
 * Copyright (C) 2003, 2004, 2005 Ondrej Lhotak
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

package jedd.internal;
import java.util.*;
import java.io.*;

public class Profiler
{ 
    private static boolean VERBOSE = false;
    private static Profiler instance;
    public static Profiler v() { return instance; }
    public static boolean enabled() {
        return instance != null;
    }
    public static void enable(PrintStream out) {
        instance = new Profiler(out);
        out.println( "drop table events;" );
        out.println( "create table events ( id integer primary key, type string, stackid int, time int, inputA int, inputB int, output int ) ;" );
        out.println( "drop table stacks;" );
        out.println( "create table stacks ( id integer primary key, shrt string, stack string ) ;" );
        out.println( "drop table shapes;" );
        out.println( "create table shapes ( eventid int, level int, nodes int ) ;" );
        out.println( "drop table sizes;" );
        out.println( "create table sizes ( eventid int, nodes int ) ;" );
        out.println( "drop table physdoms;" );
        out.println( "create table physdoms ( name string, minpos int, maxpos int ) ;" );
        out.println( "begin transaction;" );
    }

    private Profiler(PrintStream out) {
        this.out = out;
    }
    private PrintStream out;
    //LinkedList events = new LinkedList();
    LinkedList stack = new LinkedList();
    Map stackMap = new HashMap();
    static int nextStackTrace = 1;

    public void start( String eventName, RelationInstance bdd ) {
        RelationInstance fls = Backend.v().falseBDD();
        Backend.v().delRef( fls );
        start( eventName, bdd, fls );
    }
    public void start( String eventName, RelationInstance bdd1, RelationInstance bdd2 ) {
        if(VERBOSE) {
            System.out.println(eventName+" "+Backend.v().numNodes(bdd1)+" "+Backend.v().numNodes(bdd2));
        }
        Event e = new Event();
        e.inputA = new BDD(bdd1);
        e.inputB = new BDD(bdd2);
        e.startTime = new Date();
        stack.addLast( e );
    }
    private long timeLimit = 10;
    public static void setTimeLimit(long newLimit) {
        instance.timeLimit = newLimit;
    }
    public void finish( String eventName, RelationInstance bdd ) {
        if(VERBOSE) {
            System.out.println(eventName+" "+Backend.v().numNodes(bdd));
        }
        Event e = (Event) stack.removeLast();
        e.time = new Date().getTime() - e.startTime.getTime();
        e.type = eventName;
        e.stackTrace = stackTrace();
        //events.add( e );
        e.output = new BDD(bdd);
        if(e.time <= timeLimit) {
            e.inputA.shape = null;
            e.inputB.shape = null;
            e.output.shape = null;
        }
        if( e.time > 0 ) out.println(e.toString());
    }
    public void printInfo() {
        for( Iterator pdIt = Jedd.v().physicalDomains.iterator(); pdIt.hasNext(); ) {
            final PhysicalDomain pd = (PhysicalDomain) pdIt.next();
            out.println( "insert into physdoms values('"+pd.name()+"', "+pd.minPhysPos+", "+pd.maxPhysPos+" );" );
        }
        out.println( "insert into physdoms values('', "+PhysicalDomain.nextBit+", "+PhysicalDomain.nextBit+" );" );
        out.println( "end transaction;" );
        out.println( "create index sizesindex on sizes ( eventid );" );
        out.println( "create index shapesindex on shapes ( eventid );" );
        out.close();
    }
    private String stackTrace() {
        Throwable t = new Throwable();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        t.printStackTrace( new PrintStream(baos) );
        //return baos.toString().replaceAll("\n","\\\\r\\\\n");
        return baos.toString();
    }

    private String shortStack( String stack ) {
        StringTokenizer st = new StringTokenizer(stack,"\n");
        String token;
        while(true) {
            if( !st.hasMoreTokens() ) throw new RuntimeException("error parsing stack trace: "+stack);
            token = st.nextToken();
            if( token.indexOf("at jedd.Relation") >= 0 ) break;
            if( token.indexOf("at jedd.Jedd") >= 0 ) break;
            if( token.indexOf("at jedd.internal.Relation") >= 0 ) break;
            if( token.indexOf("at jedd.internal.Jedd") >= 0 ) break;
        }
        while(true) {
            if( !st.hasMoreTokens() ) throw new RuntimeException("error parsing stack trace: "+stack);
            token = st.nextToken();
            if( token.indexOf("at jedd.Relation") < 0
            &&  token.indexOf("at jedd.Jedd") < 0
            &&  token.indexOf("at jedd.internal.Relation") < 0
            &&  token.indexOf("at jedd.internal.Jedd") < 0 ) break;
        }
        return token;
    }


    static int nextEventId = 1;
    class Event {
        int id;
        Date startTime;
        long time;
        BDD inputA;
        BDD inputB;
        BDD output;
        String stackTrace;
        String type;
        public Event() {
            id = nextEventId++;
        }
        public String toString() {
            String ret = "";
            Integer st = (Integer) stackMap.get( stackTrace );
            if( st == null ) {
                stackMap.put( stackTrace,
                    st = new Integer(nextStackTrace++) );
                ret = "insert into stacks values( "+st+", '"+shortStack(stackTrace)+"', '"+stackTrace+"' );\n";
            }
            return ret +
                "insert into events values( "+id+", '"+type+"', "+st+", "+time+", "+
                inputA.id+", "+inputB.id+", "+output.id+");\n" +
                inputA.toString()+"\n"+
                inputB.toString()+"\n"+
                output.toString()+"\n";
        }
    }
    private static int nextBDDId = 1;
    public class BDD {
        int id;
        int nodeCount = 0;
        int[] shape;
        BDD( RelationInstance bdd ) {
            id = nextBDDId++;
            shape = new int[Backend.v().numBits()];
            Backend.v().getShape( bdd, shape );
            for( int i = 0; i < shape.length; i++ ) nodeCount += shape[i];
        }
        public String toString() {
            StringBuffer b = new StringBuffer();
            if( shape != null ) {
                for( int i = 0; i < shape.length; i++ ) {
                    if( shape[i] > 0 ) 
                        b.append("insert into shapes values( "+id+", "+i+", "+shape[i]+" );\n" );
                }
            }
            b.append("insert into sizes values( "+id+", "+nodeCount+" );");
            return b.toString();
        }
    }
}
