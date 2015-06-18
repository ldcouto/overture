package org.overture.codegen.traces;

import org.overture.codegen.cgast.INode;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.ANamedTraceDeclCG;
import org.overture.codegen.cgast.statements.ASuperCallStmCG;

/**
 * This analysis determines if a named trace can be code
 * generated.
 * 
 * Code generation of traces currently does not support call object
 * statements which contain explicit field module names
 * different from the name of the enclosing class. Example:
 * 
 * A quoted method call is only supported if the explicit
 * module name is equal to that of the enclosing class. Say A
 * is a sub class of S and 'a' is an instance of A then a.A`op();
 * is allowed (although it is the same as a.op()). However,
 * a.S`op(); is not allowed.
 *
 * This is already reported as unsupported at the IR level so there
 * is no need for this analysis to detect this case.
 * 
 * The super call statement is, however, not supported by the code
 * generator when it does appear in traces. Therefore, this analysis
 * will detect this case.
 * 
 * @author pvj
 *
 */
public class TraceSupportedAnalysis extends DepthFirstAnalysisAdaptor
{
	private ANamedTraceDeclCG trace;
	private boolean isUnsupported = false;
	private String reason;
	
	public TraceSupportedAnalysis(ANamedTraceDeclCG trace)
	{
		this.trace = trace;
	}
	
	@Override
	public void caseASuperCallStmCG(ASuperCallStmCG node)
			throws AnalysisException
	{
		if(!isWithinTrace(node))
		{
			return;
		}
		
		reason = "The super call statement is not supported in traces,"
				+ "and as a consequence the trace is not generated.";
		isUnsupported = true;
	}
	
	public void run() throws AnalysisException
	{
		trace.apply(this);
	}
	
	public String getReason()
	{
		return reason;
	}
	
	private boolean isWithinTrace(INode node)
	{
		return node.getAncestor(ANamedTraceDeclCG.class) == trace;
	}
	
	public boolean isUnsupported()
	{
		return isUnsupported;
	}
}
