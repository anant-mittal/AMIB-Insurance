




package com.insurance.user_registartion.model;

public class CustomerRegistrationDetails
{
	String civilId;

	String mobiileNumber;

	String otp;

	String password;

	public String getCivilId()
	{
		return civilId;
	}

	public void setCivilId(String civilId)
	{
		this.civilId = civilId;
	}

	public String getMobiileNumber()
	{
		return mobiileNumber;
	}

	public void setMobiileNumber(String mobiileNumber)
	{
		this.mobiileNumber = mobiileNumber;
	}

	public String getOtp()
	{
		return otp;
	}

	public void setOtp(String otp)
	{
		this.otp = otp;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	@Override
	public String toString()
	{
		return "CustomerRegistrationDetails [civilId=" + civilId + ", mobiileNumber=" + mobiileNumber + ", otp=" + otp + ", password=" + password + "]";
	}

}
