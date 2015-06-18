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
package org.overture.codegen.ir;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.overture.ast.definitions.AExplicitOperationDefinition;
import org.overture.ast.definitions.PDefinition;
import org.overture.ast.definitions.SClassDefinition;
import org.overture.ast.node.INode;
import org.overture.ast.statements.AIdentifierStateDesignator;
import org.overture.codegen.assistant.AssistantManager;
import org.overture.codegen.assistant.BindAssistantCG;
import org.overture.codegen.assistant.DeclAssistantCG;
import org.overture.codegen.assistant.ExpAssistantCG;
import org.overture.codegen.assistant.LocationAssistantCG;
import org.overture.codegen.assistant.StmAssistantCG;
import org.overture.codegen.assistant.TypeAssistantCG;
import org.overture.codegen.cgast.SBindCG;
import org.overture.codegen.cgast.SDeclCG;
import org.overture.codegen.cgast.SExpCG;
import org.overture.codegen.cgast.SExportCG;
import org.overture.codegen.cgast.SExportsCG;
import org.overture.codegen.cgast.SImportCG;
import org.overture.codegen.cgast.SImportsCG;
import org.overture.codegen.cgast.SModifierCG;
import org.overture.codegen.cgast.SMultipleBindCG;
import org.overture.codegen.cgast.SObjectDesignatorCG;
import org.overture.codegen.cgast.SPatternCG;
import org.overture.codegen.cgast.SStateDesignatorCG;
import org.overture.codegen.cgast.SStmCG;
import org.overture.codegen.cgast.STermCG;
import org.overture.codegen.cgast.STraceCoreDeclCG;
import org.overture.codegen.cgast.STraceDeclCG;
import org.overture.codegen.cgast.STypeCG;
import org.overture.codegen.cgast.declarations.AClassDeclCG;
import org.overture.codegen.cgast.declarations.AModuleDeclCG;
import org.overture.codegen.logging.Logger;
import org.overture.codegen.visitor.CGVisitor;
import org.overture.codegen.visitor.VisitorManager;
import org.overture.typechecker.assistant.TypeCheckerAssistantFactory;

public class IRInfo
{
	// Visitors
	private VisitorManager visitorManager;

	// Assistants
	private AssistantManager assistantManager;

	// VDM assistant factory
	private TypeCheckerAssistantFactory tcFactory;

	// Quotes:
	private List<String> quoteVaues;

	// Unsupported VDM nodes
	private Set<VdmNodeInfo> unsupportedNodes;
	
	// Transformation warnings
	private Set<IrNodeInfo> transformationWarnings;

	// For generating variable names
	private ITempVarGen tempVarNameGen;

	// For configuring code generation
	private IRSettings settings;

	// To look up object initializer call names
	private Map<AExplicitOperationDefinition, String> objectInitCallNames;

	// Object initialization call prefix
	private String objectInitCallPrefix;
	
	// Definitions for identifier state designators
	private Map<AIdentifierStateDesignator, PDefinition> idStateDesignatorDefs;
	
	public IRInfo(String objectInitCallPrefix)
	{
		super();

		this.visitorManager = new VisitorManager();
		this.assistantManager = new AssistantManager();
		this.tcFactory = new TypeCheckerAssistantFactory();
		this.quoteVaues = new LinkedList<String>();
		this.unsupportedNodes = new HashSet<VdmNodeInfo>();
		this.transformationWarnings = new HashSet<IrNodeInfo>();
		this.tempVarNameGen = new TempVarNameGen();

		this.settings = new IRSettings();

		this.objectInitCallPrefix = objectInitCallPrefix;
		this.objectInitCallNames = new HashMap<AExplicitOperationDefinition, String>();
		
		this.idStateDesignatorDefs = new HashMap<AIdentifierStateDesignator, PDefinition>();
	}

	public AssistantManager getAssistantManager()
	{
		return assistantManager;
	}

	public CGVisitor<AClassDeclCG> getClassVisitor()
	{
		return visitorManager.getClassVisitor();
	}
	
	public CGVisitor<AModuleDeclCG> getModuleVisitor()
	{
		return visitorManager.getModuleVisitor();
	}
	
	public CGVisitor<SImportsCG> getImportsVisitor()
	{
		return visitorManager.getImportsVisitor();
	}
	
	public CGVisitor<SImportCG> getImportVisitor()
	{
		return visitorManager.getImportVisitor();
	}

	public CGVisitor<SExportsCG> getExportsVisitor()
	{
		return visitorManager.getExportsVisitor();
	}
	
	public CGVisitor<SExportCG> getExportVisitor()
	{
		return visitorManager.getExportVisitor();
	}
	
	public CGVisitor<SDeclCG> getDeclVisitor()
	{
		return visitorManager.getDeclVisitor();
	}

	public CGVisitor<SExpCG> getExpVisitor()
	{
		return visitorManager.getExpVisitor();
	}

	public CGVisitor<STypeCG> getTypeVisitor()
	{
		return visitorManager.getTypeVisitor();
	}

	public CGVisitor<SStmCG> getStmVisitor()
	{
		return visitorManager.getStmVisitor();
	}

	public CGVisitor<SStateDesignatorCG> getStateDesignatorVisitor()
	{
		return visitorManager.getStateDesignatorVisitor();
	}

	public CGVisitor<SObjectDesignatorCG> getObjectDesignatorVisitor()
	{
		return visitorManager.getObjectDesignatorVisitor();
	}

	public CGVisitor<SMultipleBindCG> getMultipleBindVisitor()
	{
		return visitorManager.getMultipleBindVisitor();
	}

	public CGVisitor<SBindCG> getBindVisitor()
	{
		return visitorManager.getBindVisitor();
	}

	public CGVisitor<SPatternCG> getPatternVisitor()
	{
		return visitorManager.getPatternVisitor();
	}

	public CGVisitor<SModifierCG> getModifierVisitor()
	{
		return visitorManager.getModifierVisitor();
	}

	public CGVisitor<STermCG> getTermVisitor()
	{
		return visitorManager.getTermVisitor();
	}

	public CGVisitor<STraceDeclCG> getTraceDeclVisitor()
	{
		return visitorManager.getTraceDeclVisitor();
	}

	public CGVisitor<STraceCoreDeclCG> getTraceCoreDeclVisitor()
	{
		return visitorManager.getTraceCoreDeclVisitor();
	}

	public ExpAssistantCG getExpAssistant()
	{
		return assistantManager.getExpAssistant();
	}

	public DeclAssistantCG getDeclAssistant()
	{
		return assistantManager.getDeclAssistant();
	}

	public StmAssistantCG getStmAssistant()
	{
		return assistantManager.getStmAssistant();
	}

	public TypeAssistantCG getTypeAssistant()
	{
		return assistantManager.getTypeAssistant();
	}

	public LocationAssistantCG getLocationAssistant()
	{
		return assistantManager.getLocationAssistant();
	}

	public BindAssistantCG getBindAssistant()
	{
		return assistantManager.getBindAssistant();
	}

	public void registerQuoteValue(String value)
	{
		if (value == null || value.isEmpty())
		{
			Logger.getLog().printErrorln("Tried to register invalid qoute value");
		} else
		{
			if (!quoteVaues.contains(value))
			{
				quoteVaues.add(value);
			}
		}
	}

	public TypeCheckerAssistantFactory getTcFactory()
	{
		return tcFactory;
	}

	public List<String> getQuoteValues()
	{
		return quoteVaues;
	}

	public void clearNodes()
	{
		unsupportedNodes.clear();
	}
	
	public void addUnsupportedNode(INode node)
	{
		VdmNodeInfo info = new VdmNodeInfo(node);
		unsupportedNodes.add(info);
	}

	public void addUnsupportedNode(INode node, String reason)
	{
		VdmNodeInfo info = new VdmNodeInfo(node, reason);
		unsupportedNodes.add(info);
	}

	public Set<VdmNodeInfo> getUnsupportedNodes()
	{
		return unsupportedNodes;
	}
	
	public void clearTransformationWarnings()
	{
		transformationWarnings.clear();
	}
	
	public void addTransformationWarning(org.overture.codegen.cgast.INode node, String warning)
	{
		IrNodeInfo info = new IrNodeInfo(node, warning);
		transformationWarnings.add(info);
	}
	
	public Set<IrNodeInfo> getTransformationWarnings()
	{
		return transformationWarnings;
	}

	public ITempVarGen getTempVarNameGen()
	{
		return tempVarNameGen;
	}
	
	public void clear()
	{
		quoteVaues.clear();
		unsupportedNodes.clear();
		transformationWarnings.clear();
		tempVarNameGen.clear();
	}

	public IRSettings getSettings()
	{
		return settings;
	}

	public void setSettings(IRSettings settings)
	{
		this.settings = settings;
	}

	public String getObjectInitializerCall(AExplicitOperationDefinition vdmOp)
	{
		if (objectInitCallNames.containsKey(vdmOp))
		{
			return objectInitCallNames.get(vdmOp);
		} else
		{
			String enclosingClassName = vdmOp.getAncestor(SClassDefinition.class).getName().getName();
			String initName = tempVarNameGen.nextVarName(objectInitCallPrefix
					+ enclosingClassName + "_");
			objectInitCallNames.put(vdmOp, initName);

			return initName;
		}
	}

	public Map<AIdentifierStateDesignator, PDefinition> getIdStateDesignatorDefs()
	{
		return idStateDesignatorDefs;
	}

	public void setIdStateDesignatorDefs(Map<AIdentifierStateDesignator, PDefinition> idDefs)
	{
		this.idStateDesignatorDefs = idDefs;
	}
}
