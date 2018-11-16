package com.amx.jax.models;

import java.math.BigDecimal;

public class CustomizeQuoteAddPol
{
	private BigDecimal quoteSeqNumber;

	private BigDecimal verNumber;

	private String addPolicyTypeCode;

	private BigDecimal yearlyPremium;

	private String optIndex;

	private BigDecimal yearMultiplePremium;

	private String replacementTypeCode;

	public BigDecimal getQuoteSeqNumber()
	{
		return quoteSeqNumber;
	}

	public void setQuoteSeqNumber(BigDecimal quoteSeqNumber)
	{
		this.quoteSeqNumber = quoteSeqNumber;
	}

	public BigDecimal getVerNumber()
	{
		return verNumber;
	}

	public void setVerNumber(BigDecimal verNumber)
	{
		this.verNumber = verNumber;
	}

	public String getAddPolicyTypeCode()
	{
		return addPolicyTypeCode;
	}

	public void setAddPolicyTypeCode(String addPolicyTypeCode)
	{
		this.addPolicyTypeCode = addPolicyTypeCode;
	}

	public BigDecimal getYearlyPremium()
	{
		return yearlyPremium;
	}

	public void setYearlyPremium(BigDecimal yearlyPremium)
	{
		this.yearlyPremium = yearlyPremium;
	}

	public String getOptIndex()
	{
		return optIndex;
	}

	public void setOptIndex(String optIndex)
	{
		this.optIndex = optIndex;
	}

	public BigDecimal getYearMultiplePremium()
	{
		return yearMultiplePremium;
	}

	public void setYearMultiplePremium(BigDecimal yearMultiplePremium)
	{
		this.yearMultiplePremium = yearMultiplePremium;
	}

	public String getReplacementTypeCode()
	{
		return replacementTypeCode;
	}

	public void setReplacementTypeCode(String replacementTypeCode)
	{
		this.replacementTypeCode = replacementTypeCode;
	}

	@Override
	public String toString()
	{
		return "CustomizeQuoteAddPol [quoteSeqNumber=" + quoteSeqNumber + ", verNumber=" + verNumber + ", addPolicyTypeCode=" + addPolicyTypeCode + ", yearlyPremium=" + yearlyPremium + ", optIndex=" + optIndex + ", yearMultiplePremium=" + yearMultiplePremium + ", replacementTypeCode="
				+ replacementTypeCode + "]";
	}
}
