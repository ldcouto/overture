package org.overture.codegen.visitor;

import java.util.LinkedList;
import java.util.List;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.definitions.AExplicitFunctionDefinition;
import org.overture.ast.definitions.AExplicitOperationDefinition;
import org.overture.ast.definitions.AInstanceVariableDefinition;
import org.overture.ast.definitions.ATypeDefinition;
import org.overture.ast.definitions.AValueDefinition;
import org.overture.ast.patterns.PPattern;
import org.overture.ast.types.AFieldField;
import org.overture.ast.types.AFunctionType;
import org.overture.ast.types.AOperationType;
import org.overture.ast.types.ARecordInvariantType;
import org.overture.ast.types.PType;
import org.overture.codegen.assistant.DeclAssistant;
import org.overture.codegen.cgast.declarations.AClassDeclCG;
import org.overture.codegen.cgast.declarations.AFieldDeclCG;
import org.overture.codegen.cgast.declarations.AFormalParamLocalDeclCG;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;
import org.overture.codegen.cgast.declarations.PDeclCG;
import org.overture.codegen.cgast.expressions.PExpCG;
import org.overture.codegen.cgast.statements.PStmCG;
import org.overture.codegen.cgast.types.PTypeCG;
import org.overture.codegen.constants.OoAstConstants;

public class DeclVisitor extends AbstractVisitorCG<CodeGenInfo, PDeclCG>
{
	private static final long serialVersionUID = -7968170190668212627L;
	
	private DeclAssistant declAssistant;
	
	public DeclVisitor()
	{
		this.declAssistant = new DeclAssistant();
	}
	
	
	@Override
	public PDeclCG caseARecordInvariantType(ARecordInvariantType node,
			CodeGenInfo question) throws AnalysisException
	{
		String name = node.getName().getName();
		LinkedList<AFieldField> fields = node.getFields();
		
		AClassDeclCG staticClass = new AClassDeclCG();
		staticClass.setAbstract(false);
		staticClass.setStatic(true);
		//Set this public for now but it must be corrected as the access is specified
		//in the type definition instead:
		//		types
		//
		//		public R ::
		//		    x : nat
		//		    y : nat;
		staticClass.setAccess(OoAstConstants.PUBLIC);
		
		staticClass.setInnerClasses(null);
		staticClass.setMethods(null);
		staticClass.setName(name);
		staticClass.setSuperName(null);
		
		LinkedList<AFieldDeclCG> staticClassFields = staticClass.getFields();
		for (AFieldField aFieldField : fields)
		{		
			PDeclCG res = aFieldField.apply(question.getDeclVisitor(), question);
			
			if(res instanceof AFieldDeclCG)
				staticClassFields.add((AFieldDeclCG) res);
			else
				throw new AnalysisException("Could not generate fields of record: " + name);
		}
		
		return staticClass;
	}
	
	@Override
	public PDeclCG caseAFieldField(AFieldField node, CodeGenInfo question)
			throws AnalysisException
	{
		//Record fields are public
		String access = OoAstConstants.PUBLIC;
		String name = node.getTag();
		boolean isStatic = false;
		boolean isFinal = false;
		PTypeCG type = node.getType().apply(question.getTypeVisitor(), question);
		PExpCG exp = null;
		
		return declAssistant.constructField(access, name, isStatic, isFinal, type, exp);
	}
	
	@Override
	public PDeclCG caseATypeDefinition(ATypeDefinition node,
			CodeGenInfo question) throws AnalysisException
	{
//		System.out.println("*******");		
//		System.out.println("Access: " + node.getAccess());
//		System.out.println("Got a type definition: " + node.toString());
//		System.out.println("Invariant type: " + node.getInvType().toString());
//		System.out.println("Get type: " + node.getType());
//		System.out.println("Name: " + node.getName().getName());
//		System.out.println("Pattern: " + node.getInvPattern());
//		
//		System.out.println("Trying to apply type:");
//		System.out.println("The typ: " + typ.getClass().getName());	
//		System.out.println("*****");
		
		String access = node.getAccess().getAccess().toString();
		
		PDeclCG dec = node.getType().apply(question.getDeclVisitor(), question);
		
		if(dec instanceof AClassDeclCG)
		{
			AClassDeclCG staticClass = (AClassDeclCG) dec;
			staticClass.setAccess(access);
		}
		
		return dec;
	}
		
	@Override
	public PDeclCG caseAExplicitFunctionDefinition(
			AExplicitFunctionDefinition node, CodeGenInfo question)
			throws AnalysisException
	{
		if(node.getIsCurried() || node.getIsUndefined() || node.getIsTypeInvariant())
			return null;
		
		String access = node.getAccess().getAccess().toString();
		boolean isStatic = false;
		String operationName = node.getName().getName();

		PTypeCG returnType = ((AFunctionType) node.getType()).getResult().apply(question.getTypeVisitor(), question);
		PStmCG body = node.getBody().apply(question.getStatementVisitor(), question);
		boolean isAbstract = body == null;
		
		AMethodDeclCG method = new AMethodDeclCG();
		
		method.setAccess(access);
		method.setStatic(isStatic);
		method.setReturnType(returnType);
		method.setName(operationName);		
		method.setBody(body);
		method.setAbstract(isAbstract);
		
		List<PType> ptypes = ((AFunctionType) node.getType()).getParameters();
		List<PPattern> paramPatterns = node.getParamPatternList().get(0);
		
		LinkedList<AFormalParamLocalDeclCG> formalParameters = method.getFormalParams();
		
		for(int i = 0; i < paramPatterns.size(); i++)
		{
			PTypeCG type = ptypes.get(i).apply(question.getTypeVisitor(), question);
			String name = paramPatterns.get(i).toString();
			
			AFormalParamLocalDeclCG param = new AFormalParamLocalDeclCG();
			param.setType(type);
			param.setName(name);
			
			formalParameters.add(param);
		}
		
		return method;
	}
	
	@Override
	public PDeclCG caseAExplicitOperationDefinition(
			AExplicitOperationDefinition node, CodeGenInfo question)
			throws AnalysisException
	{	
		
		String access = node.getAccess().getAccess().toString();
		boolean isStatic = false;
		String operationName = node.getName().getName();
		PTypeCG returnType = node.getType().apply(question.getTypeVisitor(), question);		
		PStmCG body = node.getBody().apply(question.getStatementVisitor(), question);
		boolean isConstructor = node.getIsConstructor();
		boolean isAbstract = body == null;
		
		AMethodDeclCG method = new AMethodDeclCG();
		
		method.setAccess(access);
		method.setStatic(isStatic);
		method.setReturnType(returnType);
		method.setName(operationName);
		method.setBody(body);
		method.setIsConstructor(isConstructor);
		method.setAbstract(isAbstract);
		
		List<PType> ptypes = ((AOperationType) node.getType()).getParameters();
		LinkedList<PPattern> paramPatterns = node.getParameterPatterns();
		
		LinkedList<AFormalParamLocalDeclCG> formalParameters = method.getFormalParams();
		
		for(int i = 0; i < paramPatterns.size(); i++)
		{
			PTypeCG type = ptypes.get(i).apply(question.getTypeVisitor(), question);
			String name = paramPatterns.get(i).toString();
			
			AFormalParamLocalDeclCG param = new AFormalParamLocalDeclCG();
			param.setType(type);
			param.setName(name);
			
			formalParameters.add(param);
		}
		
		return method;
	}
	
	@Override
	public PDeclCG caseAInstanceVariableDefinition(
			AInstanceVariableDefinition node, CodeGenInfo question)
			throws AnalysisException
	{
		String access = node.getAccess().getAccess().toString();
		String name = node.getName().getName();
		boolean isStatic = node.getAccess().getStatic() != null;
		boolean isFinal = false;
		PTypeCG type = node.getType().apply(question.getTypeVisitor(), question);
		PExpCG exp = node.getExpression().apply(question.getExpVisitor(), question);
		
		
		return declAssistant.constructField(access, name, isStatic, isFinal, type, exp);
	}
	
	@Override
	public PDeclCG caseAValueDefinition(AValueDefinition node, CodeGenInfo question) throws AnalysisException
	{
		String access = node.getAccess().getAccess().toString();
		String name = node.getPattern().toString();
		boolean isStatic = true;
		boolean isFinal = true;
		PTypeCG type = node.getType().apply(question.getTypeVisitor(), question);
		PExpCG exp = node.getExpression().apply(question.getExpVisitor(), question);
		
		return declAssistant.constructField(access, name, isStatic, isFinal, type, exp);
	}
}
