package com.dario.presidentsinn.exceptions;

public class OverCapacityException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	public OverCapacityException()
	{
		super();
	}
	
	public OverCapacityException(String message)
	{
		super(message);
	}
}
