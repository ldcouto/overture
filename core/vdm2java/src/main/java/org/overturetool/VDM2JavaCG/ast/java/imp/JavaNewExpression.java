// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

// import java collection types
import java.util.*;

public class JavaNewExpression extends JavaExpression implements IJavaNewExpression
{
	// private member variable (ClassName)
	private IJavaExpression m_ClassName = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getClassName()
	{
		return m_ClassName;
	}

	// public operation to set the embedded private field value
	public void setClassName(IJavaExpression p_ClassName)
	{
		// consistency check (field must be non null!)
		assert(p_ClassName != null);

		// instantiate the member variable
		m_ClassName = p_ClassName;

		// set the parent of the parameter passed
		p_ClassName.setParent(this);
	}

	// private member variable (expressions)
	private List<IJavaExpression> m_expressions = new Vector<IJavaExpression>();

	// public operation to retrieve the embedded private field value
	public List<IJavaExpression> getExpressions()
	{
		return m_expressions;
	}

	// public operation to set the embedded private field value
	public void setExpressions(List<IJavaExpression> p_expressions)
	{
		// consistency check (field must be non null!)
		assert(p_expressions != null);

		// instantiate the member variable
		m_expressions = p_expressions;

		// set the parent of each element in the sequence parameter passed
		for (IJavaNode lnode: p_expressions) lnode.setParent(this);
	}

	// default constructor
	public JavaNewExpression()
	{
		super();
		m_ClassName = null;
		m_expressions = null;
	}

	// auxiliary constructor
	public JavaNewExpression(
		IJavaExpression p_ClassName,
		List<IJavaExpression> p_expressions
	) {
		super();
		setClassName(p_ClassName);
		setExpressions(p_expressions);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitNewExpression(this); }

	// the identity function
	public String identify() { return "JavaNewExpression"; }
}
