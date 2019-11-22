package com.amx.jax.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class CustomerRegistrationResponse
{
	@ApiMockModelProperty(example = "284090301401", value = "customer civil id")
	public String civilid;

	@ApiMockModelProperty(example = "101", value = "customer civil id")
	public BigDecimal userSequenceNumber;

	public String getCivilid()
	{
		return civilid;
	}

	public void setCivilid(String civilid)
	{
		this.civilid = civilid;
	}

	public BigDecimal getUserSequenceNumber()//
	{
		return userSequenceNumber;
	}

	public void setUserSequenceNumber(BigDecimal userSequenceNumber)
	{
		this.userSequenceNumber = userSequenceNumber;
	}

}
