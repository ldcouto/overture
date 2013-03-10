package org.overture.ide.plugins.showtraceNextGen.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.overture.ide.plugins.showtraceNextGen.data.*;
import org.overture.ide.plugins.showtraceNextGen.draw.*;
import org.overture.ide.plugins.showtraceNextGen.event.BusMessageEventHandler;
import org.overture.ide.plugins.showtraceNextGen.event.BusMessageReplyEventHandler;
import org.overture.ide.plugins.showtraceNextGen.event.CPUEventFilter;
import org.overture.ide.plugins.showtraceNextGen.event.EventFilter;
import org.overture.ide.plugins.showtraceNextGen.event.EventHandler;
import org.overture.ide.plugins.showtraceNextGen.event.OperationEventHandler;
import org.overture.ide.plugins.showtraceNextGen.event.OverviewEventFilter;
import org.overture.ide.plugins.showtraceNextGen.event.ThreadEventHandler;
import org.overture.ide.plugins.showtraceNextGen.event.ThreadSwapEventHandler;
import org.overture.interpreter.messages.rtlog.nextgen.*;

public class TraceFileRunner
{
	private TraceData data;
	private Map<Class<?>, EventHandler> eventHandlers;
	
	public TraceFileRunner(TraceData data, ConjectureData conjectures)
	{
		this.data = data;
		this.eventHandlers = new HashMap<Class<?>, EventHandler>();
		
		//Register Events
		eventHandlers.put(NextGenThreadEvent.class, new ThreadEventHandler(data, conjectures));
		eventHandlers.put(NextGenThreadSwapEvent.class, new ThreadSwapEventHandler(data, conjectures));
		eventHandlers.put(NextGenOperationEvent.class, new OperationEventHandler(data, conjectures));
		eventHandlers.put(NextGenBusMessageEvent.class, new BusMessageEventHandler(data, conjectures));
		eventHandlers.put(NextGenBusMessageReplyRequestEvent.class, new BusMessageReplyEventHandler(data, conjectures));
	}

	public void drawArchitecture(GenericTabItem tab) throws Exception 
	{
		data.reset();
		ArchitectureViewer viewer = new ArchitectureViewer();
		viewer.drawArchitecture(tab, data.getCPUs(), data.getBuses());	
	}

	public void drawOverview(GenericTabItem tab, Long eventStartTime)
			throws Exception 
	{
		data.reset();
		TraceEventViewer viewer = new OverviewEventViewer();
		viewer.drawStaticItems(tab, data.getCPUs(), data.getBuses());
		
		drawView(tab, eventStartTime, viewer, new OverviewEventFilter());
		
		//FIXME - MVQ: Dirty hack in order to extend the blue line (Active/Blocked) to the end of canvas.
		for(TraceCPU cpu : data.getCPUs())
		{
			TraceThread tThread = null;
			Long threadId = cpu.getCurrentThread();
			if(threadId != null)
			{
				tThread = data.getThread(threadId);
			}
			((OverviewEventViewer)viewer).updateCpu(tab, cpu, tThread);
		}
	}

	public void drawCpu(GenericTabItem tab, Long cpuId, Long eventStartTime)
			throws Exception 
	{
		data.reset();
		TraceEventViewer viewer = new CpuEventViewer();
		viewer.drawStaticItems(tab, null, data.getConnectedBuses(cpuId));
		
		drawView(tab, eventStartTime, viewer, new CPUEventFilter(cpuId));		
	}
	
	private void drawView(GenericTabItem tab, Long eventStartTime, TraceEventViewer viewer, EventFilter filter) throws Exception
	{
		//Draw events as long as there is room and time
		Long eventTime = 0L;
		TraceEventManager eventManager = data.getEventManager();
		TraceEventViewer dummyViewer = new DummyViewer();
		TraceEventViewer currentView = null;
		List<INextGenEvent> events = eventManager.getEvents(eventTime);
		
		while(!tab.isCanvasOverrun() && eventTime <= eventManager.getLastEventTime()) 
		{
			//TODO: Remove DUMMY. Introduced to hack time travels
			currentView = (eventManager.getCurrentEventTime() < eventStartTime) ? dummyViewer : viewer;
			
			for(INextGenEvent event : events)
			{		
				if(filter.apply(event)) { 
					EventHandler handler = eventHandlers.get(event.getClass());
					handler.handleEvent(event, currentView, tab);
				}
			}
			
			events = eventManager.getEvents();
		}
		
		//Draw a final timemarker for the next series of events
		List<INextGenEvent> nextEvents = eventManager.getEvents();
		if(!nextEvents.isEmpty()) {
			viewer.drawTimeMarker(tab, nextEvents.get(0).getTime().getAbsoluteTime());
		}
				
		//Finally add timelines 
		viewer.drawTimelines(tab);
	}
	
	public Vector<Long> getCpuIds() 
	{
		return data.getOrderedCpus();
	}

	public String getCpuName(Long cpuId) 
	{
		return data.getCPU(cpuId).getName();
	}
}
