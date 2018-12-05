package com.amx.jax.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class CustomizeQuoteInfo
{
	
	@ApiMockModelProperty(example = "121", value = "Customer country code")
	private BigDecimal quoteSeqNumber;
	
	public BigDecimal getQuoteSeqNumber()
	{
		return quoteSeqNumber;
	}

	public void setQuoteSeqNumber(BigDecimal quoteSeqNumber)
	{
		this.quoteSeqNumber = quoteSeqNumber;
	}
}
