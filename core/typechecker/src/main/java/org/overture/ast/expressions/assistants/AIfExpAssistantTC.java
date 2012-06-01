package org.overture.ast.expressions.assistants;

import org.overture.ast.expressions.AElseIfExp;
import org.overture.ast.expressions.AIfExp;
import org.overturetool.vdmj.lex.LexNameList;

public class AIfExpAssistantTC {

	public static LexNameList getOldNames(AIfExp expression) {
		
		LexNameList list = PExpAssistantTC.getOldNames(expression.getTest());
		list.addAll(PExpAssistantTC.getOldNames(expression.getThen()));

		for (AElseIfExp elif: expression.getElseList())
		{
			list.addAll(PExpAssistantTC.getOldNames(elif));
		}

		if (expression.getElse() != null)
		{
			list.addAll(PExpAssistantTC.getOldNames(expression.getElse()));
		}

		return list;
	}

}