




package com.insurance.exception;

import com.insurance.error.AmxApiError;

public class RemittanceTransactionValidationException extends AbstractJaxException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RemittanceTransactionValidationException(AmxApiError error)
	{
		super(error);
	}

}
