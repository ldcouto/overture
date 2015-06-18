package org.overture.codegen.trans;

import org.overture.ast.definitions.AExplicitFunctionDefinition;
import org.overture.ast.node.INode;
import org.overture.codegen.cgast.SDeclCG;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;
import org.overture.codegen.cgast.declarations.AVarDeclCG;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.AIdentifierVarExpCG;
import org.overture.codegen.cgast.expressions.AStringLiteralExpCG;
import org.overture.codegen.cgast.statements.ABlockStmCG;
import org.overture.codegen.cgast.statements.AReturnStmCG;
import org.overture.codegen.ir.IRInfo;
import org.overture.codegen.ir.SourceNode;
import org.overture.codegen.logging.Logger;
import org.overture.codegen.trans.assistants.TransAssistantCG;

public class PostCheckTransformation extends DepthFirstAnalysisAdaptor
{
	private IPostCheckCreator postCheckCreator;
	private IRInfo info;
	private TransAssistantCG transformationAssistant;
	private String funcResultNamePrefix;
	private Object conditionalCallTag;

	public PostCheckTransformation(IPostCheckCreator postCheckCreator,
			IRInfo info, TransAssistantCG transformationAssistant,
			String funcResultNamePrefix, Object conditionalCallTag)
	{
		this.postCheckCreator = postCheckCreator;
		this.info = info;
		this.transformationAssistant = transformationAssistant;
		this.funcResultNamePrefix = funcResultNamePrefix;
		this.conditionalCallTag = conditionalCallTag;
	}

	@Override
	public void caseAMethodDeclCG(AMethodDeclCG node) throws AnalysisException
	{
		if (!info.getSettings().generatePostCondChecks())
		{
			return;
		}

		SDeclCG postCond = node.getPostCond();

		if (postCond == null)
		{
			return;
		}

		if (!(postCond instanceof AMethodDeclCG))
		{
			Logger.getLog().printErrorln("Expected post condition to be a method declaration at this point. Got: "
					+ postCond);
			return;
		}

		SourceNode sourceNode = postCond.getSourceNode();

		if (sourceNode == null)
		{
			Logger.getLog().printErrorln("Could not find source node for method declaration in the post check transformation");
			return;
		}

		INode vdmNode = sourceNode.getVdmNode();

		if (vdmNode == null)
		{
			Logger.getLog().printErrorln("Could not find VDM source node for method declaration in the post check transformation");
			return;
		}

		if (!(vdmNode instanceof AExplicitFunctionDefinition))
		{
			// Generation of post conditions is not supported for operations
			return;
		}

		node.getBody().apply(this);
	}

	@Override
	public void caseAReturnStmCG(AReturnStmCG node) throws AnalysisException
	{
		SExpCG result = node.getExp();

		if (result == null)
		{
			Logger.getLog().printErrorln("Expected a value to be returned in the post check transformation");
			return;
		}

		AMethodDeclCG method = node.getAncestor(AMethodDeclCG.class);

		if (method == null)
		{
			Logger.getLog().printError("Could not find enclosing method for a return statement in the post check transformation");
			return;
		}
		
		if(method.getStatic() == null || !method.getStatic())
		{
			// Generation of a post condition is only supported for static operations
			// where no 'self' and '~self' are being passed
			return;
		}

		SDeclCG postCond = method.getPostCond();
		
		if (!(postCond instanceof AMethodDeclCG))
		{
			Logger.getLog().printErrorln("Expected post condition to be a method declaration at this point. Got: "
					+ postCond);
			return;
		}
		
		AApplyExpCG postCondCall = transformationAssistant.consConditionalCall(method, (AMethodDeclCG) method.getPostCond());
		postCondCall.setTag(conditionalCallTag);

		String funcResultVarName = info.getTempVarNameGen().nextVarName(funcResultNamePrefix);
		AVarDeclCG resultDecl = transformationAssistant.consDecl(funcResultVarName, method.getMethodType().getResult().clone(), node.getExp().clone());
		AIdentifierVarExpCG resultVar = transformationAssistant.consIdentifierVar(funcResultVarName, resultDecl.getType().clone());

		postCondCall.getArgs().add(resultVar.clone());
		AStringLiteralExpCG methodName = info.getExpAssistant().consStringLiteral(method.getName(), false);
		AApplyExpCG postCheckCall = postCheckCreator.consPostCheckCall(method, postCondCall, resultVar, methodName);

		ABlockStmCG replacementBlock = new ABlockStmCG();
		replacementBlock.getLocalDefs().add(resultDecl);

		transformationAssistant.replaceNodeWith(node.getExp(), postCheckCall);
		transformationAssistant.replaceNodeWith(node, replacementBlock);

		replacementBlock.getStatements().add(node);
	}
}
