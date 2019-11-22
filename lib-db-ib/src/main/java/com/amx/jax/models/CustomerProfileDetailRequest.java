
package com.amx.jax.models;

import java.math.BigDecimal;

public class CustomerProfileDetailRequest
{
	String civilId;

	BigDecimal custSeqNumber;

	public String getCivilId()
	{
		return civilId;
	}

	public void setCivilId(String civilId)
	{
		this.civilId = civilId;
	}

	public BigDecimal getCustSeqNumber()
	{
		return custSeqNumber;
	}

	public void setCustSeqNumber(BigDecimal custSeqNumber)
	{
		this.custSeqNumber = custSeqNumber;
	}

}
