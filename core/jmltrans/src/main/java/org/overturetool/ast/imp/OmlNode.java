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



public class OmlNode implements IOmlNode {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=ivInfo KEEP=NO
  private HashMap ivInfo = new HashMap();
// ***** VDMTOOLS END Name=ivInfo

// ***** VDMTOOLS START Name=ivLine KEEP=NO
  private Long ivLine = null;
// ***** VDMTOOLS END Name=ivLine

// ***** VDMTOOLS START Name=ivColumn KEEP=NO
  private Long ivColumn = null;
// ***** VDMTOOLS END Name=ivColumn

// ***** VDMTOOLS START Name=prefix KEEP=NO
  public static final String prefix = new String("Oml");
// ***** VDMTOOLS END Name=prefix


// ***** VDMTOOLS START Name=IOmlNode KEEP=NO
  public OmlNode () throws CGException {
    try {

      ivInfo = new HashMap();
      ivLine = new Long(0);
      ivColumn = new Long(0);
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=IOmlNode


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("Node");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept KEEP=NO
  public void accept (final IOmlVisitor pVisitor) throws CGException {
    pVisitor.visitNode((IOmlNode) this);
  }
// ***** VDMTOOLS END Name=accept


// ***** VDMTOOLS START Name=getContextInfo KEEP=NO
  public IOmlContextInfo getContextInfo (final Long pci) throws CGException {
    return (IOmlContextInfo) (IOmlContextInfo) ivInfo.get(pci);
  }
// ***** VDMTOOLS END Name=getContextInfo


// ***** VDMTOOLS START Name=getContextInfoCount KEEP=NO
  public Long getContextInfoCount () throws CGException {

    Long rexpr_1 = null;
    HashSet unArg_2 = new HashSet();
    unArg_2.clear();
    unArg_2.addAll(ivInfo.keySet());
    rexpr_1 = new Long(unArg_2.size());
    return rexpr_1;
  }
// ***** VDMTOOLS END Name=getContextInfoCount


// ***** VDMTOOLS START Name=addContextInfo KEEP=NO
  public Long addContextInfo (final IOmlContextInfo pci) throws CGException {

    Long res = null;
    Long var1_2 = null;
    HashSet unArg_3 = new HashSet();
    unArg_3.clear();
    unArg_3.addAll(ivInfo.keySet());
    var1_2 = new Long(unArg_3.size());
    res = new Long(var1_2.intValue() + new Long(1).intValue());
    HashMap rhs_6 = new HashMap();
    HashMap var2_8 = new HashMap();
    var2_8 = new HashMap();
    var2_8.put(res, pci);
    {

      HashMap m1_15 = (HashMap) ivInfo.clone();
      HashMap m2_16 = var2_8;
      HashSet com_11 = new HashSet();
      com_11.addAll(m1_15.keySet());
      com_11.retainAll(m2_16.keySet());
      boolean all_applies_12 = true;
      Object d_13;
      for (Iterator bb_14 = com_11.iterator(); bb_14.hasNext() && all_applies_12; ) {

        d_13 = bb_14.next();
        all_applies_12 = m1_15.get(d_13).equals(m2_16.get(d_13));
      }
      if (!all_applies_12) 
        UTIL.RunTime("Run-Time Error:Map Merge: Incompatible maps");
      m1_15.putAll(m2_16);
      rhs_6 = m1_15;
    }
    ivInfo = (HashMap) UTIL.clone(rhs_6);
    return res;
  }
// ***** VDMTOOLS END Name=addContextInfo


// ***** VDMTOOLS START Name=getLine KEEP=NO
  public Long getLine () throws CGException {
    return ivLine;
  }
// ***** VDMTOOLS END Name=getLine


// ***** VDMTOOLS START Name=setLine KEEP=NO
  public void setLine (final Long pl) throws CGException {
    ivLine = UTIL.NumberToLong(UTIL.clone(pl));
  }
// ***** VDMTOOLS END Name=setLine


// ***** VDMTOOLS START Name=getColumn KEEP=NO
  public Long getColumn () throws CGException {
    return ivColumn;
  }
// ***** VDMTOOLS END Name=getColumn


// ***** VDMTOOLS START Name=setColumn KEEP=NO
  public void setColumn (final Long pc) throws CGException {
    ivColumn = UTIL.NumberToLong(UTIL.clone(pc));
  }
// ***** VDMTOOLS END Name=setColumn


// ***** VDMTOOLS START Name=setPosition KEEP=NO
  public void setPosition (final Long pl, final Long pc) throws CGException {

    setLine(pl);
    setColumn(pc);
  }
// ***** VDMTOOLS END Name=setPosition


// ***** VDMTOOLS START Name=setPosLexem KEEP=NO
  public void setPosLexem (final IOmlLexem pol) throws CGException {

    Long tmpArg_v_3 = null;
    tmpArg_v_3 = pol.getLine();
    setLine(tmpArg_v_3);
    Long tmpArg_v_5 = null;
    tmpArg_v_5 = pol.getColumn();
    setColumn(tmpArg_v_5);
  }
// ***** VDMTOOLS END Name=setPosLexem


// ***** VDMTOOLS START Name=setPosNode KEEP=NO
  public void setPosNode (final IOmlNode pnd) throws CGException {

    Long tmpArg_v_3 = null;
    tmpArg_v_3 = pnd.getLine();
    setLine(tmpArg_v_3);
    Long tmpArg_v_5 = null;
    tmpArg_v_5 = pnd.getColumn();
    setColumn(tmpArg_v_5);
  }
// ***** VDMTOOLS END Name=setPosNode

}
;
