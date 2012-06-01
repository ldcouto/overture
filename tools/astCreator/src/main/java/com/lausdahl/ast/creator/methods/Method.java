package com.lausdahl.ast.creator.methods;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import com.lausdahl.ast.creator.definitions.BaseClassDefinition;
import com.lausdahl.ast.creator.definitions.IClassDefinition;
import com.lausdahl.ast.creator.definitions.IInterfaceDefinition;
import com.lausdahl.ast.creator.env.Environment;

public abstract class Method
{
	public static class Argument
	{
		public Argument()
		{
		}

		public Argument(String type, String name)
		{
			this.type = type;
			this.name = name;
		}

		public String type;
		public String name;

		@Override
		public String toString()
		{
			return type + " " + name;
		}
	}

	public boolean isConstructor = false;
	public boolean isAbstract = false;
	protected String javaDoc = "";
	public String name;
	public String returnType = "void";
	public List<Argument> arguments = new Vector<Method.Argument>();
	public String body;
	public String annotation;
	public IClassDefinition classDefinition;
	protected Set<String> requiredImports = new HashSet<String>();
	// private boolean isStructureFinal = false;
	protected boolean skip = false;
	protected boolean optionalVdmArgument = true;
	protected Environment env;

	public Method(IClassDefinition c, Environment env)
	{
		this.env = env;
		setClassDefinition(c);
	}

	// public Method(IClassDefinition c, boolean finalStructure)
	// {
	// this.isStructureFinal = finalStructure;
	// setClassDefinition(c);
	// }

	public void setClassDefinition(IClassDefinition c)
	{
		this.classDefinition = c;
	}

	private void internalPrepare()
	{
		// if (isStructureFinal)
		// {
		// return;
		// }
		requiredImports.clear();
		arguments.clear();
		prepare();
	}

	private void internalVdmPrepare()
	{
		optionalVdmArgument = true;
		skip = false;
		requiredImports.clear();
		arguments.clear();
		prepareVdm();
	}

	protected void prepare()
	{

	}

	protected void prepareVdm()
	{
		prepare();
	}

	public Set<String> getRequiredImports()
	{
		internalPrepare();
		addImportForType(returnType);
		for (Argument arg : arguments)
		{
			addImportForType(arg.type);
		}

		return requiredImports;
	}

	private void addImportForType(String typeName)
	{
		//TODO type name as a string is not good
		Set<String> names = new HashSet<String>();
		if (typeName.contains("<"))
		{
			names.add(typeName.substring(0, typeName.indexOf('<')).trim());
			if (!typeName.contains("extends"))
			{
				String tmp = typeName.substring(typeName.indexOf('<') + 1, typeName.indexOf('>')).trim();
				names.addAll(Arrays.asList(tmp.split("\\,")));
			} else
			{
				String tmp = typeName.substring(typeName.indexOf('<') + 1, typeName.indexOf('>')).trim();
				tmp = tmp.replaceAll("\\?", "");
				tmp = tmp.replaceAll("extends", "");
				tmp = tmp.replaceAll(" ", "");
				names.addAll(Arrays.asList(tmp.split("\\,")));
			}
		} else
		{
			names.add(typeName);
		}

		for (String name : names)
		{
			IInterfaceDefinition defIntf = env.lookUpInterface(name);
			if (defIntf != null)
			{
				requiredImports.add(defIntf.getName().getCanonicalName());
			}

			IClassDefinition def = env.lookUp(name);
			if (def != null)
			{
				requiredImports.add(def.getName().getCanonicalName());
			}
		}

	}

	public Set<String> getRequiredImportsSignature()
	{
		internalPrepare();
		addImportForType(returnType);
		for (Argument arg : arguments)
		{
			addImportForType(arg.type);
		}

		return requiredImports;
	}

	@Override
	public String toString()
	{
		return getJavaSourceCode();
	}

	public String getJavaSourceCode()
	{
		internalPrepare();
		if (skip)
		{
			return "";
		}

		String tmp = javaDoc != null ? javaDoc + "\n" : "";
		if (annotation != null)
		{
			tmp += "\t" + annotation + "\n";
		}
		tmp += getSignature();

		if (isAbstract)
		{
			tmp += ";\n";
		} else
		{
			tmp += "\n\t{\n";

			if (body != null)
			{
				tmp += body;
			}
			tmp += "\n\t}\n\n";
		}
		return tmp;
	}

	public String getVdmSourceCode()
	{
		internalVdmPrepare();
		if (skip)
		{
			return "";
		}

		String tmp = javaDoc != null ? javaDoc + "\n" : "";
		if (annotation != null)
		{
			tmp += "\t--" + annotation + "\n";
		}
		tmp += getVdmSignature() + "\n";

		if (isAbstract)
		{
			tmp += "is subclass responsibility;";
		} else
		{
			tmp += "\t(\n";

			if (body != null)
			{
				tmp += body;
			}
			tmp += "\n\t);\n\n";
		}
		return tmp;
	}

	public String getSignature()
	{
		internalPrepare();
		String tmp = "\t"
				+ ("public " + (isAbstract ? "abstract " : "") + returnType).trim()
				+ " " + name + "(";
		for (Argument a : arguments)
		{
			tmp += a.toString() + ", ";
		}
		if (!arguments.isEmpty())
		{
			tmp = tmp.substring(0, tmp.length() - 2);
		}
		tmp += ")";
		return tmp;
	}

	public String getVdmSignature()
	{
		internalVdmPrepare();
		String tmp = "\t" + "public " + name + ": ";
		for (Argument a : arguments)
		{
			String typeName = a.type;
			if (typeName.contains("List<"))
			{
				typeName = "seq of "
						+ typeName.substring(typeName.lastIndexOf(' '), typeName.lastIndexOf('>')).trim();
			}
			tmp += (optionalVdmArgument ? "[" : "") + typeName
					+ (optionalVdmArgument ? "]" : "") + " * ";
		}
		if (!arguments.isEmpty())
		{
			tmp = tmp.substring(0, tmp.length() - 2);
		} else
		{
			tmp += "() ";
		}

		String returnTypeName = BaseClassDefinition.stripGenericArguments(returnType);
		tmp += " ==> "
				+ (returnType.length() == 0 ? name
						: (returnType.equals("void") ? "()" : returnTypeName))
				+ "\n";
		tmp += "\t" + name + "(";
		for (Argument a : arguments)
		{
			tmp += a.name + ", ";
		}
		if (!arguments.isEmpty())
		{
			tmp = tmp.substring(0, tmp.length() - 2);
		}

		tmp += ") == ";
		return tmp;
	}

	public String getJavaDoc()
	{
		internalPrepare();
		return javaDoc;
	}

	public void setEnvironment(Environment env2)
	{
		this.env = env2;
	}

	protected String getSpecializedTypeName(IInterfaceDefinition c)
	{
		IInterfaceDefinition intfForClass = env.getInterfaceForCommonTreeNode(c);
		if (intfForClass == null)
		{
			return c.getName().getName();
		} else
		{
			return intfForClass.getName().getName();
		}
	}

	public boolean isOverride(Method m)
	{
		if (this.name.equals(m.name) && arguments.size() == m.arguments.size())
		{
			for (int i = 0; i < arguments.size(); i++)
			{
				Argument sourceArg1 = arguments.get(i);
				Argument basearg1 = m.arguments.get(i);
				if (!env.isSuperTo(env.lookUpType(basearg1.type), env.lookUpType(sourceArg1.type)))
				{
					return false;
				}
			}
			return true;
		} else
		{
			return false;
		}
	}

	public boolean isSignatureEqual(Method m)
	{
		if (this.name.equals(m.name) && arguments.size() == m.arguments.size()
				&& returnType.equals(m.returnType))
		{
			for (int i = 0; i < arguments.size(); i++)
			{
				Argument sourceArg1 = arguments.get(i);
				Argument basearg1 = m.arguments.get(i);
				if (!basearg1.type.equals(sourceArg1.type))
				{
					return false;
				}

			}
			return true;
		} else
		{
			return false;
		}
	}
}