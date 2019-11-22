package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="IRB_PAYMENT_LINK")
public class PaymentLinkModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6581864348917544221L;
	
	@Id
	@GeneratedValue(generator = "irb_payment_link_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "irb_payment_link_seq", sequenceName = "IRB_PAYMENT_LINK_SEQ", allocationSize = 1)
	@Column(name="PAYMENT_LINK_ID")
	private BigDecimal paymentLinkId;
	
	@Column(name="AMIB_CUSTCD")
	private String amibCustCd;
	
	@Column(name="APPL_SEQNO")
	private String applSeqNo;
	
	@Column(name="CUST_SEQNO")
	private BigDecimal custSeqNo;
	
	@Column(name="ISACTIVE")
	private String isActive;
	
	@Column(name="LINK_DATE")
	private Date linkDate;
	
	@Column(name="LINK_FOR")
	private String linkFor;
	
	@Column(name="PAYMENT_AMOUNT")
	private BigDecimal paymentAmount;
	
	@Column(name="PAYMENT_DATE")
	private Date paymentDate;
	
	@Column(name="QUOTE_SEQNO")
	private BigDecimal quoteSeqNo;
	
	@Column(name="VERIFICATION_CODE")
	private String verifyCode;
	
	@Column(name= "VER_NO")
	private BigDecimal versionNo;

	public String getAmibCustCd() {
		return amibCustCd;
	}

	public void setAmibCustCd(String amibCustCd) {
		this.amibCustCd = amibCustCd;
	}

	public String getApplSeqNo() {
		return applSeqNo;
	}

	public void setApplSeqNo(String applSeqNo) {
		this.applSeqNo = applSeqNo;
	}

	public BigDecimal getCustSeqNo() {
		return custSeqNo;
	}

	public void setCustSeqNo(BigDecimal custSeqNo) {
		this.custSeqNo = custSeqNo;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public Date getLinkDate() {
		return linkDate;
	}

	public void setLinkDate(Date linkDate) {
		this.linkDate = linkDate;
	}

	public String getLinkFor() {
		return linkFor;
	}

	public void setLinkFor(String linkFor) {
		this.linkFor = linkFor;
	}

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getPaymentLinkId() {
		return paymentLinkId;
	}

	public void setPaymentLinkId(BigDecimal paymentLinkId) {
		this.paymentLinkId = paymentLinkId;
	}

	public BigDecimal getQuoteSeqNo() {
		return quoteSeqNo;
	}

	public void setQuoteSeqNo(BigDecimal quoteSeqNo) {
		this.quoteSeqNo = quoteSeqNo;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public BigDecimal getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(BigDecimal versionNo) {
		this.versionNo = versionNo;
	}
	
}
