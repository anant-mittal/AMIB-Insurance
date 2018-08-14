package com.amx.jax.models;

public class CustomerLoginResponse
{
	public int userSeqNum;

	public int amibRef;
	
	private String contactUsEmail;

	private String contactUsHelpLineNumber;

	public int getUserSeqNum()
	{
		return userSeqNum;
	}

	public void setUserSeqNum(int userSeqNum)
	{
		this.userSeqNum = userSeqNum;
	}

	public int getAmibRef()//
	{
		return amibRef;
	}

	public void setAmibRef(int amibRef)
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
