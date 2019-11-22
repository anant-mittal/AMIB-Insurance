package com.amx.jax.models;

import java.math.BigDecimal;
import java.util.Date;

import com.amx.jax.swagger.ApiMockModelProperty;

public class MyQuoteModel
{
	@ApiMockModelProperty(example = "1", value = "Country Unique ID")
	private BigDecimal countryId;

	@ApiMockModelProperty(example = "10", value = "Company ID")
	private BigDecimal compCd;

	@ApiMockModelProperty(example = "124", value = "Application Sequence Number")
	private BigDecimal appSeqNumber;

	@ApiMockModelProperty(example = "28-Nov-18", value = "Application Applied Date")
	private String appDate;

	@ApiMockModelProperty(example = "NEW", value = "Application Applied Type New/Old")
	private String appType;

	@ApiMockModelProperty(example = "1", value = "Insurance Policy Duration")
	private BigDecimal policyDuration;

	@ApiMockModelProperty(example = "Q", value = "Insurance Application Status")
	private String appStatus;

	@ApiMockModelProperty(example = "A", value = "Insurance Status")
	private String status;

	@ApiMockModelProperty(example = "28-Nov-18", value = "Insurance Quote Date")
	private String quoteDate;

	@ApiMockModelProperty(example = "124", value = "Insurance Unique Quote Sequence Number ")
	private BigDecimal quoteSeqNumber;

	@ApiMockModelProperty(example = "1", value = "Insurance Ver Number ")
	private BigDecimal verNumber;

	@ApiMockModelProperty(example = "TAAZUR TAKAFUL INS CO", value = "Insurance Provider Company Desc")
	private String companyName;

	@ApiMockModelProperty(example = "100008", value = "Insurance Provider Company Code")
	private BigDecimal companyCode;

	@ApiMockModelProperty(example = "TTIC", value = "Insurance Provider Company Short Code for Icon")
	private String companyShortCode;

	@ApiMockModelProperty(example = "ACU", value = "Vehicle Details Make Code ")
	private String makeCode;

	@ApiMockModelProperty(example = "ACURA", value = "Vehicle Details Make Desc ")
	private String makeDesc;

	@ApiMockModelProperty(example = "TV6", value = "Vehicle Details Sub Make Code")
	private String subMakeCode;

	@ApiMockModelProperty(example = "TLX V6", value = "Vehicle Details Sub Make Desc")
	private String subMakeDesc;

	@ApiMockModelProperty(example = "2017", value = "Vehicle Model Year")
	private BigDecimal modelYear;

	@ApiMockModelProperty(example = "SAL", value = "Vehicle Shape Code")
	private String shapeCode;

	@ApiMockModelProperty(example = "SALON", value = "Vehicle Shape Desc")
	private String shapeDesc;

	@ApiMockModelProperty(example = "ADC", value = "Vehicle Colour Code")
	private String colourCode;

	@ApiMockModelProperty(example = "ADD COLUOR", value = "Vehicle Colour Desc")
	private String colourDesc;

	@ApiMockModelProperty(example = "4", value = "Vehicle Seating Capacity/ Nubmber of Passanger")
	private BigDecimal numberOfPassenger;

	@ApiMockModelProperty(example = "12356451514541554", value = "Vehicle Chasis Number")
	private String chassisNumber;

	@ApiMockModelProperty(example = "12345", value = "Vehicle KT Number")
	private String ktNumber;

	@ApiMockModelProperty(example = "N", value = "Vehicle Condition Code")
	private String vehicleConditionCode;

	@ApiMockModelProperty(example = "New", value = "Vehicle Condition Desc")
	private String vehicleConditionDesc;

	@ApiMockModelProperty(example = "BR", value = "Vehicle Purpose Code")
	private String purposeCode;

	@ApiMockModelProperty(example = "TAXI", value = "Vehicle Purpose Desc")
	private String purposeDesc;

	@ApiMockModelProperty(example = "B", value = "Vehicle Fuel Type Code")
	private String fuelCode;

	@ApiMockModelProperty(example = "BATTERY", value = "Vehicle Fuel Type Desc")
	private String fuelDesc;

	@ApiMockModelProperty(example = "1200.000", value = "Max Vehicle Vallue")
	private BigDecimal vehicleValue;

	@ApiMockModelProperty(example = "10", value = "Insurance Basic Premium")
	private BigDecimal basicPremium;

	@ApiMockModelProperty(example = "25", value = "Insurance Supervision Fees")
	private BigDecimal supervisionFees;

	@ApiMockModelProperty(example = "35", value = "Insurance Issue Fees")
	private BigDecimal issueFee;

	@ApiMockModelProperty(example = "0", value = "Insurance Discount Available")
	private BigDecimal discount;

	@ApiMockModelProperty(example = "100", value = "Insurance Additional Coverage Premium")
	private BigDecimal addCoveragePremium;

	@ApiMockModelProperty(example = "180", value = "Insurance Net/Total Amount")
	private BigDecimal netAmount;

	@ApiMockModelProperty(example = "Conditions to be entered manually on quotation screen", value = "Conditions to be entered manually on quotation screen")
	private String polCondition;

	@ApiMockModelProperty(example = "SEDAN", value = "Vehicle Type")
	private String vehicleType;
	
	@ApiMockModelProperty(example = "N", value = "Vehicle Type")
	private String paymentProcessError;
	
	@ApiMockModelProperty(example = "DD-MM-YYYY", value = "Policy Start Date")
	private Date policyStartDate;

	public Date getPolicyStartDate() {
		return policyStartDate;
	}

	public void setPolicyStartDate(Date policyStartDate) {
		this.policyStartDate = policyStartDate;
	}

	public String getPaymentProcessError() 
	{
		return paymentProcessError;
	}

	public void setPaymentProcessError(String paymentProcessError) 
	{
		this.paymentProcessError = paymentProcessError;
	}

	public BigDecimal getCountryId()
	{
		return countryId;
	}

	public void setCountryId(BigDecimal countryId)
	{
		this.countryId = countryId;
	}

	public BigDecimal getCompCd()
	{
		return compCd;
	}

	public void setCompCd(BigDecimal compCd)
	{
		this.compCd = compCd;
	}

	public BigDecimal getAppSeqNumber()
	{
		return appSeqNumber;
	}

	public void setAppSeqNumber(BigDecimal appSeqNumber)
	{
		this.appSeqNumber = appSeqNumber;
	}

	public String getAppDate()
	{
		return appDate;
	}

	public void setAppDate(String appDate)
	{
		this.appDate = appDate;
	}

	public String getAppType()
	{
		return appType;
	}

	public void setAppType(String appType)
	{
		this.appType = appType;
	}

	public BigDecimal getPolicyDuration()
	{
		return policyDuration;
	}

	public void setPolicyDuration(BigDecimal policyDuration)
	{
		this.policyDuration = policyDuration;
	}

	public String getAppStatus()
	{
		return appStatus;
	}

	public void setAppStatus(String appStatus)
	{
		this.appStatus = appStatus;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getQuoteDate()
	{
		return quoteDate;
	}

	public void setQuoteDate(String quoteDate)
	{
		this.quoteDate = quoteDate;
	}

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

	public String getCompanyName()
	{
		return companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public BigDecimal getCompanyCode()
	{
		return companyCode;
	}

	public void setCompanyCode(BigDecimal companyCode)
	{
		this.companyCode = companyCode;
	}

	public String getCompanyShortCode()
	{
		return companyShortCode;
	}

	public void setCompanyShortCode(String companyShortCode)
	{
		this.companyShortCode = companyShortCode;
	}

	public String getMakeCode()
	{
		return makeCode;
	}

	public void setMakeCode(String makeCode)
	{
		this.makeCode = makeCode;
	}

	public String getMakeDesc()
	{
		return makeDesc;
	}

	public void setMakeDesc(String makeDesc)
	{
		this.makeDesc = makeDesc;
	}

	public String getSubMakeCode()
	{
		return subMakeCode;
	}

	public void setSubMakeCode(String subMakeCode)
	{
		this.subMakeCode = subMakeCode;
	}

	public String getSubMakeDesc()
	{
		return subMakeDesc;
	}

	public void setSubMakeDesc(String subMakeDesc)
	{
		this.subMakeDesc = subMakeDesc;
	}

	public BigDecimal getModelYear()
	{
		return modelYear;
	}

	public void setModelYear(BigDecimal modelYear)
	{
		this.modelYear = modelYear;
	}

	public String getShapeCode()
	{
		return shapeCode;
	}

	public void setShapeCode(String shapeCode)
	{
		this.shapeCode = shapeCode;
	}

	public String getShapeDesc()
	{
		return shapeDesc;
	}

	public void setShapeDesc(String shapeDesc)
	{
		this.shapeDesc = shapeDesc;
	}

	public String getColourCode()
	{
		return colourCode;
	}

	public void setColourCode(String colourCode)
	{
		this.colourCode = colourCode;
	}

	public String getColourDesc()
	{
		return colourDesc;
	}

	public void setColourDesc(String colourDesc)
	{
		this.colourDesc = colourDesc;
	}

	public BigDecimal getNumberOfPassenger()
	{
		return numberOfPassenger;
	}

	public void setNumberOfPassenger(BigDecimal numberOfPassenger)
	{
		this.numberOfPassenger = numberOfPassenger;
	}

	public String getChassisNumber()
	{
		return chassisNumber;
	}

	public void setChassisNumber(String chassisNumber)
	{
		this.chassisNumber = chassisNumber;
	}

	public String getKtNumber()
	{
		return ktNumber;
	}

	public void setKtNumber(String ktNumber)
	{
		this.ktNumber = ktNumber;
	}

	public String getVehicleConditionCode()
	{
		return vehicleConditionCode;
	}

	public void setVehicleConditionCode(String vehicleConditionCode)
	{
		this.vehicleConditionCode = vehicleConditionCode;
	}

	public String getVehicleConditionDesc()
	{
		return vehicleConditionDesc;
	}

	public void setVehicleConditionDesc(String vehicleConditionDesc)
	{
		this.vehicleConditionDesc = vehicleConditionDesc;
	}

	public String getPurposeCode()
	{
		return purposeCode;
	}

	public void setPurposeCode(String purposeCode)
	{
		this.purposeCode = purposeCode;
	}

	public String getPurposeDesc()
	{
		return purposeDesc;
	}

	public void setPurposeDesc(String purposeDesc)
	{
		this.purposeDesc = purposeDesc;
	}

	public String getFuelCode()
	{
		return fuelCode;
	}

	public void setFuelCode(String fuelCode)
	{
		this.fuelCode = fuelCode;
	}

	public String getFuelDesc()
	{
		return fuelDesc;
	}

	public void setFuelDesc(String fuelDesc)
	{
		this.fuelDesc = fuelDesc;
	}

	public BigDecimal getVehicleValue()
	{
		return vehicleValue;
	}

	public void setVehicleValue(BigDecimal vehicleValue)
	{
		this.vehicleValue = vehicleValue;
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

	public BigDecimal getDiscount()
	{
		return discount;
	}

	public void setDiscount(BigDecimal discount)
	{
		this.discount = discount;
	}

	public BigDecimal getAddCoveragePremium()
	{
		return addCoveragePremium;
	}

	public void setAddCoveragePremium(BigDecimal addCoveragePremium)
	{
		this.addCoveragePremium = addCoveragePremium;
	}

	public BigDecimal getNetAmount()
	{
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount)
	{
		this.netAmount = netAmount;
	}

	public String getPolCondition()
	{
		return polCondition;
	}

	public void setPolCondition(String polCondition)
	{
		this.polCondition = polCondition;
	}

	public String getVehicleType()
	{
		return vehicleType;
	}

	public void setVehicleType(String vehicleType)
	{
		this.vehicleType = vehicleType;
	}

	@Override
	public String toString()
	{
		return "MyQuoteModel [countryId=" + countryId + ", compCd=" + compCd + ", appSeqNumber=" + appSeqNumber + ", appDate=" + appDate + ", appType=" + appType + ", policyDuration=" + policyDuration + ", appStatus=" + appStatus + ", status=" + status + ", quoteDate=" + quoteDate
				+ ", quoteSeqNumber=" + quoteSeqNumber + ", verNumber=" + verNumber + ", companyName=" + companyName + ", companyCode=" + companyCode + ", companyShortCode=" + companyShortCode + ", makeCode=" + makeCode + ", makeDesc=" + makeDesc + ", subMakeCode=" + subMakeCode + ", subMakeDesc="
				+ subMakeDesc + ", modelYear=" + modelYear + ", shapeCode=" + shapeCode + ", shapeDesc=" + shapeDesc + ", colourCode=" + colourCode + ", colourDesc=" + colourDesc + ", numberOfPassenger=" + numberOfPassenger + ", chassisNumber=" + chassisNumber + ", ktNumber=" + ktNumber
				+ ", vehicleConditionCode=" + vehicleConditionCode + ", vehicleConditionDesc=" + vehicleConditionDesc + ", purposeCode=" + purposeCode + ", purposeDesc=" + purposeDesc + ", fuelCode=" + fuelCode + ", fuelDesc=" + fuelDesc + ", vehicleValue=" + vehicleValue + ", basicPremium="
				+ basicPremium + ", supervisionFees=" + supervisionFees + ", issueFee=" + issueFee + ", discount=" + discount + ", addCoveragePremium=" + addCoveragePremium + ", netAmount=" + netAmount + ", polCondition=" + polCondition + ", vehicleType=" + vehicleType + "]";
	}

}
