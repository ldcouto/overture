package org.overture.codegen.vdm2jml;

import java.util.HashSet;
import java.util.Set;

import org.overture.codegen.cgast.declarations.AClassDeclCG;
import org.overture.codegen.cgast.declarations.AFieldDeclCG;

/**
 * Convenience class for generating parameter names that do not collide with other names used in a given class.
 * 
 * @author pvj
 */
public class NameGen
{
	private Set<String> toAvoid;

	public NameGen(AClassDeclCG classDecl)
	{
		this.toAvoid = new HashSet<String>();

		for (AFieldDeclCG field : classDecl.getFields())
		{
			this.toAvoid.add(field.getName());
		}
	}
	
	public void addName(String name)
	{
		toAvoid.add(name);
	}

	public String getName(String suggestion)
	{
		if (!toAvoid.contains(suggestion))
		{
			toAvoid.add(suggestion);
			return suggestion;
		} else
		{
			int counter = 1;

			String prefix = suggestion + "_";
			
			String newSuggestion = prefix + counter;

			while (toAvoid.contains(newSuggestion))
			{
				counter++;
				newSuggestion = prefix + counter;
			}
			
			toAvoid.add(newSuggestion);
			return newSuggestion;
		}
	}
}
