package com.amx.jax.models;

import com.amx.jax.swagger.ApiMockModelProperty;

public class CustomerLoginRequest
{
	@ApiMockModelProperty(example = "284090301401", value = "customer civil id")
	public String civilId;

	@ApiMockModelProperty(example = "xxxxxxxxxxxx", value = "customer civil id")
	public String password;

	public String getCivilId()
	{
		return civilId;
	}

	public void setCivilId(String civilId)
	{
		this.civilId = civilId;
	}

	public String getPassword()
	{
		return password;//
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}
