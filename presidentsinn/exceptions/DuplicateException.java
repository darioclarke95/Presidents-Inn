package com.dario.presidentsinn.exceptions;

public class DuplicateException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;

	public DuplicateException()
	{
		super();
	}
	
	public DuplicateException(String message)
	{
		super(message);
	}
}
