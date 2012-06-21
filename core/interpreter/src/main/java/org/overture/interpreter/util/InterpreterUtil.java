package org.overture.interpreter.util;

import java.io.File;
import java.util.List;

import org.overture.ast.modules.AModuleModules;
import org.overture.interpreter.runtime.ClassInterpreter;
import org.overture.interpreter.runtime.Interpreter;
import org.overture.interpreter.runtime.ModuleInterpreter;
import org.overture.interpreter.values.Value;
import org.overture.typechecker.util.TypeCheckerUtil;
import org.overture.typechecker.util.TypeCheckerUtil.TypeCheckResult;

public class InterpreterUtil
{
	public static Interpreter getInterpreter(ModuleListInterpreter modules)
			throws Exception
	{
		return new ModuleInterpreter(modules);
	}

	public static Interpreter getInterpreter(ClassListInterpreter classes)
			throws Exception
	{
		return new ClassInterpreter(classes);
	}

	public static Value interpret(String content) throws Exception
	{
		
		Interpreter interpreter = getInterpreter(new ModuleListInterpreter());
		interpreter.init(null);
		Value val = interpreter.execute(content, null);
		return val;
	}

	public static Value interpret(String entry, File file) throws Exception
	{
		TypeCheckResult<List<AModuleModules>> result = TypeCheckerUtil.typeCheckSl(file);

		if (result.errors.isEmpty())
		{
			ModuleListInterpreter list = new ModuleListInterpreter();
			list.addAll(result.result);
			Interpreter interpreter = getInterpreter(list);
			interpreter.init(null);
			interpreter.setDefaultName(list.get(0).getName().name);
			Value val = interpreter.execute(entry, null);
			return val;
		}
		return null;
	}
}