




package com.insurance.exception;

import com.insurance.error.AmxApiError;

public class ResourceNotFoundException extends AbstractJaxException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(AmxApiError error)
	{
		super(error);
	}

}
