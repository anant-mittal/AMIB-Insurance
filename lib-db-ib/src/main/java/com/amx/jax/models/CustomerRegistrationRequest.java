package com.amx.jax.models;

import com.amx.jax.swagger.ApiMockModelProperty;

public class CustomerRegistrationRequest
{
	@ApiMockModelProperty(example = "xxxxxxxxxxx", value = "customer password")
	private String password;

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}
}
