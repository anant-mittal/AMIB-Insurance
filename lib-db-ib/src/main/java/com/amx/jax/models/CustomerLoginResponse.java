package com.amx.jax.models;

import java.math.BigDecimal;

public class CustomerLoginResponse
{
	public BigDecimal userSeqNum;

	public BigDecimal amibRef;

	private String contactUsEmail;

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
