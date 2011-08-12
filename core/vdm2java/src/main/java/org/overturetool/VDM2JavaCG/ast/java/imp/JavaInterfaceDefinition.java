// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaInterfaceDefinition extends JavaDefinition implements IJavaInterfaceDefinition
{
	// private member variable (accessdefinition)
	private IJavaAccessDefinition m_accessdefinition = null;

	// public operation to retrieve the embedded private field value
	public IJavaAccessDefinition getAccessdefinition()
	{
		return m_accessdefinition;
	}

	// public operation to set the embedded private field value
	public void setAccessdefinition(IJavaAccessDefinition p_accessdefinition)
	{
		// consistency check (field must be non null!)
		assert(p_accessdefinition != null);

		// instantiate the member variable
		m_accessdefinition = p_accessdefinition;

		// set the parent of the parameter passed
		p_accessdefinition.setParent(this);
	}

	// private member variable (modifiers)
	private IJavaModifier m_modifiers = null;

	// public operation to retrieve the embedded private field value
	public IJavaModifier getModifiers()
	{
		return m_modifiers;
	}

	// public operation to set the embedded private field value
	public void setModifiers(IJavaModifier p_modifiers)
	{
		// consistency check (field must be non null!)
		assert(p_modifiers != null);

		// instantiate the member variable
		m_modifiers = p_modifiers;

		// set the parent of the parameter passed
		p_modifiers.setParent(this);
	}

	// private member variable (identifier)
	private IJavaIdentifier m_identifier = null;

	// public operation to retrieve the embedded private field value
	public IJavaIdentifier getIdentifier()
	{
		return m_identifier;
	}

	// public operation to set the embedded private field value
	public void setIdentifier(IJavaIdentifier p_identifier)
	{
		// consistency check (field must be non null!)
		assert(p_identifier != null);

		// instantiate the member variable
		m_identifier = p_identifier;

		// set the parent of the parameter passed
		p_identifier.setParent(this);
	}

	// private member variable (inheritance_clause)
	private IJavaInheritanceClause m_inheritance_clause = null;

	// public operation to check optional type status
	public boolean hasInheritanceClause() { return (m_inheritance_clause != null); };

	// public operation to retrieve the embedded private field value
	public IJavaInheritanceClause getInheritanceClause()
	{
		return m_inheritance_clause;
	}

	// public operation to set the embedded private field value
	public void setInheritanceClause(IJavaInheritanceClause p_inheritance_clause)
	{
		// instantiate the member variable
		m_inheritance_clause = p_inheritance_clause;

		// set the parent of the parameter passed
		p_inheritance_clause.setParent(this);
	}

	// private member variable (body)
	private IJavaDefinitionList m_body = null;

	// public operation to retrieve the embedded private field value
	public IJavaDefinitionList getBody()
	{
		return m_body;
	}

	// public operation to set the embedded private field value
	public void setBody(IJavaDefinitionList p_body)
	{
		// consistency check (field must be non null!)
		assert(p_body != null);

		// instantiate the member variable
		m_body = p_body;

		// set the parent of the parameter passed
		p_body.setParent(this);
	}

	// default constructor
	public JavaInterfaceDefinition()
	{
		super();
		m_accessdefinition = null;
		m_modifiers = null;
		m_identifier = null;
		m_inheritance_clause = null;
		m_body = null;
	}

	// auxiliary constructor
	public JavaInterfaceDefinition(
		IJavaAccessDefinition p_accessdefinition,
		IJavaModifier p_modifiers,
		IJavaIdentifier p_identifier,
		IJavaInheritanceClause p_inheritance_clause,
		IJavaDefinitionList p_body
	) {
		super();
		setAccessdefinition(p_accessdefinition);
		setModifiers(p_modifiers);
		setIdentifier(p_identifier);
		setInheritanceClause(p_inheritance_clause);
		setBody(p_body);
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitInterfaceDefinition(this); }

	// the identity function
	public String identify() { return "JavaInterfaceDefinition"; }
}
