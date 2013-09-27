package org.overture.typechecker.utilities;

import java.util.List;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.analysis.QuestionAnswerAdaptor;
import org.overture.ast.definitions.AEqualsDefinition;
import org.overture.ast.definitions.AExplicitFunctionDefinition;
import org.overture.ast.definitions.AExplicitOperationDefinition;
import org.overture.ast.definitions.AExternalDefinition;
import org.overture.ast.definitions.AImplicitFunctionDefinition;
import org.overture.ast.definitions.AImplicitOperationDefinition;
import org.overture.ast.definitions.AImportedDefinition;
import org.overture.ast.definitions.AInheritedDefinition;
import org.overture.ast.definitions.AInstanceVariableDefinition;
import org.overture.ast.definitions.AMultiBindListDefinition;
import org.overture.ast.definitions.AMutexSyncDefinition;
import org.overture.ast.definitions.ANamedTraceDefinition;
import org.overture.ast.definitions.APerSyncDefinition;
import org.overture.ast.definitions.ARenamedDefinition;
import org.overture.ast.definitions.AStateDefinition;
import org.overture.ast.definitions.AThreadDefinition;
import org.overture.ast.definitions.ATypeDefinition;
import org.overture.ast.definitions.AValueDefinition;
import org.overture.ast.definitions.PDefinition;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.ast.intf.lex.ILexNameToken;
import org.overture.ast.typechecker.NameScope;
import org.overture.typechecker.TypeCheckerErrors;
import org.overture.typechecker.assistant.ITypeCheckerAssistantFactory;
import org.overture.typechecker.assistant.definition.PDefinitionAssistantTC;
import org.overture.typechecker.assistant.definition.PDefinitionListAssistantTC;
import org.overture.typechecker.util.HelpLexNameToken;

/**
 * This class implements a way to find type from a node in the AST
 * 
 * @author kel
 */
public class NameFinder extends QuestionAnswerAdaptor<NameFinder.Newquestion, PDefinition>
{
	public static class Newquestion
	{
		final ILexNameToken sought;
		final NameScope scope;
		
		public Newquestion(ILexNameToken sought,NameScope scope)
		{
			this.scope = scope;
			this.sought = sought;
		}

	} 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected ITypeCheckerAssistantFactory af;

	public NameFinder(ITypeCheckerAssistantFactory af)
	{
		this.af = af;
	}
	
	@Override
	public PDefinition defaultSClassDefinition(SClassDefinition node,
			Newquestion question) throws AnalysisException
	{
		PDefinition def = null;

		for (PDefinition d : node.getDefinitions())
		{
			PDefinition found = d.apply(this, question);//PDefinitionAssistantTC.findName(d, question.sought, question.scope);

			// It is possible to have an ambiguous name if the name has
			// type qualifiers that are a union of types that match several
			// overloaded functions/ops (even though they themselves are
			// distinguishable).

			if (found != null)
			{
				if (def == null)
				{
					def = found;

					if (question.sought.getTypeQualifier() == null)
					{
						break; // Can't be ambiguous
					}
				} else
				{
					if (!def.getLocation().equals(found.getLocation())
							&& PDefinitionAssistantTC.isFunctionOrOperation(def))
					{
						TypeCheckerErrors.report(3010, "Name " + question.sought
								+ " is ambiguous", question.sought.getLocation(), question.sought);
						TypeCheckerErrors.detail2("1", def.getLocation(), "2", found.getLocation());
						break;
					}
				}
			}
		}

		if (def == null)
		{
			for (PDefinition d : node.getAllInheritedDefinitions())
			{
				PDefinition indef = d.apply(this, question);//PDefinitionAssistantTC.findName(d, question.sought, question.scope);

				// See above for the following...

				if (indef != null)
				{
					if (def == null)
					{
						def = indef;

						if (question.sought.getTypeQualifier() == null)
						{
							break; // Can't be ambiguous
						}
					} else if (def.equals(indef)
							&& // Compares qualified names
							!def.getLocation().equals(indef.getLocation())
							&& !PDefinitionAssistantTC.hasSupertype(def.getClassDefinition(), indef.getClassDefinition().getType())
							&& PDefinitionAssistantTC.isFunctionOrOperation(def))
					{
						TypeCheckerErrors.report(3011, "Name " + question.sought
								+ " is multiply defined in class", question.sought.getLocation(), question.sought);
						TypeCheckerErrors.detail2("1", def.getLocation(), "2", indef.getLocation());
						break;
					}
				}
			}
		}

		return def;
	}
	
	@Override
	public PDefinition caseAEqualsDefinition(AEqualsDefinition node,
			Newquestion question) throws AnalysisException
	{
		List<PDefinition> defs = node.getDefs();

		if (defs != null)
		{
			PDefinition def = PDefinitionListAssistantTC.findName(defs, question.sought, question.scope);

			if (def != null)
			{
				return def;
			}
		}
		return null;
	}
	
	@Override
	public PDefinition caseAExplicitFunctionDefinition(
			AExplicitFunctionDefinition node, Newquestion question)
			throws AnalysisException
	{
		if (PDefinitionAssistantTC.findNameBaseCase(node, question.sought, question.scope) != null)
		{
			return node;
		}

		PDefinition predef = node.getPredef();
		if (predef != null
				&& predef.apply(this, question) != null)//PDefinitionAssistantTC.findName(predef, sought, scope) != null)
		{
			return predef;
		}

		PDefinition postdef = node.getPostdef();
		if (postdef != null
				&& postdef.apply(this, question) != null)//PDefinitionAssistantTC.findName(postdef, sought, scope) != null)
		{
			return postdef;
		}

		return null;
	}
	
	@Override
	public PDefinition caseAExplicitOperationDefinition(
			AExplicitOperationDefinition node, Newquestion question)
			throws AnalysisException
	{
		if (PDefinitionAssistantTC.findNameBaseCase(node, question.sought, question.scope) != null)
		{
			return node;
		}

		PDefinition predef = node.getPredef();
		if (predef != null
				&& predef.apply(this, question) != null)//PDefinitionAssistantTC.findName(predef, sought, scope) != null)
		{
			return predef;
		}

		PDefinition postdef = node.getPostdef();
		if (postdef != null
				&& postdef.apply(this, question) != null)//PDefinitionAssistantTC.findName(postdef, sought, scope) != null)
		{
			return postdef;
		}

		return null;
	}
	
	@Override
	public PDefinition caseAExternalDefinition(AExternalDefinition node,
			Newquestion question) throws AnalysisException
	{
		if (question.sought.getOld())
		{
			return (question.sought.equals(node.getOldname())) ? node : null;
		}

		return (question.sought.equals(node.getState().getName())) ? node : null;
	}
	
	@Override
	public PDefinition caseAImplicitFunctionDefinition(
			AImplicitFunctionDefinition node, Newquestion question)
			throws AnalysisException
	{
		if (PDefinitionAssistantTC.findNameBaseCase(node, question.sought, question.scope) != null)
		{
			return node;
		}

		PDefinition predef = node.getPredef();
		if (predef != null
				&& predef.apply(this, question) != null)//PDefinitionAssistantTC.findName(predef, sought, scope) != null)
		{
			return predef;
		}

		PDefinition postdef = node.getPostdef();
		if (postdef != null
				&& postdef.apply(this, question) != null) //PDefinitionAssistantTC.findName(postdef, sought, scope) != null)
		{
			return postdef;
		}

		return null;
	}
	
	@Override
	public PDefinition caseAImplicitOperationDefinition(
			AImplicitOperationDefinition node, Newquestion question)
			throws AnalysisException
	{
		if (PDefinitionAssistantTC.findNameBaseCase(node, question.sought, question.scope) != null)
		{
			return node;
		}

		PDefinition predef = node.getPredef();
		if (predef != null
				&& predef.apply(this, question) != null)//PDefinitionAssistantTC.findName(predef, sought, scope) != null)
		{
			return predef;
		}

		PDefinition postdef = node.getPostdef();
		if (postdef != null
				&& postdef.apply(this, question) != null)//PDefinitionAssistantTC.findName(postdef, sought, scope) != null)
		{
			return postdef;
		}

		return null;
	}
	
	@Override
	public PDefinition caseAImportedDefinition(AImportedDefinition node,
			Newquestion question) throws AnalysisException
	{
		PDefinition def = node.getDef().apply(this, question);//PDefinitionAssistantTC.findName(d.getDef(), sought, scope);

		if (def != null)
		{
			PDefinitionAssistantTC.markUsed(node);
		}

		return def;
	}

	@Override
	public PDefinition caseAInheritedDefinition(AInheritedDefinition node,
			Newquestion question) throws AnalysisException
	{
		// The problem is, when the InheritedDefinition is created, we
		// don't know its fully qualified name.

		ILexNameToken name = node.getName();
		name.setTypeQualifier(node.getSuperdef().getName().getTypeQualifier());

		if (HelpLexNameToken.isEqual(name, question.sought))
		{
			return node;
		} else if (question.scope.matches(NameScope.OLDSTATE)
				&& node.getOldname().equals(question.sought))
		{
			return node;
		}

		return null;
	}
	@Override
	public PDefinition caseAInstanceVariableDefinition(
			AInstanceVariableDefinition node, Newquestion question)
			throws AnalysisException
	{
		PDefinition found = PDefinitionAssistantTC.findNameBaseCase(node, question.sought, question.scope);
		if (found != null)
			return found;
		return question.scope.matches(NameScope.OLDSTATE)
				&& node.getOldname().equals(question.sought) ? node : null;
	}
	
	@Override
	public PDefinition caseAMultiBindListDefinition(
			AMultiBindListDefinition node, Newquestion question)
			throws AnalysisException
	{
		if (node.getDefs() != null)
		{
			PDefinition def = PDefinitionListAssistantTC.findName(node.getDefs(), question.sought, question.scope);

			if (def != null)
			{
				return def;
			}
		}

		return null;
	}
	
	@Override
	public PDefinition caseAMutexSyncDefinition(AMutexSyncDefinition node,
			Newquestion question) throws AnalysisException
	{
		return null;
	}
	
	@Override
	public PDefinition caseANamedTraceDefinition(ANamedTraceDefinition node,
			Newquestion question) throws AnalysisException
	{
		if (PDefinitionAssistantTC.findNameBaseCase(node, question.sought, question.scope) != null)
		{
			return node;
		}

		return null;
	}
	
	@Override
	public PDefinition caseAPerSyncDefinition(APerSyncDefinition node,
			Newquestion question) throws AnalysisException
	{
		return null;
	}
	
	@Override
	public PDefinition caseARenamedDefinition(ARenamedDefinition node,
			Newquestion question) throws AnalysisException
	{		
		PDefinition renamed = PDefinitionAssistantTC.findNameBaseCase(node, question.sought, question.scope);

		if (renamed != null)
		{
			PDefinitionAssistantTC.markUsed(node.getDef());
			return renamed;
		} else
		{
			// Renamed definitions hide the original name
			return null;// PDefinitionAssistantTC.findName(d.getDef(),sought, scope);
		}
	}
	
	@Override
	public PDefinition caseAStateDefinition(AStateDefinition node,
			Newquestion question) throws AnalysisException
	{		
		if (question.scope.matches(NameScope.NAMES))
		{
			PDefinition invdef = node.getInvdef();

			if (invdef != null
					&& invdef.apply(this, question) != null)//PDefinitionAssistantTC.findName(invdef, sought, scope) != null)
			{
				return invdef;
			}

			PDefinition initdef = node.getInitdef();
			if (initdef != null
					&& initdef.apply(this, question) != null)//PDefinitionAssistantTC.findName(initdef, sought, scope) != null)
			{
				return initdef;
			}
		}

		// if ( PDefinitionAssistantTC.findName(definition.getRecordDefinition(), sought, scope) != null)
		// {
		// return definition.getRecordDefinition();
		// }

		for (PDefinition d : node.getStateDefs())
		{
			PDefinition def = d.apply(this, question);//PDefinitionAssistantTC.findName(d, sought, scope);

			if (def != null)
			{
				return def;
			}
		}

		return null;
	}
	
	@Override
	public PDefinition caseAThreadDefinition(AThreadDefinition node,
			Newquestion question) throws AnalysisException
	{
		return node.getOperationDef().apply(this,question); //PDefinitionAssistantTC.findName(definition.getOperationDef(), sought, scope);
	}
	
	@Override
	public PDefinition caseATypeDefinition(ATypeDefinition node,
			Newquestion question) throws AnalysisException
	{
		PDefinition invdef = node.getInvdef();

		if (invdef != null
				&& invdef.apply(this, question) != null)//PDefinitionAssistantTC.findName(invdef, sought, scope) != null)
		{
			return invdef;
		}

		return null;
	}

	@Override
	public PDefinition caseAValueDefinition(AValueDefinition node,
			Newquestion question) throws AnalysisException
	{
		if (question.scope.matches(NameScope.NAMES))
		{
			return PDefinitionListAssistantTC.findName(node.getDefs(), question.sought, question.scope);
		}

		return null;
	}
	
	@Override
	public PDefinition defaultPDefinition(PDefinition node, Newquestion question)
			throws AnalysisException
	{
		return PDefinitionAssistantTC.findNameBaseCase(node, question.sought, question.scope);
	}
}