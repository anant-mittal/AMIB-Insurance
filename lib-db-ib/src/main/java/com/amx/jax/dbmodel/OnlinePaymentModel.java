package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="IRB_ONLINE_PAYMENTS")
@Entity
public class OnlinePaymentModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="PAY_SEQNO")
	private BigDecimal paySeqNo;
	
	@Column(name="CUST_SEQNO")
	private BigDecimal custSeqNo;
	
	@Column(name="APPL_SEQNO")
	private BigDecimal applSeqNo;
	
	@Column(name="PAY_AMT")
	private BigDecimal payAmount;
	
	@Column(name="PAY_DATE")
	private Date paymentDate;
	
	@Column(name="CRE_ON")
	private Date createdOn;
	
	@Column(name="RESULT_CD")
	private String resultCode;
	
	@Column(name="UPD_ON")
	private Date updatedOn;
	
	@Column(name="QUOTE_SEQNO")
	private BigDecimal quoteSeqNo;
	
	public BigDecimal getQuoteSeqNo() {
		return quoteSeqNo;
	}

	public void setQuoteSeqNo(BigDecimal quoteSeqNo) {
		this.quoteSeqNo = quoteSeqNo;
	}

	public BigDecimal getPaySeqNo() {
		return paySeqNo;
	}

	public void setPaySeqNo(BigDecimal paySeqNo) {
		this.paySeqNo = paySeqNo;
	}

	public BigDecimal getCustSeqNo() {
		return custSeqNo;
	}

	public void setCustSeqNo(BigDecimal custSeqNo) {
		this.custSeqNo = custSeqNo;
	}

	public BigDecimal getApplSeqNo() {
		return applSeqNo;
	}

	public void setApplSeqNo(BigDecimal applSeqNo) {
		this.applSeqNo = applSeqNo;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public Date getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Date updatedOn) {
		this.updatedOn = updatedOn;
	}

}
