package com.amx.jax.models;

import java.math.BigDecimal;

public class QuotationDetails
{
	private String makeCode;
	
	private String makeDesc;

	private String subMakeCode;

	private String subMakeDesc;
	
	private String companyName;

	private BigDecimal companyCode;

	private String companyShortCode;
	
	private String chassisNumber;
	
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
