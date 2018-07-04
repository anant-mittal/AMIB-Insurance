




package com.insurance.exception;

import com.insurance.error.AmxApiError;

public class LimitExeededException extends AbstractJaxException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LimitExeededException(AmxApiError error)
	{
		super(error);
	}

}
