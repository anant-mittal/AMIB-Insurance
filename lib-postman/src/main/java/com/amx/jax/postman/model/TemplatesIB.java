package com.amx.jax.postman.model;

import com.amx.jax.postman.model.File.PDFConverter;
import com.amx.jax.postman.model.ITemplates.ITemplate;

public enum TemplatesIB implements ITemplate {

	OTP_SMS("otp-sms"),
	OTP_EMAIL("otp-email"),
	REG_SUCCESS_EMAIL("reg-success-email"),
	REG_INCOMPLETE_EMAIL("reg-incomplete-email"),
	QUOTE_SUBMIT_EMAIL("quote-submit-email");

	String fileName;
	PDFConverter converter;
	String sampleJSON;
	boolean thymleaf = true;

	@Override
	public String getFileName() {
		return fileName;
	}

	TemplatesIB(String fileName, PDFConverter converter, String sampleJSON) {
		this.fileName = fileName;
		this.converter = converter;
		this.sampleJSON = sampleJSON;
		if (this.converter == PDFConverter.JASPER) {
			this.thymleaf = false;
		}
	}

	TemplatesIB(String fileName, PDFConverter converter) {
		this(fileName, converter, null);
	}

	TemplatesIB(String fileName, String sampleJSON) {
		this(fileName, null, sampleJSON);
	}

	TemplatesIB(String fileName) {
		this(fileName, null, null);
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
