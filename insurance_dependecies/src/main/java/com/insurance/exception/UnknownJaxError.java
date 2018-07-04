




package com.insurance.exception;

import com.insurance.error.AmxApiError;

public class UnknownJaxError extends AbstractJaxException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnknownJaxError(AmxApiError error)
	{
		super(error);
	}

}
