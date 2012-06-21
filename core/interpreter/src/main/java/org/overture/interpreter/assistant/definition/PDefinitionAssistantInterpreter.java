package org.overture.interpreter.assistant.definition;



import org.overture.ast.definitions.AAssignmentDefinition;
import org.overture.ast.definitions.AEqualsDefinition;
import org.overture.ast.definitions.AExplicitFunctionDefinition;
import org.overture.ast.definitions.AExplicitOperationDefinition;
import org.overture.ast.definitions.AImplicitFunctionDefinition;
import org.overture.ast.definitions.AImplicitOperationDefinition;
import org.overture.ast.definitions.AImportedDefinition;
import org.overture.ast.definitions.AInheritedDefinition;
import org.overture.ast.definitions.AInstanceVariableDefinition;
import org.overture.ast.definitions.ALocalDefinition;
import org.overture.ast.definitions.APerSyncDefinition;
import org.overture.ast.definitions.ARenamedDefinition;
import org.overture.ast.definitions.AStateDefinition;
import org.overture.ast.definitions.AThreadDefinition;
import org.overture.ast.definitions.ATypeDefinition;
import org.overture.ast.definitions.AUntypedDefinition;
import org.overture.ast.definitions.AValueDefinition;
import org.overture.ast.definitions.PDefinition;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.ast.expressions.PExp;
import org.overture.interpreter.runtime.Context;
import org.overture.interpreter.runtime.ObjectContext;
import org.overture.interpreter.values.NameValuePairList;
import org.overture.interpreter.values.ValueList;
import org.overture.pog.obligation.POContextStack;
import org.overture.pog.obligation.ProofObligationList;
import org.overture.pog.visitor.PogVisitor;
import org.overture.typechecker.assistant.definition.PDefinitionAssistantTC;

public class PDefinitionAssistantInterpreter extends PDefinitionAssistantTC
{

	public static NameValuePairList getNamedValues(PDefinition d,
			Context initialContext)
	{
		switch (d.kindPDefinition())
		{
			case ASSIGNMENT:
				try
				{
					return AAssignmentDefinitionAssistantInterpreter.getNamedValues((AAssignmentDefinition) d, initialContext);
				} catch (Throwable e1)
				{
					e1.printStackTrace();
					return new NameValuePairList();
				}
			case EQUALS:
				try
				{
					return AEqualsDefinitionAssistantInterpreter.getNamedValues((AEqualsDefinition)d,initialContext);
				} catch (Throwable e)
				{
					e.printStackTrace();
					return new NameValuePairList();
				}
			case EXPLICITFUNCTION:
				return AExplicitFunctionDefinitionAssistantInterpreter.getNamedValues((AExplicitFunctionDefinition)d,initialContext);
			case EXPLICITOPERATION:
				return AExplicitOperationDefinitionAssistantInterpreter.getNamedValues((AExplicitOperationDefinition)d,initialContext);
			case IMPLICITFUNCTION:
				return AImplicitFunctionDefinitionAssistantInterpreter.getNamedValues((AImplicitFunctionDefinition)d,initialContext);
			case IMPLICITOPERATION:
				return AImplicitOperationDefinitionAssistantInterpreter.getNamedValues((AImplicitOperationDefinition)d,initialContext);
			case IMPORTED:
				return AImportedDefinitionAssistantInterpreter.getNamedValues((AImportedDefinition)d,initialContext);
			case INHERITED:
				return AInheritedDefinitionAssistantInterpreter.getNamedValues((AInheritedDefinition)d, initialContext);
			case INSTANCEVARIABLE:
				try
				{
					return AInstanceVariableDefinitionAssistantInterpreter.getNamedValues((AInstanceVariableDefinition)d,initialContext);
				} catch (Throwable e)
				{
					e.printStackTrace();
					return new NameValuePairList();
				}
			case LOCAL:
				return ALocalDefinitionAssistantInterpreter.getNamedValues((ALocalDefinition)d,initialContext);
			case RENAMED:
				return ARenamedDefinitionAssistantInterpreter.getNamedValues((ARenamedDefinition)d,initialContext);
			case THREAD:
				return AThreadDefinitionAssistantInterpreter.getNamedValues((AThreadDefinition)d,initialContext);
			case TYPE:
				return ATypeDefinitionAssistantInterpreter.getNamedValues((ATypeDefinition)d,initialContext);
			case UNTYPED:
				return AUntypedDefinitionAssistantInterpreter.getNamedValues((AUntypedDefinition)d,initialContext);
			case VALUE:
				try
				{
					return AValueDefinitionAssistantInterpreter.getNamedValues((AValueDefinition)d,initialContext);
				} catch (Throwable e)
				{
					e.printStackTrace();
					return new NameValuePairList();
				}
			default:
				return new NameValuePairList();		// Overridden
		}
	}

	public static ProofObligationList getProofObligations(
			PDefinition d, POContextStack ctxt)
	{
		try
		{
			return d.apply(new PogVisitor(), new POContextStack());
		} catch (Throwable e)
		{
			e.printStackTrace();
		}
		return new ProofObligationList();
	}

	/**
	 * Return a list of external values that are read by the definition.
	 * @param ctxt The context in which to evaluate the expressions.
	 * @return A list of values read.
	 */
	public static ValueList getValues(PDefinition d,
			ObjectContext ctxt)
	{
		switch (d.kindPDefinition())
		{
			case ASSIGNMENT:
				return AAssignmentDefinitionAssistantInterpreter.getValues((AAssignmentDefinition)d,ctxt);
			case EQUALS:
				return AEqualsDefinitionAssistantInterpreter.getValues((AEqualsDefinition)d, ctxt);
			case INSTANCEVARIABLE:
				return AInstanceVariableDefinitionAssistantInterpreter.getValues((AInstanceVariableDefinition)d,ctxt);
			case VALUE:
				return AValueDefinitionAssistantInterpreter.getValues((AValueDefinition)d,ctxt);
			default:
				return new ValueList();
		}
		
	}

	public static PExp findExpression(PDefinition d, int lineno)
	{
		switch (d.kindPDefinition())
		{
			case ASSIGNMENT:
				return AAssignmentDefinitionAssistantInterpreter.findExpression((AAssignmentDefinition)d,lineno);
			case CLASS:
				return SClassDefinitionAssistantInterpreter.findExpression((SClassDefinition)d,lineno);
			case EQUALS:
				return AEqualsDefinitionAssistantInterpreter.findExpression((AEqualsDefinition)d,lineno);
			case EXPLICITFUNCTION:
				return AExplicitFunctionDefinitionAssistantInterpreter.findExpression((AExplicitFunctionDefinition)d,lineno);
			case EXPLICITOPERATION:
				return AExplicitOperationDefinitionAssistantInterpreter.findExpression((AExplicitOperationDefinition)d,lineno);
			case IMPLICITFUNCTION:
				return AImplicitFunctionDefinitionAssistantInterpreter.findExpression((AImplicitFunctionDefinition)d,lineno);
			case IMPLICITOPERATION:
				return AImplicitOperationDefinitionAssistantInterpreter.findExpression((AImplicitOperationDefinition)d,lineno);
			case INSTANCEVARIABLE:
				return AInstanceVariableDefinitionAssistantInterpreter.findExpression((AInstanceVariableDefinition)d,lineno);
			case PERSYNC:
				return APerSyncDefinitionAssistantInterpreter.findExpression((APerSyncDefinition)d,lineno);
			case STATE:
				return AStateDefinitionAssistantInterpreter.findExpression((AStateDefinition)d,lineno);
			case THREAD:
				return AThreadDefinitionAssistantInterpreter.findExpression((AThreadDefinition)d,lineno);
			case TYPE:
				return ATypeDefinitionAssistantInterpreter.findExpression((ATypeDefinition)d,lineno);
			case VALUE:
				return AValueDefinitionAssistantInterpreter.findExpression((AValueDefinition)d,lineno);
			default:
				return null;
		}
	}

}