package org.overture.ast.definitions.assistants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.overture.ast.analysis.QuestionAnswerAdaptor;
import org.overture.ast.definitions.AExplicitFunctionDefinition;
import org.overture.ast.definitions.AImplicitOperationDefinition;
import org.overture.ast.definitions.AStateDefinition;
import org.overture.ast.definitions.PDefinition;
import org.overture.ast.expressions.APostOpExp;
import org.overture.ast.expressions.APreOpExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.patterns.AIdentifierPattern;
import org.overture.ast.patterns.APatternListTypePair;
import org.overture.ast.patterns.PPattern;
import org.overture.ast.patterns.assistants.APatternTypePairAssistant;
import org.overture.ast.statements.ASubclassResponsibilityStm;
import org.overture.ast.types.PType;
import org.overture.ast.types.assistants.AOperationTypeAssistant;
import org.overture.ast.types.assistants.APatternListTypePairAssistant;
import org.overture.ast.types.assistants.PTypeAssistant;
import org.overture.typecheck.Environment;
import org.overture.typecheck.TypeCheckInfo;
import org.overturetool.vdmjV2.lex.LexNameList;
import org.overturetool.vdmjV2.lex.LexNameToken;
import org.overturetool.vdmjV2.typechecker.NameScope;

public class AImplicitOperationDefinitionAssistant {

	public static PDefinition findName(AImplicitOperationDefinition d,
			LexNameToken sought, NameScope scope) {
		if (PDefinitionAssistantTC.findNameBaseCase(d, sought, scope) != null)
		{
			return d;
		}

		PDefinition predef = d.getPredef();
		if (predef != null && PDefinitionAssistantTC.findName(predef, sought, scope) != null)
		{
			return predef;
		}

		PDefinition postdef = d.getPostdef();
		if (postdef != null && PDefinitionAssistantTC.findName(postdef,sought, scope) != null)
		{
			return postdef;
		}

		return null;
	}

	public static List<PDefinition> getDefinitions(
			AImplicitOperationDefinition d) {
		List<PDefinition> defs = new Vector<PDefinition>();
		defs.add(d);

		if (d.getPredef() != null)
		{
			defs.add(d.getPredef());
		}

		if (d.getPostdef() != null)
		{
			defs.add(d.getPostdef());
		}

		return defs;
	}

	public static LexNameList getVariableNames(AImplicitOperationDefinition d) {
		return new LexNameList(d.getName());
	}

	public static void typeResolve(AImplicitOperationDefinition d,
			QuestionAnswerAdaptor<TypeCheckInfo, PType> rootVisitor,
			TypeCheckInfo question) {
		
		d.setType(PTypeAssistant.typeResolve(d.getType(), null, rootVisitor, question));

		if (d.getResult() != null)
		{
			APatternTypePairAssistant.typeResolve(d.getResult(), rootVisitor, question);
		}

		if (question.env.isVDMPP())
		{
			d.getName().setTypeQualifier(d.getType().getParameters());

			if (d.getBody() instanceof ASubclassResponsibilityStm)
			{
				d.getClassDefinition().setIsAbstract(true);
			}
		}

		if (d.getPrecondition() != null)
		{
			PDefinitionAssistantTC.typeResolve(d.getPredef(), rootVisitor, question);
		}

		if (d.getPostcondition() != null)
		{
			PDefinitionAssistantTC.typeResolve(d.getPostdef(), rootVisitor, question);
		}

		for (APatternListTypePair ptp: d.getParameterPatterns())
		{
			APatternListTypePairAssistant.typeResolve(ptp, rootVisitor, question);
		}
		
	}

	public static void implicitDefinitions(AImplicitOperationDefinition d,
			Environment base) {
		
		d.setState(base.findStateDefinition());

		if (d.getPrecondition() != null)
		{
			d.setPredef(getPreDefinition(d,base));
			PDefinitionAssistantTC.markUsed(d.getPredef());
		}

		if (d.getPostcondition() != null)
		{
			d.setPostdef(getPostDefinition(d,base));
			PDefinitionAssistantTC.markUsed(d.getPostdef());
		}
		
	}

	private static AExplicitFunctionDefinition getPostDefinition(
			AImplicitOperationDefinition d, Environment base) {
		
		List<List<PPattern>> parameters = new Vector<List<PPattern>>();
		List<PPattern> plist = new Vector<PPattern>();

		for (APatternListTypePair pl: (LinkedList<APatternListTypePair>) d.getParameterPatterns())
		{
			plist.addAll((Collection<PPattern>) pl.getPatterns().clone());
		}

		if (d.getResult() != null)
		{
			plist.add(d.getResult().getPattern().clone());
		}

		AStateDefinition state = d.getState();
		
		if (state != null)
		{
			plist.add(new AIdentifierPattern(state.getLocation(), null, false, state.getName().getOldName()));
			plist.add(new AIdentifierPattern(state.getLocation(), null, false, state.getName().clone()));
		}
		else if (base.isVDMPP() && !PAccessSpecifierAssistantTC.isStatic(d.getAccess()))
		{
			plist.add(new AIdentifierPattern(d.getName().getLocation(), null, false, d.getName().getSelfName().getOldName()));
			plist.add(new AIdentifierPattern(d.getName().getLocation(), null, false, d.getName().getSelfName()));
		}

		parameters.add(plist);
		PExp postop = new APostOpExp(null, d.getLocation(),d.getName().clone(), d.getPrecondition(), d.getPostcondition(), d.getErrors(), state, null);

		
		AExplicitFunctionDefinition def = new AExplicitFunctionDefinition(d.getPostcondition().getLocation(), 
				d.getName().getPostName(d.getPostcondition().getLocation()), 
				NameScope.GLOBAL, false, null, 
				PAccessSpecifierAssistantTC.getDefault(), 
				null, 
				parameters, 
				AOperationTypeAssistant.getPostType(d.getType(),state, d.getClassDefinition(), PAccessSpecifierAssistantTC.isStatic(d.getAccess())), 
				postop, null, null, null, null, null, null, 
				null, false, false, null, null, null, null, parameters.size() > 1, null);
		
//		AExplicitFunctionDefinition def = new AExplicitFunctionDefinition(
//				d.getPostcondition().getLocation(),
//				d.getName().getPostName(d.getPostcondition().getLocation()), 
//				NameScope.GLOBAL,
//				false,
//				PAccessSpecifierAssistantTC.getDefault(),
//				null,
//				parameters,
//				AOperationTypeAssistant.getPostType(d.getType(),state, d.getClassDefinition(), PAccessSpecifierAssistantTC.isStatic(d.getAccess())),
//				postop, 
//				null, null, null);

		// Operation postcondition functions are effectively not static as
		// their expression can directly refer to instance variables, even
		// though at runtime these are passed via a "self" parameter.

		def.setAccess(PAccessSpecifierAssistantTC.getStatic(d, false));
		def.setClassDefinition(d.getClassDefinition());
		return def;
	}

	private static AExplicitFunctionDefinition getPreDefinition(
			AImplicitOperationDefinition d, Environment base) {
		
		List<List<PPattern>> parameters = new Vector<List<PPattern>>();
		List<PPattern> plist = new Vector<PPattern>();

		for (APatternListTypePair pl: (LinkedList<APatternListTypePair>) d.getParameterPatterns())
		{
			plist.addAll((Collection<PPattern>) pl.getPatterns().clone());
		}

		AStateDefinition state = d.getState();
		
		if (state != null)
		{
			plist.add(new AIdentifierPattern(state.getLocation(),null, false,state.getName()));
		}
		else if (base.isVDMPP() && !PAccessSpecifierAssistantTC.isStatic(d.getAccess()))
		{
			plist.add(new AIdentifierPattern(d.getName().getLocation(),null, false,d.getName().getSelfName()));
		}

		parameters.add(plist);
		PExp preop = new APreOpExp(null,d.getLocation(),(LexNameToken) d.getName().clone(), d.getPrecondition(), d.getErrors(), state);

		
		AExplicitFunctionDefinition def = new AExplicitFunctionDefinition(d.getPrecondition().getLocation(), 
				d.getName().getPreName(d.getPrecondition().getLocation()), 
				NameScope.GLOBAL, false, null, PAccessSpecifierAssistantTC.getDefault(), null, 
				parameters, 
				AOperationTypeAssistant.getPreType(d.getType(), state, d.getClassDefinition(), PAccessSpecifierAssistantTC.isStatic(d.getAccess())),
				preop, null, null, null, null, null, null, null, false, false, null, null, null, null, parameters.size() > 1, null);
		
//		AExplicitFunctionDefinition def = new AExplicitFunctionDefinition(
//			d.getPrecondition().getLocation(),
//			d.getName().getPreName(d.getPrecondition().getLocation()), 
//			NameScope.GLOBAL,
//			false,
//			PAccessSpecifierAssistantTC.getDefault(),
//			null,
//			parameters,
//		    AOperationTypeAssistant.getPreType(d.getType(), state, d.getClassDefinition(), PAccessSpecifierAssistantTC.isStatic(d.getAccess())),
//			preop, 
//			null, null, null);

		// Operation precondition functions are effectively not static as
		// their expression can directly refer to instance variables, even
		// though at runtime these are passed via a "self" parameter.

		def.setAccess(PAccessSpecifierAssistantTC.getStatic(d, false));
		def.setClassDefinition(d.getClassDefinition());
		return def;
	}

	public static List<PPattern> getParamPatternList(AImplicitOperationDefinition definition)
	{
		List<PPattern> plist = new ArrayList<PPattern>();

		for (APatternListTypePair pl: definition.getParameterPatterns() )
		{
			plist.addAll(pl.getPatterns());
		}

		return plist;
	}
	
	public static List<List<PPattern>> getListParamPatternList(AImplicitOperationDefinition func) {
		List<List<PPattern>> parameters = new ArrayList<List<PPattern>>();
		List<PPattern> plist = new ArrayList<PPattern>();

		for (APatternListTypePair pl: func.getParameterPatterns())
		{
			plist.addAll(pl.getPatterns());
		}

		parameters.add(plist);
		return parameters;
	}

}