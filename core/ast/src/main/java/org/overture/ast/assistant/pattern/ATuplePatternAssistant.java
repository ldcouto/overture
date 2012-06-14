package org.overture.ast.assistant.pattern;

import org.overture.ast.lex.LexNameList;
import org.overture.ast.patterns.ATuplePattern;
import org.overture.ast.patterns.PPattern;

public class ATuplePatternAssistant {

	
	public static LexNameList getAllVariableNames(ATuplePattern pattern) {
		LexNameList list = new LexNameList();

		for (PPattern p: pattern.getPlist())
		{
			list.addAll(PPatternAssistant.getAllVariableNames(p));
		}

		return list;
	}

	
}