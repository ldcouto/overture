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
package org.overture.codegen.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.overture.ast.analysis.AnalysisException;
import org.overture.ast.definitions.AClassClassDefinition;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.ast.expressions.PExp;
import org.overture.ast.factory.AstFactory;
import org.overture.ast.intf.lex.ILexLocation;
import org.overture.ast.lex.Dialect;
import org.overture.ast.lex.LexLocation;
import org.overture.ast.lex.LexNameToken;
import org.overture.ast.node.INode;
import org.overture.ast.typechecker.NameScope;
import org.overture.ast.types.AVoidType;
import org.overture.ast.util.definitions.ClassList;
import org.overture.ast.util.modules.ModuleList;
import org.overture.codegen.analysis.vdm.Renaming;
import org.overture.codegen.analysis.violations.InvalidNamesResult;
import org.overture.codegen.analysis.violations.UnsupportedModelingException;
import org.overture.codegen.analysis.violations.Violation;
import org.overture.codegen.assistant.AssistantManager;
import org.overture.codegen.assistant.LocationAssistantCG;
import org.overture.codegen.ir.ITempVarGen;
import org.overture.codegen.ir.IrNodeInfo;
import org.overture.codegen.ir.VdmNodeInfo;
import org.overture.codegen.logging.Logger;
import org.overture.config.Settings;
import org.overture.interpreter.VDMPP;
import org.overture.interpreter.VDMRT;
import org.overture.interpreter.VDMSL;
import org.overture.interpreter.util.ClassListInterpreter;
import org.overture.interpreter.util.ExitStatus;
import org.overture.parser.lex.LexException;
import org.overture.parser.lex.LexTokenReader;
import org.overture.parser.messages.Console;
import org.overture.parser.messages.VDMErrorsException;
import org.overture.parser.syntax.ClassReader;
import org.overture.parser.syntax.ExpressionReader;
import org.overture.parser.syntax.ParserException;
import org.overture.parser.util.ParserUtil;
import org.overture.parser.util.ParserUtil.ParserResult;
import org.overture.typechecker.ClassTypeChecker;
import org.overture.typechecker.Environment;
import org.overture.typechecker.PublicClassEnvironment;
import org.overture.typechecker.TypeCheckInfo;
import org.overture.typechecker.TypeChecker;
import org.overture.typechecker.assistant.TypeCheckerAssistantFactory;
import org.overture.typechecker.util.TypeCheckerUtil;
import org.overture.typechecker.util.TypeCheckerUtil.TypeCheckResult;
import org.overture.typechecker.visitor.TypeCheckVisitor;

public class GeneralCodeGenUtils
{
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	public static List<SClassDefinition> consClassList(List<File> files, Dialect dialect)
			throws AnalysisException
	{
		Settings.dialect = dialect;
		VDMPP vdmrt = (dialect == Dialect.VDM_RT ? new VDMRT() : new VDMPP());
		vdmrt.setQuiet(true);

		ExitStatus status = vdmrt.parse(files);

		if (status != ExitStatus.EXIT_OK)
		{
			throw new AnalysisException("Could not parse files!");
		}

		status = vdmrt.typeCheck();

		if (status != ExitStatus.EXIT_OK)
		{
			throw new AnalysisException("Could not type check files!");
		}

		ClassListInterpreter classes;
		try
		{
			classes = vdmrt.getInterpreter().getClasses();
		} catch (Exception e)
		{
			throw new AnalysisException("Could not get classes from class list interpreter!");
		}

		List<SClassDefinition> mergedParseList = new LinkedList<SClassDefinition>();

		for (SClassDefinition vdmClass : classes)
		{
			if (vdmClass instanceof AClassClassDefinition) {
				mergedParseList.add(vdmClass);
			}
		}

		return mergedParseList;
	}
	
	public static ModuleList consModuleList(List<File> files) throws AnalysisException
	{
		Settings.dialect = Dialect.VDM_SL;
		VDMSL vdmSl = new VDMSL();
		vdmSl.setQuiet(true);

		ExitStatus status = vdmSl.parse(files);

		if (status != ExitStatus.EXIT_OK)
		{
			throw new AnalysisException("Could not parse files!");
		}

		status = vdmSl.typeCheck();

		if (status != ExitStatus.EXIT_OK)
		{
			throw new AnalysisException("Could not type check files!");
		}

		try
		{
			return vdmSl.getInterpreter().getModules();
		} catch (Exception e)
		{
			throw new AnalysisException("Could not get classes from class list interpreter!");
		}
	}
	
	public static TypeCheckResult<List<SClassDefinition>> validateFile(File file)
			throws AnalysisException
	{
		if (!file.exists() || !file.isFile())
		{
			throw new AnalysisException("Could not find file: "
					+ file.getAbsolutePath());
		}

		ParserResult<List<SClassDefinition>> parseResult = ParserUtil.parseOo(file, "UTF-8");

		if (parseResult.errors.size() > 0)
		{
			throw new AnalysisException("File did not parse: "
					+ file.getAbsolutePath());
		}

		TypeCheckResult<List<SClassDefinition>> typeCheckResult = TypeCheckerUtil.typeCheckPp(file);

		if (typeCheckResult.errors.size() > 0)
		{
			throw new AnalysisException("File did not pass the type check: "
					+ file.getName());
		}

		return typeCheckResult;

	}

	public static TypeCheckResult<PExp> validateExp(String exp)
			throws AnalysisException
	{
		if (exp == null || exp.isEmpty())
		{
			throw new AnalysisException("No expression to generate from");
		}

		ParserResult<PExp> parseResult = null;
		try
		{
			parseResult = ParserUtil.parseExpression(exp);
		} catch (Exception e)
		{
			throw new AnalysisException("Unable to parse expression: " + exp
					+ ". Message: " + e.getMessage());
		}

		if (parseResult.errors.size() > 0)
		{
			throw new AnalysisException("Unable to parse expression: " + exp);
		}

		TypeCheckResult<PExp> typeCheckResult = null;
		try
		{
			typeCheckResult = TypeCheckerUtil.typeCheckExpression(exp);
		} catch (Exception e)
		{
			throw new AnalysisException("Unable to type check expression: "
					+ exp + ". Message: " + e.getMessage());
		}

		return typeCheckResult;
	}
	
	public static SClassDefinition consMainClass(
			List<SClassDefinition> mergedParseLists, String expression,
			Dialect dialect, String mainClassName, ITempVarGen nameGen) throws VDMErrorsException, AnalysisException
	{
		ClassList classes = new ClassList();
		classes.addAll(mergedParseLists);
		PExp entryExp = typeCheckEntryPoint(classes, expression, dialect);

		String resultTypeStr = entryExp.getType() instanceof AVoidType ? "()"
				: "?";

		// Collect all the class names
		List<String> namesToAvoid = new LinkedList<>();

		for (SClassDefinition c : classes)
		{
			namesToAvoid.add(c.getName().getName());
		}

		// If the user already uses the name proposed for the main class
		// we have to find a new name for the main class
		if (namesToAvoid.contains(mainClassName))
		{
			String prefix = mainClassName + "_";
			mainClassName = nameGen.nextVarName(prefix);

			while (namesToAvoid.contains(mainClassName))
			{
				mainClassName = nameGen.nextVarName(prefix);
			}
		}

		String entryClassTemplate = "class " + mainClassName + "\n"
				+ "operations\n" + "public static Run : () ==> "
				+ resultTypeStr + "\n" + "Run () == " + expression + ";\n"
				+ "end " + mainClassName;

		SClassDefinition clazz = parseClass(entryClassTemplate, mainClassName, dialect);

		return tcClass(classes, clazz);
	}
	
	public static PExp typeCheckEntryPoint(ClassList classes, String expression, Dialect dialect)
			throws VDMErrorsException, AnalysisException
	{
		SClassDefinition defaultModule = null;

		LexNameToken name =new LexNameToken("CLASS", "DEFAULT", new LexLocation());
		defaultModule = AstFactory.newAClassClassDefinition(name, null, null);
		defaultModule.setUsed(true);
		PExp exp = parseExpression(expression, defaultModule.getName().getName(),dialect);

		return tcExp(classes, exp);
	}
	
	public static PExp tcExp(ClassList classes, PExp exp)
			throws AnalysisException, VDMErrorsException
	{
		TypeCheckerAssistantFactory af = new TypeCheckerAssistantFactory();
		ClassTypeChecker.clearErrors();
		ClassTypeChecker classTc = new ClassTypeChecker(classes, af);

		classTc.typeCheck();

		TypeCheckVisitor tc = new TypeCheckVisitor();
		TypeChecker.clearErrors();
		Environment env = new PublicClassEnvironment(af, classes, null);

		exp.apply(tc, new TypeCheckInfo(af, env, NameScope.NAMESANDSTATE));

		if (TypeChecker.getErrorCount() > 0)
		{
			throw new VDMErrorsException(TypeChecker.getErrors());
		}
		else
		{
			return exp;
		}
	}

	public static SClassDefinition tcClass(ClassList classes,
			SClassDefinition clazz) throws AnalysisException,
			VDMErrorsException

	{
		TypeCheckerAssistantFactory af = new TypeCheckerAssistantFactory();
		ClassTypeChecker.clearErrors();
		ClassTypeChecker classTc = new ClassTypeChecker(classes, af);

		classes.add(clazz);
		classTc.typeCheck();
		
		if (TypeChecker.getErrorCount() > 0)
		{
			throw new VDMErrorsException(TypeChecker.getErrors());
		}
		else
		{
			return clazz;
		}
	}
	
	public static PExp parseExpression(String expression,
			String defaultModuleName, Dialect dialect) throws ParserException, LexException
	{
		LexTokenReader ltr = new LexTokenReader(expression, dialect, Console.charset);
		ExpressionReader reader = new ExpressionReader(ltr);
		reader.setCurrentModule(defaultModuleName);
		
		return reader.readExpression();
	}
	
	public static SClassDefinition parseClass(String classStr,
			String defaultModuleName, Dialect dialect)
	{
		LexTokenReader ltr = new LexTokenReader(classStr, dialect, Console.charset);
		ClassReader reader = new ClassReader(ltr);
		reader.setCurrentModule(defaultModuleName);
		
		// There should be only one class
		for(SClassDefinition clazz : reader.readClasses())
		{
			if(clazz.getName().getName().equals(defaultModuleName))
			{
				return clazz;
			}
		}
		
		return null;
	}

	public static void replaceInFile(File file, String regex, String replacement)
	{
		replaceInFile(file.getAbsolutePath(), regex, replacement);
	}

	public static void replaceInFile(String filePath, String regex,
			String replacement)
	{
		try
		{
			File file = new File(filePath);
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = "", oldtext = "";
			while ((line = reader.readLine()) != null)
			{
				oldtext += line + "\n";
			}
			reader.close();
			String newtext = oldtext.replaceAll(regex, replacement);

			FileWriter writer = new FileWriter(filePath);
			writer.write(newtext);
			writer.close();
		} catch (IOException ioe)
		{
			Logger.getLog().printErrorln("Error replacing characters in file: "
					+ filePath);
			ioe.printStackTrace();
		}
	}

	public static void copyDirectory(File sourceLocation, File targetLocation)
			throws IOException
	{
		if (!targetLocation.exists())
		{
			targetLocation.getParentFile().mkdirs();
		}

		if (sourceLocation.isDirectory())
		{
			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++)
			{
				copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
			}
		} else
		{
			targetLocation.createNewFile();

			InputStream in = new FileInputStream(sourceLocation);
			OutputStream out = new FileOutputStream(targetLocation);

			// Copy the bits from instream to outstream
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
	}

	public static List<String> getClassesToSkip(String userInput)
	{
		if(userInput == null)
		{
			return new LinkedList<String>();
		}
		
		String[] split = userInput.split(";");

		List<String> classesToSkip = new LinkedList<String>();
		
		for(String element : split)
		{
			element = element.trim();
			
			if(element != null && !element.isEmpty())
			{
				if(!classesToSkip.contains(element))
				{
					classesToSkip.add(element);
				}
			}
		}
		
		return classesToSkip;
	}
	
	public static String constructNameViolationsString(
			InvalidNamesResult invalidNames)
	{
		StringBuffer buffer = new StringBuffer();

		List<Violation> reservedWordViolations = asSortedList(invalidNames.getReservedWordViolations());
		List<Violation> typenameViolations = asSortedList(invalidNames.getTypenameViolations());
		List<Violation> tempVarViolations = asSortedList(invalidNames.getTempVarViolations());

		String correctionMessage = String.format("Prefix '%s' has been added to the name"
				+ LINE_SEPARATOR, invalidNames.getCorrectionPrefix());

		for (Violation violation : reservedWordViolations)
		{
			buffer.append("Reserved name violation: " + violation + ". "
					+ correctionMessage);
		}

		for (Violation violation : typenameViolations)
		{
			buffer.append("Type name violation: " + violation + ". "
					+ correctionMessage);
		}

		for (Violation violation : tempVarViolations)
		{
			buffer.append("Temporary variable violation: " + violation + ". "
					+ correctionMessage);
		}

		return buffer.toString();
	}
	
	public static String constructVarRenamingString(List<Renaming> renamings)
	{
		StringBuilder sb = new StringBuilder();
		
		for(Renaming r : renamings)
		{
			sb.append(r).append('\n');
		}
		
		return sb.toString();
	}
	
	public static String constructUnsupportedModelingString(
			UnsupportedModelingException e)
	{
		StringBuffer buffer = new StringBuffer();

		List<Violation> violations = asSortedList(e.getViolations());

		for (Violation violation : violations)
		{
			buffer.append(violation + LINE_SEPARATOR);
		}

		return buffer.toString();
	}
	
	public static List<Violation> asSortedList(Set<Violation> violations)
	{
		LinkedList<Violation> list = new LinkedList<Violation>(violations);
		Collections.sort(list);

		return list;
	}
	
	public static void printMergeErrors(List<Exception> mergeErrors)
	{
		for (Exception error : mergeErrors)
		{
			Logger.getLog().println(error.toString());
		}
	}
	
	public static void printUnsupportedIrNodes(Set<VdmNodeInfo> unsupportedNodes)
	{
		AssistantManager assistantManager = new AssistantManager();
		LocationAssistantCG locationAssistant = assistantManager.getLocationAssistant();

		List<VdmNodeInfo> nodesSorted = locationAssistant.getVdmNodeInfoLocationSorted(unsupportedNodes);

		for (VdmNodeInfo vdmNodeInfo : nodesSorted)
		{
			Logger.getLog().print(vdmNodeInfo.getNode().toString() + 
					" (" + vdmNodeInfo.getNode().getClass().getSimpleName() + ")");

			ILexLocation location = locationAssistant.findLocation(vdmNodeInfo.getNode());

			Logger.getLog().print(location != null ? " at [line, pos] = ["
					+ location.getStartLine() + ", " + location.getStartPos()
					+ "]" : "");

			String reason = vdmNodeInfo.getReason();

			if (reason != null)
			{
				Logger.getLog().print(". Reason: " + reason);
			}

			Logger.getLog().println("");
		}
	}
	
	public static void printUnsupportedNodes(Set<IrNodeInfo> unsupportedNodes)
	{
		AssistantManager assistantManager = new AssistantManager();
		LocationAssistantCG locationAssistant = assistantManager.getLocationAssistant();
		
		List<IrNodeInfo> nodesSorted = locationAssistant.getIrNodeInfoLocationSorted(unsupportedNodes);

		for (IrNodeInfo nodeInfo : nodesSorted)
		{
			INode vdmNode = locationAssistant.getVdmNode(nodeInfo);
			Logger.getLog().print(vdmNode != null ? vdmNode.toString() : nodeInfo.getNode().getClass().getSimpleName());

			ILexLocation location = locationAssistant.findLocation(nodeInfo);

			Logger.getLog().print(location != null ? " at [line, pos] = ["
					+ location.getStartLine() + ", " + location.getStartPos()
					+ "]" : "");

			String reason = nodeInfo.getReason();

			if (reason != null)
			{
				Logger.getLog().print(". Reason: " + reason);
			}

			Logger.getLog().println("");
		}
	}
}
