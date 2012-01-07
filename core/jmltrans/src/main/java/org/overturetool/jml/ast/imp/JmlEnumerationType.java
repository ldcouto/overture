//
// THIS FILE IS AUTOMATICALLY GENERATED!!
//
// Generated at Mon 07-Jul-2008 by the VDM++ to JAVA Code Generator
// (v8.1.1b - Fri 06-Jun-2008 09:02:11)
//
// Supported compilers:
// jdk1.4
//

// ***** VDMTOOLS START Name=HeaderComment KEEP=NO
// ***** VDMTOOLS END Name=HeaderComment

// ***** VDMTOOLS START Name=package KEEP=NO
package org.overturetool.jml.ast.imp;

// ***** VDMTOOLS END Name=package

// ***** VDMTOOLS START Name=imports KEEP=YES

import jp.co.csk.vdm.toolbox.VDM.*;
import java.util.*;
import org.overturetool.jml.ast.itf.*;
@SuppressWarnings(all) 
// ***** VDMTOOLS END Name=imports



public class JmlEnumerationType extends JmlType implements IJmlEnumerationType {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=ivEnumLiteral KEEP=NO
  private IJmlEnumLiteral ivEnumLiteral = null;
// ***** VDMTOOLS END Name=ivEnumLiteral


// ***** VDMTOOLS START Name=JmlEnumerationType KEEP=NO
  public JmlEnumerationType () throws CGException {
    try {
      ivEnumLiteral = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=JmlEnumerationType


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("EnumerationType");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept KEEP=NO
  public void accept (final IJmlVisitor pVisitor) throws CGException {
    pVisitor.visitEnumerationType((IJmlEnumerationType) this);
  }
// ***** VDMTOOLS END Name=accept


// ***** VDMTOOLS START Name=JmlEnumerationType KEEP=NO
  public JmlEnumerationType (final IJmlEnumLiteral p1) throws CGException {

    try {
      ivEnumLiteral = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    setEnumLiteral((IJmlEnumLiteral) p1);
  }
// ***** VDMTOOLS END Name=JmlEnumerationType


// ***** VDMTOOLS START Name=JmlEnumerationType KEEP=NO
  public JmlEnumerationType (final IJmlEnumLiteral p1, final Long line, final Long column) throws CGException {

    try {
      ivEnumLiteral = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    {

      setEnumLiteral((IJmlEnumLiteral) p1);
      setPosition(line, column);
    }
  }
// ***** VDMTOOLS END Name=JmlEnumerationType


// ***** VDMTOOLS START Name=init KEEP=NO
  public void init (final HashMap data) throws CGException {

    String fname = new String("enum_literal");
    Boolean cond_4 = null;
    cond_4 = new Boolean(data.containsKey(fname));
    if (cond_4.booleanValue()) 
      setEnumLiteral((IJmlEnumLiteral) data.get(fname));
  }
// ***** VDMTOOLS END Name=init


// ***** VDMTOOLS START Name=getEnumLiteral KEEP=NO
  public IJmlEnumLiteral getEnumLiteral () throws CGException {
    return (IJmlEnumLiteral) ivEnumLiteral;
  }
// ***** VDMTOOLS END Name=getEnumLiteral


// ***** VDMTOOLS START Name=setEnumLiteral KEEP=NO
  public void setEnumLiteral (final IJmlEnumLiteral parg) throws CGException {
    ivEnumLiteral = (IJmlEnumLiteral) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setEnumLiteral
  
  public String toString() {
	  
	  return this.ivEnumLiteral.toString();
	  
  }

}
;