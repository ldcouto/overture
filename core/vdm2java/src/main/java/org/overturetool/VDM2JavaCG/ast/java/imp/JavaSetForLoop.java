// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaSetForLoop extends JavaForLoop implements IJavaSetForLoop
{
	// private member variable (VarName)
	private String m_VarName = new String();

	// public operation to retrieve the embedded private field value
	public String getVarName()
	{
		return m_VarName;
	}

	// public operation to set the embedded private field value
	public void setVarName(String p_VarName)
	{
		// consistency check (field must be non null!)
		assert(p_VarName != null);

		// instantiate the member variable
		m_VarName = p_VarName;
	}

	// private member variable (Set)
	private IJavaExpression m_Set = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getSet()
	{
		return m_Set;
	}

	// public operation to set the embedded private field value
	public void setSet(IJavaExpression p_Set)
	{
		// consistency check (field must be non null!)
		assert(p_Set != null);

		// instantiate the member variable
		m_Set = p_Set;

		// set the parent of the parameter passed
		p_Set.setParent(this);
	}

	// private member variable (statement)
	private IJavaStatement m_statement = null;

	// public operation to retrieve the embedded private field value
	public IJavaStatement getStatement()
	{
		return m_statement;
	}

	// public operation to set the embedded private field value
	public void setStatement(IJavaStatement p_statement)
	{
		// consistency check (field must be non null!)
		assert(p_statement != null);

		// instantiate the member variable
		m_statement = p_statement;

		// set the parent of the parameter passed
		p_statement.setParent(this);
	}

	// default constructor
	public JavaSetForLoop()
	{
		super();
		m_VarName = null;
		m_Set = null;
		m_statement = null;
	}

	// auxiliary constructor
	public JavaSetForLoop(
		String p_VarName,
		IJavaExpression p_Set,
		IJavaStatement p_statement
	) {
		super();
		setVarName(p_VarName);
		setSet(p_Set);
		setStatement(p_statement);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitSetForLoop(this); }

	// the identity function
	public String identify() { return "JavaSetForLoop"; }
}
