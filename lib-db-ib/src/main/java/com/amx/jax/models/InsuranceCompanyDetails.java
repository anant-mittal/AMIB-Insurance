package com.amx.jax.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class InsuranceCompanyDetails
{
	@ApiMockModelProperty(example = "100008", value = "Insurance Provider Company Code")
	private BigDecimal companyCode;

	@ApiMockModelProperty(example = "TAAZUR TAKAFUL INS CO", value = "Insurance Provider Company Desc")
	private String companyName;

	@ApiMockModelProperty(example = "TTIC", value = "Insurance Provider Company Short Code for Icon")
	private String companyShortCode;

	@ApiMockModelProperty(example = "Y", value = "Insurance Provider Company Short Code for Icon")
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
