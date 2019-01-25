package com.amx.jax.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class TermsCondition
{
	@ApiMockModelProperty(example = "AMIB TERMS AND CONDIOTION", value = "AMIB terms and conditon")
	private String termsAndCondition;
	
	@ApiMockModelProperty(example = "1", value = "AMIB terms and conditon")
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
