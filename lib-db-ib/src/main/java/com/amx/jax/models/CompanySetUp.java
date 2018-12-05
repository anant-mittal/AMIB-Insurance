
package com.amx.jax.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class CompanySetUp
{
	@ApiMockModelProperty(example = "1", value = "Customer country code")
	private BigDecimal cntryCd;

	@ApiMockModelProperty(example = "10", value = "Customer company code")
	private BigDecimal compCd;

	@ApiMockModelProperty(example = "AL MULLA INSURANCE BROKERAGE COMPANY W.L.L", value = "Company Name")
	private String companyName;

	private String cbox;

	private String cpo;

	@ApiMockModelProperty(example = "22250888", value = "AMIB contact number ")
	private String teli;//

	@ApiMockModelProperty(example = "22250999", value = "AMIB contact number ")
	private String teli2;

	private String fax;

	@ApiMockModelProperty(example = "amibmotor@almullagroup.com", value = "AMIB email id , help line email ")
	private String email;

	private String regNumber;

	private BigDecimal mainAct;

	private BigDecimal mainActCenter;

	private String heading;

	private BigDecimal decimalPlaceUpTo;

	@ApiMockModelProperty(example = "KWD", value = "Current Currency")
	private String currency;

	@ApiMockModelProperty(example = "0", value = "Language ID 0 for English language")
	private BigDecimal langId;

	@ApiMockModelProperty(example = "AMIB", value = "APP Name")
	private String appName;

	@ApiMockModelProperty(example = "AMIB INSURE", value = "SMS Sednder ID")
	private String smsSenderId;

	@ApiMockModelProperty(example = "+965 60466081/66597236", value = "AMib Help line numebr")
	private String helpLineNumber;

	@ApiMockModelProperty(example = "www.almullainsurancebrokerage.com", value = "AMIB wesite")
	private String webSite;

	@ApiMockModelProperty(example = "amibmotor@almullagroup.com", value = "AMib Help line numebr")
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
