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



public class XmlData {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=data KEEP=NO
  public String data = null;
// ***** VDMTOOLS END Name=data


// ***** VDMTOOLS START Name=vdm_init_XmlData KEEP=NO
  private void vdm_init_XmlData () throws CGException {
    try {
      data = new String("");
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=vdm_init_XmlData


// ***** VDMTOOLS START Name=XmlData KEEP=NO
  public XmlData () throws CGException {
    vdm_init_XmlData();
  }
// ***** VDMTOOLS END Name=XmlData


// ***** VDMTOOLS START Name=XmlData#1|String KEEP=NO
  public XmlData (final String pdata) throws CGException {

    vdm_init_XmlData();
    data = UTIL.ConvertToString(UTIL.clone(pdata));
  }
// ***** VDMTOOLS END Name=XmlData#1|String


// ***** VDMTOOLS START Name=accept#1|XmlVisitor KEEP=NO
  public void accept (final XmlVisitor pxv) throws CGException {
    pxv.VisitXmlData((XmlData) this);
  }
// ***** VDMTOOLS END Name=accept#1|XmlVisitor

}
;
