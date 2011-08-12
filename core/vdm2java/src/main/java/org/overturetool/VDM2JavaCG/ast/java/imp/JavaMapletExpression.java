// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaMapletExpression extends JavaMapExpression implements IJavaMapletExpression
{
	// private member variable (left)
	private IJavaExpression m_left = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getLeft()
	{
		return m_left;
	}

	// public operation to set the embedded private field value
	public void setLeft(IJavaExpression p_left)
	{
		// consistency check (field must be non null!)
		assert(p_left != null);

		// instantiate the member variable
		m_left = p_left;

		// set the parent of the parameter passed
		p_left.setParent(this);
	}

	// private member variable (right)
	private IJavaExpression m_right = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getRight()
	{
		return m_right;
	}

	// public operation to set the embedded private field value
	public void setRight(IJavaExpression p_right)
	{
		// consistency check (field must be non null!)
		assert(p_right != null);

		// instantiate the member variable
		m_right = p_right;

		// set the parent of the parameter passed
		p_right.setParent(this);
	}

	// default constructor
	public JavaMapletExpression()
	{
		super();
		m_left = null;
		m_right = null;
	}

	// auxiliary constructor
	public JavaMapletExpression(
		IJavaExpression p_left,
		IJavaExpression p_right
	) {
		super();
		setLeft(p_left);
		setRight(p_right);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitMapletExpression(this); }

	// the identity function
	public String identify() { return "JavaMapletExpression"; }
}
