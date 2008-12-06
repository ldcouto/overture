//
// THIS FILE IS AUTOMATICALLY GENERATED!!
//
// Generated at 2008-11-08 by the VDM++ to JAVA Code Generator
// (v8.1.1b - Wed 29-Oct-2008 09:29:09)
//
// Supported compilers: jdk 1.4/1.5/1.6
//

// ***** VDMTOOLS START Name=HeaderComment KEEP=NO
// ***** VDMTOOLS END Name=HeaderComment

// ***** VDMTOOLS START Name=package KEEP=NO
package org.overturetool.umltrans;

// ***** VDMTOOLS END Name=package

// ***** VDMTOOLS START Name=imports KEEP=NO

import jp.co.csk.vdm.toolbox.VDM.*;
import java.util.*;
import jp.co.csk.vdm.toolbox.VDM.quotes.*;
// ***** VDMTOOLS END Name=imports



public class UmlClassType extends IUmlClassType {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=ivId KEEP=NO
  private String ivId = null;
// ***** VDMTOOLS END Name=ivId

// ***** VDMTOOLS START Name=ivFieldList KEEP=NO
  private Vector ivFieldList = null;
// ***** VDMTOOLS END Name=ivFieldList


// ***** VDMTOOLS START Name=vdm_init_UmlClassType KEEP=NO
  private void vdm_init_UmlClassType () throws CGException {
    try {

      ivId = UTIL.ConvertToString(new String());
      ivFieldList = new Vector();
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=vdm_init_UmlClassType


// ***** VDMTOOLS START Name=UmlClassType KEEP=NO
  public UmlClassType () throws CGException {
    vdm_init_UmlClassType();
  }
// ***** VDMTOOLS END Name=UmlClassType


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("ClassType");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept#1|IUmlVisitor KEEP=NO
  public void accept (final IUmlVisitor pVisitor) throws CGException {
    pVisitor.visitClassType((IUmlClassType) this);
  }
// ***** VDMTOOLS END Name=accept#1|IUmlVisitor


// ***** VDMTOOLS START Name=UmlClassType#2|String|Vector KEEP=NO
  public UmlClassType (final String p1, final Vector p2) throws CGException {

    vdm_init_UmlClassType();
    {

      setId(p1);
      setFieldList(p2);
    }
  }
// ***** VDMTOOLS END Name=UmlClassType#2|String|Vector


// ***** VDMTOOLS START Name=UmlClassType#4|String|Vector|Integer|Integer KEEP=NO
  public UmlClassType (final String p1, final Vector p2, final Integer line, final Integer column) throws CGException {

    vdm_init_UmlClassType();
    {

      setId(p1);
      setFieldList(p2);
      setPosition(line, column);
    }
  }
// ***** VDMTOOLS END Name=UmlClassType#4|String|Vector|Integer|Integer


// ***** VDMTOOLS START Name=init#1|HashMap KEEP=NO
  public void init (final HashMap data) throws CGException {

    {

      String fname = new String("id");
      Boolean cond_4 = null;
      cond_4 = new Boolean(data.containsKey(fname));
      if (cond_4.booleanValue()) 
        setId(UTIL.ConvertToString(data.get(fname)));
    }
    {

      String fname = new String("field_list");
      Boolean cond_13 = null;
      cond_13 = new Boolean(data.containsKey(fname));
      if (cond_13.booleanValue()) 
        setFieldList((Vector) data.get(fname));
    }
  }
// ***** VDMTOOLS END Name=init#1|HashMap


// ***** VDMTOOLS START Name=getId KEEP=NO
  public String getId () throws CGException {
    return ivId;
  }
// ***** VDMTOOLS END Name=getId


// ***** VDMTOOLS START Name=setId#1|String KEEP=NO
  public void setId (final String parg) throws CGException {
    ivId = UTIL.ConvertToString(UTIL.clone(parg));
  }
// ***** VDMTOOLS END Name=setId#1|String


// ***** VDMTOOLS START Name=getFieldList KEEP=NO
  public Vector getFieldList () throws CGException {
    return ivFieldList;
  }
// ***** VDMTOOLS END Name=getFieldList


// ***** VDMTOOLS START Name=setFieldList#1|Vector KEEP=NO
  public void setFieldList (final Vector parg) throws CGException {
    ivFieldList = (Vector) UTIL.ConvertToList(UTIL.clone(parg));
  }
// ***** VDMTOOLS END Name=setFieldList#1|Vector


// ***** VDMTOOLS START Name=addFieldList#1|IUmlNode KEEP=NO
  public void addFieldList (final IUmlNode parg) throws CGException {
    ivFieldList.add(parg);
  }
// ***** VDMTOOLS END Name=addFieldList#1|IUmlNode

}
;
