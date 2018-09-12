package com.amx.jax.models;

import java.math.BigDecimal;

public class ImageInitInfo
{
	private String docCode;

	private String docDesc;
	
	private BigDecimal docSeqNumber;

	private String requiredCheck;

	private String dispOrder;

	private String docStatus;

	public String getDocCode()
	{
		return docCode;
	}

	public void setDocCode(String docCode)
	{
		this.docCode = docCode;
	}

	public String getDocDesc()
	{
		return docDesc;
	}

	public void setDocDesc(String docDesc)
	{
		this.docDesc = docDesc;
	}

	public String getRequiredCheck()
	{
		return requiredCheck;
	}

	public void setRequiredCheck(String requiredCheck)
	{
		this.requiredCheck = requiredCheck;
	}

	public String getDispOrder()
	{
		return dispOrder;
	}

	public void setDispOrder(String dispOrder)
	{
		this.dispOrder = dispOrder;
	}

	public String getDocStatus()
	{
		return docStatus;
	}

	public void setDocStatus(String docStatus)
	{
		this.docStatus = docStatus;
	}
	
	public BigDecimal getDocSeqNumber()
	{
		return docSeqNumber;
	}

	public void setDocSeqNumber(BigDecimal docSeqNumber)
	{
		this.docSeqNumber = docSeqNumber;
	}
}
