// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

// import java collection types
import java.util.*;

public class JavaProductType extends JavaType implements IJavaProductType
{
	// private member variable (Types)
	private List<IJavaType> m_Types = new Vector<IJavaType>();

	// public operation to retrieve the embedded private field value
	public List<IJavaType> getTypes()
	{
		return m_Types;
	}

	// public operation to set the embedded private field value
	public void setTypes(List<IJavaType> p_Types)
	{
		// consistency check (field must be non null!)
		assert(p_Types != null);

		// instantiate the member variable
		m_Types = p_Types;

		// set the parent of each element in the sequence parameter passed
		for (IJavaNode lnode: p_Types) lnode.setParent(this);
	}

	// default constructor
	public JavaProductType()
	{
		super();
		m_Types = null;
	}

	// auxiliary constructor
	public JavaProductType(
		List<IJavaType> p_Types
	) {
		super();
		setTypes(p_Types);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitProductType(this); }

	// the identity function
	public String identify() { return "JavaProductType"; }
}