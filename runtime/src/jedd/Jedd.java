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
    public static Jedd v() {
        return instance;
    }
    public void enableProfiling() {
        Profiler.enable();
    }
    public void outputProfile( PrintStream stream ) throws IOException {
        if( Profiler.enabled() ) Profiler.v().printInfo( stream );
    }
    public void gbc() {
        jedd.internal.Jedd.v().gbc();
    }
    public void setOrder( Object[] order, boolean msbAtTop ) {
        jedd.internal.Jedd.v().setOrder( order, msbAtTop );
    }
    public void setBackend( String type ) {
        jedd.internal.Jedd.v().setBackend( type );
    }
}
