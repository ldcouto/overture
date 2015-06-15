package org.overture.interpreter.tests.newtests.coverage;

import java.io.File;
import java.util.Collection;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.overture.config.Release;
import org.overture.config.Settings;
import org.overture.interpreter.tests.newtests.ClassesRtClassicTest;

@RunWith(Parameterized.class)
public class CoverageRtClassicTest extends CoverageTest
{

	public CoverageRtClassicTest(String nameParameter, String inputParameter,
			String resultParameter)
	{
		super(nameParameter, inputParameter, resultParameter);
	}

	@Parameters(name = "{index} : {0}")
	public static Collection<Object[]> testData()
	{
		return ClassesRtClassicTest.testData();
	}

	@Before
	public void setUp() throws Exception
	{
		Settings.release = Release.CLASSIC;

	}

	@Override
	protected File getEntryFile()
	{
		// the rt tests have an extension for some reason...
		String fileName = modelPath.split("\\.")[0];
		return new File(fileName + ".entry");
	}
	
}
