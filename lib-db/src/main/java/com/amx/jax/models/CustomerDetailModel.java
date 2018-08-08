package com.amx.jax.models;

import java.sql.Date;

public class CustomerDetailModel
{
	private String userType;

	private String civilId;

	private String password;

	private String createdDeviceId;

	private String civilid;

	private int userSequenceNumber;

	private int countryId;//

	private int compCd;

	private String mobile;

	private String email;

	private int languageId;

	private boolean status;

	private String errorMessage;

	private String errorCode;

	private String deviceType;

	private String mobileVerify;

	private String mailVerify;

	private Date lastLogin;

	private String deviceId;

	private String dbStatus;

	private String otp;

	public String getUserType()
	{
		return userType;
	}

	public void setUserType(String userType)
	{
		this.userType = userType;
	}

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
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getCreatedDeviceId()
	{
		return createdDeviceId;
	}

	public void setCreatedDeviceId(String createdDeviceId)
	{
		this.createdDeviceId = createdDeviceId;
	}

	public String getCivilid()
	{
		return civilid;
	}

	public void setCivilid(String civilid)
	{
		this.civilid = civilid;
	}

	public int getUserSequenceNumber()
	{
		return userSequenceNumber;
	}

	public void setUserSequenceNumber(int userSequenceNumber)
	{
		this.userSequenceNumber = userSequenceNumber;
	}

	public int getCountryId()
	{
		return countryId;
	}

	public void setCountryId(int countryId)
	{
		this.countryId = countryId;
	}

	public int getCompCd()
	{
		return compCd;
	}

	public void setCompCd(int compCd)
	{
		this.compCd = compCd;
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

	public boolean getStatus()
	{
		return status;
	}

	public void setStatus(boolean status)
	{
		this.status = status;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	public String getDeviceType()
	{
		return deviceType;
	}

	public void setDeviceType(String deviceType)
	{
		this.deviceType = deviceType;
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

	public String getDbStatus()
	{
		return dbStatus;
	}

	public void setDbStatus(String dbStatus)
	{
		this.dbStatus = dbStatus;
	}

	public String getOtp()
	{
		return otp;
	}

	public void setOtp(String otp)
	{
		this.otp = otp;
	}

	@Override
	public String toString()
	{
		return "CustomerDetailModel [userType=" + userType + ", civilId=" + civilId + ", password=" + password + ", createdDeviceId=" + createdDeviceId + ", civilid=" + civilid + ", userSequenceNumber=" + userSequenceNumber + ", countryId=" + countryId + ", compCd=" + compCd + ", mobile=" + mobile
				+ ", email=" + email + ", languageId=" + languageId + ", status=" + status + ", errorMessage=" + errorMessage + ", errorCode=" + errorCode + ", deviceType=" + deviceType + ", mobileVerify=" + mobileVerify + ", mailVerify=" + mailVerify + ", lastLogin=" + lastLogin + ", deviceId="
				+ deviceId + ", dbStatus=" + dbStatus + "]";
	}

}
