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
    public void enableProfiling() {
        Profiler.enable();
    }
    /** Outputs the profiling data recorded by the profiler to an SQL file.
     */
    public void outputProfile( PrintStream stream ) throws IOException {
        if( Profiler.enabled() ) Profiler.v().printInfo( stream );
    }
    /** Forces the underlying BDD backend to perform a garbage collection. */
    public void gbc() {
        jedd.internal.Jedd.v().gbc();
    }
    /** Sets the physical domain ordering in the BDD. The first argument
     * is an array containing the physical domains in the order in which
     * they should appear in the BDD, from the top of the BDD to the bottom
     * (constant nodes). To interleave the bits of several physical domains,
     * place them in a separate array, and then include this array as an
     * element of the main array. The second argument determines the order
     * of bits within each domain. Set it to true to order the bits 
     * from most significant to least significant when moving down the BDD
     * from top to bottom, or to false for the opposite order.
     */
    public void setOrder( Object[] order, boolean msbAtTop ) {
        jedd.internal.Jedd.v().setOrder( order, msbAtTop );
    }
    /** Sets the BDD backend that Jedd should use. Currently valid values are
     * "buddy", "cudd", "sablejbdd", and "javabdd".
     */
    public void setBackend( String type ) {
        jedd.internal.Jedd.v().setBackend( type );
    }
    /** Creates a Shifter that can be applied to shift bits within a BDD. */
    public Shifter makeShifter( int[] fromBits, int[] toBits ) {
        return jedd.internal.Jedd.v().makeShifter(fromBits, toBits);
    }
    public interface Shifter {}
}
