package org.overture.codegen.traces;

import java.util.List;

import org.overture.codegen.cgast.SStmCG;
import org.overture.codegen.cgast.analysis.AnalysisException;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AClassDeclCG;
import org.overture.codegen.cgast.declarations.AFormalParamLocalParamCG;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;
import org.overture.codegen.cgast.declarations.ANamedTraceDeclCG;
import org.overture.codegen.cgast.expressions.AIdentifierVarExpCG;
import org.overture.codegen.cgast.expressions.ATypeArgExpCG;
import org.overture.codegen.cgast.statements.ABlockStmCG;
import org.overture.codegen.cgast.statements.APlainCallStmCG;
import org.overture.codegen.cgast.types.AClassTypeCG;
import org.overture.codegen.cgast.types.AExternalTypeCG;
import org.overture.codegen.cgast.types.AMethodTypeCG;
import org.overture.codegen.cgast.types.AVoidTypeCG;
import org.overture.codegen.ir.IRConstants;
import org.overture.codegen.ir.IRInfo;
import org.overture.codegen.logging.Logger;
import org.overture.codegen.trans.TempVarPrefixes;
import org.overture.codegen.trans.assistants.TransAssistantCG;
import org.overture.codegen.trans.iterator.ILanguageIterator;

public class TracesTransformation extends DepthFirstAnalysisAdaptor
{
	private IRInfo irInfo;
	private List<AClassDeclCG> classes;
	private TransAssistantCG transAssistant;
	private TempVarPrefixes tempVarPrefixes;
	private ILanguageIterator langIterator;
	private ICallStmToStringMethodBuilder toStringBuilder;
	private TraceNames tracePrefixes;

	public TracesTransformation(IRInfo irInfo, List<AClassDeclCG> classes,
			TransAssistantCG transAssistant, TempVarPrefixes tempVarPrefixes,
			TraceNames tracePrefixes, ILanguageIterator langIterator,
			ICallStmToStringMethodBuilder toStringBuilder)
	{
		this.irInfo = irInfo;
		this.classes = classes;
		this.transAssistant = transAssistant;
		this.tempVarPrefixes = tempVarPrefixes;
		this.langIterator = langIterator;
		this.toStringBuilder = toStringBuilder;

		this.tracePrefixes = tracePrefixes;
	}

	@Override
	public void caseANamedTraceDeclCG(ANamedTraceDeclCG node)
			throws AnalysisException
	{
		if(!irInfo.getSettings().generateTraces())
		{
			return;
		}
		
		TraceSupportedAnalysis supportedAnalysis = new TraceSupportedAnalysis(node);
		if (!traceIsSupported(supportedAnalysis))
		{
			irInfo.addTransformationWarning(node, supportedAnalysis.getReason());
			return;
		}

		AClassDeclCG enclosingClass = node.getAncestor(AClassDeclCG.class);

		if (enclosingClass != null)
		{
			enclosingClass.getMethods().add(consTraceMethod(node));
		} else
		{
			Logger.getLog().printErrorln("Class enclosing trace could not be found so the "
					+ "generated trace could not be added as a method to the corresponding class");
		}
	}

	private boolean traceIsSupported(TraceSupportedAnalysis supportedAnalysis)
	{
		
		try
		{
			supportedAnalysis.run();
		} catch (AnalysisException e)
		{
			Logger.getLog().printErrorln("Could not determine if a trace could be code generated");
			e.printStackTrace();
			return false;
		}

		return !supportedAnalysis.isUnsupported();
	}

	private AMethodDeclCG consTraceMethod(ANamedTraceDeclCG node)
			throws AnalysisException
	{
		AClassTypeCG testAccType = transAssistant.consClassType(tracePrefixes.testAccumulatorClassName());
		
		AMethodTypeCG methodType = new AMethodTypeCG();
		methodType.setResult(new AVoidTypeCG());
		methodType.getParams().add(testAccType);
		
		AFormalParamLocalParamCG instanceParam = new AFormalParamLocalParamCG();
		instanceParam.setType(testAccType.clone());
		instanceParam.setPattern(transAssistant.consIdPattern(tracePrefixes.traceMethodParamName()));

		AMethodDeclCG traceMethod = new AMethodDeclCG();
		
		traceMethod.getFormalParams().add(instanceParam);
		
		traceMethod.setAbstract(false);
		traceMethod.setAccess(IRConstants.PUBLIC);
		traceMethod.setBody(consTraceMethodBody(node));
		traceMethod.setIsConstructor(false);
		traceMethod.setStatic(false);
		traceMethod.setMethodType(methodType);
		traceMethod.setName(getTraceName(node) + "_"
				+ tracePrefixes.runTraceMethodName());

		return traceMethod;
	}

	private SStmCG buildTestExecutionStms(AIdentifierVarExpCG nodeVar,
			String traceEnclosingClassName)
	{
		AExternalTypeCG utilsType = new AExternalTypeCG();
		utilsType.setName(tracePrefixes.traceNodeNodeClassName());

		APlainCallStmCG executeTestsCall = new APlainCallStmCG();
		executeTestsCall.setClassType(utilsType);
		executeTestsCall.setName(tracePrefixes.executeTestsMethodName());
		executeTestsCall.setType(new AVoidTypeCG());

		ATypeArgExpCG typeArg = new ATypeArgExpCG();
		typeArg.setType(transAssistant.consClassType(traceEnclosingClassName));

		executeTestsCall.getArgs().add(nodeVar.clone());
		executeTestsCall.getArgs().add(typeArg);
		executeTestsCall.getArgs().add(transAssistant.consIdentifierVar(tracePrefixes.traceMethodParamName(),
				transAssistant.consClassType(tracePrefixes.testAccumulatorClassName())));
		executeTestsCall.getArgs().add(transAssistant.consIdentifierVar(tracePrefixes.storeVarName(),
				transAssistant.consClassType(tracePrefixes.storeClassName())));
		

		return executeTestsCall;
	}
	
	private SStmCG consTraceMethodBody(ANamedTraceDeclCG node)
			throws AnalysisException
	{
		String traceEnclosingClass = getTraceEnclosingClass(node);
		TraceStmsBuilder stmBuilder = new TraceStmsBuilder(irInfo, classes, transAssistant, 
				tempVarPrefixes, tracePrefixes, langIterator, toStringBuilder, traceEnclosingClass);

		TraceNodeData nodeData = stmBuilder.buildFromDeclTerms(node.getTerms());

		ABlockStmCG stms = new ABlockStmCG();
		stms.getLocalDefs().add(transAssistant.consClassVarDeclDefaultCtor(tracePrefixes.storeClassName(), tracePrefixes.storeVarName()));
		stms.getLocalDefs().add(transAssistant.consClassVarDeclDefaultCtor(tracePrefixes.idGeneratorClassName(), tracePrefixes.idGeneratorVarName()));
		stms.getStatements().add(nodeData.getStms());
		stms.getStatements().add(buildTestExecutionStms(nodeData.getNodeVar(), getClassName(node)));

		return stms;
	}
	
	private String getTraceEnclosingClass(ANamedTraceDeclCG trace)
	{
		if (trace != null)
		{
			AClassDeclCG enclosingClass = trace.getAncestor(AClassDeclCG.class);
			if (enclosingClass != null)
			{
				return enclosingClass.getName();
			}
		}

		Logger.getLog().printErrorln("Could not find class declaration enclosing the trace node "
				+ trace + " in TraceStmsBuilder");

		return null;
	}

	private String getTraceName(ANamedTraceDeclCG node)
	{
		String methodName = getClassName(node);

		for (int i = 0; i < node.getPathname().size(); i++)
		{
			methodName += "_" + node.getPathname().get(i).getName();
		}

		return methodName;
	}

	private String getClassName(ANamedTraceDeclCG node)
	{
		AClassDeclCG enclosingClass = node.getAncestor(AClassDeclCG.class);

		String traceClassName = "";
		if (enclosingClass != null)
		{
			traceClassName = enclosingClass.getName();
		} else
		{
			Logger.getLog().printErrorln("Could not find enclosing class for "
					+ node);
			traceClassName = "Unknown";
		}

		return traceClassName;
	}
}
