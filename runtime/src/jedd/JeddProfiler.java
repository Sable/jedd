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

package jedd;
import java.util.*;
import java.io.*;

public class JeddProfiler
{ 
    private static JeddProfiler instance = new JeddProfiler();
    public static JeddProfiler v() { return instance; }

    LinkedList events = new LinkedList();
    LinkedList stack = new LinkedList();

    public void start( String eventName, int bdd ) {
        int fls = JeddNative.falseBDD();
        JeddNative.delRef( fls );
        start( eventName, bdd, fls );
    }
    public void start( String eventName, int bdd1, int bdd2 ) {
        Event e = new Event();
        e.bdd1 = bdd1;
        e.bdd2 = bdd2;
        e.startTime = new Date();
        stack.addLast( e );
    }
    public void finish( String eventName, int bdd ) {
        Event e = (Event) stack.removeLast();
        e.time = new Date().getTime() - e.startTime.getTime();
        e.type = eventName;
        e.stackTrace = stackTrace();
        if( e.time > 0 ) events.add( e );
    }
    public void printInfo( PrintStream out ) {
        out.println( "drop table events;" );
        out.println( "create table events ( type string, stack string, time int ) ;" );
        for( Iterator eIt = events.iterator(); eIt.hasNext(); ) {
            final Event e = (Event) eIt.next();
            out.println( e.toString() );
        }
    }
    private String stackTrace() {
        Throwable t = new Throwable();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        t.printStackTrace( new PrintStream(baos) );
        //return baos.toString().replaceAll("\n","\\\\r\\\\n");
        return baos.toString();
    }

    class Event {
        Date startTime;
        long time;
        int bdd1;
        int bdd2;
        int bdd3;
        String stackTrace;
        String type;
        public String toString() {
            return "insert into events values( '"+type+"', '"+stackTrace+"', "+time+" );";
        }
    }
}
