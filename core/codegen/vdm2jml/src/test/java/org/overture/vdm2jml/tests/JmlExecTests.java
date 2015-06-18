package org.overture.vdm2jml.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.overture.ast.lex.LexLocation;
import org.overture.codegen.utils.GeneralUtils;
import org.overture.codegen.vdm2java.IJavaCodeGenConstants;
import org.overture.codegen.vdm2java.JavaCodeGen;
import org.overture.codegen.vdm2java.JavaCodeGenUtil;
import org.overture.codegen.vdm2java.JavaToolsUtils;
import org.overture.codegen.vdm2jml.IOpenJmlConsts;
import org.overture.test.framework.Properties;
import org.overture.vdm2jml.tests.util.ProcessResult;

@RunWith(Parameterized.class)
public class JmlExecTests extends OpenJmlValidationBase
{
	private static final String PROPERTY_ID = "exec";

	private static final String TESTS_VDM2JML_PROPERTY_PREFIX = "tests.vdm2jml.override.";

	private static final String MAIN_CLASS_NAME = "Main";

	private static final String MAIN_CLASS_RES = "exec_entry_point";

	public static final String RESULT_FILE_EXT = ".result";
	
	public static final String DEFAULT_JAVA_ROOT_PACKAGE = "project";

	private boolean isTypeChecked;

	public JmlExecTests(File inputFile)
	{
		super(inputFile);
		this.isTypeChecked = false;
	}

	@Before
	public void assumeTools()
	{
		assumeOpenJml();
		assumeJmlRuntime();
	}

	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> data()
	{
		File folder = new File(AnnotationTestsBase.TEST_RES_DYNAMIC_ANALYSIS_ROOT);
		List<File> files = GeneralUtils.getFilesRecursive(folder);

		return collectVdmslFiles(files);
	}

	@Test
	public void execJml()
	{
		try
		{
			codeGenerateInputFile();
			
			configureResultGeneration();

			compileJmlJava();

			String actualRes = processResultStr(execJmlJava().toString());

			if (Properties.recordTestResults)
			{
				storeResult(actualRes.toString());
			} else
			{
				try
				{
					String expectedRes = GeneralUtils.readFromFile(getResultFile()).trim();
					Assert.assertEquals("Expected result and actual result are different", expectedRes, actualRes);

				} catch (IOException e)
				{
					e.printStackTrace();
					Assert.assertTrue("Could not read the expected result from the result file: "
							+ e.getMessage(), false);
				}
			}
		} finally
		{
			unconfigureResultGeneration();
		}
	}

	public void storeResult(String resultStr)
	{
		storeJmlOutput(resultStr);
		storeGeneratedJml();
	}

	private void storeGeneratedJml()
	{
		String projDir = File.separatorChar + DEFAULT_JAVA_ROOT_PACKAGE
				+ File.separatorChar;
		String quotesDir = File.separatorChar + JavaCodeGen.QUOTES
				+ File.separatorChar;

		try
		{
			List<File> files = GeneralUtils.getFilesRecursive(genJavaFolder);

			List<File> filesToStore = new LinkedList<File>();

			for (File file : files)
			{
				String absPath = file.getAbsolutePath();

				if (absPath.endsWith(IJavaCodeGenConstants.JAVA_FILE_EXTENSION)
						&& absPath.contains(projDir)
						&& !absPath.contains(quotesDir))
				{
					filesToStore.add(file);
				}
			}

			File testFolder = inputFile.getParentFile();

			for (File file : filesToStore)
			{
				File javaFile = new File(testFolder, file.getName());
				FileUtils.copyFile(file, javaFile);
			}
		} catch (IOException e)
		{
			e.printStackTrace();
			Assert.assertTrue("Problems storing generated JML: "
					+ e.getMessage(), false);
		}
	}

	private void storeJmlOutput(String resultStr)
	{
		resultStr = processResultStr(resultStr);
		
		File resultFile = getResultFile();

		PrintWriter printWriter = null;
		try
		{
			FileOutputStream fileOutStream = new FileOutputStream(resultFile, false);
			OutputStreamWriter outStream = new OutputStreamWriter(fileOutStream, "UTF-8");
			
			printWriter = new PrintWriter(outStream);
			printWriter.write(resultStr);
			printWriter.flush();
			
		} catch (UnsupportedEncodingException | FileNotFoundException e)
		{
			e.printStackTrace();
			Assert.assertTrue("Could not store result: " + e.getMessage(), false);
		} finally
		{
			if (printWriter != null)
			{
				printWriter.close();
			}
		}
	}
	
	private String processResultStr(String resultStr)
	{
		resultStr = resultStr.trim();
		resultStr = resultStr.replaceAll("\r", "");

		String[] lines = resultStr.split("\n");
		StringBuilder sb = new StringBuilder();
		Pattern pattern = Pattern.compile("(?m)^.*?[^a-zA-Z]([a-zA-Z]+\\.java:[0-9]+:.*?)(:|$)");
		
		for(String line : lines)
		{
			Matcher matcher = pattern.matcher(line);
			
			if(matcher.find())
			{
				sb.append(matcher.group(1));
			}
			else
			{
				sb.append(line);
			}
			sb.append('\n');
		}
		
		return sb.toString().trim();
	}

	private void createExecEntryPoint()
	{
		File mainClassRes = new File(AnnotationTestsBase.TEST_RESOURCES_ROOT, MAIN_CLASS_RES);
		File mainClassJavaFile = new File(genJavaFolder, MAIN_CLASS_NAME
				+ IJavaCodeGenConstants.JAVA_FILE_EXTENSION);

		try
		{
			// Create a Main.java (a class with a main method)
			FileUtils.copyFile(mainClassRes, mainClassJavaFile);
		} catch (IOException e)
		{
			Assume.assumeTrue("Problems generating execution entry point", false);
		}
	}

	public void compileJmlJava()
	{
		ProcessResult processResult = runOpenJmlProcess();

		assertNoProcessErrors(processResult);

		isTypeChecked = true;
	}

	public StringBuilder execJmlJava()
	{
		ProcessResult processResult = runOpenJmlProcess();

		assertNoProcessErrors(processResult);

		return processResult.getOutput();
	}

	public String[] getTypeCheckArgs(File genJavaFolder)
	{
		// Compiles files with runtime assertions in preparation to execution
		// of the JML annotated Java code
		// java
		// -jar
		// $OPENJML/openjml.jar
		// -classpath
		// codegen-runtime.jar
		// -rac
		// -racCompileToJavaAssert (currently disabled)
		// -no-purityCheck
		// <javafiles>

		String[] openJmlConfig = new String[] { JavaToolsUtils.JAVA,
				JavaToolsUtils.JAR_ARG, openJml.getAbsolutePath(),
				IOpenJmlConsts.CP_ARG, cgRuntime.getAbsolutePath(),
				IOpenJmlConsts.RAC_ARG,
				/*IOpenJmlConsts.RAC_TO_ASSERT_ARG,*/
				IOpenJmlConsts.NO_PURITY_CHECKS_ARG };

		String[] javaFiles = JavaCodeGenUtil.findJavaFilePathsRec(genJavaFolder);

		return GeneralUtils.concat(openJmlConfig, javaFiles);
	}

	private String[] getExecArgs()
	{
		// Executes the OpenJML runtime assertion checker
		// java
		// -classpath
		// ".:codegen-runtime.jar:jmlruntime.jar"
		// -ea (currently disabled)
		// Main

		// Note that use of File.pathSeparatorChar makes it a platform dependent construction
		// of the classpath
		String runtimes = jmlRuntime.getAbsolutePath() + File.pathSeparatorChar
				+ openJml.getAbsolutePath() + File.pathSeparatorChar
				+ genJavaFolder.getAbsolutePath() + File.pathSeparatorChar
				+ cgRuntime.getAbsolutePath();
		
		String[] args = new String[] { JavaToolsUtils.JAVA,
				JavaToolsUtils.CP_ARG, runtimes,
				/*JavaToolsUtils.ENABLE_ASSERTIONS_ARG,*/ MAIN_CLASS_NAME };

		return args;
	}

	protected File getResultFile()
	{
		File resultFile = new File(inputFile.getAbsolutePath()
				+ RESULT_FILE_EXT);

		if (!resultFile.exists())
		{
			resultFile.getParentFile().mkdirs();
			try
			{
				resultFile.createNewFile();
			} catch (IOException e)
			{
				Assert.assertTrue("Problems creating result file: "
						+ e.getMessage(), false);
				e.printStackTrace();
				return null;
			}
		}

		return resultFile;
	}

	@Override
	public void beforeRunningOpenJmlProcess()
	{
		if (!isTypeChecked)
		{
			clearCodeFolder();
			createExecEntryPoint();
			codeGenerateInputFile();
		}
	}

	@Override
	public String[] getProcessArgs()
	{
		if (!isTypeChecked)
		{
			return getTypeCheckArgs(genJavaFolder);
		} else
		{
			return getExecArgs();
		}
	}

	protected String getPropertyId()
	{
		return PROPERTY_ID;
	}

	protected void configureResultGeneration()
	{
		LexLocation.absoluteToStringLocation = false;
		if (System.getProperty(TESTS_VDM2JML_PROPERTY_PREFIX + "all") != null
				|| getPropertyId() != null
				&& System.getProperty(TESTS_VDM2JML_PROPERTY_PREFIX
						+ getPropertyId()) != null)
		{
			Properties.recordTestResults = true;
		}
	}

	protected void unconfigureResultGeneration()
	{
		Properties.recordTestResults = false;
	}
}
