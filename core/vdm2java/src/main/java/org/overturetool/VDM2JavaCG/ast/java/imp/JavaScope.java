// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaScope extends JavaNode implements IJavaScope
{
	// private member variable to store enumerated value
	private JavaScopeEnum m_enum;

	// public operation to set the enumeration value
	public void setEnum(JavaScopeEnum p_enum) { m_enum = p_enum; }

	// public member operation to query the quoted value
	public boolean isPUBLIC() { return (m_enum == JavaScopeEnum.EPUBLIC); }

	// public member operation to query the quoted value
	public boolean isPRIVATE() { return (m_enum == JavaScopeEnum.EPRIVATE); }

	// public member operation to query the quoted value
	public boolean isPROTECTED() { return (m_enum == JavaScopeEnum.EPROTECTED); }

	// default constructor
	public JavaScope()
	{
		super();
		m_enum = null;
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitScope(this); }

	// the identity function
	public String identify() { return m_enum.identify(); }
}