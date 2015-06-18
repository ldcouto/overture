package org.overture.vdm2jml.tests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.overture.ast.analysis.AnalysisException;
import org.overture.codegen.analysis.violations.UnsupportedModelingException;
import org.overture.codegen.cgast.declarations.AFieldDeclCG;
import org.overture.codegen.cgast.declarations.AMethodDeclCG;

public class StateTests extends AnnotationTestsBase
{
	@BeforeClass
	public static void init() throws AnalysisException, UnsupportedModelingException
	{
		AnnotationTestsBase.init("State.vdmsl");
	}
	
	@Before
	public void prepareTest()
	{
		validateGenModuleAndStateType();
	}
	
	@Test
	public void testModuleStateIsSpecPublic()
	{
		Assert.assertTrue("Expected a single field to represent the state", genModule.getFields().size() == 1);

		AFieldDeclCG stateField = genModule.getFields().getFirst();

		Assert.assertTrue("Expected only a single JML annotation for the state field", stateField.getMetaData().size() == 1);

		String annotation = stateField.getMetaData().get(0).value;

		Assert.assertEquals("Expected state field to be @spec_public", SPEC_PUBLIC_ANNOTATION, annotation);
	}
	
	@Test
	public void testGenStateTypeMethodsArePure()
	{
		Assert.assertTrue("Expected five methods in the state type ", genStateType.getMethods().size() == 5);

		for (AMethodDeclCG m : genStateType.getMethods())
		{
			if (!m.getIsConstructor())
			{
				assertPureMethod(m);
			}
		}
	}
	
	@Test
	public void testInvFuncIsHelper()
	{
		assertHelper(genModule.getInvariant(), "Expected invariant function to be a helper");
	}
	
	@Test
	public void testInv()
	{
		Assert.assertTrue("Expected state invariant to exist", !genModule.getMetaData().isEmpty());
		
		String fieldName = genModule.getFields().getFirst().getName();
		
		String expected = String.format("//@ public static invariant St != null ==> inv_%1$s(%1$s);", fieldName);
		
		Assert.assertEquals("Got unexpected invariant", expected, genModule.getMetaData().get(0).value);
	}
}
