package org.overture.pog.strategies;

import java.util.LinkedList;
import java.util.List;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.analysis.QuestionAnswerAdaptor;
import org.overture.ast.expressions.AAndBooleanBinaryExp;
import org.overture.ast.expressions.AExistsExp;
import org.overture.ast.expressions.AForAllExp;
import org.overture.ast.expressions.AImpliesBooleanBinaryExp;
import org.overture.ast.expressions.AOrBooleanBinaryExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstFactory;
import org.overture.ast.types.AUnionType;
import org.overture.ast.types.PType;
import org.overture.pog.obligation.LpfAndPo;
import org.overture.pog.obligation.LpfOrPo;
import org.overture.pog.obligation.ProofObligationList;
import org.overture.pog.obligation.SubTypeObligation;
import org.overture.pog.pub.IPOContextStack;
import org.overture.pog.pub.IPogAssistantFactory;
import org.overture.pog.pub.IProofObligation;
import org.overture.pog.pub.IProofObligationList;

public class LpfStrategy implements IPogStrategy
{

	@Override
	public IProofObligationList executeAnd(
			AAndBooleanBinaryExp node,
			IPOContextStack question,
			QuestionAnswerAdaptor<IPOContextStack, IProofObligationList> visitor,
			IPogAssistantFactory assistantFactory) throws AnalysisException
	{
		IProofObligationList obligations = new ProofObligationList();

		PExp lExp = node.getLeft();
		PType lType = lExp.getType();
		PExp rExp = node.getRight();
		PType rType = rExp.getType();

		if (assistantFactory.createPTypeAssistant().isUnion(lType))
		{
			SubTypeObligation stol = SubTypeObligation.newInstance(lExp, AstFactory.newABooleanBasicType(lExp.getLocation()), lType, question, assistantFactory);
			if (stol != null)
			{
				obligations.add(stol);
			}
		}
		if (assistantFactory.createPTypeAssistant().isUnion(rType))
		{
			SubTypeObligation stor = SubTypeObligation.newInstance(rExp, AstFactory.newABooleanBasicType(rExp.getLocation()), rType, question, assistantFactory);
			if (stor != null)
			{
				obligations.add(stor);
			}
		}

		IProofObligationList lobligations = new ProofObligationList();
		IProofObligationList robligations = new ProofObligationList();

		lobligations.addAll(lExp.apply(visitor, question));
		robligations.addAll(rExp.apply(visitor, question));

		if (!lobligations.isEmpty() || !robligations.isEmpty())
		{
			List<PExp> definedPredicates = new LinkedList<PExp>();
			for (IProofObligation po : lobligations)
			{
				definedPredicates.add(po.getStitch());
			}
			for (IProofObligation po : robligations)
			{
				definedPredicates.add(po.getStitch());
			}
			obligations.add(new LpfAndPo(node, lExp, rExp, definedPredicates, question));
		}

		return obligations;
	}

	@Override
	public IProofObligationList executeOr(
			AOrBooleanBinaryExp node,
			IPOContextStack question,
			QuestionAnswerAdaptor<IPOContextStack, IProofObligationList> visitor,
			IPogAssistantFactory assistantFactory) throws AnalysisException
	{
		IProofObligationList obligations = new ProofObligationList();

		PExp lExp = node.getLeft();
		PExp rExp = node.getRight();
		PType lType = lExp.getType();
		PType rType = rExp.getType();

		if (lType instanceof AUnionType)
		{
			SubTypeObligation sto = SubTypeObligation.newInstance(lExp, AstFactory.newABooleanBasicType(lExp.getLocation()), lType, question, assistantFactory);
			if (sto != null)
			{
				obligations.add(sto);
			}
		}

		if (rType instanceof AUnionType)
		{
			SubTypeObligation sto = SubTypeObligation.newInstance(rExp, AstFactory.newABooleanBasicType(rExp.getLocation()), rType, question, assistantFactory);
			if (sto != null)
			{
				obligations.add(sto);
			}
		}

		IProofObligationList lobligations = new ProofObligationList();
		IProofObligationList robligations = new ProofObligationList();

		lobligations.addAll(lExp.apply(visitor, question));
		robligations.addAll(rExp.apply(visitor, question));

		if (!lobligations.isEmpty() || !robligations.isEmpty())
		{
			List<PExp> definedPredicates = new LinkedList<PExp>();
			for (IProofObligation po : lobligations)
			{
				definedPredicates.add(po.getStitch());
			}
			for (IProofObligation po : robligations)
			{
				definedPredicates.add(po.getStitch());
			}
			obligations.add(new LpfOrPo(node, lExp, rExp, definedPredicates, question));
		}

		return obligations;

	}

	@Override
	public IProofObligationList executeImplies(
			AImpliesBooleanBinaryExp node,
			IPOContextStack question,
			QuestionAnswerAdaptor<IPOContextStack, IProofObligationList> visitor,
			IPogAssistantFactory assistantFactory) throws AnalysisException
	{
		ProofObligationList obligations = new ProofObligationList();

		PExp lExp = node.getLeft();
		PType lType = lExp.getType();
		PExp rExp = node.getRight();
		PType rType = rExp.getType();
	

		if (assistantFactory.createPTypeAssistant().isUnion(lType))
		{
			obligations.add(SubTypeObligation.newInstance(lExp, AstFactory.newABooleanBasicType(lExp.getLocation()), lType, question, assistantFactory));
		}

		if (assistantFactory.createPTypeAssistant().isUnion(rType))
		{
			obligations.add(SubTypeObligation.newInstance(rExp, AstFactory.newABooleanBasicType(rExp.getLocation()), rType, question, assistantFactory));
		}

		IProofObligationList lobligations = new ProofObligationList();
		IProofObligationList robligations = new ProofObligationList();

		lobligations.addAll(lExp.apply(visitor, question));
		robligations.addAll(rExp.apply(visitor, question));

		if (!lobligations.isEmpty() || !robligations.isEmpty())
		{
			List<PExp> definedPredicates = new LinkedList<PExp>();
			for (IProofObligation po : lobligations)
			{
				definedPredicates.add(po.getStitch());
			}
			for (IProofObligation po : robligations)
			{
				definedPredicates.add(po.getStitch());
			}
			obligations.add(new LpfImpPo(node, lExp, rExp, definedPredicates, question));
		}

		return obligations;
	}

	@Override
	public IProofObligationList executeForall(
			AForAllExp node,
			IPOContextStack question,
			QuestionAnswerAdaptor<IPOContextStack, IProofObligationList> visitor,
			IPogAssistantFactory assistantFactory) throws AnalysisException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IProofObligationList executeExists(
			AExistsExp node,
			IPOContextStack question,
			QuestionAnswerAdaptor<IPOContextStack, IProofObligationList> visitor,
			IPogAssistantFactory assistantFactory) throws AnalysisException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
