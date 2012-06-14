package org.overture.typechecker.assistant.pattern;

import java.util.List;

import org.overture.ast.lex.LexNameList;
import org.overture.ast.patterns.ASetBind;
import org.overture.ast.patterns.ATypeBind;
import org.overture.ast.patterns.PBind;
import org.overture.ast.patterns.PMultipleBind;

public class PBindAssistantTC {

	public static List<PMultipleBind> getMultipleBindList(PBind bind) {
		
		switch (bind.kindPBind()) {
		case SET:
			return ASetBindAssistantTC.getMultipleBindList((ASetBind)bind);
		case TYPE:
			return ATypeBindAssistantTC.getMultipleBindList((ATypeBind)bind);
		
		}
		return null;
		
	}
	
	public static LexNameList getOldNames(PBind bind)
	{
		switch (bind.kindPBind()) {
		case SET:
			return ASetBindAssistantTC.getOldNames((ASetBind)bind);
		case TYPE:
			return ATypeBindAssistantTC.getOldNames((ATypeBind)bind);
		default:
			assert false : "Should not happen";
			return null;	
		}
		
	}

	
}