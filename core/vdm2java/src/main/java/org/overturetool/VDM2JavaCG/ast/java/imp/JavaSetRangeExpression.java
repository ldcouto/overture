// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaSetRangeExpression extends JavaSetExpression implements IJavaSetRangeExpression
{
	// private member variable (head)
	private IJavaExpression m_head = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getHead()
	{
		return m_head;
	}

	// public operation to set the embedded private field value
	public void setHead(IJavaExpression p_head)
	{
		// consistency check (field must be non null!)
		assert(p_head != null);

		// instantiate the member variable
		m_head = p_head;

		// set the parent of the parameter passed
		p_head.setParent(this);
	}

	// private member variable (tail)
	private IJavaExpression m_tail = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getTail()
	{
		return m_tail;
	}

	// public operation to set the embedded private field value
	public void setTail(IJavaExpression p_tail)
	{
		// consistency check (field must be non null!)
		assert(p_tail != null);

		// instantiate the member variable
		m_tail = p_tail;

		// set the parent of the parameter passed
		p_tail.setParent(this);
	}

	// default constructor
	public JavaSetRangeExpression()
	{
		super();
		m_head = null;
		m_tail = null;
	}

	// auxiliary constructor
	public JavaSetRangeExpression(
		IJavaExpression p_head,
		IJavaExpression p_tail
	) {
		super();
		setHead(p_head);
		setTail(p_tail);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitSetRangeExpression(this); }

	// the identity function
	public String identify() { return "JavaSetRangeExpression"; }
}