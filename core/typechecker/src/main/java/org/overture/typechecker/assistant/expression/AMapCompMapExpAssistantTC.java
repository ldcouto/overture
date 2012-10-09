package org.overture.typechecker.assistant.expression;

import org.overture.ast.expressions.AMapCompMapExp;
import org.overture.ast.lex.LexNameList;
import org.overture.ast.patterns.PMultipleBind;
import org.overture.typechecker.assistant.pattern.PMultipleBindAssistantTC;

public class AMapCompMapExpAssistantTC {

	public static LexNameList getOldNames(AMapCompMapExp expression) {
		LexNameList list = PExpAssistantTC.getOldNames(expression.getFirst());

		for (PMultipleBind mb: expression.getBindings())
		{
			list.addAll(PMultipleBindAssistantTC.getOldNames(mb));
		}

		if (expression.getPredicate() != null)
		{
			list.addAll(PExpAssistantTC.getOldNames(expression.getPredicate()));
		}

		return list;
	}

}