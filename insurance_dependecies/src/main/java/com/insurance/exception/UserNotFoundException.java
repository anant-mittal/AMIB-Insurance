




package com.insurance.exception;

import com.insurance.error.AmxApiError;

public class UserNotFoundException extends AbstractJaxException
{

	public UserNotFoundException(String errorMessage)
	{
		super(errorMessage);
	}

	public UserNotFoundException(AmxApiError error)
	{
		super(error);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
