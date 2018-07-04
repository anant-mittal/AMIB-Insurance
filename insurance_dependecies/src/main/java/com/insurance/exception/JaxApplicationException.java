




package com.insurance.exception;

import com.insurance.error.AmxApiError;

public class JaxApplicationException extends AbstractJaxException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JaxApplicationException(AmxApiError error)
	{
		super(error);
	}

}
