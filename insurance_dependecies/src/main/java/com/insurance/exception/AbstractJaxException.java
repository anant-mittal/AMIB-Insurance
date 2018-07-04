




package com.insurance.exception;

import com.insurance.error.AmxApiError;
import com.insurance.error.AmxApiException;
import com.insurance.error.IExceptionEnum;
import com.insurance.error.JaxError;;

public abstract class AbstractJaxException extends AmxApiException
{

	private static final long serialVersionUID = 1L;

	public AbstractJaxException(Exception e)
	{
		super(e);
	}

	public AbstractJaxException(AmxApiError error)
	{
		super(error);
	}

	public AbstractJaxException(String errorMessage)
	{
		super(errorMessage);
	}

	public AbstractJaxException(String errorMessage, String errorKey)
	{
		super(errorMessage, errorKey);
	}

	@Override
	public AmxApiException getInstance(AmxApiError apiError)
	{
		return null;
	}

	@Override
	public IExceptionEnum getErrorIdEnum(String errorId)
	{
		return JaxError.valueOf(errorId);
	}
}
