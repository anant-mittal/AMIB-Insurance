package com.amx.jax.models;

import java.math.BigDecimal;

import com.amx.jax.swagger.ApiMockModelProperty;

public class InsuranceClaimDetails
{
	@ApiMockModelProperty(example = "10", value = "country code")
	private BigDecimal countryCode;
	
	@ApiMockModelProperty(example = "10", value = "Company Code")
	private BigDecimal companyCode;
	
	@ApiMockModelProperty(example = "100008", value = "Insurance Provider Company Code")
	private BigDecimal insCompanyCode;
	
	@ApiMockModelProperty(example = "TAAZUR TAKAFUL INS CO", value = "Insurance Provider Company Desc")
	private String inscompanyName;
	
	@ApiMockModelProperty(example = "erashokkalal@gmail.com", value = "email Id")
	private String emailId;
	
	@ApiMockModelProperty(example = "Mr.Rabil", value = "claim contact person name")
	private String claimContactName;
	
	@ApiMockModelProperty(example = "n.abboud@partnernetworkgroup.com", value = "claim contact person email id")
	private String claimEmailId;

	@ApiMockModelProperty(example = "2888444", value = "claim contact person number")
	private String claimContactNo;
	
	@ApiMockModelProperty(example = "FTIC", value = "Insurance Provider Company Desc")
	private String shortInscPrefix;


	public BigDecimal getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(BigDecimal countryCode) {
		this.countryCode = countryCode;
	}

	public BigDecimal getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(BigDecimal companyCode) {
		this.companyCode = companyCode;
	}

	
	public String getClaimContactName() {
		return claimContactName;
	}

	public void setClaimContactName(String claimContactName) {
		this.claimContactName = claimContactName;
	}

	public String getClaimEmailId() {
		return claimEmailId;
	}

	public void setClaimEmailId(String claimEmailId) {
		this.claimEmailId = claimEmailId;
	}

	

	public String getShortInscPrefix() {
		return shortInscPrefix;
	}

	public void setShortInscPrefix(String shortInscPrefix) {
		this.shortInscPrefix = shortInscPrefix;
	}

	public BigDecimal getInsCompanyCode() {
		return insCompanyCode;
	}

	public void setInsCompanyCode(BigDecimal insCompanyCode) {
		this.insCompanyCode = insCompanyCode;
	}

	public String getInscompanyName() {
		return inscompanyName;
	}

	public void setInscompanyName(String inscompanyName) {
		this.inscompanyName = inscompanyName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getClaimContactNo() {
		return claimContactNo;
	}

	public void setClaimContactNo(String claimContactNo) {
		this.claimContactNo = claimContactNo;
	}
	
	

	

	
}
