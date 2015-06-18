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
package org.overture.codegen.utils;

import java.util.List;
import java.util.Set;

import org.overture.codegen.cgast.INode;
import org.overture.codegen.ir.IrNodeInfo;
import org.overture.codegen.ir.VdmNodeInfo;

public class GeneratedModule extends Generated
{
	private String name;
	private INode irNode;

	public GeneratedModule(String name, INode irDecl, String content)
	{
		super(content);
		this.name = name;
		this.irNode = irDecl;
	}

	public GeneratedModule(String name, Set<VdmNodeInfo> unsupportedIrNodes, Set<IrNodeInfo> unsupportedInTargLang)
	{
		super(unsupportedIrNodes, unsupportedInTargLang);
		this.name = name;
	}

	public GeneratedModule(String name, INode irDecl, List<Exception> mergeErrors)
	{
		super(mergeErrors);
		this.name = name;
		this.irNode = irDecl;
	}

	public String getName()
	{
		return name;
	}
	
	public INode getIrNode()
	{
		return irNode;
	}
}
