package com.amx.jax.models;

public class VehicleDetailsUpdateModel
{
	private boolean status;

	private String errorMessage;

	private String errorCode;

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

	@Override
	public String toString()
	{
		return "VehicleDetailsUpdateModel [status=" + status + ", errorMessage=" + errorMessage + ", errorCode=" + errorCode + "]";
	}

}
