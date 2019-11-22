package com.amx.jax.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class TotalPremium
{
	@ApiMockModelProperty(example = "10", value = "Customize Quote Basic Premium")
	private BigDecimal basicPremium;

	@ApiMockModelProperty(example = "50", value = "Customize Quote Additional Coverage Premium")
	private BigDecimal addCoveragePremium;

	@ApiMockModelProperty(example = "30", value = "Customize Quote Issue Fees")
	private BigDecimal issueFee;

	@ApiMockModelProperty(example = "20", value = "Customize Quote Supervision Fees")
	private BigDecimal supervisionFees;

	@ApiMockModelProperty(example = "135", value = "Customize Quote Supervision Fees")
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
