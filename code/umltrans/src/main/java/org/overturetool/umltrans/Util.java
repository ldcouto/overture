//
// THIS FILE IS AUTOMATICALLY GENERATED!!
//
// Generated at 2009-02-27 by the VDM++ to JAVA Code Generator
// (v8.2b - Wed 18-Feb-2009 16:15:35)
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
// ***** VDMTOOLS END Name=imports



public class Util extends StdLib {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=writeType KEEP=NO
  private static Object writeType = new jp.co.csk.vdm.toolbox.VDM.quotes.start();
// ***** VDMTOOLS END Name=writeType

// ***** VDMTOOLS START Name=buf KEEP=NO
  private static String buf = new String("");
// ***** VDMTOOLS END Name=buf

// ***** VDMTOOLS START Name=outputFileName KEEP=NO
  public static String outputFileName = new String("tmp.xmi");
// ***** VDMTOOLS END Name=outputFileName


// ***** VDMTOOLS START Name=vdm_init_Util KEEP=NO
  private void vdm_init_Util () throws CGException {}
// ***** VDMTOOLS END Name=vdm_init_Util


// ***** VDMTOOLS START Name=Util KEEP=NO
  public Util () throws CGException {
    vdm_init_Util();
  }
// ***** VDMTOOLS END Name=Util


// ***** VDMTOOLS START Name=Put#1|String KEEP=NO
  static public void Put (final String pVal) throws CGException {

    String rhs_2 = null;
    rhs_2 = buf.concat(pVal);
    buf = UTIL.ConvertToString(UTIL.clone(rhs_2));
  }
// ***** VDMTOOLS END Name=Put#1|String


// ***** VDMTOOLS START Name=ViewBuf KEEP=NO
  static public void ViewBuf () throws CGException {
    Print(buf);
  }
// ***** VDMTOOLS END Name=ViewBuf


// ***** VDMTOOLS START Name=SaveBuf#1|String KEEP=NO
  static public void SaveBuf (final String fileName) throws CGException {

    SetFileName(fileName);
    PrintL(buf);
  }
// ***** VDMTOOLS END Name=SaveBuf#1|String


// ***** VDMTOOLS START Name=Clear KEEP=NO
  static public void Clear () throws CGException {
    buf = UTIL.ConvertToString(UTIL.clone(new String("")));
  }
// ***** VDMTOOLS END Name=Clear


// ***** VDMTOOLS START Name=Print#1|String KEEP=NO
  static public void Print (final String debugString) throws CGException {

    IOProxy file = (IOProxy) new IOProxy();
    file.print(debugString);
  }
// ***** VDMTOOLS END Name=Print#1|String


// ***** VDMTOOLS START Name=PrintL#1|String KEEP=NO
  static public void PrintL (final String line) throws CGException {

    IOProxy file = (IOProxy) new IOProxy();
    file.overwrite(outputFileName, line);
  }
// ***** VDMTOOLS END Name=PrintL#1|String


// ***** VDMTOOLS START Name=SetFileName#1|String KEEP=NO
  static public void SetFileName (final String name) throws CGException {

    outputFileName = UTIL.ConvertToString(UTIL.clone(name));
    writeType = UTIL.clone(new jp.co.csk.vdm.toolbox.VDM.quotes.start());
  }
// ***** VDMTOOLS END Name=SetFileName#1|String

}
;
