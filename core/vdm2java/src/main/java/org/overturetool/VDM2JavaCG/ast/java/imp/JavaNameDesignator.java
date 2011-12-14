// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaNameDesignator extends JavaObjectDesignator implements IJavaNameDesignator
{
	// private member variable (name)
	private IJavaIdentifier m_name = null;

	// public operation to retrieve the embedded private field value
	public IJavaIdentifier getName()
	{
		return m_name;
	}

	// public operation to set the embedded private field value
	public void setName(IJavaIdentifier p_name)
	{
		// consistency check (field must be non null!)
		assert(p_name != null);

		// instantiate the member variable
		m_name = p_name;

		// set the parent of the parameter passed
		p_name.setParent(this);
	}

	// default constructor
	public JavaNameDesignator()
	{
		super();
		m_name = null;
	}

	// auxiliary constructor
	public JavaNameDesignator(
		IJavaIdentifier p_name
	) {
		super();
		setName(p_name);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitNameDesignator(this); }

	// the identity function
	public String identify() { return "JavaNameDesignator"; }
}