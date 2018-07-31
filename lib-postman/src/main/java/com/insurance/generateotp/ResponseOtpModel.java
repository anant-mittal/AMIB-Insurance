




package com.insurance.generateotp;

public class ResponseOtpModel
{

	private static final long serialVersionUID = -7991527354328804802L;

	private String civilId;

	private String emailId;

	private String mobileNumber;

	private String otp;

	private String otpPrefix;

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

	public String getOtp()
	{
		return otp;
	}

	public void setOtp(String otp)
	{
		this.otp = otp;
	}

	public String getOtpPrefix()
	{
		return otpPrefix;
	}

	public void setOtpPrefix(String otpPrefix)
	{
		this.otpPrefix = otpPrefix;
	}

}
