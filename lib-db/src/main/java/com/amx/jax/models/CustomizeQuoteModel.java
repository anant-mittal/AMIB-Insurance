package com.amx.jax.models;

import java.util.ArrayList;

public class CustomizeQuoteModel
{
	private QuotationDetails quotationDetails;

	ArrayList<QuoteAddPolicyDetails> quoteAddPolicyDetails;

	private TotalPremium totalPremium;

	public QuotationDetails getQuotationDetails()
	{
		return quotationDetails;
	}

	public void setQuotationDetails(QuotationDetails quotationDetails)
	{
		this.quotationDetails = quotationDetails;
	}

	public ArrayList<QuoteAddPolicyDetails> getQuoteAddPolicyDetails()
	{
		return quoteAddPolicyDetails;
	}

	public void setQuoteAddPolicyDetails(ArrayList<QuoteAddPolicyDetails> quoteAddPolicyDetails)
	{
		this.quoteAddPolicyDetails = quoteAddPolicyDetails;
	}

	public TotalPremium getTotalPremium()
	{
		return totalPremium;
	}

	public void setTotalPremium(TotalPremium totalPremium)
	{
		this.totalPremium = totalPremium;
	}
}
