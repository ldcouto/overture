package org.overture.interpreter.tests.newtests.coverage;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import org.junit.Assert;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.ast.modules.AModuleModules;
import org.overture.ast.node.INode;
import org.overture.interpreter.debug.DBGPReaderV2;
import org.overture.interpreter.runtime.Interpreter;
import org.overture.interpreter.tests.newtests.Message;
import org.overture.interpreter.tests.newtests.ParamInterpreterTest;
import org.overture.interpreter.tests.newtests.StringInterpreterResult;
import org.overture.interpreter.util.ClassListInterpreter;
import org.overture.interpreter.util.InterpreterUtil;
import org.overture.interpreter.util.ModuleListInterpreter;
import org.overture.interpreter.values.Value;

public abstract class CoverageTest extends ParamInterpreterTest
{

	public CoverageTest(String nameParameter, String inputParameter,
			String resultParameter)
	{
		super(nameParameter, inputParameter, resultParameter);
	}

	@Override
	public StringInterpreterResult processModel(List<INode> ast)
	{
		String entry = "1+1";
		if (getEntryFile() == null || !getEntryFile().exists())
		{
			entry = createEntryFile();
			if (entry == null)
			{
				if (getEntryFile() == null || !getEntryFile().exists())
				{
					Assert.fail("No entry for model (" + getEntryFile() + ")");
				}
			}
		} else
		{
			try
			{
				entry = super.getEntries().get(0);
			} catch (IOException e)
			{
				fail("Could not process entry file for " + testName);
			}
		}
		try
		{
			Interpreter interpreter;

			if (ast.get(0) instanceof SClassDefinition)
			{
				ClassListInterpreter list = new ClassListInterpreter();
				for (INode n : ast)
				{
					list.add((SClassDefinition) n);
				}
				interpreter = InterpreterUtil.getInterpreter(list);
				interpreter.setDefaultName(list.get(0).getName().getName());
			} else
			{
				ModuleListInterpreter list = new ModuleListInterpreter();
				for (INode n : ast)
				{
					list.add((AModuleModules) n);
				}
				interpreter = InterpreterUtil.getInterpreter(list);
				interpreter.setDefaultName(list.get(0).getName().getName());
			}

			interpreter.init(null);
			Value val = interpreter.execute(entry, null);

			StringInterpreterResult result = new StringInterpreterResult(val.toString(), new Vector<Message>(), new Vector<Message>());

			File coverageFolder = new File("target/vdm-coverage".replace('/', File.separatorChar));
			coverageFolder.mkdirs();

			DBGPReaderV2.writeMCDCCoverage(interpreter, coverageFolder);

			return result;
		} catch (Exception e)
		{
			return wrap(e);
		}
	}

	@Override
	protected String getUpdatePropertyString()
	{
		return "testes.update.interpreter.coverage";
	}

}
