/*
 * #%~
 * VDM Code Generator
 * %%
 * Copyright (C) 2008 - 2014 Overture
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #~%
 */
package org.overture.codegen.vdm2java;

import java.io.File;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.velocity.app.Velocity;
import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.definitions.PDefinition;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.ast.definitions.SFunctionDefinition;
import org.overture.ast.definitions.SOperationDefinition;
import org.overture.ast.expressions.ANotYetSpecifiedExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.modules.AModuleModules;
import org.overture.ast.node.INode;
import org.overture.ast.statements.AIdentifierStateDesignator;
import org.overture.ast.statements.ANotYetSpecifiedStm;
import org.overture.ast.util.modules.CombinedDefaultModule;
import org.overture.codegen.analysis.vdm.IdStateDesignatorDefCollector;
import org.overture.codegen.analysis.vdm.NameCollector;
import org.overture.codegen.analysis.vdm.Renaming;
import org.overture.codegen.analysis.vdm.UnreachableStmRemover;
import org.overture.codegen.analysis.vdm.VarRenamer;
import org.overture.codegen.analysis.vdm.VarShadowingRenameCollector;
import org.overture.codegen.analysis.violations.GeneratedVarComparison;
import org.overture.codegen.analysis.violations.InvalidNamesResult;
import org.overture.codegen.analysis.violations.ReservedWordsComparison;
import org.overture.codegen.analysis.violations.TypenameComparison;
import org.overture.codegen.analysis.violations.UnsupportedModelingException;
import org.overture.codegen.analysis.violations.VdmAstAnalysis;
import org.overture.codegen.analysis.violations.Violation;
import org.overture.codegen.assistant.AssistantManager;
import org.overture.codegen.assistant.DeclAssistantCG;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.analysis.DepthFirstAnalysisAdaptor;
import org.overture.codegen.cgast.declarations.AClassDeclCG;
import org.overture.codegen.cgast.declarations.AInterfaceDeclCG;
import org.overture.codegen.cgast.declarations.AModuleDeclCG;
import org.overture.codegen.ir.CodeGenBase;
import org.overture.codegen.ir.IRConstants;
import org.overture.codegen.ir.IREventCoordinator;
import org.overture.codegen.ir.IREventObserver;
import org.overture.codegen.ir.IRStatus;
import org.overture.codegen.ir.IrNodeInfo;
import org.overture.codegen.ir.VdmNodeInfo;
import org.overture.codegen.logging.ILogger;
import org.overture.codegen.logging.Logger;
import org.overture.codegen.merging.MergeVisitor;
import org.overture.codegen.merging.TemplateStructure;
import org.overture.codegen.trans.ModuleToClassTransformation;
import org.overture.codegen.trans.OldNameRenamer;
import org.overture.codegen.trans.assistants.TransAssistantCG;
import org.overture.codegen.trans.funcvalues.FunctionValueAssistant;
import org.overture.codegen.utils.GeneralCodeGenUtils;
import org.overture.codegen.utils.GeneralUtils;
import org.overture.codegen.utils.Generated;
import org.overture.codegen.utils.GeneratedData;
import org.overture.codegen.utils.GeneratedModule;
import org.overture.config.Settings;

public class JavaCodeGen extends CodeGenBase implements IREventCoordinator
{
	public static final String JAVA_TEMPLATES_ROOT_FOLDER = "JavaTemplates";

	public static final String[] JAVA_RESERVED_TYPE_NAMES = {
			// Classes used from the Java standard library
			"Utils", "Record", "Long", "Double", "Character", "String", "List",
			"Set" };

	public static final String JAVA_QUOTE_NAME_SUFFIX = "Quote";
	public static final String JAVA_MAIN_CLASS_NAME = "Main";

	private JavaFormat javaFormat;
	private TemplateStructure javaTemplateStructure;
	
	private IREventObserver irObserver;

	public JavaCodeGen()
	{
		super(null);
		init();
	}

	public JavaCodeGen(ILogger log)
	{
		super(log);
		init();
	}

	private void init()
	{
		this.irObserver = null;
		initVelocity();

		this.javaTemplateStructure = new TemplateStructure(JAVA_TEMPLATES_ROOT_FOLDER);
		this.transAssistant = new TransAssistantCG(generator.getIRInfo(), varPrefixes);
		this.javaFormat = new JavaFormat(varPrefixes, javaTemplateStructure, generator.getIRInfo());
	}
	
	public void setJavaTemplateStructure(TemplateStructure javaTemplateStructure)
	{
		this.javaTemplateStructure = javaTemplateStructure;
	}

	public TemplateStructure getJavaTemplateStructure()
	{
		return javaTemplateStructure;
	}

	public void setJavaSettings(JavaSettings javaSettings)
	{
		this.javaFormat.setJavaSettings(javaSettings);
	}

	public JavaSettings getJavaSettings()
	{
		return this.javaFormat.getJavaSettings();
	}

	public void clear()
	{
		javaFormat.init();
		generator.clear();
	}

	private void initVelocity()
	{
		Velocity.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
		Velocity.init();
	}

	public JavaFormat getJavaFormat()
	{
		return javaFormat;
	}

	public List<GeneratedModule> generateJavaFromVdmQuotes()
	{
		try
		{
			List<String> quoteValues = generator.getQuoteValues();

			if (quoteValues.isEmpty())
			{
				return null; // Nothing to generate
			}

			javaFormat.init();

			JavaQuoteValueCreator quoteValueCreator = new JavaQuoteValueCreator(generator.getIRInfo(), transAssistant);

			List<GeneratedModule> modules = new LinkedList<GeneratedModule>();
			for (String quoteNameVdm : quoteValues)
			{
				AClassDeclCG quoteDecl = quoteValueCreator.consQuoteValue(quoteNameVdm
						+ JAVA_QUOTE_NAME_SUFFIX, quoteNameVdm, getJavaSettings().getJavaRootPackage());

				StringWriter writer = new StringWriter();
				quoteDecl.apply(javaFormat.getMergeVisitor(), writer);
				String code = writer.toString();

				if(getJavaSettings().formatCode())
				{
					code = JavaCodeGenUtil.formatJavaCode(code);
				}

				modules.add(new GeneratedModule(quoteNameVdm, quoteDecl, code));
			}

			return modules;

		} catch (org.overture.codegen.cgast.analysis.AnalysisException e)
		{
			Logger.getLog().printErrorln("Error when formatting quotes: "
					+ e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	public GeneratedData generateJavaFromVdmModules(List<AModuleModules> ast)
			throws AnalysisException, UnsupportedModelingException {

		return generateVdmFromNodes(ast, null, new LinkedList<String>());
	}
	
	public GeneratedData generateJavaFromVdm(List<SClassDefinition> ast)
			throws AnalysisException, UnsupportedModelingException
	{
		SClassDefinition mainClass = null;

		List<String> warnings = new LinkedList<String>();
		if (getJavaSettings().getVdmEntryExp() != null)
		{
			try
			{
				mainClass = GeneralCodeGenUtils.consMainClass(ast, getJavaSettings().getVdmEntryExp(), Settings.dialect, JAVA_MAIN_CLASS_NAME, getInfo().getTempVarNameGen());
				ast.add(mainClass);
			} catch (Exception e)
			{
				// It can go wrong if the VDM entry point does not type check
				warnings.add("The chosen launch configuration could not be type checked: "
						+ e.getMessage());
				warnings.add("Skipping launch configuration..");
			}
		}

		return generateVdmFromNodes(ast, mainClass, warnings);
	}

	private GeneratedData generateVdmFromNodes(List<? extends INode> ast,
			SClassDefinition mainClass, List<String> warnings)
			throws AnalysisException, UnsupportedModelingException {
		
		List<INode> userModules = getUserModules(ast);
		
		handleOldNames(ast);

		List<Renaming> allRenamings = normaliseIdentifiers(userModules);
		computeDefTable(userModules);

		// To document any renaming of variables shadowing other variables
		removeUnreachableStms(ast);

		allRenamings.addAll(performRenaming(userModules, getInfo().getIdStateDesignatorDefs()));

		for (INode node : ast)
		{
			if (generator.getIRInfo().getAssistantManager().getDeclAssistant().isLibrary(node))
			{
				simplifyLibrary(node);
			}
		}

		InvalidNamesResult invalidNamesResult = validateVdmModelNames(userModules);
		validateVdmModelingConstructs(userModules);

		List<IRStatus<org.overture.codegen.cgast.INode>> statuses = new LinkedList<>();

		for (INode node : ast)
		{
			IRStatus<org.overture.codegen.cgast.INode> status = generator.generateFrom(node);
			
			if(status != null)
			{
				statuses.add(status);
			}
		}

		List<GeneratedModule> generated = new LinkedList<GeneratedModule>();
		
		// Event notification
		statuses = initialIrEvent(statuses);
		statuses = filter(statuses, generated);
		
		List<IRStatus<AModuleDeclCG>> moduleStatuses = IRStatus.extract(statuses, AModuleDeclCG.class);
		List<IRStatus<org.overture.codegen.cgast.INode>> modulesAsNodes = IRStatus.extract(moduleStatuses);
			
		ModuleToClassTransformation moduleTransformation = new ModuleToClassTransformation(getInfo(),
				transAssistant, getModuleDecls(moduleStatuses));
		
		for(IRStatus<org.overture.codegen.cgast.INode> moduleStatus : modulesAsNodes)
		{
			try
			{
				generator.applyTotalTransformation(moduleStatus, moduleTransformation);

			} catch (org.overture.codegen.cgast.analysis.AnalysisException e)
			{
				Logger.getLog().printErrorln("Error when generating code for module "
						+ moduleStatus.getIrNodeName() + ": " + e.getMessage());
				Logger.getLog().printErrorln("Skipping module..");
				e.printStackTrace();
			}
			
		}

		List<IRStatus<AClassDeclCG>> classStatuses = IRStatus.extract(modulesAsNodes, AClassDeclCG.class);
		classStatuses.addAll(IRStatus.extract(statuses, AClassDeclCG.class));
		
		if (getJavaSettings().getJavaRootPackage() != null)
		{
			for (IRStatus<AClassDeclCG> irStatus : classStatuses)
			{
				irStatus.getIrNode().setPackage(getJavaSettings().getJavaRootPackage());
			}
		}

		List<AClassDeclCG> classes = getClassDecls(classStatuses);
		javaFormat.setClasses(classes);

		List<IRStatus<AClassDeclCG>> canBeGenerated = new LinkedList<IRStatus<AClassDeclCG>>();

		for (IRStatus<AClassDeclCG> status : classStatuses)
		{
			if (status.canBeGenerated())
			{
				canBeGenerated.add(status);
			} else
			{
				generated.add(new GeneratedModule(status.getIrNodeName(), status.getUnsupportedInIr(), new HashSet<IrNodeInfo>()));
			}
		}

		FunctionValueAssistant functionValueAssistant = new FunctionValueAssistant();

		JavaTransSeries javaTransSeries = new JavaTransSeries(this);
		DepthFirstAnalysisAdaptor[] analyses = javaTransSeries.consAnalyses(classes, functionValueAssistant);

		for (DepthFirstAnalysisAdaptor transformation : analyses)
		{
			for (IRStatus<AClassDeclCG> status : canBeGenerated)
			{
				try
				{
					if (!getInfo().getDeclAssistant().isLibraryName(status.getIrNodeName()))
					{
						generator.applyPartialTransformation(status, transformation);
					}

				} catch (org.overture.codegen.cgast.analysis.AnalysisException e)
				{
					Logger.getLog().printErrorln("Error when generating code for class "
							+ status.getIrNodeName() + ": " + e.getMessage());
					Logger.getLog().printErrorln("Skipping class..");
					e.printStackTrace();
				}
			}
		}
		
		// Event notification
		canBeGenerated = IRStatus.extract(finalIrEvent(IRStatus.extract(canBeGenerated)), AClassDeclCG.class);
		canBeGenerated = filter(canBeGenerated, generated);
		
		List<String> skipping = new LinkedList<String>();

		MergeVisitor mergeVisitor = javaFormat.getMergeVisitor();
		javaFormat.setFunctionValueAssistant(functionValueAssistant);

		for (IRStatus<AClassDeclCG> status : canBeGenerated)
		{
			StringWriter writer = new StringWriter();
			AClassDeclCG classCg = status.getIrNode();
			String className = status.getIrNodeName();
			INode vdmClass = status.getIrNode().getSourceNode().getVdmNode();

			if (vdmClass == mainClass)
			{
				classCg.setTag(new JavaMainTag(classCg));
			}

			javaFormat.init();

			try
			{
				if (shouldBeGenerated(vdmClass, generator.getIRInfo().getAssistantManager().getDeclAssistant()))
				{
					classCg.apply(mergeVisitor, writer);

					if (mergeVisitor.hasMergeErrors())
					{
						generated.add(new GeneratedModule(className, classCg, mergeVisitor.getMergeErrors()));
					} else if (mergeVisitor.hasUnsupportedTargLangNodes())
					{
						generated.add(new GeneratedModule(className, new HashSet<VdmNodeInfo>(), mergeVisitor.getUnsupportedInTargLang()));
					} else
					{
						String code = writer.toString();
						
						if(getJavaSettings().formatCode())
						{
							code = JavaCodeGenUtil.formatJavaCode(code); 
						}
						
						GeneratedModule generatedModule = new GeneratedModule(className, classCg, code);
						generatedModule.setTransformationWarnings(status.getTransformationWarnings());
						generated.add(generatedModule);
					}
				} else
				{
					if (!skipping.contains(className))
					{
						skipping.add(className);
					}
				}

			} catch (org.overture.codegen.cgast.analysis.AnalysisException e)
			{
				Logger.getLog().printErrorln("Error generating code for class "
						+ status.getIrNodeName() + ": " + e.getMessage());
				Logger.getLog().printErrorln("Skipping class..");
				e.printStackTrace();
			}
		}

		List<AInterfaceDeclCG> funcValueInterfaces = functionValueAssistant.getFunctionValueInterfaces();

		for (AInterfaceDeclCG funcValueInterface : funcValueInterfaces)
		{
			funcValueInterface.setPackage(getJavaSettings().getJavaRootPackage());
			
			StringWriter writer = new StringWriter();

			try
			{
				funcValueInterface.apply(mergeVisitor, writer);
				String formattedJavaCode = JavaCodeGenUtil.formatJavaCode(writer.toString());
				generated.add(new GeneratedModule(funcValueInterface.getName(), funcValueInterface, formattedJavaCode));

			} catch (org.overture.codegen.cgast.analysis.AnalysisException e)
			{
				Logger.getLog().printErrorln("Error generating code for function value interface "
						+ funcValueInterface.getName() + ": " + e.getMessage());
				Logger.getLog().printErrorln("Skipping interface..");
				e.printStackTrace();
			}
		}

		javaFormat.clearFunctionValueAssistant();
		javaFormat.clearClasses();

		GeneratedData data = new GeneratedData();
		data.setClasses(generated);
		data.setQuoteValues(generateJavaFromVdmQuotes());
		data.setInvalidNamesResult(invalidNamesResult);
		data.setSkippedClasses(skipping);
		data.setAllRenamings(allRenamings);
		data.setWarnings(warnings);

		return data;
	}

	private <T extends org.overture.codegen.cgast.INode> List<IRStatus<T>> filter(
			List<IRStatus<T>> statuses, List<GeneratedModule> generated)
	{
		List<IRStatus<T>> filtered = new LinkedList<IRStatus<T>>();
		
		for(IRStatus<T> status : statuses)
		{
			if(status.canBeGenerated())
			{
				filtered.add(status);
			}
			else
			{
				generated.add(new GeneratedModule(status.getIrNodeName(), status.getUnsupportedInIr(), new HashSet<IrNodeInfo>()));
			}
		}
		
		return filtered;
	}

	private void handleOldNames(List<? extends INode> ast) throws AnalysisException
	{
		OldNameRenamer oldNameRenamer = new OldNameRenamer();
		
		for(INode module : ast)
		{
			module.apply(oldNameRenamer);
		}
	}

	private List<INode> getUserModules(
			List<? extends INode> mergedParseLists)
	{
		List<INode> userModules = new LinkedList<INode>();
		
		if(mergedParseLists.size() == 1 && mergedParseLists.get(0) instanceof CombinedDefaultModule)
		{
			CombinedDefaultModule combined = (CombinedDefaultModule) mergedParseLists.get(0);
			
			for(AModuleModules m : combined.getModules())
			{
				userModules.add(m);
			}
			
			return userModules;
		}
		else
		{
			for (INode node : mergedParseLists)
			{
				if(!getInfo().getDeclAssistant().isLibrary(node))
				{
					userModules.add(node);
				}
			}
			
			return userModules;
		}
	}

	private List<Renaming> normaliseIdentifiers(
			List<INode> userModules) throws AnalysisException
	{
		NameCollector collector = new NameCollector();

		for (INode node : userModules)
		{
			node.apply(collector);
		}

		Set<String> allNames = collector.namesToAvoid();

		JavaIdentifierNormaliser normaliser = new JavaIdentifierNormaliser(allNames, getInfo().getTempVarNameGen());
		
		for (INode node : userModules)
		{
			node.apply(normaliser);
		}

		VarRenamer renamer = new VarRenamer();
		
		Set<Renaming> filteredRenamings = new HashSet<Renaming>();
		
		for(Renaming r : normaliser.getRenamings())
		{
			if(!getInfo().getDeclAssistant().isLibraryName(r.getLoc().getModule()))
			{
				filteredRenamings.add(r);
			}
		}
		
		for (INode node : userModules)
		{
			renamer.rename(node, filteredRenamings);
		}

		return new LinkedList<Renaming>(filteredRenamings);
	}

	private void computeDefTable(List<INode> mergedParseLists)
			throws AnalysisException
	{
		List<INode> classesToConsider = new LinkedList<>();

		for (INode node : mergedParseLists)
		{
			if (!getInfo().getDeclAssistant().isLibrary(node))
			{
				classesToConsider.add(node);
			}
		}
		
		Map<AIdentifierStateDesignator, PDefinition> idDefs = IdStateDesignatorDefCollector.getIdDefs(classesToConsider, getInfo().getTcFactory());
		getInfo().setIdStateDesignatorDefs(idDefs);
	}

	private void removeUnreachableStms(List<? extends INode> mergedParseLists)
			throws AnalysisException
	{
		UnreachableStmRemover remover = new UnreachableStmRemover();

		for (INode node : mergedParseLists)
		{
			node.apply(remover);
		}
	}

	private List<Renaming> performRenaming(
			List<INode> mergedParseLists,
			Map<AIdentifierStateDesignator, PDefinition> idDefs)
			throws AnalysisException
	{
		List<Renaming> allRenamings = new LinkedList<Renaming>();

		VarShadowingRenameCollector renamingsCollector = new VarShadowingRenameCollector(generator.getIRInfo().getTcFactory(), idDefs);
		VarRenamer renamer = new VarRenamer();

		for (INode node : mergedParseLists)
		{
			Set<Renaming> currentRenamings = renamer.computeRenamings(node, renamingsCollector);

			if (!currentRenamings.isEmpty())
			{
				renamer.rename(node, currentRenamings);
				allRenamings.addAll(currentRenamings);
			}
		}

		Collections.sort(allRenamings);
		
		return allRenamings;
	}

	private void simplifyLibrary(INode node)
	{
		List<PDefinition> defs = null;
		
		if(node instanceof SClassDefinition)
		{
			defs = ((SClassDefinition) node).getDefinitions();
		}
		else if(node instanceof AModuleModules)
		{
			defs = ((AModuleModules) node).getDefs();
		}
		else
		{
			// Nothing to do
			return;
		}
		
		for (PDefinition def : defs)
		{
			if (def instanceof SOperationDefinition)
			{
				SOperationDefinition op = (SOperationDefinition) def;

				op.setBody(new ANotYetSpecifiedStm());
				op.setPrecondition(null);
				op.setPostcondition(null);
			} else if (def instanceof SFunctionDefinition)
			{
				SFunctionDefinition func = (SFunctionDefinition) def;

				func.setBody(new ANotYetSpecifiedExp());
				func.setPrecondition(null);
				func.setPostcondition(null);
			}

		}
	}

	private List<AClassDeclCG> getClassDecls(
			List<IRStatus<AClassDeclCG>> statuses)
	{
		List<AClassDeclCG> classDecls = new LinkedList<AClassDeclCG>();

		for (IRStatus<AClassDeclCG> status : statuses)
		{
			classDecls.add(status.getIrNode());
		}

		return classDecls;
	}
	
	private List<AModuleDeclCG> getModuleDecls(List<IRStatus<AModuleDeclCG>> statuses)
	{
		List<AModuleDeclCG> modules = new LinkedList<AModuleDeclCG>();
		
		for(IRStatus<AModuleDeclCG> status : statuses)
		{
			modules.add(status.getIrNode());
		}
		
		return modules;
	}

	public Generated generateJavaFromVdmExp(PExp exp) throws AnalysisException
	{
		// There is no name validation here.
		IRStatus<SExpCG> expStatus = generator.generateFrom(exp);

		StringWriter writer = new StringWriter();

		try
		{
			SExpCG expCg = expStatus.getIrNode();

			if (expStatus.canBeGenerated())
			{
				javaFormat.init();
				MergeVisitor mergeVisitor = javaFormat.getMergeVisitor();
				expCg.apply(mergeVisitor, writer);

				if (mergeVisitor.hasMergeErrors())
				{
					return new Generated(mergeVisitor.getMergeErrors());
				} else if (mergeVisitor.hasUnsupportedTargLangNodes())
				{
					return new Generated(new HashSet<VdmNodeInfo>(), mergeVisitor.getUnsupportedInTargLang());
				} else
				{
					String code = writer.toString();

					return new Generated(code);
				}
			} else
			{

				return new Generated(expStatus.getUnsupportedInIr(), new HashSet<IrNodeInfo>());
			}

		} catch (org.overture.codegen.cgast.analysis.AnalysisException e)
		{
			Logger.getLog().printErrorln("Could not generate expression: "
					+ exp);
			e.printStackTrace();
			return null;
		}
	}
	
	public void genJavaSourceFiles(File root,
			List<GeneratedModule> generatedClasses)
	{
		for (GeneratedModule classCg : generatedClasses)
		{
			genJavaSourceFile(root, classCg);
		}
	}

	public void genJavaSourceFile(File root, GeneratedModule generatedModule)
	{
		File moduleOutputDir = JavaCodeGenUtil.getModuleOutputDir(root, this, generatedModule);
		
		if(moduleOutputDir == null)
		{
			return;
		}
		
		if (generatedModule != null && generatedModule.canBeGenerated()
				&& !generatedModule.hasMergeErrors())
		{
			String javaFileName = generatedModule.getName();

			if (JavaCodeGenUtil.isQuote(generatedModule.getIrNode(), getJavaSettings()))
			{
				javaFileName += JAVA_QUOTE_NAME_SUFFIX;
			}

			javaFileName += IJavaCodeGenConstants.JAVA_FILE_EXTENSION;

			JavaCodeGenUtil.saveJavaClass(moduleOutputDir, javaFileName, generatedModule.getContent());
		}
	}

	private InvalidNamesResult validateVdmModelNames(
			List<INode> mergedParseLists) throws AnalysisException
	{
		AssistantManager assistantManager = generator.getIRInfo().getAssistantManager();
		VdmAstAnalysis analysis = new VdmAstAnalysis(assistantManager);

		Set<Violation> reservedWordViolations = analysis.usesIllegalNames(mergedParseLists, new ReservedWordsComparison(IJavaCodeGenConstants.RESERVED_WORDS, generator.getIRInfo(), INVALID_NAME_PREFIX));
		Set<Violation> typenameViolations = analysis.usesIllegalNames(mergedParseLists, new TypenameComparison(JAVA_RESERVED_TYPE_NAMES, generator.getIRInfo(), INVALID_NAME_PREFIX));

		String[] generatedTempVarNames = GeneralUtils.concat(IRConstants.GENERATED_TEMP_NAMES, varPrefixes.GENERATED_TEMP_NAMES);

		Set<Violation> tempVarViolations = analysis.usesIllegalNames(mergedParseLists, new GeneratedVarComparison(generatedTempVarNames, generator.getIRInfo(), INVALID_NAME_PREFIX));

		if (!reservedWordViolations.isEmpty() || !typenameViolations.isEmpty()
				|| !tempVarViolations.isEmpty())
		{
			return new InvalidNamesResult(reservedWordViolations, typenameViolations, tempVarViolations, INVALID_NAME_PREFIX);
		} else
		{
			return new InvalidNamesResult();
		}
	}

	private void validateVdmModelingConstructs(
			List<? extends INode> mergedParseLists) throws AnalysisException,
			UnsupportedModelingException
	{
		VdmAstAnalysis analysis = new VdmAstAnalysis(generator.getIRInfo().getAssistantManager());

		Set<Violation> violations = analysis.usesUnsupportedModelingConstructs(mergedParseLists);

		if (!violations.isEmpty())
		{
			throw new UnsupportedModelingException("The model uses modeling constructs that are not supported for Java code Generation", violations);
		}
	}

	private boolean shouldBeGenerated(INode node,
			DeclAssistantCG declAssistant)
	{
		if (declAssistant.isLibrary(node))
		{
			return false;
		}

		String name = null;
		
		if(node instanceof SClassDefinition)
		{
			name = ((SClassDefinition) node).getName().getName();
		}
		else if(node instanceof AModuleModules)
		{
			name = ((AModuleModules) node).getName().getName();
		}
		else
		{
			return true;
		}

		if (getJavaSettings().getModulesToSkip().contains(name))
		{
			return false;
		}

		// for (SClassDefinition superDef : classDef.getSuperDefs())
		// {
		// if (declAssistant.classIsLibrary(superDef))
		// {
		// return false;
		// }
		// }

		return true;
	}

	@Override
	public void register(IREventObserver obs)
	{
		if(obs != null && irObserver == null)
		{
			irObserver = obs;
		}
	}

	@Override
	public void unregister(IREventObserver obs)
	{
		if(obs != null && irObserver == obs)
		{
			irObserver = null;
		}
	}

	public List<IRStatus<org.overture.codegen.cgast.INode>> initialIrEvent(List<IRStatus<org.overture.codegen.cgast.INode>> ast)
	{
		if(irObserver != null)
		{
			return irObserver.initialIRConstructed(ast, getInfo());
		}
		
		return ast;
	}
	
	public List<IRStatus<org.overture.codegen.cgast.INode>> finalIrEvent(List<IRStatus<org.overture.codegen.cgast.INode>> ast)
	{
		if(irObserver != null)
		{
			return irObserver.finalIRConstructed(ast, getInfo());
		}
		
		return ast;
	}
}
