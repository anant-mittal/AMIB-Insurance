
package com.amx.jax.models;

import java.math.BigDecimal;
import java.sql.Date;

public class CustomerProfileDetailResponse
{

	private String englishName;

	private String nativeArabicName;

	private String genderCode;

	private String genderDesc;

	private Date idExpiryDate;

	private String businessCode;

	private String businessDesc;

	private String natyCode;

	private String natyDesc;

	private String govCode;

	private String govDesc;

	private String areaCode;

	private String areaDesc;

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

	public String getGenderDesc()
	{
		return genderDesc;
	}

	public void setGenderDesc(String genderDesc)
	{
		this.genderDesc = genderDesc;
	}

	public Date getIdExpiryDate()
	{
		return idExpiryDate;
	}

	public void setIdExpiryDate(Date idExpiryDate)
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

	public String getBusinessDesc()
	{
		return businessDesc;
	}

	public void setBusinessDesc(String businessDesc)
	{
		this.businessDesc = businessDesc;
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
		return "CustomerProfileDetailResponse [englishName=" + englishName + ", nativeArabicName=" + nativeArabicName + ", genderCode=" + genderCode + ", genderDesc=" + genderDesc + ", idExpiryDate=" + idExpiryDate + ", businessCode=" + businessCode + ", businessDesc=" + businessDesc + ", natyCode="
				+ natyCode + ", natyDesc=" + natyDesc + ", govCode=" + govCode + ", govDesc=" + govDesc + ", areaCode=" + areaCode + ", areaDesc=" + areaDesc + ", mobile=" + mobile + ", email=" + email + ", languageId=" + languageId + "]";
	}

}
