package com.insurance.model;

public class RequestOtpModel
{
	private String civilId;

	private String emailId;

	private String mobileNumber;

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

	@Override
	public String toString()
	{
		return "RequestOtpModel [civilId=" + civilId + ", emailId=" + emailId + ", mobileNumber=" + mobileNumber + "]";
	}
}
