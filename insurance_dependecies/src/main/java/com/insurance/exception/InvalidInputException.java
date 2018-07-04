




package com.insurance.exception;

import com.insurance.error.AmxApiError;

public class InvalidInputException extends AbstractJaxException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidInputException(AmxApiError error)
	{
		super(error);
	}

}
