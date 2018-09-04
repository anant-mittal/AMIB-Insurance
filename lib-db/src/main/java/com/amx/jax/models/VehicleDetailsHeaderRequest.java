package com.amx.jax.models;

import java.math.BigDecimal;

public class VehicleDetailsHeaderRequest
{
	private BigDecimal docNumber;
	
	private BigDecimal policyDuration;
	
	public BigDecimal getPolicyDuration()
	{
		return policyDuration;
	}

	public void setPolicyDuration(BigDecimal policyDuration)
	{
		this.policyDuration = policyDuration;
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
