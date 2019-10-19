
package com.amx.jax.models;

import java.math.BigDecimal;
import java.sql.Date;

public class CustomerProfileDetailModel
{
	private BigDecimal countryId;

	private BigDecimal compCd;

	private String userType;

	private String civilId;

	private BigDecimal languageId;

	private BigDecimal custSequenceNumber;

	private boolean status;

	private String errorMessage;

	private String errorCode;

	private String mobile;

	private String email;

	private String areaCode;

	private String areaDesc;

	private String govCode;

	private String govDesc;

	private String natyCode;

	private String natyDesc;

	private String genderCode;

	private String genderDesc;

	private String businessCode;

	private String businessDesc;

	private Date idExpiryDate;

	private String englishName;

	private String nativeArabicName;
	
	private String addressType;
	
	private String addressDesc;
	
	private String block;
	
	private String street;
	
	private String building;
	
	private String flat;
	
	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	public String getAddressDesc() {
		return addressDesc;
	}

	public void setAddressDesc(String addressDesc) {
		this.addressDesc = addressDesc;
	}
	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}

	public String getFlat() {
		return flat;
	}

	public void setFlat(String flat) {
		this.flat = flat;
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

	public String getUserType()
	{
		return userType;
	}

	public void setUserType(String userType)
	{
		this.userType = userType;
	}

	public String getCivilId()
	{
		return civilId;
	}

	public void setCivilId(String civilId)
	{
		this.civilId = civilId;
	}

	public BigDecimal getLanguageId()
	{
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId)
	{
		this.languageId = languageId;
	}

	public BigDecimal getCustSequenceNumber()
	{
		return custSequenceNumber;
	}

	public void setCustSequenceNumber(BigDecimal custSequenceNumber)
	{
		this.custSequenceNumber = custSequenceNumber;
	}

	public boolean getStatus()
	{
		return status;
	}

	public void setStatus(boolean status)
	{
		this.status = status;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getAreaCode()
	{
		return areaCode;
	}

	public void setAreaCode(String areaCode)
	{
		this.areaCode = areaCode;
	}

	public String getAreaDesc()
	{
		return areaDesc;
	}

	public void setAreaDesc(String areaDesc)
	{
		this.areaDesc = areaDesc;
	}

	public String getGovCode()
	{
		return govCode;
	}

	public void setGovCode(String govCode)
	{
		this.govCode = govCode;
	}

	public String getGovDesc()
	{
		return govDesc;
	}

	public void setGovDesc(String govDesc)
	{
		this.govDesc = govDesc;
	}

	public String getNatyCode()
	{
		return natyCode;
	}

	public void setNatyCode(String natyCode)
	{
		this.natyCode = natyCode;
	}

	public String getNatyDesc()
	{
		return natyDesc;
	}

	public void setNatyDesc(String natyDesc)
	{
		this.natyDesc = natyDesc;
	}

	public String getGenderCode()
	{
		return genderCode;
	}

	public void setGenderCode(String genderCode)
	{
		this.genderCode = genderCode;
	}

	public String getGenderDesc()
	{
		return genderDesc;
	}

	public void setGenderDesc(String genderDesc)
	{
		this.genderDesc = genderDesc;
	}

	public String getBusinessCode()
	{
		return businessCode;
	}

	public void setBusinessCode(String businessCode)
	{
		this.businessCode = businessCode;
	}

	public String getBusinessDesc()
	{
		return businessDesc;
	}

	public void setBusinessDesc(String businessDesc)
	{
		this.businessDesc = businessDesc;
	}

	public Date getIdExpiryDate()
	{
		return idExpiryDate;
	}

	public void setIdExpiryDate(Date idExpiryDate)
	{
		this.idExpiryDate = idExpiryDate;
	}

	public String getEnglishName()
	{
		return englishName;
	}

	public void setEnglishName(String englishName)
	{
		this.englishName = englishName;
	}

	public String getNativeArabicName()
	{
		return nativeArabicName;
	}

	public void setNativeArabicName(String nativeArabicName)
	{
		this.nativeArabicName = nativeArabicName;
	}

	@Override
	public String toString()
	{
		return "CustomerProfileDetailModel [countryId=" + countryId + ", compCd=" + compCd + ", userType=" + userType + ", civilId=" + civilId + ", languageId=" + languageId + ", custSequenceNumber=" + custSequenceNumber + ", status=" + status + ", errorMessage=" + errorMessage + ", errorCode="
				+ errorCode + ", mobile=" + mobile + ", email=" + email + ", areaCode=" + areaCode + ", areaDesc=" + areaDesc + ", govCode=" + govCode + ", govDesc=" + govDesc + ", natyCode=" + natyCode + ", natyDesc=" + natyDesc + ", genderCode=" + genderCode + ", genderDesc=" + genderDesc
				+ ", businessCode=" + businessCode + ", businessDesc=" + businessDesc + ", idExpiryDate=" + idExpiryDate + ", englishName=" + englishName + ", nativeArabicName=" + nativeArabicName + "]";
	}
}
