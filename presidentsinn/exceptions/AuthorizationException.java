package com.dario.presidentsinn.exceptions;

public class AuthorizationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	public AuthorizationException()
	{
		super();
	}
	
	public AuthorizationException(String message)
	{
		super(message);
	}

}
