package com.amx.jax.models;

public class CompanySetupRequest
{
	private int languageId;
	
	private String userType;

	private String deviceType;
	
	private String deviceId;
	
	public int getLanguageId()
	{
		return languageId;
	}

	public void setLanguageId(int languageId)
	{
		this.languageId = languageId;//
	}

	public String getUserType()
	{
		return userType;
	}

	public void setUserType(String userType)
	{
		this.userType = userType;
	}

	public String getDeviceType()
	{
		return deviceType;
	}

	public void setDeviceType(String deviceType)
	{
		this.deviceType = deviceType;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

}
