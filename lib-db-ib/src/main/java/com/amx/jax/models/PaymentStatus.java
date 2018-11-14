package com.amx.jax.models;

import java.math.BigDecimal;

public class PaymentStatus 
{
	
	private String paymentStatus;
	
	private String paymentDate;

	private String payId;
	
	private String refId;
	
	private BigDecimal modePremium;
	
	private BigDecimal supervisionFees;
	
	private BigDecimal issueFees;
	
	private BigDecimal additionalPremium;
	
	private BigDecimal discount;
	
	private BigDecimal totalAmount;
	
	private String transactionId;
	
	private PaymentReceipt paymentReceipt;
	
	public PaymentReceipt getPaymentReceipt() {
		return paymentReceipt;
	}

	public void setPaymentReceipt(PaymentReceipt paymentReceipt) {
		this.paymentReceipt = paymentReceipt;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getPayToken() {
		return payToken;
	}

	public void setPayToken(String payToken) {
		this.payToken = payToken;
	}

	private String payToken;
	
	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public String getRefId() {
		return refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public BigDecimal getModePremium() {
		return modePremium;
	}

	public void setModePremium(BigDecimal modePremium) {
		this.modePremium = modePremium;
	}

	public BigDecimal getSupervisionFees() {
		return supervisionFees;
	}

	public void setSupervisionFees(BigDecimal supervisionFees) {
		this.supervisionFees = supervisionFees;
	}

	public BigDecimal getIssueFees() {
		return issueFees;
	}

	public void setIssueFees(BigDecimal issueFees) {
		this.issueFees = issueFees;
	}

	public BigDecimal getAdditionalPremium() {
		return additionalPremium;
	}

	public void setAdditionalPremium(BigDecimal additionalPremium) {
		this.additionalPremium = additionalPremium;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	@Override
	public String toString() {
		return "PaymentStatus [paymentStatus=" + paymentStatus + ", paymentDate=" + paymentDate + ", payId=" + payId
				+ ", refId=" + refId + ", modePremium=" + modePremium + ", supervisionFees=" + supervisionFees
				+ ", issueFees=" + issueFees + ", additionalPremium=" + additionalPremium + ", discount=" + discount
				+ ", totalAmount=" + totalAmount + ", transactionId=" + transactionId + ", payToken=" + payToken + "]";
	}
}
