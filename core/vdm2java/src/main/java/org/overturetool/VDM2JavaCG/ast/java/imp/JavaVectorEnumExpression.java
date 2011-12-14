// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

// import java collection types
import java.util.*;

public class JavaVectorEnumExpression extends JavaExpression implements IJavaVectorEnumExpression
{
	// private member variable (elements)
	private List<IJavaExpression> m_elements = new Vector<IJavaExpression>();

	// public operation to retrieve the embedded private field value
	public List<IJavaExpression> getElements()
	{
		return m_elements;
	}

	// public operation to set the embedded private field value
	public void setElements(List<IJavaExpression> p_elements)
	{
		// consistency check (field must be non null!)
		assert(p_elements != null);

		// instantiate the member variable
		m_elements = p_elements;

		// set the parent of each element in the sequence parameter passed
		for (IJavaNode lnode: p_elements) lnode.setParent(this);
	}

	// default constructor
	public JavaVectorEnumExpression()
	{
		super();
		m_elements = null;
	}

	// auxiliary constructor
	public JavaVectorEnumExpression(
		List<IJavaExpression> p_elements
	) {
		super();
		setElements(p_elements);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitVectorEnumExpression(this); }

	// the identity function
	public String identify() { return "JavaVectorEnumExpression"; }
}