package org.overture.interpreter.runtime;

import org.overture.ast.lex.LexLocation;
import org.overture.interpreter.values.Value;
import org.overture.parser.messages.LocatedException;


public class VdmRuntimeError
{

	public static Value abort(LexLocation location, int number, String msg, Context ctxt)
	{
		throw new ContextException(number, msg, location, ctxt);
	}

	public static Value abort(LexLocation location, ValueException ve)
	{
		throw new ContextException(ve, location);
	}

	/**
	 * Abort the runtime interpretation of the definition, throwing a
	 * ContextException to indicate the call stack. The information is
	 * based on that in the Exception passed.
	 *
	 * @param e		The Exception that caused the problem.
	 * @param ctxt	The runtime context that caught the exception.
	 */

	public static Value abort(LocatedException e, Context ctxt)
	{
		throw new ContextException(e.number, e.getMessage(), e.location, ctxt);
	}
		
	
	/**
	 * Throw a PatternMatchException with the given message.
	 * @throws PatternMatchException
	 */
	public static void patternFail( int number, String msg,LexLocation location) throws PatternMatchException
	{
		throw new PatternMatchException(number, msg, location);
	}
	
	/**
	 * Throw a PatternMatchException with a message from the ValueException.
	 * @throws PatternMatchException
	 */

	public static Value patternFail(ValueException ve,LexLocation location) throws PatternMatchException
	{
		throw new PatternMatchException(ve.number, ve.getMessage(), location);
	}

//	public static Value abortRethrow(Throwable e)
//	{
//		if(e instanceof RuntimeException)
//		{
//			throw (RuntimeException) e;
//		}
//		else
//		{
//			e.printStackTrace();
//			return null;
//		}
//		
//	}
	
}
