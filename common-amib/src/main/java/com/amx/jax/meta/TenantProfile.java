package com.amx.jax.meta;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;
@Component
public class TenantProfile implements Serializable {

	private BigDecimal countryId;

	private BigDecimal compCd;

	private BigDecimal languageId;

	private String contactUsEmail;

	private String contactUsHelpLineNumber;

	private String amibWebsiteLink;

	private String currency;

	private BigDecimal decplc;
	
	private String companyName;
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getCompCd() {
		return compCd;
	}

	public void setCompCd(BigDecimal compCd) {
		this.compCd = compCd;
	}

	public BigDecimal getLanguageId() {
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId) {
		this.languageId = languageId;
	}

	public String getContactUsEmail() {
		return contactUsEmail;
	}

	public void setContactUsEmail(String contactUsEmail) {
		this.contactUsEmail = contactUsEmail;
	}

	public String getContactUsHelpLineNumber() {
		return contactUsHelpLineNumber;
	}

	public void setContactUsHelpLineNumber(String contactUsHelpLineNumber) {
		this.contactUsHelpLineNumber = contactUsHelpLineNumber;
	}

	public String getAmibWebsiteLink() {
		return amibWebsiteLink;
	}

	public void setAmibWebsiteLink(String amibWebsiteLink) {
		this.amibWebsiteLink = amibWebsiteLink;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getDecplc() {
		return decplc;
	}

	public void setDecplc(BigDecimal decplc) {
		this.decplc = decplc;
	}

}
