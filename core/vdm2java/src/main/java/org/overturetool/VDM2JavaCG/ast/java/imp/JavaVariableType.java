// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaVariableType extends JavaNode implements IJavaVariableType
{
	// default constructor
	public JavaVariableType()
	{
		super();
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitVariableType(this); }

	// the identity function
	public String identify() { return "JavaVariableType"; }
}