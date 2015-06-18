package org.overture.vdm2jml.tests;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.overture.ast.analysis.AnalysisException;
import org.overture.codegen.analysis.violations.UnsupportedModelingException;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;

public class PostCondNoStateTests extends AnnotationTestsBase
{
	@BeforeClass
	public static void init() throws AnalysisException,
			UnsupportedModelingException
	{
		AnnotationTestsBase.init("PostCondNoState.vdmsl");
	}
	
	@Test
	public void testPostCondOfFuncIsPure()
	{
		assertFuncIsPureOnly("post_f");
	}
	
	@Test
	public void testFuncEnsuresAnnotation()
	{
		AMethodDeclCG func = getMethod(genModule.getMethods(), "f");
		
		Assert.assertEquals("Got unexpected ensures annotation for function 'f'", 
				"//@ ensures post_f(a,b,\\result);",
				getAnnotation(func, 0));
	}
	
	@Test
	public void testPostCondOfOpNoResIsPure()
	{
		assertFuncIsPureOnly("post_opNoRes");
	}
	
	@Test
	public void testOpNoResRequiresAnnotation()
	{
		AMethodDeclCG opNoRes = getMethod(genModule.getMethods(), "opNoRes");
		
		Assert.assertEquals("Got unexpected ensures annotation for operation 'opNoRes'",
				"//@ ensures post_opNoRes();",
				getAnnotation(opNoRes, 0));
	}
	
	// We'll not test that the post condition of 'opRes' is pure
	
	@Test
	public void testOpResRequresAnnotation()
	{
		AMethodDeclCG opRes = getMethod(genModule.getMethods(), "opRes");
		
		Assert.assertEquals("Got unexpected ensures annotation for operation 'opRes'",
				"//@ ensures post_opRes(a,\\result);",
				getAnnotation(opRes, 0));
	}
}
