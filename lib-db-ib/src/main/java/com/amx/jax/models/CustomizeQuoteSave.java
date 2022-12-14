package com.amx.jax.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CustomizeQuoteSave
{
	private BigDecimal quotSeqNumber;

	private BigDecimal verNumber;

	BigDecimal basicPremium;

	private BigDecimal supervisionFees;

	private BigDecimal issueFee;

	private BigDecimal disscountAmt;

	private BigDecimal addCoveragePremium;

	private BigDecimal totalAmount;

	public BigDecimal getQuotSeqNumber()
	{
		return quotSeqNumber;
	}

	public void setQuotSeqNumber(BigDecimal quotSeqNumber)
	{
		this.quotSeqNumber = quotSeqNumber;
	}

	public BigDecimal getVerNumber()
	{
		return verNumber;
	}

	public void setVerNumber(BigDecimal verNumber)
	{
		this.verNumber = verNumber;
	}

	public BigDecimal getBasicPremium()
	{
		return basicPremium;
	}

	public void setBasicPremium(BigDecimal basicPremium)
	{
		this.basicPremium = basicPremium;
	}

	public BigDecimal getSupervisionFees()
	{
		return supervisionFees;
	}

	public void setSupervisionFees(BigDecimal supervisionFees)
	{
		this.supervisionFees = supervisionFees;
	}

	public BigDecimal getIssueFee()
	{
		return issueFee;
	}

	public void setIssueFee(BigDecimal issueFee)
	{
		this.issueFee = issueFee;
	}

	public BigDecimal getDisscountAmt()
	{
		return disscountAmt;
	}

	public void setDisscountAmt(BigDecimal disscountAmt)
	{
		this.disscountAmt = disscountAmt;
	}

	public BigDecimal getAddCoveragePremium()
	{
		return addCoveragePremium;
	}

	public void setAddCoveragePremium(BigDecimal addCoveragePremium)
	{
		this.addCoveragePremium = addCoveragePremium;
	}

	public BigDecimal getTotalAmount()
	{
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount)
	{
		this.totalAmount = totalAmount;
	}

	@Override
	public String toString()
	{
		return "CustomizeQuoteSave [quotSeqNumber=" + quotSeqNumber + ", verNumber=" + verNumber + ", basicPremium=" + basicPremium + ", supervisionFees=" + supervisionFees + ", issueFee=" + issueFee + ", disscountAmt=" + disscountAmt + ", addCoveragePremium=" + addCoveragePremium + ", totalAmount="
				+ totalAmount + "]";
	}
}
