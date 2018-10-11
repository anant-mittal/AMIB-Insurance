package com.insurance.model;

public class RegSuccessTemplateModel
{
	private String customerName;

	private String customerCareEmail;
	
	private String brokerageWebsite;

	public String getBrokerageWebsite()
	{
		return brokerageWebsite;
	}

	public void setBrokerageWebsite(String brokerageWebsite)
	{
		this.brokerageWebsite = brokerageWebsite;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
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
