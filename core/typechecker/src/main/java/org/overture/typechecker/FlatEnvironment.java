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

package org.overture.typechecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import org.overture.ast.definitions.AStateDefinition;
import org.overture.ast.definitions.PDefinition;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.ast.lex.LexNameToken;
import org.overture.ast.typechecker.NameScope;
import org.overture.typechecker.assistant.definition.PDefinitionAssistantTC;
import org.overture.typechecker.assistant.definition.PDefinitionListAssistantTC;



/**
 * Define the type checking environment for a list of definitions.
 */

public class FlatEnvironment extends Environment
{
	protected final List<PDefinition> definitions;
	private boolean limitStateScope = false;

	public FlatEnvironment(List<PDefinition> definitions)
	{
		super(null);
		this.definitions = definitions;
	}

	public FlatEnvironment(List<PDefinition> definitions, Environment env)
	{
		super(env);
		this.definitions = definitions;
	}

	public FlatEnvironment(PDefinition one, Environment env)
	{
		super(env);
		this.definitions = new ArrayList<PDefinition>();
		this.definitions.add(one);
	}

	public void add(PDefinition one)
	{
		definitions.add(one);
	}

	@Override
	public PDefinition findName(LexNameToken name, NameScope scope)
	{
		PDefinition def = PDefinitionListAssistantTC.findName(definitions,name, scope);

		if (def != null)
		{
			return def;
		}

		if (outer == null)
		{
			return null;
		}
		else
		{
			if (limitStateScope)
			{
				scope = NameScope.NAMES;	// Limit NAMESAND(ANY)STATE
			}

			return outer.findName(name, scope);
		}
	}

	@Override
	public PDefinition findType(LexNameToken name, String fromModule)
	{
		PDefinition def = PDefinitionAssistantTC.findType(definitions,name, fromModule);

		if (def != null)
		{
			return def;
		}

		return (outer == null) ? null : outer.findType(name, fromModule);
	}

	@Override
	public AStateDefinition findStateDefinition()
	{
		AStateDefinition def = PDefinitionListAssistantTC.findStateDefinition(definitions);

		if (def != null)
		{
			return def;
		}

   		return (outer == null) ? null : outer.findStateDefinition();
	}

	@Override
	public void unusedCheck()
	{
		PDefinitionListAssistantTC.unusedCheck(definitions);
	}

	@Override
	public boolean isVDMPP()
	{
		return outer == null ? false : outer.isVDMPP();
	}

	@Override
	public boolean isSystem()
	{
		return outer == null ? false : outer.isSystem();
	}

	@Override
	public SClassDefinition findClassDefinition()
	{
		return outer == null ? null : outer.findClassDefinition();
	}

	@Override
	public boolean isStatic()
	{
		return outer == null ? false : outer.isStatic();
	}

	@Override
	public Set<PDefinition> findMatches(LexNameToken name)
	{
		Set<PDefinition> defs = PDefinitionListAssistantTC.findMatches(definitions,name);

		if (outer != null)
		{
			defs.addAll(outer.findMatches(name));
		}

		return defs;
	}

	@Override
    public void markUsed()
    {
		PDefinitionListAssistantTC.markUsed(definitions);
    }

	public void setLimitStateScope(boolean limitStateScope)
	{
		this.limitStateScope = limitStateScope;
	}
}