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



public class OmlCasesExpression extends OmlExpression implements IOmlCasesExpression {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=ivMatchExpression KEEP=NO
  private IOmlExpression ivMatchExpression = null;
// ***** VDMTOOLS END Name=ivMatchExpression

// ***** VDMTOOLS START Name=ivAlternativeList KEEP=NO
  private Vector ivAlternativeList = null;
// ***** VDMTOOLS END Name=ivAlternativeList

// ***** VDMTOOLS START Name=ivOthersExpression KEEP=NO
  private IOmlExpression ivOthersExpression = null;
// ***** VDMTOOLS END Name=ivOthersExpression


// ***** VDMTOOLS START Name=OmlCasesExpression KEEP=NO
  public OmlCasesExpression () throws CGException {
    try {

      ivMatchExpression = null;
      ivAlternativeList = new Vector();
      ivOthersExpression = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=OmlCasesExpression


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("CasesExpression");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept KEEP=NO
  public void accept (final IOmlVisitor pVisitor) throws CGException {
    pVisitor.visitCasesExpression((IOmlCasesExpression) this);
  }
// ***** VDMTOOLS END Name=accept


// ***** VDMTOOLS START Name=OmlCasesExpression KEEP=NO
  public OmlCasesExpression (final IOmlExpression p1, final Vector p2, final IOmlExpression p3) throws CGException {

    try {

      ivMatchExpression = null;
      ivAlternativeList = new Vector();
      ivOthersExpression = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    {

      setMatchExpression((IOmlExpression) p1);
      setAlternativeList(p2);
      setOthersExpression((IOmlExpression) p3);
    }
  }
// ***** VDMTOOLS END Name=OmlCasesExpression


// ***** VDMTOOLS START Name=OmlCasesExpression KEEP=NO
  public OmlCasesExpression (final IOmlExpression p1, final Vector p2, final IOmlExpression p3, final Long line, final Long column) throws CGException {

    try {

      ivMatchExpression = null;
      ivAlternativeList = new Vector();
      ivOthersExpression = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    {

      setMatchExpression((IOmlExpression) p1);
      setAlternativeList(p2);
      setOthersExpression((IOmlExpression) p3);
      setPosition(line, column);
    }
  }
// ***** VDMTOOLS END Name=OmlCasesExpression


// ***** VDMTOOLS START Name=init KEEP=NO
  public void init (final HashMap data) throws CGException {

    {

      String fname = new String("match_expression");
      Boolean cond_4 = null;
      cond_4 = new Boolean(data.containsKey(fname));
      if (cond_4.booleanValue()) 
        setMatchExpression((IOmlExpression) data.get(fname));
    }
    {

      String fname = new String("alternative_list");
      Boolean cond_13 = null;
      cond_13 = new Boolean(data.containsKey(fname));
      if (cond_13.booleanValue()) 
        setAlternativeList((Vector) data.get(fname));
    }
    {

      String fname = new String("others_expression");
      Boolean cond_22 = null;
      cond_22 = new Boolean(data.containsKey(fname));
      if (cond_22.booleanValue()) 
        setOthersExpression((IOmlExpression) data.get(fname));
    }
  }
// ***** VDMTOOLS END Name=init


// ***** VDMTOOLS START Name=getMatchExpression KEEP=NO
  public IOmlExpression getMatchExpression () throws CGException {
    return (IOmlExpression) ivMatchExpression;
  }
// ***** VDMTOOLS END Name=getMatchExpression


// ***** VDMTOOLS START Name=setMatchExpression KEEP=NO
  public void setMatchExpression (final IOmlExpression parg) throws CGException {
    ivMatchExpression = (IOmlExpression) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setMatchExpression


// ***** VDMTOOLS START Name=getAlternativeList KEEP=NO
  public Vector getAlternativeList () throws CGException {
    return ivAlternativeList;
  }
// ***** VDMTOOLS END Name=getAlternativeList


// ***** VDMTOOLS START Name=setAlternativeList KEEP=NO
  public void setAlternativeList (final Vector parg) throws CGException {
    ivAlternativeList = (Vector) UTIL.ConvertToList(UTIL.clone(parg));
  }
// ***** VDMTOOLS END Name=setAlternativeList


// ***** VDMTOOLS START Name=addAlternativeList KEEP=NO
  public void addAlternativeList (final IOmlNode parg) throws CGException {
    ivAlternativeList.add(parg);
  }
// ***** VDMTOOLS END Name=addAlternativeList


// ***** VDMTOOLS START Name=getOthersExpression KEEP=NO
  public IOmlExpression getOthersExpression () throws CGException {
    return (IOmlExpression) ivOthersExpression;
  }
// ***** VDMTOOLS END Name=getOthersExpression


// ***** VDMTOOLS START Name=hasOthersExpression KEEP=NO
  public Boolean hasOthersExpression () throws CGException {
    return new Boolean(!UTIL.equals(ivOthersExpression, null));
  }
// ***** VDMTOOLS END Name=hasOthersExpression


// ***** VDMTOOLS START Name=setOthersExpression KEEP=NO
  public void setOthersExpression (final IOmlExpression parg) throws CGException {
    ivOthersExpression = (IOmlExpression) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setOthersExpression

  public String toString() {
	  
	  String match = new String("");
	  String alternative = new String("");
	  String others = new String("");
	  int i, size = this.ivAlternativeList.size();
	  
	  match = this.ivMatchExpression.toString();
	  if (size > 0) {
	  for(i=0;i<size-1;i++) {
	  alternative += "\t" + this.ivAlternativeList.get(i).toString() + ",\n";
	  }
	  alternative += "\t" + this.ivAlternativeList.get(size-1).toString();
	  }
	  try {
		if (hasOthersExpression()) others = ",\n\t" + "others -> " + this.ivOthersExpression.toString();
	} catch (CGException e) {
		e.printStackTrace();
	}
	  
	  return "cases " + match + ":\n" + alternative + others + "\n end ";
  }
}
;
