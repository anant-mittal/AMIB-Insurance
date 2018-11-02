package com.amx.jax.models;

import java.math.BigDecimal;

public class RequestQuoteInfo
{
	private BigDecimal appSeqNumber;

	private BigDecimal docNumber;
	
	private String applicationType;

	public String getApplicationType()
	{
		return applicationType;
	}

	public void setApplicationType(String applicationType)
	{
		this.applicationType = applicationType;
	}
	
	public BigDecimal getAppSeqNumber()
	{
		return appSeqNumber;
	}

	public void setAppSeqNumber(BigDecimal appSeqNumber)
	{
		this.appSeqNumber = appSeqNumber;
	}

	public BigDecimal getDocNumber()
	{
		return docNumber;
	}

	public void setDocNumber(BigDecimal docNumber)
	{
		this.docNumber = docNumber;
	}
}
