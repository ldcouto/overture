package org.overture.codegen.trans;

import java.util.List;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.ast.intf.lex.ILexLocation;
import org.overture.ast.intf.lex.ILexNameToken;
import org.overture.ast.lex.LexNameToken;
import org.overture.ast.types.PType;
import org.overture.codegen.logging.Logger;

public class OldNameRenamer extends DepthFirstAnalysisAdaptor
{
	private char OLD_PREFIX = '_';
	
	@Override
	public void caseILexNameToken(ILexNameToken node) throws AnalysisException
	{
		if (node.getOld())
		{
			String module = node.getModule();
			String name = OLD_PREFIX + node.getName();
			ILexLocation loc = node.getLocation();
			boolean old = node.getOld();
			boolean expl = node.getExplicit();

			ILexNameToken oldRepl = new LexNameToken(module, name, loc, old, expl);

			List<PType> typeQualifier = node.getTypeQualifier();
			oldRepl.setTypeQualifier(typeQualifier);

			if (node.parent() != null)
			{
				node.parent().replaceChild(node, oldRepl);
			} else
			{
				Logger.getLog().printErrorln("Could not find parent of lex name token: "
						+ node + " in '" + this.getClass().getSimpleName() + "'");
			}
		}
	}
}
