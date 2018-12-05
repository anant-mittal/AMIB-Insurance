package com.amx.jax.models;

import com.amx.jax.swagger.ApiMockModelProperty;

public class ChangePasswordRequest
{
	@ApiMockModelProperty(example = "xxxxxxxxxx", value = "customer password")
	String newPassword;

	public String getNewPassword()
	{
		return newPassword;
	}

	public void setNewPassword(String newPassword)
	{
		this.newPassword = newPassword;
	}

}
