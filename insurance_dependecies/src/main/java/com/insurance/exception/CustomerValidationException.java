




package com.insurance.exception;

import com.insurance.error.AmxApiError;

public class CustomerValidationException extends AbstractJaxException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerValidationException(String errorMessage, String errorCode)
	{
		super(errorMessage, errorCode);
	}

	public CustomerValidationException(AmxApiError error)
	{
		super(error);
	}

}
