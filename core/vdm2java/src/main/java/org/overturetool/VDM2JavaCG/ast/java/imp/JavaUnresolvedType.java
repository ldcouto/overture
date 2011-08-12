// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaUnresolvedType extends JavaType implements IJavaUnresolvedType
{
	// private member variable (typename)
	private IJavaIdentifier m_typename = null;

	// public operation to retrieve the embedded private field value
	public IJavaIdentifier getTypename()
	{
		return m_typename;
	}

	// public operation to set the embedded private field value
	public void setTypename(IJavaIdentifier p_typename)
	{
		// consistency check (field must be non null!)
		assert(p_typename != null);

		// instantiate the member variable
		m_typename = p_typename;

		// set the parent of the parameter passed
		p_typename.setParent(this);
	}

	// default constructor
	public JavaUnresolvedType()
	{
		super();
		m_typename = null;
	}

	// auxiliary constructor
	public JavaUnresolvedType(
		IJavaIdentifier p_typename
	) {
		super();
		setTypename(p_typename);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitUnresolvedType(this); }

	// the identity function
	public String identify() { return "JavaUnresolvedType"; }
}
