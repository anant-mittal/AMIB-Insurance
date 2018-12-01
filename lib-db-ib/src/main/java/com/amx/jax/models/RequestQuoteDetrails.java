package com.amx.jax.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class RequestQuoteDetrails
{
	@ApiMockModelProperty(example = "179", value = "Customer Created Aplication Unique App Sequence Number")
	BigDecimal appSeqNumber;

	@ApiMockModelProperty(example = "179", value = "Application Stage")
	String appStage;

	public BigDecimal getAppSeqNumber()
	{
		return appSeqNumber;
	}

	public void setAppSeqNumber(BigDecimal appSeqNumber)
	{
		this.appSeqNumber = appSeqNumber;
	}

	public String getAppStage()
	{
		return appStage;
	}

	public void setAppStage(String appStage)
	{
		this.appStage = appStage;
	}
}
