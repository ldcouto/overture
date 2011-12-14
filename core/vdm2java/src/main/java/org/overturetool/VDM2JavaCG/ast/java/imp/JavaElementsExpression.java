// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaElementsExpression extends JavaExpression implements IJavaElementsExpression
{
	// private member variable (seqname)
	private IJavaExpression m_seqname = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getSeqname()
	{
		return m_seqname;
	}

	// public operation to set the embedded private field value
	public void setSeqname(IJavaExpression p_seqname)
	{
		// consistency check (field must be non null!)
		assert(p_seqname != null);

		// instantiate the member variable
		m_seqname = p_seqname;

		// set the parent of the parameter passed
		p_seqname.setParent(this);
	}

	// default constructor
	public JavaElementsExpression()
	{
		super();
		m_seqname = null;
	}

	// auxiliary constructor
	public JavaElementsExpression(
		IJavaExpression p_seqname
	) {
		super();
		setSeqname(p_seqname);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitElementsExpression(this); }

	// the identity function
	public String identify() { return "JavaElementsExpression"; }
}