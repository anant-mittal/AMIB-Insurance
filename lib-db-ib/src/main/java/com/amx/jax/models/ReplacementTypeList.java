package com.amx.jax.models;

import java.math.BigDecimal;

public class ReplacementTypeList
{
	private String replacementTypeCode;

	private String replacementTypeDesc;

	private BigDecimal yearlyPremium;

	public String getReplacementTypeCode()
	{
		return replacementTypeCode;
	}

	public void setReplacementTypeCode(String replacementTypeCode)
	{
		this.replacementTypeCode = replacementTypeCode;
	}

	public String getReplacementTypeDesc()
	{
		return replacementTypeDesc;
	}

	public void setReplacementTypeDesc(String replacementTypeDesc)
	{
		this.replacementTypeDesc = replacementTypeDesc;
	}

	public BigDecimal getYearlyPremium()
	{
		return yearlyPremium;
	}

	public void setYearlyPremium(BigDecimal yearlyPremium)
	{
		this.yearlyPremium = yearlyPremium;
	}

}
