package org.overture.parser.tests;

import java.io.File;
import java.util.List;

import org.overture.parser.tests.framework.BaseParserTestCase;
import org.overturetool.vdmj.Settings;
import org.overturetool.vdmj.definitions.ClassDefinition;
import org.overturetool.vdmj.lex.Dialect;
import org.overturetool.vdmj.lex.LexException;
import org.overturetool.vdmj.lex.LexTokenReader;
import org.overturetool.vdmj.syntax.ClassReader;
import org.overturetool.vdmj.syntax.ParserException;

public class SpecificatopnPpTestCase extends BaseParserTestCase<ClassReader>
{
	static boolean hasRunBefore = false;
	public SpecificatopnPpTestCase(File file)
	{
		super(file);
	}
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		Settings.dialect = Dialect.VDM_PP;
	}

	public SpecificatopnPpTestCase(String name, String content)
	{
		super(name, content);
	}

	@Override
	protected ClassReader getReader(LexTokenReader ltr)
	{
		return new ClassReader(ltr);
	}

	@Override
	protected List<ClassDefinition> read(ClassReader reader) throws ParserException, LexException
	{
		return reader.readClasses();
	}

	@Override
	protected String getReaderTypeName()
	{
		return "Specificatopn PP";
	}

	@Override
	protected void setHasRunBefore(boolean b)
	{
		hasRunBefore = b;
	}

	@Override
	protected boolean hasRunBefore()
	{
		return hasRunBefore;
	}

	
}