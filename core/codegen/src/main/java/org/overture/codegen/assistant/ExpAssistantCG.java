package org.overture.codegen.assistant;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.expressions.ARealLiteralExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.expressions.SBinaryExp;
import org.overture.ast.types.AIntNumericBasicType;
import org.overture.ast.types.ANatNumericBasicType;
import org.overture.ast.types.ANatOneNumericBasicType;
import org.overture.ast.types.PType;
import org.overture.codegen.cgast.expressions.AIsolationUnaryExpCG;
import org.overture.codegen.cgast.expressions.PExpCG;
import org.overture.codegen.cgast.expressions.SBinaryExpCG;
import org.overture.codegen.lookup.OperatorInfo;
import org.overture.codegen.lookup.OperatorLookup;
import org.overture.codegen.lookup.TypeLookup;
import org.overture.codegen.visitor.OoAstInfo;
import org.overture.codegen.visitor.ExpVisitorCG;


public class ExpAssistantCG
{	
	private OperatorLookup opLookup;
	
	public ExpAssistantCG(ExpVisitorCG expVisitor)
	{
		this.opLookup = new OperatorLookup();
	}
	
	public PExpCG handleBinaryExp(SBinaryExp vdmExp, SBinaryExpCG codeGenExp, OoAstInfo question, TypeLookup typeLookup) throws AnalysisException
	{	
		codeGenExp.setType(typeLookup.getType(vdmExp.getType()));
		
		//Set the expressions
		codeGenExp.setLeft(formatExp(vdmExp, vdmExp.getLeft(), question));
		codeGenExp.setRight(formatExp(vdmExp, vdmExp.getRight(), question));
		
		//Set the expression types:
		PType leftVdmType = vdmExp.getLeft().getType();
		codeGenExp.getLeft().setType(typeLookup.getType(leftVdmType));
		PType rightVdmType = vdmExp.getRight().getType();
		codeGenExp.getRight().setType(typeLookup.getType(rightVdmType));
		
		return codeGenExp;
	}
	
	public PExpCG formatExp(SBinaryExp parent, PExp child, OoAstInfo question) throws AnalysisException
	{
		
		PExpCG exp = child.apply(question.getExpVisitor(), question);
		
		boolean wrap = childExpHasLowerPrecedence(parent, child);

		if(wrap)
		{
			AIsolationUnaryExpCG isolatioNExp = new AIsolationUnaryExpCG();
			isolatioNExp.setType(exp.getType());
			isolatioNExp.setExp(exp);
			exp = isolatioNExp;
		}
		
		return exp;
	}
	
	public boolean isIntegerType(PExp exp)
	{	
		PType type = exp.getType();
		
		return (type instanceof ANatOneNumericBasicType 
				|| type instanceof ANatNumericBasicType
				|| type instanceof AIntNumericBasicType) 
				&& !(exp instanceof ARealLiteralExp);
		//Expressions like 1.0 are considered real literal expressions
		//of type NatOneNumericBasicType
	}
	
	
	public boolean childExpHasLowerPrecedence(SBinaryExp parent, PExp child)
	{				
		if(!(child instanceof SBinaryExp))
			return false;

		OperatorInfo parentOpInfo = opLookup.find(parent.getClass());
		
		SBinaryExp binExpChild = (SBinaryExp) child;
		OperatorInfo childInfo = opLookup.find(binExpChild.getClass());
		
		return childInfo.getPrecedence() < parentOpInfo.getPrecedence();
	}
}