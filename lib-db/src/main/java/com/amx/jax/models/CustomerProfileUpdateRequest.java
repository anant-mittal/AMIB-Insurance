
package com.amx.jax.models;

import java.math.BigDecimal;

public class CustomerProfileUpdateRequest
{
	private String englishName;

	private String nativeArabicName;

	private String genderCode;

	private String idExpiryDate;

	private String businessCode;

	private String natyCode;

	private String govCode;

	private String areaCode;

	private String mobile;

	private String email;

	private BigDecimal languageId;

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

	public BigDecimal getLanguageId()
	{
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId)
	{
		this.languageId = languageId;
	}

	@Override
	public String toString()
	{
		return "CustomerProfileUpdateRequest [englishName=" + englishName + ", nativeArabicName=" + nativeArabicName + ", genderCode=" + genderCode + ", idExpiryDate=" + idExpiryDate + ", businessCode=" + businessCode + ", natyCode=" + natyCode + ", govCode=" + govCode + ", areaCode=" + areaCode
				+ ", mobile=" + mobile + ", email=" + email + ", languageId=" + languageId + "]";
	}

}
