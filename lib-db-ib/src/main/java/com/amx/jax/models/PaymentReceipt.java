package com.amx.jax.models;

import java.math.BigDecimal;

public class PaymentReceipt
{
	private BigDecimal applicationId;

	private BigDecimal customerId;

	private String paymentDate;

	private String paymentMode;

	private BigDecimal amountPaidNumber;

	private String amountPaidWord;

	private String paymentId;

	private String customerName;

	private String civilId;

	private String mobileNumber;

	private String emialId;

	private BigDecimal policyDuration;

	private String governate;

	private String areaDesc;

	private String address;

	private String make;

	private String subMake;

	private String ktNumber;

	private String chasisNumber;

	private BigDecimal modelYear;
	
	private String trnsReceiptRef;
	
	public String getTrnsReceiptRef() 
	{
		return trnsReceiptRef;
	}

	public void setTrnsReceiptRef(String trnsReceiptRef) 
	{
		this.trnsReceiptRef = trnsReceiptRef;
	}

	public BigDecimal getApplicationId()
	{
		return applicationId;
	}

	public void setApplicationId(BigDecimal applicationId)
	{
		this.applicationId = applicationId;
	}

	public BigDecimal getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId)
	{
		this.customerId = customerId;
	}

	public String getPaymentDate()
	{
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate)
	{
		this.paymentDate = paymentDate;
	}

	public String getPaymentMode()
	{
		return paymentMode;
	}

	public void setPaymentMode(String paymentMode)
	{
		this.paymentMode = paymentMode;
	}

	public BigDecimal getAmountPaidNumber()
	{
		return amountPaidNumber;
	}

	public void setAmountPaidNumber(BigDecimal amountPaidNumber)
	{
		this.amountPaidNumber = amountPaidNumber;
	}

	public String getAmountPaidWord()
	{
		return amountPaidWord;
	}

	public void setAmountPaidWord(String amountPaidWord)
	{
		this.amountPaidWord = amountPaidWord;
	}

	public String getPaymentId()
	{
		return paymentId;
	}

	public void setPaymentId(String paymentId)
	{
		this.paymentId = paymentId;
	}

	public String getCustomerName()
	{
		return customerName;
	}

	public void setCustomerName(String customerName)
	{
		this.customerName = customerName;
	}

	public String getCivilId()
	{
		return civilId;
	}

	public void setCivilId(String civilId)
	{
		this.civilId = civilId;
	}

	public String getMobileNumber()
	{
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}

	public String getEmialId()
	{
		return emialId;
	}

	public void setEmialId(String emialId)
	{
		this.emialId = emialId;
	}

	public BigDecimal getPolicyDuration()
	{
		return policyDuration;
	}

	public void setPolicyDuration(BigDecimal policyDuration)
	{
		this.policyDuration = policyDuration;
	}

	public String getGovernate()
	{
		return governate;
	}

	public void setGovernate(String governate)
	{
		this.governate = governate;
	}

	public String getAreaDesc()
	{
		return areaDesc;
	}

	public void setAreaDesc(String areaDesc)
	{
		this.areaDesc = areaDesc;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getMake()
	{
		return make;
	}

	public void setMake(String make)
	{
		this.make = make;
	}

	public String getSubMake()
	{
		return subMake;
	}

	public void setSubMake(String subMake)
	{
		this.subMake = subMake;
	}

	public String getKtNumber()
	{
		return ktNumber;
	}

	public void setKtNumber(String ktNumber)
	{
		this.ktNumber = ktNumber;
	}

	public String getChasisNumber()
	{
		return chasisNumber;
	}

	public void setChasisNumber(String chasisNumber)
	{
		this.chasisNumber = chasisNumber;
	}

	public BigDecimal getModelYear()
	{
		return modelYear;
	}

	public void setModelYear(BigDecimal modelYear)
	{
		this.modelYear = modelYear;
	}
	
	@Override
	public String toString() {
		return "PaymentReceipt [applicationId=" + applicationId + ", customerId=" + customerId + ", paymentDate="
				+ paymentDate + ", paymentMode=" + paymentMode + ", amountPaidNumber=" + amountPaidNumber
				+ ", amountPaidWord=" + amountPaidWord + ", paymentId=" + paymentId + ", customerName=" + customerName
				+ ", civilId=" + civilId + ", mobileNumber=" + mobileNumber + ", emialId=" + emialId
				+ ", policyDuration=" + policyDuration + ", governate=" + governate + ", areaDesc=" + areaDesc
				+ ", address=" + address + ", make=" + make + ", subMake=" + subMake + ", ktNumber=" + ktNumber
				+ ", chasisNumber=" + chasisNumber + ", modelYear=" + modelYear + ", trnsReceiptRef=" + trnsReceiptRef
				+ "]";
	}
}
