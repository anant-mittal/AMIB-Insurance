
package com.amx.jax.models;

import java.math.BigDecimal;
import java.util.Date;

import com.amx.jax.swagger.ApiMockModelProperty;


public class CustomerProfileDetailResponse
{

	@ApiMockModelProperty(example = "Amana Devadi", value = "customer english name")
	private String englishName;

	@ApiMockModelProperty(example = "Adm", value = "customer native arabic name")
	private String nativeArabicName;

	@ApiMockModelProperty(example = "F", value = "customer gender code")
	private String genderCode;

	@ApiMockModelProperty(example = "FEMALE", value = "customer gender desc")
	private String genderDesc;

	@ApiMockModelProperty(example = "13-Dec-18", value = "civil id expiry date")
	private String idExpiryDate;

	@ApiMockModelProperty(example = "ACH", value = "customer profession code")
	private String businessCode;

	@ApiMockModelProperty(example = "ARCHITECT", value = "customer profession desc")
	private String businessDesc;

	@ApiMockModelProperty(example = "IND", value = "customer nationality code")
	private String natyCode;

	@ApiMockModelProperty(example = "INDIAN", value = "customer nationality desc")
	private String natyDesc;

	@ApiMockModelProperty(example = "FAR", value = "governate code")
	private String govCode;

	@ApiMockModelProperty(example = "AL FARWANYA", value = "governate desc")
	private String govDesc;

	@ApiMockModelProperty(example = "AIR", value = "customer area code")
	private String areaCode;

	@ApiMockModelProperty(example = "AIR PORT", value = "customer area desc")
	private String areaDesc;

	@ApiMockModelProperty(example = "98345678", value = "customer registered mobile number")
	private String mobile;

	@ApiMockModelProperty(example = "amx@gmail.com", value = "customer registered email id")
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
