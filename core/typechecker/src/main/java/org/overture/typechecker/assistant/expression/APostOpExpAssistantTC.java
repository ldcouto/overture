package org.overture.typechecker.assistant.expression;

import org.overture.ast.expressions.APostOpExp;
import org.overture.ast.lex.LexNameList;

public class APostOpExpAssistantTC {

	public static LexNameList getOldNames(APostOpExp expression) {
		return PExpAssistantTC.getOldNames(expression.getPostexpression());
	}

}