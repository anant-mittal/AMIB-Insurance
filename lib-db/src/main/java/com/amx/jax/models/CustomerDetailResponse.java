
package com.amx.jax.models;

import java.sql.Date;

public class CustomerDetailResponse
{
	private String civilId;
	
	private String mobile;

	private String email;

	private int languageId;

	private String mobileVerify;

	private String mailVerify;

	private Date lastLogin;

	private String deviceId;

	private String deviceType;

	private String dbStatus;

	private int custSeqNumber;
	
	public String getCivilId()
	{
		return civilId;
	}

	public void setCivilId(String civilId)
	{
		this.civilId = civilId;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public int getLanguageId()
	{
		return languageId;
	}

	public void setLanguageId(int languageId)
	{
		this.languageId = languageId;
	}

	public String getMobileVerify()
	{
		return mobileVerify;
	}

	public void setMobileVerify(String mobileVerify)
	{
		this.mobileVerify = mobileVerify;
	}

	public String getMailVerify()
	{
		return mailVerify;
	}

	public void setMailVerify(String mailVerify)
	{
		this.mailVerify = mailVerify;
	}

	public Date getLastLogin()
	{
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin)
	{
		this.lastLogin = lastLogin;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public String getDeviceType()
	{
		return deviceType;
	}

	public void setDeviceType(String deviceType)
	{
		this.deviceType = deviceType;
	}

	public String getDbStatus()
	{
		return dbStatus;
	}

	public void setDbStatus(String dbStatus)
	{
		this.dbStatus = dbStatus;
	}

	public int getCustSeqNumber()
	{
		return custSeqNumber;
	}

	public void setCustSeqNumber(int custSeqNumber)
	{
		this.custSeqNumber = custSeqNumber;
	}

}
