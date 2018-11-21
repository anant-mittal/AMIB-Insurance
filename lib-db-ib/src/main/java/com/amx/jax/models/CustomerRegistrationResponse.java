package com.amx.jax.models;

import java.math.BigDecimal;

public class CustomerRegistrationResponse
{
	public String civilid;

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
