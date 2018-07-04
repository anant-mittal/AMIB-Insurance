




package com.insurance.exception;

import com.insurance.error.AmxApiError;

public class AlreadyExistsException extends AbstractJaxException
{

	private static final long serialVersionUID = 1L;

	public AlreadyExistsException(AmxApiError error)
	{
		super(error);
	}

}
