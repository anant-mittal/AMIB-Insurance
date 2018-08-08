
package com.amx.jax.models;

public class CustomerRegistrationResponse
{
	public String civilid;

	public int userSequenceNumber;
	
	public String getCivilid()
	{
		return civilid;
	}

	public void setCivilid(String civilid)
	{
		this.civilid = civilid;
	}

	public int getUserSequenceNumber()//
	{
		return userSequenceNumber;
	}

	public void setUserSequenceNumber(int userSequenceNumber)
	{
		this.userSequenceNumber = userSequenceNumber;
	}

}
