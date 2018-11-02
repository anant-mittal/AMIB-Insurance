
package com.amx.jax.models;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MetaData implements Serializable
{
	private static final long serialVersionUID = 4265567700694960490L;

	private MetaData metaData;

	private BigDecimal countryId;

	private BigDecimal compCd;

	private String deviceType;

	private String deviceId;

	private BigDecimal languageId;

	private String contactUsEmail;

	private String contactUsHelpLineNumber;

	//String emailFromConfigured;

	private String amibWebsiteLink;

	private BigDecimal decplc;

	private String companyName;

	public String getCompanyName()
	{
		return companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public BigDecimal getDecplc()
	{
		return decplc;
	}

	public void setDecplc(BigDecimal decplc)
	{
		this.decplc = decplc;
	}

	public String getAmibWebsiteLink()
	{
		return amibWebsiteLink;
	}

	public void setAmibWebsiteLink(String amibWebsiteLink)
	{
		this.amibWebsiteLink = amibWebsiteLink;
	}

	/*public String getEmailFromConfigured()
	{
		return emailFromConfigured;
	}

	public void setEmailFromConfigured(String emailFromConfigured)
	{
		this.emailFromConfigured = emailFromConfigured;
	}*/

	public String getContactUsEmail()
	{
		return contactUsEmail;
	}

	public void setContactUsEmail(String contactUsEmail)
	{
		this.contactUsEmail = contactUsEmail;
	}

	public String getContactUsHelpLineNumber()
	{
		return contactUsHelpLineNumber;
	}

	public void setContactUsHelpLineNumber(String contactUsHelpLineNumber)
	{
		this.contactUsHelpLineNumber = contactUsHelpLineNumber;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
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

	public String getDeviceType()
	{
		return deviceType;
	}

	public void setDeviceType(String deviceType)
	{
		this.deviceType = deviceType;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public BigDecimal getLanguageId()
	{
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId)
	{
		this.languageId = languageId;
	}

	public MetaData getMetaData()
	{
		return metaData;
	}

	public void setMetaData(MetaData metaData)
	{
		this.metaData = metaData;
	}
}
