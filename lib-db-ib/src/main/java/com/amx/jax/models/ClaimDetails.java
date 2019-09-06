package com.amx.jax.models;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.amx.jax.swagger.ApiMockModelProperty;

public class ClaimDetails {
	@NotNull
	@ApiMockModelProperty(example = "151/60/2019/152/0049", value = "Country Unique ID")
	private String policyNo;

	@NotNull
	@ApiMockModelProperty(example = "981545", value = "9145455")
	private BigDecimal claimPhoneNo;

	@NotNull
	@ApiMockModelProperty(example = "TAAZUR TAKAFUL INS CO", value = "Insurance Provider Company Desc")
	private String insCompanyDesc;


	public BigDecimal getClaimPhoneNo() {
		return claimPhoneNo;
	}

	public void setClaimPhoneNo(BigDecimal claimPhoneNo) {
		this.claimPhoneNo = claimPhoneNo;
	}

	public String getInsCompanyDesc() {
		return insCompanyDesc;
	}

	public void setInsCompanyDesc(String insCompanyDesc) {
		this.insCompanyDesc = insCompanyDesc;
	}
	
	

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	@Override
	public String toString() {
		return "ClaimDetails [policyNo=" + policyNo + ", claimPhoneNo=" + claimPhoneNo + ", insCompanyDesc="
				+ insCompanyDesc + "]";
	}

	
	
	

}
