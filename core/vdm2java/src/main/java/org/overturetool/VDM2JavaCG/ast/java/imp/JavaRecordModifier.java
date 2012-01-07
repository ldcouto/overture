// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

// import java collection types
import java.util.*;

public class JavaRecordModifier extends JavaRecordExpression implements IJavaRecordModifier
{
	// private member variable (RecordName)
	private IJavaExpression m_RecordName = null;

	// public operation to retrieve the embedded private field value
	public IJavaExpression getRecordName()
	{
		return m_RecordName;
	}

	// public operation to set the embedded private field value
	public void setRecordName(IJavaExpression p_RecordName)
	{
		// consistency check (field must be non null!)
		assert(p_RecordName != null);

		// instantiate the member variable
		m_RecordName = p_RecordName;

		// set the parent of the parameter passed
		p_RecordName.setParent(this);
	}

	// private member variable (modification)
	private List<IJavaRecordModification> m_modification = new Vector<IJavaRecordModification>();

	// public operation to retrieve the embedded private field value
	public List<IJavaRecordModification> getModification()
	{
		return m_modification;
	}

	// public operation to set the embedded private field value
	public void setModification(List<IJavaRecordModification> p_modification)
	{
		// consistency check (field must be non null!)
		assert(p_modification != null);

		// instantiate the member variable
		m_modification = p_modification;

		// set the parent of each element in the sequence parameter passed
		for (IJavaNode lnode: p_modification) lnode.setParent(this);
	}

	// default constructor
	public JavaRecordModifier()
	{
		super();
		m_RecordName = null;
		m_modification = null;
	}

	// auxiliary constructor
	public JavaRecordModifier(
		IJavaExpression p_RecordName,
		List<IJavaRecordModification> p_modification
	) {
		super();
		setRecordName(p_RecordName);
		setModification(p_modification);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitRecordModifier(this); }

	// the identity function
	public String identify() { return "JavaRecordModifier"; }
}