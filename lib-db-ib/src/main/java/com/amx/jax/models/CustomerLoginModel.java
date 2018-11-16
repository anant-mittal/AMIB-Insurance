
package com.amx.jax.models;

import java.math.BigDecimal;

public class CustomerLoginModel
{
	public BigDecimal CountryId;

	public BigDecimal CompCd;

	public String UserType;

	public String CivilId;

	public String Password;

	public BigDecimal userSeqNum;

	public BigDecimal amibRef;

	public String errorCode;//

	public String errorMessage;

	public String deviceId;

	public String deviceType;

	public boolean status;

	public BigDecimal getCountryId()
	{
		return CountryId;
	}

	public void setCountryId(BigDecimal countryId)
	{
		CountryId = countryId;
	}

	public BigDecimal getCompCd()
	{
		return CompCd;
	}

	public void setCompCd(BigDecimal compCd)
	{
		CompCd = compCd;
	}

	public String getUserType()
	{
		return UserType;
	}

	public void setUserType(String userType)
	{
		UserType = userType;
	}

	public String getCivilId()
	{
		return CivilId;
	}

	public void setCivilId(String civilId)
	{
		CivilId = civilId;
	}

	public String getPassword()
	{
		return Password;
	}

	public void setPassword(String password)
	{
		Password = password;
	}

	public BigDecimal getUserSeqNum()
	{
		return userSeqNum;
	}

	public void setUserSeqNum(BigDecimal userSeqNum)
	{
		this.userSeqNum = userSeqNum;
	}

	public BigDecimal getAmibRef()
	{
		return amibRef;
	}

	public void setAmibRef(BigDecimal amibRef)
	{
		this.amibRef = amibRef;
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

	public boolean getStatus()
	{
		return status;
	}

	public void setStatus(boolean status)
	{
		this.status = status;
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

	@Override
	public String toString()
	{
		return "CustomerLoginModel [CountryId=" + CountryId + ", CompCd=" + CompCd + ", UserType=" + UserType + ", CivilId=" + CivilId + ", Password=" + Password + ", userSeqNum=" + userSeqNum + ", amibRef=" + amibRef + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", status="
				+ status + "]";
	}

}
