
package com.insurance.generateotp;

public class ResponseOtpModel
{

	private static final long serialVersionUID = -7991527354328804802L;

	private String civilId;

	private String motpPrefix;

	private String eotpPrefix;

	public String getCivilId()
	{
		return civilId;
	}

	public void setCivilId(String civilId)
	{
		this.civilId = civilId;
	}

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
