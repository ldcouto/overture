// this file is automatically generated by treegen. do not modify!

package org.overture.tools.treegen.ast.imp;

// import the abstract tree interfaces
import org.overture.tools.treegen.ast.itf.*;

// import java collection types
import java.util.*;

@SuppressWarnings("unchecked")
public class TreeGenAstClassDefinition extends TreeGenAstNode implements ITreeGenAstClassDefinition
{
	// default version identifier for serialize
	public static final long serialVersionUID = 1L;

	// private member variable (opts)
	private org.overture.tools.treegen.TreeGenOptions m_opts = null;

	// public operation to check optional type status
	public boolean hasOpts() { return (m_opts != null); };

	// public operation to retrieve the embedded private field value
	public org.overture.tools.treegen.TreeGenOptions getOpts()
	{
		return m_opts;
	}

	// public operation to set the embedded private field value
	public void setOpts(org.overture.tools.treegen.TreeGenOptions p_opts)
	{
		// instantiate the member variable
		m_opts = p_opts;
	}

	// private member variable (class_name)
	private String m_class_name = new String();

	// public operation to retrieve the embedded private field value
	public String getClassName()
	{
		return m_class_name;
	}

	// public operation to set the embedded private field value
	public void setClassName(String p_class_name)
	{
		// consistency check (field must be non null!)
		assert(p_class_name != null);

		// instantiate the member variable
		m_class_name = p_class_name;
	}

	// private member variable (super_class)
	private String m_super_class = new String();

	// public operation to retrieve the embedded private field value
	public String getSuperClass()
	{
		return m_super_class;
	}

	// public operation to set the embedded private field value
	public void setSuperClass(String p_super_class)
	{
		// consistency check (field must be non null!)
		assert(p_super_class != null);

		// instantiate the member variable
		m_super_class = p_super_class;
	}

	// private member variable (defs)
	private List m_defs = new Vector<ITreeGenAstDefinitions>();

	// public operation to retrieve the embedded private field value
	public List<ITreeGenAstDefinitions> getDefs()
	{
		return m_defs;
	}

	// public operation to set the embedded private field value
	public void setDefs(List<? extends ITreeGenAstDefinitions> p_defs)
	{
		// consistency check (field must be non null!)
		assert(p_defs != null);

		// instantiate the member variable
		m_defs = p_defs;

		// set the parent of each element in the sequence parameter passed
		for (ITreeGenAstNode lnode: p_defs) lnode.setParent(this);
	}

	// public operation to add an element to the collection
	public void addDefs(ITreeGenAstDefinitions p_defs)
	{
		// consistency check
		assert(p_defs != null);

		// add element to collection and set parent pointer (if applicable)
		m_defs.add(p_defs);
		p_defs.setParent(this);
	}

	// default constructor
	public TreeGenAstClassDefinition() { super(); }

	// auxiliary constructor
	public TreeGenAstClassDefinition(
		org.overture.tools.treegen.TreeGenOptions p_opts,
		String p_class_name,
		String p_super_class,
		List<? extends ITreeGenAstDefinitions> p_defs
	) {
		super();
		setOpts(p_opts);
		setClassName(p_class_name);
		setSuperClass(p_super_class);
		setDefs(p_defs);
	}

	// visitor support
	public void accept(ITreeGenAstVisitor pVisitor) { pVisitor.visitClassDefinition(this); }

	// the identity function
	public String identify() { return "TreeGenAstClassDefinition"; }

	// the toString function
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		buf.append("new "+identify()+"(");
		if (hasOpts()) {
			buf.append(convertToString(getOpts()));
		} else {
			buf.append("nil");
		}
		buf.append(",");
		buf.append(convertToString(getClassName()));
		buf.append(",");
		buf.append(convertToString(getSuperClass()));
		buf.append(",");
		buf.append(convertToString(getDefs()));
		buf.append(")");
		return buf.toString();
	}
}