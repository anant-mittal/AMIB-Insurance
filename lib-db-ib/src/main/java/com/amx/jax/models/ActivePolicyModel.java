package com.amx.jax.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class ActivePolicyModel
{
	@ApiMockModelProperty(example = "1", value = "Country Unique ID")
	private BigDecimal countryId;

	@ApiMockModelProperty(example = "10", value = "Company ID")
	private BigDecimal compCd;

	@ApiMockModelProperty(example = "40157", value = "Application Doc Number")
	private BigDecimal docNumber;

	@ApiMockModelProperty(example = "23-Nov-18", value = "Application Doc Date")
	private String docDate;

	@ApiMockModelProperty(example = "121", value = "Application Finance")
	private BigDecimal finance;

	@ApiMockModelProperty(example = "15", value = "Application Showroom")
	private BigDecimal showRoom;

	@ApiMockModelProperty(example = "55", value = "Sales Man")
	private BigDecimal salesman;

	@ApiMockModelProperty(example = "428689", value = "Customer AMIB Ref Number")
	private BigDecimal userAmibCustRef;

	@ApiMockModelProperty(example = "Tiren Kerki", value = "Insured English Name")
	private String insuredEnglishName;

	@ApiMockModelProperty(example = "Tiren Kerki", value = "Insured Arabic Name")
	private String insuredArabicName;

	@ApiMockModelProperty(example = "280052003608", value = "ID Number")
	private BigDecimal idNumber;

	@ApiMockModelProperty(example = "100008", value = "Insured Company Code")
	private BigDecimal insCompanyCode;

	@ApiMockModelProperty(example = "TAAZUR TAKAFUL INS CO", value = "Insurance Provider Company Desc")
	private String insCompanyDesc;
	
	@ApiMockModelProperty(example = "TTIC", value = "Insurance Provider Company Short Code for Icon")
	private String insCompanyPrefix;

	@ApiMockModelProperty(example = "428689", value = "")
	private BigDecimal dbcust;

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
	private BigDecimal noPass;

	@ApiMockModelProperty(example = "12356451514541554", value = "Vehicle Chasis Number")
	private String chassis;

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

	@ApiMockModelProperty(example = "1", value = "Vehicle SR Number")
	private BigDecimal vehicleSrNumber;

	@ApiMockModelProperty(example = "MOD", value = "Insurance policy type code")
	private String policyTypeCode;

	@ApiMockModelProperty(example = "MOTOR OWN DAMAGE", value = "Insurance policy type Desc")
	private String policyTypeDesc;

	@ApiMockModelProperty(example = "721805900712", value = "Insurance policy Number")
	private String policyNumber;

	@ApiMockModelProperty(example = "1200.000", value = "Max Insured Amount")
	private BigDecimal maxInsuredAmount;

	@ApiMockModelProperty(example = "23-Nov-18", value = "Insurance Policy Start Date")
	private String startDate;

	@ApiMockModelProperty(example = "22-Nov-19", value = "Insurance Policy End Date")
	private String endDate;

	@ApiMockModelProperty(example = "1", value = "Supervision Key")
	private BigDecimal supervisionKey;

	@ApiMockModelProperty(example = "10", value = "Insurance issue fees")
	private BigDecimal issueFee;

	@ApiMockModelProperty(example = "107", value = "Total Premium")
	private BigDecimal premium;

	@ApiMockModelProperty(example = "0", value = "Total Discount")
	private BigDecimal discount;

	@ApiMockModelProperty(example = "N", value = "To disable the field")
	private String renewalIndic;
	
	@ApiMockModelProperty(example = "B", value = "Vehicle Fuel Type Code")
	private String fuelCode;
	
	@ApiMockModelProperty(example = "BATTERY", value = "Vehicle Fuel Type Desc")
	private String fuelDesc;
	
	@ApiMockModelProperty(example = "N", value = "Specify if insurance is to be renewed")
	private String renewableApplCheck;
	
	public String getRenewableApplCheck()
	{
		return renewableApplCheck;
	}

	public void setRenewableApplCheck(String renewableApplCheck)
	{
		this.renewableApplCheck = renewableApplCheck;
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

	public BigDecimal getDocNumber()
	{
		return docNumber;
	}

	public void setDocNumber(BigDecimal docNumber)
	{
		this.docNumber = docNumber;
	}

	public String getDocDate()
	{
		return docDate;
	}

	public void setDocDate(String docDate)
	{
		this.docDate = docDate;
	}

	public BigDecimal getFinance()
	{
		return finance;
	}

	public void setFinance(BigDecimal finance)
	{
		this.finance = finance;
	}

	public BigDecimal getShowRoom()
	{
		return showRoom;
	}

	public void setShowRoom(BigDecimal showRoom)
	{
		this.showRoom = showRoom;
	}

	public BigDecimal getSalesman()
	{
		return salesman;
	}

	public void setSalesman(BigDecimal salesman)
	{
		this.salesman = salesman;
	}

	public BigDecimal getUserAmibCustRef()
	{
		return userAmibCustRef;
	}

	public void setUserAmibCustRef(BigDecimal userAmibCustRef)
	{
		this.userAmibCustRef = userAmibCustRef;
	}

	public String getInsuredEnglishName()
	{
		return insuredEnglishName;
	}

	public void setInsuredEnglishName(String insuredEnglishName)
	{
		this.insuredEnglishName = insuredEnglishName;
	}

	public String getInsuredArabicName()
	{
		return insuredArabicName;
	}

	public void setInsuredArabicName(String insuredArabicName)
	{
		this.insuredArabicName = insuredArabicName;
	}

	public BigDecimal getIdNumber()
	{
		return idNumber;
	}

	public void setIdNumber(BigDecimal idNumber)
	{
		this.idNumber = idNumber;
	}

	public BigDecimal getInsCompanyCode()
	{
		return insCompanyCode;
	}

	public void setInsCompanyCode(BigDecimal insCompanyCode)
	{
		this.insCompanyCode = insCompanyCode;
	}

	public String getInsCompanyDesc()
	{
		return insCompanyDesc;
	}

	public void setInsCompanyDesc(String insCompanyDesc)
	{
		this.insCompanyDesc = insCompanyDesc;
	}

	public String getInsCompanyPrefix()
	{
		return insCompanyPrefix;
	}

	public void setInsCompanyPrefix(String insCompanyPrefix)
	{
		this.insCompanyPrefix = insCompanyPrefix;
	}
	
	public BigDecimal getDbcust()
	{
		return dbcust;
	}

	public void setDbcust(BigDecimal dbcust)
	{
		this.dbcust = dbcust;
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

	public BigDecimal getNoPass()
	{
		return noPass;
	}

	public void setNoPass(BigDecimal noPass)
	{
		this.noPass = noPass;
	}

	public String getChassis()
	{
		return chassis;
	}

	public void setChassis(String chassis)
	{
		this.chassis = chassis;
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

	public BigDecimal getVehicleSrNumber()
	{
		return vehicleSrNumber;
	}

	public void setVehicleSrNumber(BigDecimal vehicleSrNumber)
	{
		this.vehicleSrNumber = vehicleSrNumber;
	}

	public String getPolicyTypeCode()
	{
		return policyTypeCode;
	}

	public void setPolicyTypeCode(String policyTypeCode)
	{
		this.policyTypeCode = policyTypeCode;
	}

	public String getPolicyTypeDesc()
	{
		return policyTypeDesc;
	}

	public void setPolicyTypeDesc(String policyTypeDesc)
	{
		this.policyTypeDesc = policyTypeDesc;
	}

	public String getPolicyNumber()
	{
		return policyNumber;
	}

	public void setPolicyNumber(String policyNumber)
	{
		this.policyNumber = policyNumber;
	}

	public BigDecimal getMaxInsuredAmount()
	{
		return maxInsuredAmount;
	}

	public void setMaxInsuredAmount(BigDecimal maxInsuredAmount)
	{
		this.maxInsuredAmount = maxInsuredAmount;
	}

	public String getStartDate()
	{
		return startDate;
	}

	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}

	public String getEndDate()
	{
		return endDate;
	}

	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}

	public BigDecimal getSupervisionKey()
	{
		return supervisionKey;
	}

	public void setSupervisionKey(BigDecimal supervisionKey)
	{
		this.supervisionKey = supervisionKey;
	}

	public BigDecimal getIssueFee()
	{
		return issueFee;
	}

	public void setIssueFee(BigDecimal issueFee)
	{
		this.issueFee = issueFee;
	}

	public BigDecimal getPremium()
	{
		return premium;
	}

	public void setPremium(BigDecimal premium)
	{
		this.premium = premium;
	}

	public BigDecimal getDiscount()
	{
		return discount;
	}

	public void setDiscount(BigDecimal discount)
	{
		this.discount = discount;
	}

	public String getRenewalIndic()
	{
		return renewalIndic;
	}

	public void setRenewalIndic(String renewalIndic)
	{
		this.renewalIndic = renewalIndic;
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
	
	@Override
	public String toString() {
		return "ActivePolicyModel [countryId=" + countryId + ", compCd=" + compCd + ", docNumber=" + docNumber
				+ ", docDate=" + docDate + ", finance=" + finance + ", showRoom=" + showRoom + ", salesman=" + salesman
				+ ", userAmibCustRef=" + userAmibCustRef + ", insuredEnglishName=" + insuredEnglishName
				+ ", insuredArabicName=" + insuredArabicName + ", idNumber=" + idNumber + ", insCompanyCode="
				+ insCompanyCode + ", insCompanyDesc=" + insCompanyDesc + ", insCompanyPrefix=" + insCompanyPrefix
				+ ", dbcust=" + dbcust + ", makeCode=" + makeCode + ", makeDesc=" + makeDesc + ", subMakeCode="
				+ subMakeCode + ", subMakeDesc=" + subMakeDesc + ", modelYear=" + modelYear + ", shapeCode=" + shapeCode
				+ ", shapeDesc=" + shapeDesc + ", colourCode=" + colourCode + ", colourDesc=" + colourDesc + ", noPass="
				+ noPass + ", chassis=" + chassis + ", ktNumber=" + ktNumber + ", vehicleConditionCode="
				+ vehicleConditionCode + ", vehicleConditionDesc=" + vehicleConditionDesc + ", purposeCode="
				+ purposeCode + ", purposeDesc=" + purposeDesc + ", vehicleSrNumber=" + vehicleSrNumber
				+ ", policyTypeCode=" + policyTypeCode + ", policyTypeDesc=" + policyTypeDesc + ", policyNumber="
				+ policyNumber + ", maxInsuredAmount=" + maxInsuredAmount + ", startDate=" + startDate + ", endDate="
				+ endDate + ", supervisionKey=" + supervisionKey + ", issueFee=" + issueFee + ", premium=" + premium
				+ ", discount=" + discount + ", renewalIndic=" + renewalIndic + ", fuelCode=" + fuelCode + ", fuelDesc="
				+ fuelDesc + ", renewableApplCheck=" + renewableApplCheck
				+ "]";
	}
}

