package com.amx.jax.models;

public class PaymentResult {
	private PaymentStatus paymentStatus;

	private PaymentReceipt paymentReceipt;

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public PaymentReceipt getPaymentReceipt() {
		return paymentReceipt;
	}

	public void setPaymentReceipt(PaymentReceipt paymentReceipt) {
		this.paymentReceipt = paymentReceipt;
	}
}
