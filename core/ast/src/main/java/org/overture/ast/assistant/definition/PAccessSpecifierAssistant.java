package org.overture.ast.assistant.definition;

import org.overture.ast.assistant.IAstAssistantFactory;
import org.overture.ast.definitions.AProtectedAccess;
import org.overture.ast.definitions.APublicAccess;
import org.overture.ast.factory.AstFactory;
import org.overture.ast.types.AAccessSpecifierAccessSpecifier;

public class PAccessSpecifierAssistant
{

	protected static IAstAssistantFactory af;

	@SuppressWarnings("static-access")
	public PAccessSpecifierAssistant(IAstAssistantFactory af)
	{
		this.af = af;
	}


	public boolean isStatic(AAccessSpecifierAccessSpecifier access)
	{

		return access != null && access.getStatic() != null;
	}

	public boolean isPublic(AAccessSpecifierAccessSpecifier access)
	{

		return access != null && access.getAccess() instanceof APublicAccess;
	}

	public AAccessSpecifierAccessSpecifier getPublic()
	{
		return AstFactory.newAAccessSpecifierAccessSpecifier(new APublicAccess(), false, false);
	}

	public AAccessSpecifierAccessSpecifier getProtected()
	{
		return AstFactory.newAAccessSpecifierAccessSpecifier(new AProtectedAccess(), false, false);
	}

}
