package com.amx.jax.models;

import java.math.BigDecimal;

public class IncompleteApplModel
{
	BigDecimal appSeqNumber;

	String appStage;
	
	private String errorMessage;

	private String errorCode;
	
	private boolean status;

	public BigDecimal getAppSeqNumber()
	{
		return appSeqNumber;
	}

	public void setAppSeqNumber(BigDecimal appSeqNumber)
	{
		this.appSeqNumber = appSeqNumber;
	}

	public String getAppStage()
	{
		return appStage;
	}

	public void setAppStage(String appStage)
	{
		this.appStage = appStage;
	}
	
	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}
	
	public boolean isStatus()
	{
		return status;
	}

	public void setStatus(boolean status)
	{
		this.status = status;
	}
}
