package org.overture.interpreter.tests.newtests.coverage;

import java.util.Collection;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.overture.config.Release;
import org.overture.config.Settings;
import org.overture.interpreter.tests.newtests.ClassesPpClassicTest;

@RunWith(Parameterized.class)
public class CoverageClassesClassicTest extends CoverageTest
{

	public CoverageClassesClassicTest(String nameParameter,
			String inputParameter, String resultParameter)
	{
		super(nameParameter, inputParameter, resultParameter);
	}

	@Parameters(name = "{index} : {0}")
	public static Collection<Object[]> testData()
	{
		return ClassesPpClassicTest.testData();
	}

	@Before
	public void setUp() throws Exception
	{
		Settings.release = Release.CLASSIC;
	}

}
