//
// THIS FILE IS AUTOMATICALLY GENERATED!!
//
// Generated at 2008-11-08 by the VDM++ to JAVA Code Generator
// (v8.1.1b - Fri 24-Oct-2008 08:59:25)
//
// Supported compilers: jdk 1.4/1.5/1.6
//

// ***** VDMTOOLS START Name=HeaderComment KEEP=NO
// ***** VDMTOOLS END Name=HeaderComment

// ***** VDMTOOLS START Name=package KEEP=NO
package org.overturetool.potrans;

// ***** VDMTOOLS END Name=package

// ***** VDMTOOLS START Name=imports KEEP=YES

import jp.co.csk.vdm.toolbox.VDM.*;
import java.util.*;
// ***** VDMTOOLS END Name=imports



public class HolReferedFunction extends HolFunction {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=reference KEEP=NO
  private HolIdentifier reference = null;
// ***** VDMTOOLS END Name=reference


// ***** VDMTOOLS START Name=vdm_init_HolReferedFunction KEEP=NO
  private void vdm_init_HolReferedFunction () throws CGException {}
// ***** VDMTOOLS END Name=vdm_init_HolReferedFunction


// ***** VDMTOOLS START Name=HolReferedFunction KEEP=NO
  public HolReferedFunction () throws CGException {
    vdm_init_HolReferedFunction();
  }
// ***** VDMTOOLS END Name=HolReferedFunction


// ***** VDMTOOLS START Name=HolReferedFunction#1|String KEEP=NO
  public HolReferedFunction (final String newRef) throws CGException {

    vdm_init_HolReferedFunction();
    reference = (HolIdentifier) UTIL.clone(new HolIdentifier(newRef));
  }
// ***** VDMTOOLS END Name=HolReferedFunction#1|String


// ***** VDMTOOLS START Name=HolReferedFunction#1|HolIdentifier KEEP=NO
  public HolReferedFunction (final HolIdentifier newRef) throws CGException {

    vdm_init_HolReferedFunction();
    reference = (HolIdentifier) UTIL.clone(newRef);
  }
// ***** VDMTOOLS END Name=HolReferedFunction#1|HolIdentifier


// ***** VDMTOOLS START Name=requires KEEP=NO
  public HashSet requires () throws CGException {

    HashSet rexpr_1 = new HashSet();
    rexpr_1 = reference.requires();
    return rexpr_1;
  }
// ***** VDMTOOLS END Name=requires


// ***** VDMTOOLS START Name=print KEEP=NO
  public String print () throws CGException {

    String rexpr_1 = null;
    rexpr_1 = reference.print();
    return rexpr_1;
  }
// ***** VDMTOOLS END Name=print

}
;