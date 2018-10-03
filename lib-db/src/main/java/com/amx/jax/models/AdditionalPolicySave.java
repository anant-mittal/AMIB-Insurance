package com.amx.jax.models;

import java.math.BigDecimal;

public class AdditionalPolicySave
{
	private String addPolicyTypeCode;

	private BigDecimal yearlyPremium;

	private String replacementType;

	private String addPolicyTypeEnable;

	private BigDecimal yearMultiplePremium;

	public String getAddPolicyTypeCode()
	{
		return addPolicyTypeCode;
	}

	public void setAddPolicyTypeCode(String addPolicyTypeCode)
	{
		this.addPolicyTypeCode = addPolicyTypeCode;
	}

	public BigDecimal getYearlyPremium()
	{
		return yearlyPremium;
	}

	public void setYearlyPremium(BigDecimal yearlyPremium)
	{
		this.yearlyPremium = yearlyPremium;
	}

	public String getReplacementType()
	{
		return replacementType;
	}

	public void setReplacementType(String replacementType)
	{
		this.replacementType = replacementType;
	}

	public String getAddPolicyTypeEnable()
	{
		return addPolicyTypeEnable;
	}

	public void setAddPolicyTypeEnable(String addPolicyTypeEnable)
	{
		this.addPolicyTypeEnable = addPolicyTypeEnable;
	}

	public BigDecimal getYearMultiplePremium()
	{
		return yearMultiplePremium;
	}

	public void setYearMultiplePremium(BigDecimal yearMultiplePremium)
	{
		this.yearMultiplePremium = yearMultiplePremium;
	}
}
