// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaMapRangeExpression extends JavaMapExpression implements IJavaMapRangeExpression
{
	// private member variable (mapname)
	private IJavaExpression m_mapname = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getMapname()
	{
		return m_mapname;
	}

	// public operation to set the embedded private field value
	public void setMapname(IJavaExpression p_mapname)
	{
		// consistency check (field must be non null!)
		assert(p_mapname != null);

		// instantiate the member variable
		m_mapname = p_mapname;

		// set the parent of the parameter passed
		p_mapname.setParent(this);
	}

	// default constructor
	public JavaMapRangeExpression()
	{
		super();
		m_mapname = null;
	}

	// auxiliary constructor
	public JavaMapRangeExpression(
		IJavaExpression p_mapname
	) {
		super();
		setMapname(p_mapname);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitMapRangeExpression(this); }

	// the identity function
	public String identify() { return "JavaMapRangeExpression"; }
}