
package com.amx.jax.models;

import java.math.BigDecimal;

public class FailureException
{
	private String civilId;
	
	private String emailId;

	private String mobileNumber;
	
	private String motpPrefix;
	
	private String eotpPrefix;
	
	private BigDecimal countryId;

	private BigDecimal compCd;

	private String userType;

	private String deviceType;
	
	private String deviceId;
	
	private BigDecimal languageId;

	private String errorCode;

	private String errorMessage;
	
	public boolean status;
	
	public String exceptionType;
	
	public String exceptionMsg;
	
	public String getExceptionMsg()
	{
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg)
	{
		this.exceptionMsg = exceptionMsg;
	}

	public String getExceptionType()
	{
		return exceptionType;
	}

	public void setExceptionType(String exceptionType)
	{
		this.exceptionType = exceptionType;
	}

	public String getCivilId()
	{
		return civilId;
	}

	public void setCivilId(String civilId)
	{
		this.civilId = civilId;
	}

	public String getEmailId()
	{
		return emailId;
	}

	public void setEmailId(String emailId)
	{
		this.emailId = emailId;
	}

	public String getMobileNumber()
	{
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
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

	public BigDecimal getCountryId()
	{
		return countryId;
	}

	public void setCountryId(BigDecimal countryId)
	{
		this.countryId = countryId;
	}

	public BigDecimal getCompCd()
	{
		return compCd;
	}

	public void setCompCd(BigDecimal compCd)
	{
		this.compCd = compCd;
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

	public BigDecimal getLanguageId()
	{
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId)
	{
		this.languageId = languageId;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public boolean isStatus()
	{
		return status;
	}

	public void setStatus(boolean status)
	{
		this.status = status;
	}

	
}
