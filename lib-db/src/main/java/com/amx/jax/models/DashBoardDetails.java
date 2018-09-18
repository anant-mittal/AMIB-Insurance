package com.amx.jax.models;

public class DashBoardDetails
{
	private RequestQuoteDetrails requestQuoteDetrails;

	private ActivePolicyDetails activePolicyDetails;

	private MyProfileDetails myProfileDetails;

	private MyQuotesDetaills myQuotesDetaills;

	public ActivePolicyDetails getActivePolicyDetails()
	{
		return activePolicyDetails;
	}

	public void setActivePolicyDetails(ActivePolicyDetails activePolicyDetails)
	{
		this.activePolicyDetails = activePolicyDetails;
	}

	public MyProfileDetails getMyProfileDetails()
	{
		return myProfileDetails;
	}

	public void setMyProfileDetails(MyProfileDetails myProfileDetails)
	{
		this.myProfileDetails = myProfileDetails;
	}

	public MyQuotesDetaills getMyQuotesDetaills()
	{
		return myQuotesDetaills;
	}

	public void setMyQuotesDetaills(MyQuotesDetaills myQuotesDetaills)
	{
		this.myQuotesDetaills = myQuotesDetaills;
	}

	public RequestQuoteDetrails getRequestQuoteDetrails()
	{
		return requestQuoteDetrails;
	}

	public void setRequestQuoteDetrails(RequestQuoteDetrails requestQuoteDetrails)
	{
		this.requestQuoteDetrails = requestQuoteDetrails;
	}

}
