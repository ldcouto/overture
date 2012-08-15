/*******************************************************************************
 * Copyright (c) 2009, 2011 Overture Team and others.
 *
 * Overture is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Overture is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Overture.  If not, see <http://www.gnu.org/licenses/>.
 * 	
 * The Overture Tool web-site: http://overturetool.org/
 *******************************************************************************/
// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 31-07-2009 16:17:13
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   tdCPU.java

package org.overture.ide.plugins.showtraceNextGen.viewer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.overture.interpreter.messages.rtlog.nextgen.NextGenBus;
import org.overture.interpreter.messages.rtlog.nextgen.NextGenCpu;
import org.overture.interpreter.messages.rtlog.nextgen.NextGenObject;
import org.overture.interpreter.messages.rtlog.nextgen.NextGenRTLogger;

import jp.co.csk.vdm.toolbox.VDM.CGException;
import jp.co.csk.vdm.toolbox.VDM.UTIL;

// Referenced classes of package org.overturetool.tracefile.viewer:
//            tdResource, TraceData, tdThread, tdObject
@SuppressWarnings({"unchecked","rawtypes"})
public class tdCPU extends tdResource
{
	static jp.co.csk.vdm.toolbox.VDM.UTIL.VDMCompare vdmComp = new jp.co.csk.vdm.toolbox.VDM.UTIL.VDMCompare();
    private Long id;
	private NextGenRTLogger rtLogger;
    private String name;
    private Boolean expl;
    
    public tdCPU(int cpuID)
    {
    	rtLogger = NextGenRTLogger.getInstance();
    	Map<Integer, NextGenCpu> cpus = rtLogger.getCpuMap();
    	NextGenCpu cpu = cpus.get(cpuID);
    	
    	if(cpu != null)
    	{
    		id = new Long(cpu.id);
            name = cpu.name;
            expl = cpu.expl;
    	}
    	else
    	{
    		//TODO MAA: Exception?
    	}

    }

    public Long getId()
    {
        return id;
    }
 
    public String getName()
    {
        return name;
    }

    public Boolean isExplicit()
    {
        return expl;
    }

    public HashSet connects()
        throws CGException
    {
    	HashSet res = new HashSet();
    	
    	Map<Integer, NextGenBus> buses = rtLogger.getBusMap();
    	
    	for(Integer key : buses.keySet())
    	{
    		NextGenBus currentBus = buses.get(key);
    		List<NextGenCpu> currentBusCpus = currentBus.cpus;
    		
    		for (NextGenCpu currentCpu : currentBusCpus) {
				
    			if(id.intValue() == currentCpu.id)
    				res.add(new Long(currentBus.id));
			}  		
    	}
    	
    	
    	
        return res; //TODO
    }

    public Boolean hasObject(Long pobjid)
    {
    	return rtLogger.getObjectMap().get(pobjid.intValue()) != null;
    }

    
    public HashSet<Long> getObjects()
        throws CGException
    {
    	HashSet<Long> objectIds = new HashSet<Long>();
    	
    	Map<Integer, NextGenObject> objects = rtLogger.getObjectMap();
    	
        for(Integer key : objects.keySet())
        {
        	NextGenObject currentObject = objects.get(key);
        	if(currentObject.cpu.id.intValue() == id)
        		objectIds.add(new Long(currentObject.id));
        }
        
    	return objectIds;
    }

}