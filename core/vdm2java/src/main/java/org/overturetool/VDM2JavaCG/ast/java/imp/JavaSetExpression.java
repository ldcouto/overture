// this file is automatically generated by treegen. do not modify!

package org.overturetool.VDM2JavaCG.ast.java.imp;

// import the abstract tree interfaces
import org.overturetool.VDM2JavaCG.ast.java.itf.*;

public class JavaSetExpression extends JavaExpression implements IJavaSetExpression
{
	// default constructor
	public JavaSetExpression()
	{
		super();
	}

	// visitor support
	public void accept(IJavaVisitor pVisitor) { pVisitor.visitSetExpression(this); }

	// the identity function
	public String identify() { return "JavaSetExpression"; }
}