package com.amx.jax.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class CustomerDetails
{
	@ApiMockModelProperty(example = "285011606536", value = "Customer civil Id")
	private String civilId;

	@ApiMockModelProperty(example = "60454022", value = "Customer contact number")
	private String mobile;

	@ApiMockModelProperty(example = "janis@gmail.com", value = "Customer email id")
	private String email;

	@ApiMockModelProperty(example = "0", value = "Prefered language ID 0 is for english")
	private BigDecimal languageId;

	@ApiMockModelProperty(example = "Y", value = "Customer mobile number verified")
	private String mobileVerify;

	@ApiMockModelProperty(example = "Y", value = "Customer email id verified")
	private String mailVerify;

	@ApiMockModelProperty(example = "Fri, 30 Nov 2018 10:01:20 AM", value = "Customer last login")
	private String lastLogin;

	@ApiMockModelProperty(example = "0:0:0:0:0:0:0:1", value = "Customer device Id")
	private String deviceId;

	@ApiMockModelProperty(example = "ONLINE", value = "Customer device type")
	private String deviceType;

	@ApiMockModelProperty(example = "A", value = "Customer db status")
	private String dbStatus;

	@ApiMockModelProperty(example = "123", value = "Customer Sequence Number")
	private BigDecimal custSeqNumber;
	
	@ApiMockModelProperty(example = "123", value = "Customer Sequence Number")
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
