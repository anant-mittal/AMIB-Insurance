package com.amx.jax.models;

import java.math.BigDecimal;

public class IncompleteApplRequest
{
	BigDecimal appSeqNumber;

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
