package com.amx.jax.models;

import java.math.BigDecimal;

public class RequestQuoteInfo
{
	private BigDecimal appSeqNumber;

	private BigDecimal docNumber;
	
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
