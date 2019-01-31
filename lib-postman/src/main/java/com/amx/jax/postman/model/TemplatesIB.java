

package com.amx.jax.postman.model;

import com.amx.jax.postman.model.File.PDFConverter;
import com.amx.jax.postman.model.ITemplates.ITemplate;
import com.amx.jax.postman.model.Notipy.Channel;

public enum TemplatesIB implements ITemplate {
	
	OTP_SMS("otp-sms", Channel.NOTIPY),
	OTP_EMAIL("otp-email"),
	REG_SUCCESS_EMAIL("reg-success-email"),
	REG_INCOMPLETE_EMAIL("reg-incomplete-email"),
	TERMS("terms"),
	QUOTE_SUBMIT_EMAIL_TO_UESR("quote-submit-user-email"),
	QUOTE_SUBMIT_EMAIL_TO_AMIB("quote-submit-amib-email"),
	POLICY_DUE_REMINDER("policy-due-reminder"),
	KNET_SUCCESS_EMAIL("knet-success-email"),
	QUOTE_READY_AMIB("quote-submit-email"),
	TRNX_RECEIPT("TransactionReceipt", PDFConverter.JASPER, "TransactionReceipt.json"),
	POLICY_RECEIPT("PolicyReceipt", PDFConverter.JASPER, "PolicyReceipt.json"),
	CONTACT_US("ContactForm"),
	REMIT_RECEIPT_JASPER("TransactionReceipt_jasper", PDFConverter.JASPER, "TransactionReceipt.json");
		
	
	
	String fileName;
	PDFConverter converter;
	String sampleJSON;
	boolean thymleaf = true;
	Channel channel = null;

	@Override
	public String getFileName() {
		return fileName;
	}

	TemplatesIB(String fileName, PDFConverter converter, String sampleJSON, Channel channel) {
		this.fileName = fileName;
		this.converter = converter;
		this.sampleJSON = sampleJSON;
		if (this.converter == PDFConverter.JASPER) {
			this.thymleaf = false;
		}
		this.channel = channel;
	}

	TemplatesIB(String fileName, PDFConverter converter, String sampleJSON) {
		this(fileName, converter, sampleJSON, null);
	}

	TemplatesIB(String fileName, PDFConverter converter) {
		this(fileName, converter, null, null);
	}

	TemplatesIB(String fileName, Channel channel) {
		this(fileName, null, null, channel);
	}

	TemplatesIB(String fileName) {
		this(fileName, null, null, null);
	}
	
	TemplatesIB(){
	}

	@Override
	public PDFConverter getConverter() {
		return converter;
	}

	@Override
	public String getSampleJSON() {
		if (sampleJSON == null) {
			return this.fileName + ".json";
		}
		return sampleJSON;
	}

	@Override
	public boolean isThymleaf() {
		return thymleaf;
	}

	public String toString() {
		return this.name();
	}

	@Override
	public String getJsonFile() {
		return "json/" + fileName;
	}

	@Override
	public String getHtmlFile() {
		return "html/" + fileName;
	}

}