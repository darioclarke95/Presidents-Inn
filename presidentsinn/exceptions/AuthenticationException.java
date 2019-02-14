package com.dario.presidentsinn.exceptions;

public class AuthenticationException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	public AuthenticationException()
	{
		super();
	}
	
	public AuthenticationException(String message)
	{
		super(message);
	}

}
