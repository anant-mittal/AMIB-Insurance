




package com.insurance.exception;

import com.insurance.error.AmxApiError;
import com.insurance.error.JaxError;

public class JaxSystemError extends AbstractJaxException
{

	private static final long serialVersionUID = 1L;

	public JaxSystemError(AmxApiError error)
	{
		super(error);
	}

	public JaxSystemError()
	{
		super("System error occured");
		this.setError(JaxError.JAX_SYSTEM_ERROR);
	}

	public JaxSystemError(Exception e)
	{
		super(e);
		this.setError(JaxError.JAX_SYSTEM_ERROR);
	}

}
