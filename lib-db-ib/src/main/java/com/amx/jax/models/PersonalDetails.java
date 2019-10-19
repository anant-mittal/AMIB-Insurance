package com.amx.jax.models;

import com.amx.jax.swagger.ApiMockModelProperty;

public class PersonalDetails
{
	@ApiMockModelProperty(example = "Amana Devadi", value = "customer english name")
	private String englishName;

	@ApiMockModelProperty(example = "Adm", value = "customer native arabic name")
	private String nativeArabicName;

	@ApiMockModelProperty(example = "F", value = "customer gender code")
	private String genderCode;

	@ApiMockModelProperty(example = "13-Dec-18", value = "civil id expiry date")
	private String idExpiryDate;

	@ApiMockModelProperty(example = "ACH", value = "customer profession code")
	private String businessCode;

	@ApiMockModelProperty(example = "IND", value = "customer nationality code")
	private String natyCode;

	@ApiMockModelProperty(example = "FAR", value = "customer governate code")
	private String govCode;

	@ApiMockModelProperty(example = "AIR", value = "customer area code")
	private String areaCode;

	private String mobile;

	private String email;
	
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

	public String getGenderCode()
	{
		return genderCode;
	}

	public void setGenderCode(String genderCode)
	{
		this.genderCode = genderCode;
	}

	public String getIdExpiryDate()
	{
		return idExpiryDate;
	}

	public void setIdExpiryDate(String idExpiryDate)
	{
		this.idExpiryDate = idExpiryDate;
	}

	public String getBusinessCode()
	{
		return businessCode;
	}

	public void setBusinessCode(String businessCode)
	{
		this.businessCode = businessCode;
	}

	public String getNatyCode()
	{
		return natyCode;
	}

	public void setNatyCode(String natyCode)
	{
		this.natyCode = natyCode;
	}

	public String getGovCode()
	{
		return govCode;
	}

	public void setGovCode(String govCode)
	{
		this.govCode = govCode;
	}

	public String getAreaCode()
	{
		return areaCode;
	}

	public void setAreaCode(String areaCode)
	{
		this.areaCode = areaCode;
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

	@Override
	public String toString()
	{
		return "PersonalDetails [englishName=" + englishName + ", nativeArabicName=" + nativeArabicName + ", genderCode=" + genderCode + ", idExpiryDate=" + idExpiryDate + ", businessCode=" + businessCode + ", natyCode=" + natyCode + ", govCode=" + govCode + ", areaCode=" + areaCode + ", mobile="
				+ mobile + ", email=" + email + "]";
	}

}
