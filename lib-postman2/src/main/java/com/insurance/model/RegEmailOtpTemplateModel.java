package com.insurance.model;

public class RegEmailOtpTemplateModel
{
	private String customerName;

	private String otp;

	private String customerCareEmail;

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	public String getOtp()
	{
		return otp;
	}

	public void setOtp(String otp)
	{
		this.otp = otp;
	}

	public String getCustomerCareEmail()
	{
		return customerCareEmail;
	}

	public void setCustomerCareEmail(String customerCareEmail)
	{
		this.customerCareEmail = customerCareEmail;
	}
}
