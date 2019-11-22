package com.amx.jax.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class CustomerLoginResponse
{
	@ApiMockModelProperty(example = "245", value = "Customer Sequence Number")
	public BigDecimal userSeqNum;

	@ApiMockModelProperty(example = "354", value = "Customer AMIB Ref Number")
	public BigDecimal amibRef;
	
	@ApiMockModelProperty(example = "amibmotor@almullagroup.com", value = "AMIB email ID")
	private String contactUsEmail;

	@ApiMockModelProperty(example = "68541445", value = "AMI")
	private String contactUsHelpLineNumber;

	public BigDecimal getUserSeqNum()
	{
		return userSeqNum;
	}

	public void setUserSeqNum(BigDecimal userSeqNum)
	{
		this.userSeqNum = userSeqNum;
	}

	public BigDecimal getAmibRef()//
	{
		return amibRef;
	}

	public void setAmibRef(BigDecimal amibRef)
	{
		this.amibRef = amibRef;
	}

	public String getContactUsEmail()
	{
		return contactUsEmail;
	}

	public void setContactUsEmail(String contactUsEmail)
	{
		this.contactUsEmail = contactUsEmail;
	}

	public String getContactUsHelpLineNumber()
	{
		return contactUsHelpLineNumber;
	}

	public void setContactUsHelpLineNumber(String contactUsHelpLineNumber)
	{
		this.contactUsHelpLineNumber = contactUsHelpLineNumber;
	}

	@Override
	public String toString()
	{
		return "CustomerLoginResponse [userSeqNum=" + userSeqNum + ", amibRef=" + amibRef + ", contactUsEmail=" + contactUsEmail + ", contactUsHelpLineNumber=" + contactUsHelpLineNumber + "]";
	}

}
