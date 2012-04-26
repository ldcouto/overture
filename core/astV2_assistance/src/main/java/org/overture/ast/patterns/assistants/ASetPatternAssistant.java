package org.overture.ast.patterns.assistants;

import org.overture.ast.patterns.ASetPattern;
import org.overture.ast.patterns.PPattern;
import org.overturetool.vdmjV2.lex.LexNameList;

public class ASetPatternAssistant {



	public static LexNameList getVariableNames(ASetPattern pattern) {
		LexNameList list = new LexNameList();

		for (PPattern p: pattern.getPlist())
		{
			list.addAll(PPatternAssistant.getVariableNames(p));
		}

		return list;
	}



}