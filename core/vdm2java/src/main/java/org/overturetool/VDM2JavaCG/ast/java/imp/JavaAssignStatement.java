// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaAssignStatement extends JavaStatement implements IJavaAssignStatement
{
	// private member variable (state_designator)
	private IJavaStateDesignator m_state_designator = null;

	// public operation to retrieve the embedded private field value
	public IJavaStateDesignator getStateDesignator()
	{
		return m_state_designator;
	}

	// public operation to set the embedded private field value
	public void setStateDesignator(IJavaStateDesignator p_state_designator)
	{
		// consistency check (field must be non null!)
		assert(p_state_designator != null);

		// instantiate the member variable
		m_state_designator = p_state_designator;

		// set the parent of the parameter passed
		p_state_designator.setParent(this);
	}

	// private member variable (expression)
	private IJavaExpression m_expression = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getExpression()
	{
		return m_expression;
	}

	// public operation to set the embedded private field value
	public void setExpression(IJavaExpression p_expression)
	{
		// consistency check (field must be non null!)
		assert(p_expression != null);

		// instantiate the member variable
		m_expression = p_expression;

		// set the parent of the parameter passed
		p_expression.setParent(this);
	}

	// default constructor
	public JavaAssignStatement()
	{
		super();
		m_state_designator = null;
		m_expression = null;
	}

	// auxiliary constructor
	public JavaAssignStatement(
		IJavaStateDesignator p_state_designator,
		IJavaExpression p_expression
	) {
		super();
		setStateDesignator(p_state_designator);
		setExpression(p_expression);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitAssignStatement(this); }

	// the identity function
	public String identify() { return "JavaAssignStatement"; }
}