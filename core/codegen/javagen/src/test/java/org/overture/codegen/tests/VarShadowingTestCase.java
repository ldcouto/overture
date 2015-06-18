package org.overture.codegen.tests;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.overture.ast.definitions.PDefinition;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.ast.lex.Dialect;
import org.overture.ast.node.INode;
import org.overture.ast.statements.AIdentifierStateDesignator;
import org.overture.codegen.analysis.vdm.IdStateDesignatorDefCollector;
import org.overture.codegen.analysis.vdm.Renaming;
import org.overture.codegen.analysis.vdm.VarRenamer;
import org.overture.codegen.logging.Logger;
import org.overture.codegen.utils.GeneralUtils;
import org.overture.config.Release;
import org.overture.config.Settings;
import org.overture.interpreter.util.InterpreterUtil;
import org.overture.interpreter.values.Value;
import org.overture.parser.messages.VDMWarning;
import org.overture.test.framework.BaseTestCase;
import org.overture.typechecker.assistant.TypeCheckerAssistantFactory;
import org.overture.typechecker.util.TypeCheckerUtil;
import org.overture.typechecker.util.TypeCheckerUtil.TypeCheckResult;

public class VarShadowingTestCase extends BaseTestCase
{
	public static final String EVAL_ENTRY_POINT = "Entry`Run()";
	
	private TypeCheckerAssistantFactory af = new TypeCheckerAssistantFactory();
	
	public VarShadowingTestCase()
	{
		super();
	}
	
	public VarShadowingTestCase(File file)
	{
		super(file);
	}
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		Settings.dialect = Dialect.VDM_PP;
		Settings.release = Release.VDM_10;
		Logger.getLog().setSilent(true);
	}
	
	@Override
	public String getName()
	{
		return this.content;
	}

	@Override
	public void test() throws Exception
	{
		if (content == null)
		{
			return;
		}
		
		try
		{
			TypeCheckResult<List<SClassDefinition>> originalSpecTcResult = TypeCheckerUtil.typeCheckPp(file);
			Map<AIdentifierStateDesignator, PDefinition> idDefs = IdStateDesignatorDefCollector.getIdDefs(originalSpecTcResult.result, af);

			Assert.assertTrue(getName() + " has type errors", originalSpecTcResult.errors.isEmpty());
			Value orgSpecResult = evalSpec(originalSpecTcResult.result);
			
			
			List<Renaming> renamings = new LinkedList<Renaming>(new VarRenamer().computeRenamings(originalSpecTcResult.result, af, idDefs));
			
			// It is very important that renamings are performed from the bottom, right to left, in order
			// not to mess up the location of the names!!
			Collections.sort(renamings);
			

			StringBuilder sb = GeneralUtils.readLines(file, "\n");
			
			// Perform the renaming in a string buffer
			rename(renamings, sb);

			// Type check the renamed specification
			TypeCheckResult<List<SClassDefinition>> renamed = TypeCheckerUtil.typeCheckPp(sb.toString());

			// The renamed specification must contain no type errors and no warnings about hidden variables
			Assert.assertTrue("Renamed specification contains errors", renamed.errors.isEmpty());
			Assert.assertTrue("Found hidden variable warnings in renamed specification", filter(renamed.warnings).isEmpty());
			
			Value renamedSpecResult = evalSpec(renamed.result);
			
			// The two specifications must evaluate to the same result
			Assert.assertTrue("Expected same value to be produced "
					+ "for the original specification and the specification with "
					+ "renamed variables. Got values "
					+ orgSpecResult
					+ " and "
					+ renamedSpecResult
					+ " from the original and "
					+ "the renamed specification, respectively", orgSpecResult.equals(renamedSpecResult));

		} catch (Exception e)
		{
			e.printStackTrace();
			Assert.fail("Test: " + getName() + " did not parse!");
		}	
	}

	private void rename(List<Renaming> renamings, StringBuilder sb)
	{
		for (Renaming r : renamings)
		{
			int startOffset = r.getLoc().getStartOffset() - 1;
			int endOffset = startOffset + r.getNewName().length() - 2;

			sb.replace(startOffset, endOffset, r.getNewName());
		}
	}

	private Value evalSpec(List<SClassDefinition> classes) throws Exception
	{
		return InterpreterUtil.interpret(new LinkedList<INode>(classes), EVAL_ENTRY_POINT, Settings.dialect);
	}

	private List<VDMWarning> filter(List<VDMWarning> warnings)
	{
		List<VDMWarning> filtered = new LinkedList<VDMWarning>();

		for (VDMWarning w : warnings)
		{
			if (w.number == 5007 || w.number == 5008)
			{
				filtered.add(w);
			}
		}

		return filtered;
	}
}
