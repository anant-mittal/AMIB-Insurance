package com.amx.jax.models;

import java.math.BigDecimal;

public class PgRedirectUrl {

	private BigDecimal paySeqNum;

	public BigDecimal getPaySeqNum() {
		return paySeqNum;
	}

	public void setPaySeqNum(BigDecimal paySeqNum) {
		this.paySeqNum = paySeqNum;
	}

	private String redirectUrl;

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
}
