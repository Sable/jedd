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

package polyglot.ext.jedd;

import polyglot.lex.Lexer;
import polyglot.ext.jedd.parse.Lexer_c;
import polyglot.ext.jedd.parse.Grm;
import polyglot.ext.jedd.ast.*;
import polyglot.ext.jedd.types.*;
import polyglot.ext.jedd.visit.*;

import polyglot.ast.*;
import polyglot.types.*;
import polyglot.util.*;
import polyglot.visit.*;
import polyglot.frontend.*;
import polyglot.main.*;

import java.util.*;
import java.io.*;

/**
 * Extension information for jedd extension.
 */
public class ExtensionInfo extends polyglot.ext.jl.ExtensionInfo {
    static {
        // force Topics to load
        Topics t = new Topics();
    }

    public String defaultFileExtension() {
        return "jedd";
    }

    public String compilerName() {
        return "jeddc";
    }

    public Parser parser(Reader reader, FileSource source, ErrorQueue eq) {
        Lexer lexer = new Lexer_c(reader, source.name(), eq);
        Grm grm = new Grm(lexer, ts, nf, eq);
        return new CupParser(grm, source, eq);
    }

    protected NodeFactory createNodeFactory() {
        return new JeddNodeFactory_c();
    }

    protected TypeSystem createTypeSystem() {
        return new JeddTypeSystem_c();
    }

    public static final Pass.ID METHOD_DECL_MAP = new Pass.ID("method-decl-map");
    public static final Pass.ID JEDD_BARRIER = new Pass.ID("jedd-barrier");
    public static final Pass.ID PHYSICAL_DOMAINS = new Pass.ID("physical-domains");
    public static final Pass.ID PRINT_DOMAINS = new Pass.ID("print-domains");
    public static final Pass.ID INSERT_REPLACE = new Pass.ID("insert-replace");
    public static final Pass.ID GENERATE_JAVA = new Pass.ID("generate-java");
    public static final Pass.ID DOMAINS_BARRIER = new Pass.ID("domains-barrier");
    public static final Pass.ID FOR_TRANSFORM = new Pass.ID("for-transform");
    public static final Pass.ID LIVE_VARIABLES = new Pass.ID("live-variables");

    private void doRemovePass( List passes, Pass.ID pass ) {
        for( Iterator pIt = passes.iterator(); pIt.hasNext(); ) {
            final Pass p = (Pass) pIt.next();
            if( p.id() == pass ) {
                removePass( passes, pass );
                return;
            }
        }
    }
    public List passes(Job job) {
        List passes = super.passes(job);
        beforePass(passes, Pass.DISAM_ALL,
                new VisitorPass(METHOD_DECL_MAP, job,
                    new MethodDeclMapPass(job, ts, nf) ) );
        beforePass(passes, Pass.PRE_OUTPUT_ALL,
                new GlobalBarrierPass(JEDD_BARRIER, job ) );
        beforePass(passes, Pass.PRE_OUTPUT_ALL,
                new GlobalBarrierPass(DOMAINS_BARRIER, job ) );
        if( !(job.source() instanceof CmdLineFileSource )  ) {
            doRemovePass( passes, Pass.DISAM );
            doRemovePass( passes, Pass.TYPE_CHECK );
            doRemovePass( passes, Pass.EXC_CHECK );
            doRemovePass( passes, Pass.REACH_CHECK );
            doRemovePass( passes, Pass.EXIT_CHECK );
            doRemovePass( passes, Pass.INIT_CHECK );
            doRemovePass( passes, Pass.CONSTRUCTOR_CHECK );
            doRemovePass( passes, Pass.SERIALIZE );
            doRemovePass( passes, Pass.OUTPUT );
            beforePass(passes, Pass.PRE_OUTPUT_ALL,
                    new PrintDomainsPass(PRINT_DOMAINS, job, ts, nf ) );
            return passes;
        }
        beforePass(passes, DOMAINS_BARRIER,
                new VisitorPass(PHYSICAL_DOMAINS, job,
                    new PhysicalDomains( job, ts, nf ) ) );
        beforePass(passes, Pass.PRE_OUTPUT_ALL,
                new PrintDomainsPass(PRINT_DOMAINS, job, ts, nf ) );
        /*
        beforePass(passes, Pass.PRE_OUTPUT_ALL,
                new VisitorPass(INSERT_REPLACE, job,
                    new InsertReplace( job, ts, nf ) ) );
        beforePass(passes, Pass.PRE_OUTPUT_ALL,
                new VisitorPass(FOR_TRANSFORM, job,
                    new ForTransformPass( job, ts, nf ) ) );
        beforePass(passes, Pass.PRE_OUTPUT_ALL,
                new VisitorPass(LIVE_VARIABLES, job,
                    new LiveVariables( job, ts, nf ) ) );
                    */
        beforePass(passes, Pass.PRE_OUTPUT_ALL,
                new VisitorPass(GENERATE_JAVA, job,
                    new GenerateJava( job, ts, nf ) ) );
        return passes;
    }

    private Set jobsSet = new HashSet();
    public SourceJob addJob(Source source, Node ast) {
        SourceJob ret = super.addJob( source, ast );
        jobsSet.add( ret );
        return ret;
    }
    public Collection jobs() {
        return new LinkedList(jobsSet);
    }
    public SourceLoader sourceLoader() {
        if( source_loader == null ) {
            source_loader = new JeddSourceLoader(this, getOptions().source_path);
        }
        return source_loader;
    }
}
