package com.amx.jax.models;

import java.math.BigDecimal;

public class CustomizeQuoteInfo
{
	private BigDecimal appSeqNumber;
	
	private BigDecimal quoteSeqNumber;
	
	public BigDecimal getAppSeqNumber()
	{
		return appSeqNumber;
	}

	public void setAppSeqNumber(BigDecimal appSeqNumber)
	{
		this.appSeqNumber = appSeqNumber;
	}

	public BigDecimal getQuoteSeqNumber()
	{
		return quoteSeqNumber;
	}

	public void setQuoteSeqNumber(BigDecimal quoteSeqNumber)
	{
		this.quoteSeqNumber = quoteSeqNumber;
	}
}
