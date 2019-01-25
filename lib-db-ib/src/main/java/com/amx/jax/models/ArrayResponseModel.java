package com.amx.jax.models;

import java.math.BigDecimal;
import java.util.ArrayList;

public class ArrayResponseModel
{
	private ArrayList<?> dataArray;
	
	private boolean status;

	private String errorMessage;

	private String errorCode;
	
	private String data;
	
	private Object object;
	
	private BigDecimal numericData;
	
	public BigDecimal getNumericData() 
	{
		return numericData;
	}

	public void setNumericData(BigDecimal numericData) 
	{
		this.numericData = numericData;
	}

	public Object getObject() 
	{
		return object;
	}

	public void setObject(Object object) 
	{
		this.object = object;
	}

	public String getData()
	{
		return data;
	}

	public void setData(String data)
	{
		this.data = data;
	}

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
	
	@Override
	public String toString() {
		return "ArrayResponseModel [dataArray=" + dataArray + ", status=" + status + ", errorMessage=" + errorMessage
				+ ", errorCode=" + errorCode + ", data=" + data + "]";
	}

}
