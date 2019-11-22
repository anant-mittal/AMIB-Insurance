package com.amx.jax.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class RequestQuoteInfo
{
	@ApiMockModelProperty(example = "123", value = "Application sequence number of policy")
	private BigDecimal appSeqNumber;

	@ApiMockModelProperty(example = "40157", value = "Application document number")
	private BigDecimal docNumber;
	
	@ApiMockModelProperty(example = "N", value = "Application Type")
	private String applicationType;

	public String getApplicationType()
	{
		return applicationType;
	}

	public void setApplicationType(String applicationType)
	{
		this.applicationType = applicationType;
	}
	
	public BigDecimal getAppSeqNumber()
	{
		return appSeqNumber;
	}

	public void setAppSeqNumber(BigDecimal appSeqNumber)
	{
		this.appSeqNumber = appSeqNumber;
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
