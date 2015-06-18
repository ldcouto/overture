package org.overture.codegen.trans;

import org.overture.codegen.cgast.SDeclCG;
import org.overture.codegen.cgast.SStmCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;
import org.overture.codegen.cgast.expressions.AApplyExpCG;
import org.overture.codegen.cgast.expressions.APreCondRuntimeErrorExpCG;
import org.overture.codegen.cgast.statements.ABlockStmCG;
import org.overture.codegen.cgast.statements.AIfStmCG;
import org.overture.codegen.cgast.statements.ARaiseErrorStmCG;
import org.overture.codegen.cgast.types.AErrorTypeCG;
import org.overture.codegen.ir.IRInfo;
import org.overture.codegen.logging.Logger;
import org.overture.codegen.trans.assistants.TransAssistantCG;

public class PreCheckTransformation extends DepthFirstAnalysisAdaptor {

	private IRInfo info;
	private TransAssistantCG transformationAssistant;
	private Object conditionalCallTag;
	
	public PreCheckTransformation(IRInfo info, TransAssistantCG transformationAssistant, Object conditionalCallTag)
	{
		this.info = info;
		this.transformationAssistant = transformationAssistant;
		this.conditionalCallTag = conditionalCallTag;
	}
	
	@Override
	public void caseAMethodDeclCG(AMethodDeclCG node) throws AnalysisException {
		
		if(!info.getSettings().generatePreCondChecks())
		{
			return;
		}
		
		SDeclCG preCond = node.getPreCond();
		
		if(preCond == null)
		{
			return;
		}
		
		if(!(preCond instanceof AMethodDeclCG))
		{
			Logger.getLog().printErrorln("Expected pre condition to be a method declaration at this point. Got: " + preCond);
			return;
		}

		AMethodDeclCG preCondMethod = (AMethodDeclCG) preCond;
		
		AApplyExpCG preCondCall = transformationAssistant.consConditionalCall(node, preCondMethod);
		
		if(preCondCall == null)
		{
			return;
		}
		
		preCondCall.setTag(conditionalCallTag);
		
		SStmCG body = node.getBody();
		
		APreCondRuntimeErrorExpCG runtimeError = new APreCondRuntimeErrorExpCG();
		runtimeError.setType(new AErrorTypeCG());
		runtimeError.setMessage(String.format("Precondition failure: pre_%s", node.getName()));
		
		ARaiseErrorStmCG raiseError= new ARaiseErrorStmCG();
		raiseError.setError(runtimeError);
		
		AIfStmCG ifCheck = new AIfStmCG();
		ifCheck.setIfExp(info.getExpAssistant().negate(preCondCall));
		ifCheck.setThenStm(raiseError);
		
		ABlockStmCG newBody = new ABlockStmCG();
		newBody.getStatements().add(ifCheck);
		newBody.getStatements().add(body.clone());
		
		transformationAssistant.replaceNodeWith(body, newBody);
	}
}
