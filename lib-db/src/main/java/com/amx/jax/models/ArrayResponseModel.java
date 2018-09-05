package com.amx.jax.models;

import java.util.ArrayList;

public class ArrayResponseModel
{
	private ArrayList<?> dataArray;
	
	private boolean status;

	private String errorMessage;

	private String errorCode;
	
	public ArrayList getDataArray()
	{
		return dataArray;
	}

	public void setDataArray(ArrayList<?> dataArray)
	{
		this.dataArray = dataArray;
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
}
