package com.amx.jax.models;

import java.math.BigDecimal;

public class InsuranceCompanyDetails
{
	private BigDecimal companyCode;

	private String companyName;

	private String companyShortCode;

	private String insuranceSelected;

	public BigDecimal getCompanyCode()
	{
		return companyCode;
	}

	public void setCompanyCode(BigDecimal companyCode)
	{
		this.companyCode = companyCode;
	}

	public String getCompanyName()
	{
		return companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public String getCompanyShortCode()
	{
		return companyShortCode;
	}

	public void setCompanyShortCode(String companyShortCode)
	{
		this.companyShortCode = companyShortCode;
	}

	public String getInsuranceSelected()
	{
		return insuranceSelected;
	}

	public void setInsuranceSelected(String insuranceSelected)
	{
		this.insuranceSelected = insuranceSelected;
	}
}
