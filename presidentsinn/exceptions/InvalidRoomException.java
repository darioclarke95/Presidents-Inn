package com.dario.presidentsinn.exceptions;

public class InvalidRoomException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	public InvalidRoomException()
	{
		super();
	}
	
	public InvalidRoomException(String message)
	{
		super(message);
	}

}
