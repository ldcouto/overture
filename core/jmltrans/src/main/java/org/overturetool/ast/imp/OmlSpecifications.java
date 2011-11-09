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



public class OmlSpecifications extends OmlNode implements IOmlSpecifications {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=ivClassList KEEP=NO
  private Vector ivClassList = null;
// ***** VDMTOOLS END Name=ivClassList


// ***** VDMTOOLS START Name=OmlSpecifications KEEP=NO
  public OmlSpecifications () throws CGException {
    try {
      ivClassList = new Vector();
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=OmlSpecifications


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("Specifications");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept KEEP=NO
  public void accept (final IOmlVisitor pVisitor) throws CGException {
    pVisitor.visitSpecifications((IOmlSpecifications) this);
  }
// ***** VDMTOOLS END Name=accept


// ***** VDMTOOLS START Name=OmlSpecifications KEEP=NO
  public OmlSpecifications (final Vector p1) throws CGException {

    try {
      ivClassList = new Vector();
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    setClassList(p1);
  }
// ***** VDMTOOLS END Name=OmlSpecifications


// ***** VDMTOOLS START Name=OmlSpecifications KEEP=NO
  public OmlSpecifications (final Vector p1, final Long line, final Long column) throws CGException {

    try {
      ivClassList = new Vector();
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    {

      setClassList(p1);
      setPosition(line, column);
    }
  }
// ***** VDMTOOLS END Name=OmlSpecifications


// ***** VDMTOOLS START Name=init KEEP=NO
  public void init (final HashMap data) throws CGException {

    String fname = new String("class_list");
    Boolean cond_4 = null;
    cond_4 = new Boolean(data.containsKey(fname));
    if (cond_4.booleanValue()) 
      setClassList((Vector) data.get(fname));
  }
// ***** VDMTOOLS END Name=init


// ***** VDMTOOLS START Name=getClassList KEEP=NO
  public Vector getClassList () throws CGException {
    return ivClassList;
  }
// ***** VDMTOOLS END Name=getClassList


// ***** VDMTOOLS START Name=setClassList KEEP=NO
  public void setClassList (final Vector parg) throws CGException {
    ivClassList = (Vector) UTIL.ConvertToList(UTIL.clone(parg));
  }
// ***** VDMTOOLS END Name=setClassList


// ***** VDMTOOLS START Name=addClassList KEEP=NO
  public void addClassList (final IOmlNode parg) throws CGException {
    ivClassList.add(parg);
  }
// ***** VDMTOOLS END Name=addClassList

  public String toString() {
	  String lst = new String();
	  int i, size = this.ivClassList.size();
	  
	  for(i=0; i<size; i++) {
		  lst += this.ivClassList.get(i).toString() + "\n\n";
	  }
	  return lst;
  }
}
;
