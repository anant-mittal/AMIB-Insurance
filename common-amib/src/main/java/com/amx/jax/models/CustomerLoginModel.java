
package com.amx.jax.models;

public class CustomerLoginModel
{
	public int CountryId;

	public int CompCd;

	public String UserType;

	public String CivilId;

	public String Password;

	public int userSeqNum;

	public int amibRef;

	public String errorCode;//

	public String errorMessage;
	
	public boolean status;

	public int getCountryId()
	{
		return CountryId;
	}

	public void setCountryId(int countryId)
	{
		CountryId = countryId;
	}

	public int getCompCd()
	{
		return CompCd;
	}

	public void setCompCd(int compCd)
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

	public int getUserSeqNum()
	{
		return userSeqNum;
	}

	public void setUserSeqNum(int userSeqNum)
	{
		this.userSeqNum = userSeqNum;
	}

	public int getAmibRef()
	{
		return amibRef;
	}

	public void setAmibRef(int amibRef)
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
	
	@Override
	public String toString()
	{
		return "CustomerLoginModel [CountryId=" + CountryId + ", CompCd=" + CompCd + ", UserType=" + UserType + ", CivilId=" + CivilId + ", Password=" + Password + ", userSeqNum=" + userSeqNum + ", amibRef=" + amibRef + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage + ", status="
				+ status + "]";
	}

}
