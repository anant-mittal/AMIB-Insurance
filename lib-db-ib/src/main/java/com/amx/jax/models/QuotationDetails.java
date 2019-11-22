package com.amx.jax.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class QuotationDetails
{
	@ApiMockModelProperty(example = "ACU", value = "Vehicle Details Make Code ")
	private String makeCode;
	
	@ApiMockModelProperty(example = "ACURA", value = "Vehicle Details Make Desc ")
	private String makeDesc;

	@ApiMockModelProperty(example = "TV6", value = "Vehicle Details Sub Make Code")
	private String subMakeCode;

	@ApiMockModelProperty(example = "TLX V6", value = "Vehicle Details Sub Make Desc")
	private String subMakeDesc;
	
	@ApiMockModelProperty(example = "GULF TAKAFUL INSURANCE CO KSCC", value = "Insurance Company Name Policy Provider")
	private String companyName;

	@ApiMockModelProperty(example = "100004", value = "Insurance Company Code Policy Provider")
	private BigDecimal companyCode;

	@ApiMockModelProperty(example = "GTIC", value = "Insurance Company Short Code Policy Provider for icon")
	private String companyShortCode;
	
	@ApiMockModelProperty(example = "12356451514541554", value = "Vehicle Chasis Number")
	private String chassisNumber;
	
	@ApiMockModelProperty(example = "1", value = "Policy Duration nin Year")
	private BigDecimal policyDuration;

	public String getMakeCode()
	{
		return makeCode;
	}

	public void setMakeCode(String makeCode)
	{
		this.makeCode = makeCode;
	}

	public String getMakeDesc()
	{
		return makeDesc;
	}

	public void setMakeDesc(String makeDesc)
	{
		this.makeDesc = makeDesc;
	}

	public String getSubMakeCode()
	{
		return subMakeCode;
	}

	public void setSubMakeCode(String subMakeCode)
	{
		this.subMakeCode = subMakeCode;
	}

	public String getSubMakeDesc()
	{
		return subMakeDesc;
	}

	public void setSubMakeDesc(String subMakeDesc)
	{
		this.subMakeDesc = subMakeDesc;
	}

	public String getCompanyName()
	{
		return companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public BigDecimal getCompanyCode()
	{
		return companyCode;
	}

	public void setCompanyCode(BigDecimal companyCode)
	{
		this.companyCode = companyCode;
	}

	public String getCompanyShortCode()
	{
		return companyShortCode;
	}

	public void setCompanyShortCode(String companyShortCode)
	{
		this.companyShortCode = companyShortCode;
	}

	public String getChassisNumber()
	{
		return chassisNumber;
	}

	public void setChassisNumber(String chassisNumber)
	{
		this.chassisNumber = chassisNumber;
	}

	public BigDecimal getPolicyDuration()
	{
		return policyDuration;
	}

	public void setPolicyDuration(BigDecimal policyDuration)
	{
		this.policyDuration = policyDuration;
	}
	
	@Override
	public String toString()
	{
		return "QuotationDetails [makeCode=" + makeCode + ", makeDesc=" + makeDesc + ", subMakeCode=" + subMakeCode + ", subMakeDesc=" + subMakeDesc + ", companyName=" + companyName + ", companyCode=" + companyCode + ", companyShortCode=" + companyShortCode + ", chassisNumber=" + chassisNumber
				+ ", policyDuration=" + policyDuration + "]";
	}

	
}
