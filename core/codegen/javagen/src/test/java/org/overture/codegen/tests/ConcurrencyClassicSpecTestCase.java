/*
 * #%~
 * VDM Code Generator
 * %%
 * Copyright (C) 2008 - 2014 Overture
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #~%
 */
package org.overture.codegen.tests;

import java.io.File;

import org.overture.codegen.ir.IRSettings;
import org.overture.config.Release;
import org.overture.config.Settings;

public class ConcurrencyClassicSpecTestCase extends SpecificationTestCase
{
	public ConcurrencyClassicSpecTestCase()
	{
	}

	public ConcurrencyClassicSpecTestCase(File file)
	{
		super(file);
	}

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		Settings.release = Release.CLASSIC;
	}
	
	@Override
	public IRSettings getIrSettings()
	{
		IRSettings irSettings = new IRSettings();
		irSettings.setGenerateConc(true);
		irSettings.setCharSeqAsString(true);
		
		return irSettings;
	}
}
