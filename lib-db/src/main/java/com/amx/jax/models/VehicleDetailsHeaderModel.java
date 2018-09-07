package com.amx.jax.models;

import java.math.BigDecimal;

public class VehicleDetailsHeaderModel
{

	private BigDecimal appSeqNumber;

	private BigDecimal docNumber;

	private boolean status;

	private String errorMessage;

	private String errorCode;

	public BigDecimal getAppSeqNumber()
	{
		return appSeqNumber;
	}

	public void setAppSeqNumber(BigDecimal appSeqNumber)
	{
		this.appSeqNumber = appSeqNumber;
	}

	public boolean isStatus()
	{
		return status;
	}

	public void setStatus(boolean status)
	{
		this.status = status;
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

	public BigDecimal getDocNumber()
	{
		return docNumber;
	}

	public void setDocNumber(BigDecimal docNumber)
	{
		this.docNumber = docNumber;
	}
	
	@Override
	public String toString()
	{
		return "VehicleDetailsHeaderModel [appSeqNumber=" + appSeqNumber + ", docNumber=" + docNumber + ", status=" + status + ", errorMessage=" + errorMessage + ", errorCode=" + errorCode + "]";
	}

	
}
