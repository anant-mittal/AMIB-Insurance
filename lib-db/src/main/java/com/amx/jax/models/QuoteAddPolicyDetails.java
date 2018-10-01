package com.amx.jax.models;

import java.math.BigDecimal;
import java.util.ArrayList;

public class QuoteAddPolicyDetails
{
	private boolean replacTypeVisibility;
	
	private String addPolicyTypeCode;

	private String addPolicyTypeDesc;

	private boolean addPolicyTypeEnable;

	private BigDecimal yearlyPremium;

	private String replacementType;

	public String getAddPolicyTypeCode()
	{
		return addPolicyTypeCode;
	}

	public void setAddPolicyTypeCode(String addPolicyTypeCode)
	{
		this.addPolicyTypeCode = addPolicyTypeCode;
	}

	public String getAddPolicyTypeDesc()
	{
		return addPolicyTypeDesc;
	}

	public void setAddPolicyTypeDesc(String addPolicyTypeDesc)
	{
		this.addPolicyTypeDesc = addPolicyTypeDesc;
	}

	public boolean getReplacTypeVisibility()
	{
		return replacTypeVisibility;
	}

	public void setReplacTypeVisibility(boolean replacTypeVisibility)
	{
		this.replacTypeVisibility = replacTypeVisibility;
	}

	public BigDecimal getYearlyPremium()
	{
		return yearlyPremium;
	}

	public void setYearlyPremium(BigDecimal yearlyPremium)
	{
		this.yearlyPremium = yearlyPremium;
	}

	public String getReplacementType()
	{
		return replacementType;
	}

	public void setReplacementType(String replacementType)
	{
		this.replacementType = replacementType;
	}

	public boolean getAddPolicyTypeEnable()
	{
		return addPolicyTypeEnable;
	}

	public void setAddPolicyTypeEnable(boolean addPolicyTypeEnable)
	{
		this.addPolicyTypeEnable = addPolicyTypeEnable;
	}
	
	@Override
	public String toString()
	{
		return "QuoteAddPolicyDetails [addPolicyTypeCode=" + addPolicyTypeCode + ", addPolicyTypeDesc=" + addPolicyTypeDesc + ", replacTypeVisibility=" + replacTypeVisibility + ", addPolicyTypeEnable=" + addPolicyTypeEnable + ", yearlyPremium=" + yearlyPremium + ", replacementType="
				+ replacementType + "]";
	}

	// A.ADDL_POLICY_TY = MotorPersonalAccident , Replacement Type, RoadAccident
	// ADDL_POLTYP_DESC = MotorPersonalAccident , Replacement Type, RoadAccident

	// A.REPCAR_INDIC = replacTypeVisibility = (1 = Show List) , (null or 0 = No
	// List)
	// A.ADDL_OPT_INDIC = replacTypeEnable = (Y = Its On ) , ( N = Its Off )

	// A.YEARLY_PREMIUM =
	// A.PREMIUM =

	// A.REPLACEMENT_TYP =
}
