// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaVectorApplication extends JavaExpression implements IJavaVectorApplication
{
	// private member variable (Name)
	private IJavaExpression m_Name = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getName()
	{
		return m_Name;
	}

	// public operation to set the embedded private field value
	public void setName(IJavaExpression p_Name)
	{
		// consistency check (field must be non null!)
		assert(p_Name != null);

		// instantiate the member variable
		m_Name = p_Name;

		// set the parent of the parameter passed
		p_Name.setParent(this);
	}

	// private member variable (at)
	private IJavaExpression m_at = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getAt()
	{
		return m_at;
	}

	// public operation to set the embedded private field value
	public void setAt(IJavaExpression p_at)
	{
		// consistency check (field must be non null!)
		assert(p_at != null);

		// instantiate the member variable
		m_at = p_at;

		// set the parent of the parameter passed
		p_at.setParent(this);
	}

	// default constructor
	public JavaVectorApplication()
	{
		super();
		m_Name = null;
		m_at = null;
	}

	// auxiliary constructor
	public JavaVectorApplication(
		IJavaExpression p_Name,
		IJavaExpression p_at
	) {
		super();
		setName(p_Name);
		setAt(p_at);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitVectorApplication(this); }

	// the identity function
	public String identify() { return "JavaVectorApplication"; }
}