/*******************************************************************************
 *
 *	Copyright (c) 2008 Fujitsu Services Ltd.
 *
 *	Author: Nick Battle
 *
 *	This file is part of VDMJ.
 *
 *	VDMJ is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	VDMJ is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with VDMJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 ******************************************************************************/

package org.overturetool.vdmj.syntax;

import java.util.List;
import java.util.Vector;

import org.overture.ast.definitions.AAssignmentDefinition;
import org.overture.ast.definitions.PDefinition;
import org.overture.ast.expressions.ANewExp;
import org.overture.ast.expressions.AUndefinedExp;
import org.overture.ast.expressions.AVariableExp;
import org.overture.ast.expressions.PExp;
import org.overture.ast.patterns.ADefPatternBind;
import org.overture.ast.patterns.PMultipleBind;
import org.overture.ast.patterns.PPattern;
import org.overture.ast.statements.AAlwaysStm;
import org.overture.ast.statements.AApplyObjectDesignator;
import org.overture.ast.statements.AAssignmentStm;
import org.overture.ast.statements.AAtomicStm;
import org.overture.ast.statements.ABlockSimpleBlockStm;
import org.overture.ast.statements.ACallObjectStm;
import org.overture.ast.statements.ACallStm;
import org.overture.ast.statements.ACaseAlternativeStm;
import org.overture.ast.statements.ACasesStm;
import org.overture.ast.statements.ACyclesStm;
import org.overture.ast.statements.ADefLetDefStm;
import org.overture.ast.statements.ADurationStm;
import org.overture.ast.statements.AElseIfStm;
import org.overture.ast.statements.AErrorStm;
import org.overture.ast.statements.AExitStm;
import org.overture.ast.statements.AFieldObjectDesignator;
import org.overture.ast.statements.AFieldStateDesignator;
import org.overture.ast.statements.AForAllStm;
import org.overture.ast.statements.AForIndexStm;
import org.overture.ast.statements.AForPatternBindStm;
import org.overture.ast.statements.AIdentifierObjectDesignator;
import org.overture.ast.statements.AIdentifierStateDesignator;
import org.overture.ast.statements.AIfStm;
import org.overture.ast.statements.ALetBeStStm;
import org.overture.ast.statements.AMapSeqStateDesignator;
import org.overture.ast.statements.ANewObjectDesignator;
import org.overture.ast.statements.ANonDeterministicSimpleBlockStm;
import org.overture.ast.statements.ANotYetSpecifiedStm;
import org.overture.ast.statements.AReturnStm;
import org.overture.ast.statements.ASelfObjectDesignator;
import org.overture.ast.statements.ASkipStm;
import org.overture.ast.statements.ASpecificationStm;
import org.overture.ast.statements.AStartStm;
import org.overture.ast.statements.ASubclassResponsibilityStm;
import org.overture.ast.statements.ATixeStm;
import org.overture.ast.statements.ATixeStmtAlternative;
import org.overture.ast.statements.ATrapStm;
import org.overture.ast.statements.AWhileStm;
import org.overture.ast.statements.PObjectDesignator;
import org.overture.ast.statements.PStateDesignator;
import org.overture.ast.statements.PStm;
import org.overture.ast.statements.SLetDefStm;
import org.overture.ast.types.PType;
import org.overturetool.vdmj.Release;
import org.overturetool.vdmj.Settings;
import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.lex.LexException;
import org.overturetool.vdmj.lex.LexIdentifierToken;
import org.overturetool.vdmj.lex.LexLocation;
import org.overturetool.vdmj.lex.LexNameToken;
import org.overturetool.vdmj.lex.LexToken;
import org.overturetool.vdmj.lex.LexTokenReader;
import org.overturetool.vdmj.lex.VDMToken;
import org.overturetool.vdmj.typechecker.NameScope;

/**
 * A syntax analyser to parse statements.
 */

public class StatementReader extends SyntaxReader
{
	public StatementReader(LexTokenReader reader)
	{
		super(reader);
	}

	public PStm readStatement() throws ParserException, LexException
	{
		PStm stmt = null;
		LexToken token = lastToken();
		LexLocation location = token.location;

		switch (token.type)
		{
			case LET:
				stmt = readLetStatement(token);
				break;

			case RETURN:
				stmt = readReturnStatement(location);
				break;

			case BRA:
				stmt = readBlockStatement(location);
				break;

			case NAME:
			case IDENTIFIER:
			case NEW:
			case SELF:
				stmt = readAssignmentOrCallStatement(token);
				break;

			case IF:
				nextToken(); // to allow elseif to call it too
				stmt = readConditionalStatement(location);
				break;

			case CASES:
				stmt = readCasesStatement(location);
				break;

			case FOR:
				stmt = readForStatement(location);
				break;

			case WHILE:
				stmt = readWhileStatement(location);
				break;

			case PIPEPIPE:
				stmt = readNonDetStatement(location);
				break;

			case ALWAYS:
				stmt = readAlwaysStatement(location);
				break;

			case ATOMIC:
				stmt = readAtomicStatement(location);
				break;

			case TRAP:
				stmt = readTrapStatement(location);
				break;

			case TIXE:
				stmt = readTixeStatement(location);
				break;

			case DEF:
				stmt = readDefStatement(location);
				break;

			case EXIT:
				stmt = readExitStatement(location);
				break;

			case SEQ_OPEN:
				stmt = readSpecStatement(location);
				break;

			case ERROR:
				stmt = new AErrorStm(location);
				nextToken();
				break;

			case SKIP:
				stmt = new ASkipStm(location);
				nextToken();
				break;

			case IS:
				switch (nextToken().type)
				{
					case NOT:
						nextToken();
						checkFor(VDMToken.YET, 2187, "Expecting 'is not yet specified");
						checkFor(VDMToken.SPECIFIED, 2188, "Expecting 'is not yet specified");
						stmt = new ANotYetSpecifiedStm(location, null, null);
						break;

					case SUBCLASS:
						nextToken();
						checkFor(VDMToken.RESPONSIBILITY, 2189, "Expecting 'is subclass responsibility'");
						stmt = new ASubclassResponsibilityStm(location);
						break;

					default:
						throwMessage(2062, "Expected 'is not specified' or 'is subclass responsibility'");
				}
				break;

			case START:
				stmt = readStartStatement(location);
				break;

			case STARTLIST:
				stmt = readStartlistStatement(location);
				break;

			case CYCLES:
				stmt = readCyclesStatement(location);
				break;

			case DURATION:
				stmt = readDurationStatement(location);
				break;

			default:
				throwMessage(2063, "Unexpected token in statement");
		}

		return stmt;
	}

	private PStm readExitStatement(LexLocation token) throws ParserException,
			LexException
	{
		checkFor(VDMToken.EXIT, 2190, "Expecting 'exit'");

		try
		{
			reader.push();
			PExp exp = getExpressionReader().readExpression();
			reader.unpush();
			return new AExitStm(token, exp);
		} catch (ParserException e)
		{
			reader.pop();
		}

		return new AExitStm(token, null);
	}

	private PStm readTixeStatement(LexLocation token) throws ParserException,
			LexException
	{
		checkFor(VDMToken.TIXE, 2191, "Expecting 'tixe'");

		List<ATixeStmtAlternative> traps = new Vector<ATixeStmtAlternative>();
		BindReader br = getBindReader();
		checkFor(VDMToken.SET_OPEN, 2192, "Expecting '{' after 'tixe'");

		while (lastToken().isNot(VDMToken.SET_CLOSE))
		{
			ADefPatternBind patternBind = br.readPatternOrBind();
			checkFor(VDMToken.MAPLET, 2193, "Expecting '|->' after pattern bind");
			PStm result = readStatement();
			traps.add(new ATixeStmtAlternative(patternBind, result));
			ignore(VDMToken.COMMA);
		}

		nextToken();
		checkFor(VDMToken.IN, 2194, "Expecting 'in' after tixe traps");
		PStm body = getStatementReader().readStatement();

		return new ATixeStm(token, traps, body);
	}

	private PStm readTrapStatement(LexLocation token) throws ParserException,
			LexException
	{
		checkFor(VDMToken.TRAP, 2195, "Expecting 'trap'");
		ADefPatternBind patternBind = getBindReader().readPatternOrBind();
		checkFor(VDMToken.WITH, 2196, "Expecting 'with' in trap statement");
		PStm with = getStatementReader().readStatement();
		checkFor(VDMToken.IN, 2197, "Expecting 'in' in trap statement");
		PStm body = getStatementReader().readStatement();
		return new ATrapStm(token, patternBind, with, body);
	}

	private PStm readAlwaysStatement(LexLocation token) throws ParserException,
			LexException
	{
		checkFor(VDMToken.ALWAYS, 2198, "Expecting 'always'");
		PStm always = getStatementReader().readStatement();
		checkFor(VDMToken.IN, 2199, "Expecting 'in' after 'always' statement");
		PStm body = getStatementReader().readStatement();
		return new AAlwaysStm(token, always, body);
	}

	private PStm readNonDetStatement(LexLocation token) throws ParserException,
			LexException
	{
		checkFor(VDMToken.PIPEPIPE, 2200, "Expecting '||'");
		checkFor(VDMToken.BRA, 2201, "Expecting '(' after '||'");
		ANonDeterministicSimpleBlockStm block = new ANonDeterministicSimpleBlockStm(token, new Vector<PStm>());
		block.getStatements().add(readStatement()); // Must be one

		while (ignore(VDMToken.COMMA))
		{
			block.getStatements().add(readStatement());
		}

		checkFor(VDMToken.KET, 2202, "Expecting ')' at end of '||' block");
		return block;
	}

	private PStm readAssignmentOrCallStatement(LexToken token)
			throws ParserException, LexException
	{
		ParserException assignError = null;
		PStm stmt = null;

		try
		{
			reader.push();
			stmt = readAssignmentStatement(token.location);
			reader.unpush();
			return stmt;
		} catch (ParserException e)
		{
			e.adjustDepth(reader.getTokensRead());
			reader.pop();
			assignError = e;
		}

		try
		{
			reader.push();
			stmt = readCallStatement();
			reader.unpush();
			return stmt;
		} catch (ParserException e)
		{
			e.adjustDepth(reader.getTokensRead());
			reader.pop();
			throw e.deeperThan(assignError) ? e : assignError;
		}
	}

	private PStm readAtomicStatement(LexLocation token) throws ParserException,
			LexException
	{
		checkFor(VDMToken.ATOMIC, 2203, "Expecting 'atomic'");
		checkFor(VDMToken.BRA, 2204, "Expecting '(' after 'atomic'");
		List<AAssignmentStm> assignments = new Vector<AAssignmentStm>();

		assignments.add(readAssignmentStatement(lastToken().location));
		ignore(VDMToken.SEMICOLON); // Every statement has an ignorable semicolon

		while (lastToken().isNot(VDMToken.KET))
		{
			assignments.add(readAssignmentStatement(lastToken().location));
			ignore(VDMToken.SEMICOLON);
		}

		checkFor(VDMToken.KET, 2205, "Expecting ')' after atomic assignments");
		return new AAtomicStm(token, assignments);
	}

	private PStm readCallStatement() throws ParserException, LexException
	{
		if (dialect != Dialect.VDM_SL)
		{
			return readObjectCallStatement();
		} else
		{
			return readSimpleCallStatement();
		}
	}

	private PStm readSimpleCallStatement() throws ParserException, LexException
	{
		LexNameToken name = readNameToken("Expecting operation name in call statement");

		checkFor(VDMToken.BRA, 2206, "Expecting '(' after call operation name");
		List<PExp> args = new Vector<PExp>();
		ExpressionReader er = getExpressionReader();

		if (lastToken().isNot(VDMToken.KET))
		{
			args.add(er.readExpression());

			while (ignore(VDMToken.COMMA))
			{
				args.add(er.readExpression());
			}
		}

		checkFor(VDMToken.KET, 2124, "Expecting ')' after args");

		return new ACallStm(name.location, name, args);
	}

	private PStm readObjectCallStatement() throws ParserException, LexException
	{
		PObjectDesignator designator = readObjectDesignator();

		// All operation calls actually look like object apply designators,
		// since they end with <name>([args]). So we unpick the apply
		// designator to extract the operation name and args.

		if (!(designator instanceof AApplyObjectDesignator))
		{
			throwMessage(2064, "Expecting <object>.identifier(args) or name(args)");
		}

		AApplyObjectDesignator oad = (AApplyObjectDesignator) designator;
		List<PExp> args = oad.getArgs();

		if (oad.getObject() instanceof AFieldObjectDesignator)
		{
			AFieldObjectDesignator ofd = (AFieldObjectDesignator) oad.getObject();

			if (ofd.getClassName() != null)
			{
				return new ACallObjectStm(ofd.getObject().getLocation(), ofd.getObject(), ofd.getClassName(), null, null, args, ofd.getClassName().explicit);
			} else
			{
				return new ACallObjectStm(ofd.getObject().getLocation(), ofd.getObject(), null, (LexIdentifierToken) ofd.getFieldName().clone(), null, args, false);
			}
		} else if (oad.getObject() instanceof AIdentifierObjectDesignator)
		{
			AIdentifierObjectDesignator oid = (AIdentifierObjectDesignator) oad.getObject();
			return new ACallStm(oid.getName().location, oid.getName(), args);
		} else
		{
			throwMessage(2065, "Expecting <object>.name(args) or name(args)");
			return null;
		}
	}

	private PObjectDesignator readObjectDesignator() throws ParserException,
			LexException
	{
		PObjectDesignator des = readSimpleObjectDesignator();
		boolean done = false;

		while (!done)
		{
			switch (lastToken().type)
			{
				case POINT:
					LexToken field = nextToken();

					// If we just read a qualified name, we're dealing with
					// something like new A().X`op(), else it's the more usual
					// new A().op().

					switch (field.type)
					{
						case IDENTIFIER:
							des = new AFieldObjectDesignator(des.getLocation(), des, null, (LexIdentifierToken) field);
							break;

						case NAME:
							des = new AFieldObjectDesignator(des.getLocation(), des, (LexNameToken) field, null);
							break;

						default:
							throwMessage(2066, "Expecting object field name");
					}

					nextToken();
					break;

				case BRA:
					nextToken();
					ExpressionReader er = getExpressionReader();
					List<PExp> args = new Vector<PExp>();

					if (lastToken().isNot(VDMToken.KET))
					{
						args.add(er.readExpression());

						while (ignore(VDMToken.COMMA))
						{
							args.add(er.readExpression());
						}
					}

					checkFor(VDMToken.KET, 2124, "Expecting ')' after args");
					des = new AApplyObjectDesignator(des.getLocation(), des, args);
					break;

				default:
					done = true;
					break;
			}
		}

		return des;
	}

	private PObjectDesignator readSimpleObjectDesignator() throws LexException,
			ParserException
	{
		LexToken token = readToken();

		switch (token.type)
		{
			case SELF:
				return new ASelfObjectDesignator(token.location, new LexNameToken(token.location.module, "self", token.location));

			case IDENTIFIER:
			{
				LexNameToken tok = idToName((LexIdentifierToken) token).getExplicit(true);
				return new AIdentifierObjectDesignator(token.location, idToName((LexIdentifierToken) token), new AVariableExp(token.location, tok, tok.getName()));
			}
			case NAME:
			{
				LexNameToken tok = ((LexNameToken) token).getExplicit(true);
				return new AIdentifierObjectDesignator(token.location, (LexNameToken) token, new AVariableExp(token.location, tok, tok.getName()));
			}
			case NEW:
				LexIdentifierToken name = readIdToken("Expecting class name after 'new'");
				checkFor(VDMToken.BRA, 2207, "Expecting '(' after new class name");

				List<PExp> args = new Vector<PExp>();
				ExpressionReader er = getExpressionReader();

				if (lastToken().isNot(VDMToken.KET))
				{
					args.add(er.readExpression());

					while (ignore(VDMToken.COMMA))
					{
						args.add(er.readExpression());
					}
				}

				checkFor(VDMToken.KET, 2124, "Expecting ')' after constructor args");

				return new ANewObjectDesignator(name.location, new ANewExp(name.location, name, args));

			default:
				throwMessage(2067, "Expecting 'self', 'new' or name in object designator");
				break;
		}

		return null;
	}

	private PStm readWhileStatement(LexLocation token) throws ParserException,
			LexException
	{
		checkFor(VDMToken.WHILE, 2208, "Expecting 'while'");
		PExp exp = getExpressionReader().readExpression();
		checkFor(VDMToken.DO, 2209, "Expecting 'do' after while expression");
		PStm body = getStatementReader().readStatement();
		return new AWhileStm(token, exp, body);
	}

	private PStm readForStatement(LexLocation token) throws ParserException,
			LexException
	{
		checkFor(VDMToken.FOR, 2210, "Expecting 'for'");
		PStm forstmt = null;

		if (lastToken().is(VDMToken.ALL))
		{
			nextToken();
			PPattern p = getPatternReader().readPattern();
			checkFor(VDMToken.IN, 2211, "Expecting 'in set' after 'for all'");
			checkFor(VDMToken.SET, 2212, "Expecting 'in set' after 'for all'");
			PExp set = getExpressionReader().readExpression();
			checkFor(VDMToken.DO, 2213, "Expecting 'do' after for all expression");
			PStm body = getStatementReader().readStatement();
			return new AForAllStm(token, p, set, body);
		} else
		{
			ParserException forIndexError = null;

			try
			{
				reader.push();
				forstmt = readForIndexStatement(token);
				reader.unpush();
				return forstmt;
			} catch (ParserException e)
			{
				e.adjustDepth(reader.getTokensRead());
				reader.pop();
				forIndexError = e;
			}

			try
			{
				reader.push();
				forstmt = readForPatternBindStatement(token);
				reader.unpush();
				return forstmt;
			} catch (ParserException e)
			{
				e.adjustDepth(reader.getTokensRead());
				reader.pop();
				throw e.deeperThan(forIndexError) ? e : forIndexError;
			}
		}
	}

	private PStm readForPatternBindStatement(LexLocation token)
			throws ParserException, LexException
	{
		ADefPatternBind pb = getBindReader().readPatternOrBind();
		checkFor(VDMToken.IN, 2214, "Expecting 'in' after pattern bind");

		// The old syntax used to include a "reverse" keyword as part
		// of the loop grammar, whereas the new VDM-10 syntax (LB:2791065)
		// makes the reverse a unary sequence operator.

		if (Settings.release == Release.VDM_10)
		{
			PExp exp = getExpressionReader().readExpression();
			checkFor(VDMToken.DO, 2215, "Expecting 'do' before loop statement");
			PStm body = getStatementReader().readStatement();
			return new AForPatternBindStm(token, pb, false, exp, body);
		} else
		{
			boolean reverse = ignore(VDMToken.REVERSE);
			PExp exp = getExpressionReader().readExpression();
			checkFor(VDMToken.DO, 2215, "Expecting 'do' before loop statement");
			PStm body = getStatementReader().readStatement();
			return new AForPatternBindStm(token, pb, reverse, exp, body);
		}
	}

	private PStm readForIndexStatement(LexLocation token)
			throws ParserException, LexException
	{
		LexIdentifierToken var = readIdToken("Expecting variable identifier");
		checkFor(VDMToken.EQUALS, 2216, "Expecting '=' after for variable");
		PExp from = getExpressionReader().readExpression();
		checkFor(VDMToken.TO, 2217, "Expecting 'to' after from expression");
		PExp to = getExpressionReader().readExpression();
		PExp by = null;

		if (lastToken().is(VDMToken.BY))
		{
			nextToken();
			by = getExpressionReader().readExpression();
		}

		checkFor(VDMToken.DO, 2218, "Expecting 'do' before loop statement");
		PStm body = getStatementReader().readStatement();
		return new AForIndexStm(token, idToName(var), from, to, by, body);
	}

	private PStm readConditionalStatement(LexLocation token)
			throws ParserException, LexException
	{
		PExp exp = getExpressionReader().readExpression();
		checkFor(VDMToken.THEN, 2219, "Missing 'then'");
		PStm thenStmt = readStatement();
		List<AElseIfStm> elseIfList = new Vector<AElseIfStm>();

		while (lastToken().is(VDMToken.ELSEIF))
		{
			LexToken elseif = lastToken();
			nextToken();
			elseIfList.add(readElseIfStatement(elseif.location));
		}

		PStm elseStmt = null;

		if (lastToken().is(VDMToken.ELSE))
		{
			nextToken();
			elseStmt = readStatement();
		}

		return new AIfStm(token, exp, thenStmt, elseIfList, elseStmt);
	}

	private AElseIfStm readElseIfStatement(LexLocation token)
			throws ParserException, LexException
	{
		PExp exp = getExpressionReader().readExpression();
		checkFor(VDMToken.THEN, 2220, "Missing 'then' after 'elseif' expression");
		PStm thenStmt = readStatement();
		return new AElseIfStm(token, exp, thenStmt);
	}

	private AAssignmentStm readAssignmentStatement(LexLocation token)
			throws ParserException, LexException
	{
		PStateDesignator sd = readStateDesignator();
		checkFor(VDMToken.ASSIGN, 2222, "Expecting ':=' in state assignment statement");
		return new AAssignmentStm(token, sd, getExpressionReader().readExpression());
	}

	private PStateDesignator readStateDesignator() throws ParserException,
			LexException
	{
		LexNameToken name = readNameToken("Expecting name in assignment statement");

		PStateDesignator sd = new AIdentifierStateDesignator(name.location, null, name);

		while (lastToken().is(VDMToken.POINT) || lastToken().is(VDMToken.BRA))
		{
			if (lastToken().is(VDMToken.POINT))
			{
				if (nextToken().isNot(VDMToken.IDENTIFIER))
				{
					throwMessage(2068, "Expecting field identifier");
				}

				sd = new AFieldStateDesignator(sd.getLocation(), sd, lastIdToken());
				nextToken();
			} else
			{
				nextToken();
				PExp exp = getExpressionReader().readExpression();
				checkFor(VDMToken.KET, 2223, "Expecting ')' after map/seq reference");
				sd = new AMapSeqStateDesignator(sd.getLocation(), sd, exp);
			}
		}

		return sd;
	}

	public PStm readBlockStatement(LexLocation token) throws ParserException,
			LexException
	{
		LexToken start = lastToken();
		checkFor(VDMToken.BRA, 2224, "Expecting statement block");
		ABlockSimpleBlockStm block = new ABlockSimpleBlockStm(token, null, readDclStatements());
		boolean problems = false;
		
		while (true) // Loop for continue in exceptions
		{
			try
			{
				while (!lastToken().is(VDMToken.KET))
				{
					
					block.getStatements().add(readStatement());
					if (lastToken().isNot(VDMToken.KET) && lastToken().isNot(VDMToken.SEMICOLON))
    				{
    					throwMessage(2225, "Expecting ';' after statement");
    				}
					ignore(VDMToken.SEMICOLON);
				}

				break;
			} catch (ParserException e)
			{
				problems = true;
				if (lastToken().is(VDMToken.KET)
						|| lastToken().is(VDMToken.EOF))
				{
					break;
				}

				VDMToken[] after = { VDMToken.SEMICOLON };
				VDMToken[] upto = { VDMToken.KET };
				report(e, after, upto);
				continue;
			}
		}

		checkFor(VDMToken.KET, 2226, "Expecting ')' at end of statement block");
		
		if (!problems && block.getStatements().isEmpty())
		{
			throwMessage(2296, "Block cannot be empty", start);
		}
		return block;
	}

	private List<PDefinition> readDclStatements() throws ParserException,
			LexException
	{
		List<PDefinition> defs = new Vector<PDefinition>();

		while (lastToken().is(VDMToken.DCL))
		{
			nextToken();
			defs.add(readAssignmentDefinition());

			while (ignore(VDMToken.COMMA))
			{
				defs.add(readAssignmentDefinition());
			}

			checkFor(VDMToken.SEMICOLON, 2227, "Expecting ';' after declarations");
		}

		return defs;
	}

	public AAssignmentDefinition readAssignmentDefinition()
			throws ParserException, LexException
	{
		LexIdentifierToken name = readIdToken("Expecting variable identifier");
		checkFor(VDMToken.COLON, 2228, "Expecting name:type in declaration");
		PType type = getTypeReader().readType();
		PExp exp = null;

		if (lastToken().is(VDMToken.ASSIGN))
		{
			nextToken();
			exp = getExpressionReader().readExpression();
		} else if (lastToken().is(VDMToken.EQUALSEQUALS)
				|| lastToken().is(VDMToken.EQUALS))
		{
			throwMessage(2069, "Expecting <identifier>:<type> := <expression>");
		} else
		{
			exp = new AUndefinedExp(type, name.location);
		}

		AAssignmentDefinition assignmentDefinition = new AAssignmentDefinition(name.location, idToName(name), NameScope.STATE, false, null, null, type, exp, null);
		assignmentDefinition.getLocation().executable(false);

		return assignmentDefinition;
	}

	private PStm readReturnStatement(LexLocation token) throws ParserException,
			LexException
	{
		checkFor(VDMToken.RETURN, 2229, "Expecting 'return'");

		try
		{
			reader.push();
			PExp exp = getExpressionReader().readExpression();
			reader.unpush();
			return new AReturnStm(token, exp);
		} catch (ParserException e)
		{
			int count = reader.getTokensRead();
			e.adjustDepth(count);
			reader.pop();

			if (count > 2)
			{
				// We got some way, so error is probably in exp
				throw e;
			} else
			{
				// Probably just a simple return
				return new AReturnStm(token, null);
			}
		}
	}

	private PStm readLetStatement(LexToken token) throws ParserException,
			LexException
	{
		checkFor(VDMToken.LET, 2230, "Expecting 'let'");
		ParserException letDefError = null;

		try
		{
			reader.push();
			SLetDefStm stmt = readLetDefStatement(token.location);
			reader.unpush();
			return stmt;
		} catch (ParserException e)
		{
			e.adjustDepth(reader.getTokensRead());
			reader.pop();
			letDefError = e;
		}

		try
		{
			reader.push();
			ALetBeStStm stmt = readLetBeStStatement(token.location);
			reader.unpush();
			return stmt;
		} catch (ParserException e)
		{
			e.adjustDepth(reader.getTokensRead());
			reader.pop();
			throw e.deeperThan(letDefError) ? e : letDefError;
		}
	}

	private SLetDefStm readLetDefStatement(LexLocation token)
			throws ParserException, LexException
	{
		DefinitionReader dr = getDefinitionReader();
		List<PDefinition> localDefs = new Vector<PDefinition>();
		localDefs.add(dr.readLocalDefinition(NameScope.LOCAL));

		while (ignore(VDMToken.COMMA))
		{
			localDefs.add(dr.readLocalDefinition(NameScope.LOCAL));
		}

		checkFor(VDMToken.IN, 2231, "Expecting 'in' after local definitions");
		return new ADefLetDefStm(token, localDefs, readStatement());
	}

	private ALetBeStStm readLetBeStStatement(LexLocation token)
			throws ParserException, LexException
	{
		PMultipleBind bind = getBindReader().readMultipleBind();
		PExp stexp = null;

		if (lastToken().is(VDMToken.BE))
		{
			nextToken();
			checkFor(VDMToken.ST, 2232, "Expecting 'st' after 'be' in let statement");
			stexp = getExpressionReader().readExpression();
		}

		checkFor(VDMToken.IN, 2233, "Expecting 'in' after bind in let statement");
		return new ALetBeStStm(token, bind, stexp, readStatement());
	}

	private ACasesStm readCasesStatement(LexLocation token)
			throws ParserException, LexException
	{
		checkFor(VDMToken.CASES, 2234, "Expecting 'cases'");
		PExp exp = getExpressionReader().readExpression();
		checkFor(VDMToken.COLON, 2235, "Expecting ':' after cases expression");

		List<ACaseAlternativeStm> cases = new Vector<ACaseAlternativeStm>();
		PStm others = null;
		cases.addAll(readCaseAlternatives());

		while (ignore(VDMToken.COMMA))
		{
			if (lastToken().is(VDMToken.OTHERS))
			{
				break;
			}

			cases.addAll(readCaseAlternatives());
		}

		if (lastToken().is(VDMToken.OTHERS))
		{
			nextToken();
			checkFor(VDMToken.ARROW, 2237, "Expecting '->' after others");
			others = readStatement();
		}

		checkFor(VDMToken.END, 2238, "Expecting 'end' after cases");
		return new ACasesStm(token, exp, cases, others);
	}

	private List<ACaseAlternativeStm> readCaseAlternatives()
			throws ParserException, LexException
	{
		List<ACaseAlternativeStm> alts = new Vector<ACaseAlternativeStm>();
		List<PPattern> plist = getPatternReader().readPatternList();
		checkFor(VDMToken.ARROW, 2236, "Expecting '->' after case pattern list");
		PStm result = readStatement();

		for (PPattern p : plist)
		{
			alts.add(new ACaseAlternativeStm(p.getLocation(), null, p.clone(), result.clone()));
		}

		return alts;
	}

	private ADefLetDefStm readDefStatement(LexLocation token)
			throws ParserException, LexException
	{
		checkFor(VDMToken.DEF, 2239, "Expecting 'def'");
		DefinitionReader dr = getDefinitionReader();
		List<PDefinition> equalsDefs = new Vector<PDefinition>();

		while (lastToken().isNot(VDMToken.IN))
		{
			equalsDefs.add(dr.readEqualsDefinition());
			ignore(VDMToken.SEMICOLON);
		}

		checkFor(VDMToken.IN, 2240, "Expecting 'in' after equals definitions");

		return new ADefLetDefStm(token, equalsDefs, readStatement());
		// return new DefStatement(token, equalsDefs, readStatement());
	}

	private ASpecificationStm readSpecStatement(LexLocation token)
			throws ParserException, LexException
	{
		checkFor(VDMToken.SEQ_OPEN, 2241, "Expecting '['");
		DefinitionReader dr = getDefinitionReader();
		ASpecificationStm stmt = dr.readSpecification(token, false);
		checkFor(VDMToken.SEQ_CLOSE, 2242, "Expecting ']' after specification statement");
		return stmt;
	}

	private PStm readStartStatement(LexLocation location) throws LexException,
			ParserException
	{
		checkFor(VDMToken.START, 2243, "Expecting 'start'");
		checkFor(VDMToken.BRA, 2244, "Expecting 'start('");
		PExp obj = getExpressionReader().readExpression();
		checkFor(VDMToken.KET, 2245, "Expecting ')' after start object");
		return new AStartStm(location, obj);
	}

	private PStm readStartlistStatement(LexLocation location)
			throws LexException, ParserException
	{
		checkFor(VDMToken.STARTLIST, 2246, "Expecting 'startlist'");
		checkFor(VDMToken.BRA, 2247, "Expecting 'startlist('");
		PExp set = getExpressionReader().readExpression();
		checkFor(VDMToken.KET, 2248, "Expecting ')' after startlist objects");
		return new AStartStm(location, set);
	}

	private PStm readDurationStatement(LexLocation location)
			throws LexException, ParserException
	{
		checkFor(VDMToken.DURATION, 2271, "Expecting 'duration'");
		checkFor(VDMToken.BRA, 2272, "Expecting 'duration('");
		PExp duration = getExpressionReader().readExpression();
		checkFor(VDMToken.KET, 2273, "Expecting ')' after duration");
		PStm stmt = readStatement();
		return new ADurationStm(location, duration, stmt);
	}

	private PStm readCyclesStatement(LexLocation location) throws LexException,
			ParserException
	{
		checkFor(VDMToken.CYCLES, 2274, "Expecting 'cycles'");
		checkFor(VDMToken.BRA, 2275, "Expecting 'cycles('");
		PExp duration = getExpressionReader().readExpression();
		checkFor(VDMToken.KET, 2276, "Expecting ')' after cycles");
		PStm stmt = readStatement();
		return new ACyclesStm(location, duration, stmt);
	}
}