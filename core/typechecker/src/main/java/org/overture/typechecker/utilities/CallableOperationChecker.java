package org.overture.typechecker.utilities;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.analysis.AnswerAdaptor;
import org.overture.ast.definitions.AExplicitOperationDefinition;
import org.overture.ast.definitions.AImplicitOperationDefinition;
import org.overture.ast.definitions.AImportedDefinition;
import org.overture.ast.definitions.AInheritedDefinition;
import org.overture.ast.definitions.ARenamedDefinition;
import org.overture.ast.definitions.PDefinition;
import org.overture.typechecker.assistant.ITypeCheckerAssistantFactory;

/**
 * This class implements a way to check if an operation is callable from a node in the AST
 * 
 * @author kel
 */
public class CallableOperationChecker extends AnswerAdaptor<Boolean>
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1679012585019723923L;
	
	protected ITypeCheckerAssistantFactory af;

	public CallableOperationChecker(ITypeCheckerAssistantFactory af)
	{
		this.af = af;
	}
	
	@Override
	public Boolean caseAExplicitOperationDefinition(
			AExplicitOperationDefinition node) throws AnalysisException
	{
		return true;
	}
	
	@Override
	public Boolean caseAImplicitOperationDefinition(
			AImplicitOperationDefinition node) throws AnalysisException
	{
		return node.getBody() != null;
	}

	@Override
	public Boolean caseAImportedDefinition(AImportedDefinition node)
			throws AnalysisException
	{
		return node.getDef().apply(this);
	}
	
	@Override
	public Boolean caseAInheritedDefinition(AInheritedDefinition node)
			throws AnalysisException
	{
		return node.getSuperdef().apply(this);
	}
	
	@Override
	public Boolean caseARenamedDefinition(ARenamedDefinition node)
			throws AnalysisException
	{
		return node.getDef().apply(this);
	}
	
	@Override
	public Boolean defaultPDefinition(PDefinition node)
			throws AnalysisException
	{
		return false;
	}
}