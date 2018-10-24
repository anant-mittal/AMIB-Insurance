
package com.amx.jax.models;

import java.math.BigDecimal;

public class CompanySetUp
{
	private BigDecimal cntryCd;

	private BigDecimal compCd;

	private String companyName;

	private String cbox;

	private String cpo;

	private String teli;//

	private String teli2;

	private String fax;

	private String email;

	private String regNumber;

	private BigDecimal mainAct;

	private BigDecimal mainActCenter;

	private String heading;

	private BigDecimal decimalPlaceUpTo;

	private String currency;

	private BigDecimal langId;

	private String appName;

	private String smsSenderId;

	private String helpLineNumber;

	private String webSite;

	private String emailSenderId;

	public String getHelpLineNumber()
	{
		return helpLineNumber;
	}

	public void setHelpLineNumber(String helpLineNumber)
	{
		this.helpLineNumber = helpLineNumber;
	}

	public String getWebSite()
	{
		return webSite;
	}

	public void setWebSite(String webSite)
	{
		this.webSite = webSite;
	}

	public String getEmailSenderId()
	{
		return emailSenderId;
	}

	public void setEmailSenderId(String emailSenderId)
	{
		this.emailSenderId = emailSenderId;
	}

	public BigDecimal getCntryCd()
	{
		return cntryCd;
	}

	public void setCntryCd(BigDecimal cntryCd)
	{
		this.cntryCd = cntryCd;
	}

	public BigDecimal getCompCd()
	{
		return compCd;
	}

	public void setCompCd(BigDecimal compCd)
	{
		this.compCd = compCd;
	}

	public String getCompanyName()
	{
		return companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public String getCbox()
	{
		return cbox;
	}

	public void setCbox(String cbox)
	{
		this.cbox = cbox;
	}

	public String getCpo()
	{
		return cpo;
	}

	public void setCpo(String cpo)
	{
		this.cpo = cpo;
	}

	public String getTeli()
	{
		return teli;
	}

	public void setTeli(String teli)
	{
		this.teli = teli;
	}

	public String getTeli2()
	{
		return teli2;
	}

	public void setTeli2(String teli2)
	{
		this.teli2 = teli2;
	}

	public String getFax()
	{
		return fax;
	}

	public void setFax(String fax)
	{
		this.fax = fax;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getRegNumber()
	{
		return regNumber;
	}

	public void setRegNumber(String regNumber)
	{
		this.regNumber = regNumber;
	}

	public BigDecimal getMainAct()
	{
		return mainAct;
	}

	public void setMainAct(BigDecimal mainAct)
	{
		this.mainAct = mainAct;
	}

	public BigDecimal getMainActCenter()
	{
		return mainActCenter;
	}

	public void setMainActCenter(BigDecimal mainActCenter)
	{
		this.mainActCenter = mainActCenter;
	}

	public String getHeading()
	{
		return heading;
	}

	public void setHeading(String heading)
	{
		this.heading = heading;
	}

	public BigDecimal getDecimalPlaceUpTo()
	{
		return decimalPlaceUpTo;
	}

	public void setDecimalPlaceUpTo(BigDecimal decimalPlaceUpTo)
	{
		this.decimalPlaceUpTo = decimalPlaceUpTo;
	}

	public String getCurrency()
	{
		return currency;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

	public BigDecimal getLangId()
	{
		return langId;
	}

	public void setLangId(BigDecimal langId)
	{
		this.langId = langId;
	}

	public String getAppName()
	{
		return appName;
	}

	public void setAppName(String appName)
	{
		this.appName = appName;
	}

	public String getSmsSenderId()
	{
		return smsSenderId;
	}

	public void setSmsSenderId(String smsSenderId)
	{
		this.smsSenderId = smsSenderId;
	}

	@Override
	public String toString()
	{
		return "CompanySetUp [cntryCd=" + cntryCd + ", compCd=" + compCd + ", companyName=" + companyName + ", cbox=" + cbox + ", cpo=" + cpo + ", teli=" + teli + ", teli2=" + teli2 + ", fax=" + fax + ", email=" + email + ", regNumber=" + regNumber + ", mainAct=" + mainAct + ", mainActCenter="
				+ mainActCenter + ", heading=" + heading + ", decimalPlaceUpTo=" + decimalPlaceUpTo + ", currency=" + currency + ", langId=" + langId + ", appName=" + appName + ", smsSenderId=" + smsSenderId + "]";
	}
}
