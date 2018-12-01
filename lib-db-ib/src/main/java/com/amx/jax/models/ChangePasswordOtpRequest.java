package com.amx.jax.models;

import com.amx.jax.swagger.ApiMockModelProperty;

public class ChangePasswordOtpRequest
{
	@ApiMockModelProperty(example = "xxxxxxxxxx", value = "customer civil id")
	String civilId;
	
	public String getCivilId()
	{
		return civilId;
	}

	public void setCivilId(String civilId)
	{
		this.civilId = civilId;
	}
}
