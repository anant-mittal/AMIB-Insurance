package com.amx.jax.models;

import java.math.BigDecimal;

public class TermsCondition
{
	private String termsAndCondition;
	
	private BigDecimal id;

	public BigDecimal getId()
	{
		return id;
	}

	public void setId(BigDecimal id)
	{
		this.id = id;
	}

	public String getTermsAndCondition()
	{
		return termsAndCondition;
	}

	public void setTermsAndCondition(String termsAndCondition)
	{
		this.termsAndCondition = termsAndCondition;
	}
}
