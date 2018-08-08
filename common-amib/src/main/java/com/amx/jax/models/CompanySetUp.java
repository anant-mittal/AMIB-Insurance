




package com.amx.jax.models;

public class CompanySetUp
{
	int cntryCd;

	int compCd;

	String companyName = "";

	String cbox = "";

	String cpo = "";

	String teli = "";//

	String teli2 = "";

	String fax = "";

	String email = "";

	String regNumber = "";

	int mainAct;

	int mainActCenter;

	String heading = "";

	int decplc;

	String currency = "";

	int langId;

	String appName = "";

	String smsSenderId = "";

	public int getCntryCd()
	{
		return cntryCd;
	}

	public void setCntryCd(int cntryCd)
	{
		this.cntryCd = cntryCd;
	}

	public int getCompCd()
	{
		return compCd;
	}

	public void setCompCd(int compCd)
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

	public int getMainAct()
	{
		return mainAct;
	}

	public void setMainAct(int mainAct)
	{
		this.mainAct = mainAct;
	}

	public int getMainActCenter()
	{
		return mainActCenter;
	}

	public void setMainActCenter(int mainActCenter)
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

	public int getDecplc()
	{
		return decplc;
	}

	public void setDecplc(int decplc)
	{
		this.decplc = decplc;
	}

	public String getCurrency()
	{
		return currency;
	}

	public void setCurrency(String currency)
	{
		this.currency = currency;
	}

	public int getLangId()
	{
		return langId;
	}

	public void setLangId(int langId)
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
				+ mainActCenter + ", heading=" + heading + ", decplc=" + decplc + ", currency=" + currency + ", langId=" + langId + ", appName=" + appName + ", smsSenderId=" + smsSenderId + "]";
	}
}
