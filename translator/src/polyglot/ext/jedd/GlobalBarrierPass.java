package polyglot.ext.jedd;

import polyglot.frontend.*;
import polyglot.main.Report;
import polyglot.types.*;
import polyglot.util.*;
import java.util.*;

public class GlobalBarrierPass extends AbstractPass
{
    Job job;
    private boolean doneAlready = false;

    public GlobalBarrierPass(Pass.ID id, Job job) {
      	super(id);
	this.job = job;
    }

    /** Run all the other jobs up to this pass. */
    public boolean run() {
        if( doneAlready ) return true;
        if (Report.should_report(Report.frontend, 1))
	    Report.report(1, job + " at barrier " + id());
        if (Report.should_report(Report.frontend, 2))
	    Report.report(2, "children of " + job + " = " + job.children());

        if (job.compiler().errorQueue().hasErrors()) {
            return false;
        }

        // Bring all jobs up to the barrier.
        for( Iterator childIt = ((polyglot.ext.jedd.ExtensionInfo)job.extensionInfo()).jobs().iterator(); childIt.hasNext(); ) {
            final Job child = (Job) childIt.next();

            if( child.isRunning() ) continue;
            if (Report.should_report(Report.frontend, 2))
                Report.report(2, job + " bringing " + child + " to barrier " + id());

            ((GlobalBarrierPass) child.passByID( id() )).doneAlready = true;
            if (! job.extensionInfo().runToPass(child, id())) {
                return false;
	    }
        }

        try {
            return doStuff();
        } catch( SemanticException e ) {
            job.compiler().errorQueue().enqueue(ErrorInfo.SEMANTIC_ERROR,
		                 e.getMessage(), e.position());
            return false;
        }
    }

    /** Called after all jobs reach this pass, so that we can do something global. */
    public boolean doStuff() throws SemanticException {
        return true;
    }
}
