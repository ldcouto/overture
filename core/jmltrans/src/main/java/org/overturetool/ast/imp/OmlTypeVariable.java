//
// THIS FILE IS AUTOMATICALLY GENERATED!!
//
// Generated at Wed 11-Jun-2008 by the VDM++ to JAVA Code Generator
// (v8.0.1b - Mon 28-Jan-2008 10:17:36)
//
// Supported compilers:
// jdk1.4
//

// ***** VDMTOOLS START Name=HeaderComment KEEP=NO
// ***** VDMTOOLS END Name=HeaderComment

// ***** VDMTOOLS START Name=package KEEP=NO
package org.overturetool.ast.imp;

// ***** VDMTOOLS END Name=package

// ***** VDMTOOLS START Name=imports KEEP=YES

import jp.co.csk.vdm.toolbox.VDM.*;
import java.util.*;
import org.overturetool.ast.itf.*;
@SuppressWarnings(all) 
// ***** VDMTOOLS END Name=imports



public class OmlTypeVariable extends OmlType implements IOmlTypeVariable {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=ivIdentifier KEEP=NO
  private String ivIdentifier = null;
// ***** VDMTOOLS END Name=ivIdentifier


// ***** VDMTOOLS START Name=OmlTypeVariable KEEP=NO
  public OmlTypeVariable () throws CGException {
    try {
      ivIdentifier = UTIL.ConvertToString(new String());
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=OmlTypeVariable


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("TypeVariable");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept KEEP=NO
  public void accept (final IOmlVisitor pVisitor) throws CGException {
    pVisitor.visitTypeVariable((IOmlTypeVariable) this);
  }
// ***** VDMTOOLS END Name=accept


// ***** VDMTOOLS START Name=OmlTypeVariable KEEP=NO
  public OmlTypeVariable (final String p1) throws CGException {

    try {
      ivIdentifier = UTIL.ConvertToString(new String());
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    setIdentifier(p1);
  }
// ***** VDMTOOLS END Name=OmlTypeVariable


// ***** VDMTOOLS START Name=OmlTypeVariable KEEP=NO
  public OmlTypeVariable (final String p1, final Long line, final Long column) throws CGException {

    try {
      ivIdentifier = UTIL.ConvertToString(new String());
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    {

      setIdentifier(p1);
      setPosition(line, column);
    }
  }
// ***** VDMTOOLS END Name=OmlTypeVariable


// ***** VDMTOOLS START Name=init KEEP=NO
  public void init (final HashMap data) throws CGException {

    String fname = new String("identifier");
    Boolean cond_4 = null;
    cond_4 = new Boolean(data.containsKey(fname));
    if (cond_4.booleanValue()) 
      setIdentifier(UTIL.ConvertToString(data.get(fname)));
  }
// ***** VDMTOOLS END Name=init


// ***** VDMTOOLS START Name=getIdentifier KEEP=NO
  public String getIdentifier () throws CGException {
    return ivIdentifier;
  }
// ***** VDMTOOLS END Name=getIdentifier


// ***** VDMTOOLS START Name=setIdentifier KEEP=NO
  public void setIdentifier (final String parg) throws CGException {
    ivIdentifier = UTIL.ConvertToString(UTIL.clone(parg));
  }
// ***** VDMTOOLS END Name=setIdentifier

  public String toString() {
	  return "@" + this.ivIdentifier;
  }
}
;
