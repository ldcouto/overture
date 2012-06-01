package org.overture.parser.tests;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Enumeration;

import org.overturetool.test.framework.BaseTestSuite;

import junit.framework.Test;
import junit.framework.TestSuite;


public class ExpressionTestSuite extends BaseTestSuite
{

	public static Test suite() throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException
	{
		String name = "Parser Expression TestSuite";
		String root = "src\\test\\resources\\expressions";
		TestSuite test = createTestCompleteFile(name,root+"\\complete",ExpressionTestCase.class);
		TestSuite test2 = createTestSingleLineFile(name,root+"\\singleline",ExpressionTestCase.class);
		@SuppressWarnings("unchecked")
		Enumeration<Test> e = test2.tests();
		while(e.hasMoreElements())
		{
			test.addTest(e.nextElement());
		}
		return test;
	}
	
	
}