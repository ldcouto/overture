package org.overture.codegen.tests.utils;

public class ExecutionResult
{
	private String strRepresentation;
	private Object executionResult;
	
	public ExecutionResult(String strRepresentation, Object executionResult)
	{
		this.strRepresentation = strRepresentation;
		this.executionResult = executionResult;
	}
	
	public String getStrRepresentation()
	{
		return strRepresentation;
	}
	public Object getExecutionResult()
	{
		return executionResult;
	}
}
