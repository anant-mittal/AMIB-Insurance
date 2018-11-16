package com.amx.jax.models;

import java.math.BigDecimal;

public class TotalPremium
{
	private BigDecimal basicPremium;

	private BigDecimal addCoveragePremium;

	private BigDecimal issueFee;

	private BigDecimal supervisionFees;

	private BigDecimal totalAmount;

	public BigDecimal getTotalAmount()
	{
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount)
	{
		this.totalAmount = totalAmount;
	}

	public BigDecimal getBasicPremium()
	{
		return basicPremium;
	}

	public void setBasicPremium(BigDecimal basicPremium)
	{
		this.basicPremium = basicPremium;
	}

	public BigDecimal getAddCoveragePremium()
	{
		return addCoveragePremium;
	}

	public void setAddCoveragePremium(BigDecimal addCoveragePremium)
	{
		this.addCoveragePremium = addCoveragePremium;
	}

	public BigDecimal getIssueFee()
	{
		return issueFee;
	}

	public void setIssueFee(BigDecimal issueFee)
	{
		this.issueFee = issueFee;
	}

	public BigDecimal getSupervisionFees()
	{
		return supervisionFees;
	}

	public void setSupervisionFees(BigDecimal supervisionFees)
	{
		this.supervisionFees = supervisionFees;
	}

	@Override
	public String toString()
	{
		return "TotalPremium [basicPremium=" + basicPremium + ", addCoveragePremium=" + addCoveragePremium + ", issueFee=" + issueFee + ", supervisionFees=" + supervisionFees + ", totalAmount=" + totalAmount + "]";
	}

}
