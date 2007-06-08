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
import jedd.internal.*;
import jedd.order.*;
import java.io.*;

public class Jedd {
    private static Jedd instance = new Jedd();
    private Jedd() {
    }
    /** Returns the singleton instance of Jedd. */
    public static Jedd v() {
        return instance;
    }
    /** Starts up the Jedd profiler which records the time taken by 
     * each operation. 
     */
    public void enableProfiling( PrintStream stream ) {
        Profiler.enable(stream);
    }
    /** Set the time limit for profiling: the profile records the shape of only
     * those operations which take at least timeLimit ms. Default is 10. */
    public void setTimeLimit( long timeLimit ) {
        Profiler.setTimeLimit(timeLimit);
    }
    /** Outputs the profiling data recorded by the profiler to an SQL file.
     */
    public void outputProfile() {
        if( Profiler.enabled() ) Profiler.v().printInfo();
    }
    /** Forces the underlying BDD backend to perform a garbage collection. */
    public void gbc() {
        jedd.internal.Jedd.v().gbc();
    }
    /** Sets the physical domain ordering in the BDD.
     */
    public void setOrder( Order order ) {
        jedd.internal.Jedd.v().setOrder( order );
    }
    /** Allows/prevents dynamic BDD variable reordering.
     */
    public void allowReorder( boolean setting ) {
        jedd.internal.Jedd.v().allowReorder( setting );
    }
    /** Sets the BDD backend that Jedd should use. Currently valid values are
     * "buddy", "cudd", "sablejbdd", and "javabdd".
     */
    public void setBackend( String type ) {
        jedd.internal.Jedd.v().setBackend( type );
    }
    /** Sets the BDD backend that Jedd should use. Currently valid values are
     * "buddy", "cudd", "sablejbdd", and "javabdd". Also specifies the
     * total number of BDD nodes. If the number specified is 0, the backend
     * will grow the number of nodes as required without limit. If a number
     * other than 0 is specified, that many nodes are allocated immediately,
     * and the node table is not permitted to grow further.
     */
    public void setBackend( String type, int numNodes ) {
        jedd.internal.Jedd.v().setBackend( type, numNodes );
    }
    /** Creates a Shifter that can be applied to shift bits within a BDD. */
    public Shifter makeShifter( int[] fromBits, int[] toBits ) {
        return jedd.internal.Jedd.v().makeShifter(fromBits, toBits);
    }
    /** Causes the BDD library to print memory usage at each GC. */
    public void verboseGC() {
        jedd.internal.Jedd.v().verboseGC();
    }
    public interface Shifter {}
}
