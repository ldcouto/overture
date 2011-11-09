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



public class OmlCasesStatement extends OmlStatement implements IOmlCasesStatement {

// ***** VDMTOOLS START Name=vdmComp KEEP=NO
  static UTIL.VDMCompare vdmComp = new UTIL.VDMCompare();
// ***** VDMTOOLS END Name=vdmComp

// ***** VDMTOOLS START Name=ivMatchExpression KEEP=NO
  private IOmlExpression ivMatchExpression = null;
// ***** VDMTOOLS END Name=ivMatchExpression

// ***** VDMTOOLS START Name=ivAlternativeList KEEP=NO
  private Vector ivAlternativeList = null;
// ***** VDMTOOLS END Name=ivAlternativeList

// ***** VDMTOOLS START Name=ivOthersStatement KEEP=NO
  private IOmlStatement ivOthersStatement = null;
// ***** VDMTOOLS END Name=ivOthersStatement


// ***** VDMTOOLS START Name=OmlCasesStatement KEEP=NO
  public OmlCasesStatement () throws CGException {
    try {

      ivMatchExpression = null;
      ivAlternativeList = new Vector();
      ivOthersStatement = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
  }
// ***** VDMTOOLS END Name=OmlCasesStatement


// ***** VDMTOOLS START Name=identity KEEP=NO
  public String identity () throws CGException {
    return new String("CasesStatement");
  }
// ***** VDMTOOLS END Name=identity


// ***** VDMTOOLS START Name=accept KEEP=NO
  public void accept (final IOmlVisitor pVisitor) throws CGException {
    pVisitor.visitCasesStatement((IOmlCasesStatement) this);
  }
// ***** VDMTOOLS END Name=accept


// ***** VDMTOOLS START Name=OmlCasesStatement KEEP=NO
  public OmlCasesStatement (final IOmlExpression p1, final Vector p2, final IOmlStatement p3) throws CGException {

    try {

      ivMatchExpression = null;
      ivAlternativeList = new Vector();
      ivOthersStatement = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    {

      setMatchExpression((IOmlExpression) p1);
      setAlternativeList(p2);
      setOthersStatement((IOmlStatement) p3);
    }
  }
// ***** VDMTOOLS END Name=OmlCasesStatement


// ***** VDMTOOLS START Name=OmlCasesStatement KEEP=NO
  public OmlCasesStatement (final IOmlExpression p1, final Vector p2, final IOmlStatement p3, final Long line, final Long column) throws CGException {

    try {

      ivMatchExpression = null;
      ivAlternativeList = new Vector();
      ivOthersStatement = null;
    }
    catch (Exception e){

      e.printStackTrace(System.out);
      System.out.println(e.getMessage());
    }
    {

      setMatchExpression((IOmlExpression) p1);
      setAlternativeList(p2);
      setOthersStatement((IOmlStatement) p3);
      setPosition(line, column);
    }
  }
// ***** VDMTOOLS END Name=OmlCasesStatement


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

      String fname = new String("others_statement");
      Boolean cond_22 = null;
      cond_22 = new Boolean(data.containsKey(fname));
      if (cond_22.booleanValue()) 
        setOthersStatement((IOmlStatement) data.get(fname));
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


// ***** VDMTOOLS START Name=getOthersStatement KEEP=NO
  public IOmlStatement getOthersStatement () throws CGException {
    return (IOmlStatement) ivOthersStatement;
  }
// ***** VDMTOOLS END Name=getOthersStatement


// ***** VDMTOOLS START Name=hasOthersStatement KEEP=NO
  public Boolean hasOthersStatement () throws CGException {
    return new Boolean(!UTIL.equals(ivOthersStatement, null));
  }
// ***** VDMTOOLS END Name=hasOthersStatement


// ***** VDMTOOLS START Name=setOthersStatement KEEP=NO
  public void setOthersStatement (final IOmlStatement parg) throws CGException {
    ivOthersStatement = (IOmlStatement) UTIL.clone(parg);
  }
// ***** VDMTOOLS END Name=setOthersStatement

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
		if (hasOthersStatement()) others = ",\n\t" + "others -> " + this.ivOthersStatement.toString();
	} catch (CGException e) {
		e.printStackTrace();
	}
	  
	  return "cases " + match + ":\n" + alternative + others + "\n end";
  }
}
;
