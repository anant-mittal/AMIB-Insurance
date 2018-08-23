
package com.amx.jax.models;

import java.math.BigDecimal;
import java.sql.Date;

public class CustomerDetailResponse
{
	private String civilId;

	private String mobile;

	private String email;

	private BigDecimal languageId;

	private String mobileVerify;

	private String mailVerify;

	private String lastLogin;

	private String deviceId;

	private String deviceType;

	private String dbStatus;

	private BigDecimal custSeqNumber;
	
	private String userName;

	public String getUserName()
	{
		return userName;
	}

	public void setUserName(String userName)
	{
		this.userName = userName;
	}

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

	public BigDecimal getLanguageId()
	{
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId)
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

	public String getLastLogin()
	{
		return lastLogin;
	}

	public void setLastLogin(String lastLogin)
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

	public BigDecimal getCustSeqNumber()
	{
		return custSeqNumber;
	}

	public void setCustSeqNumber(BigDecimal custSeqNumber)
	{
		this.custSeqNumber = custSeqNumber;
	}

}
