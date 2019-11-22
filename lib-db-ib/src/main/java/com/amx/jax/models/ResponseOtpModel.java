
package com.amx.jax.models;

import com.amx.jax.swagger.ApiMockModelProperty;

public class ResponseOtpModel
{

	private static final long serialVersionUID = -7991527354328804802L;

	@ApiMockModelProperty(example = "GSD ", value = "mobile otp prefix")
	private String motpPrefix;

	@ApiMockModelProperty(example = "AMW", value = "mobile otp prefix")
	private String eotpPrefix;

	public String getMotpPrefix()
	{
		return motpPrefix;
	}

	public void setMotpPrefix(String motpPrefix)
	{
		this.motpPrefix = motpPrefix;
	}

	public String getEotpPrefix()
	{
		return eotpPrefix;
	}

	public void setEotpPrefix(String eotpPrefix)
	{
		this.eotpPrefix = eotpPrefix;
	}

}
