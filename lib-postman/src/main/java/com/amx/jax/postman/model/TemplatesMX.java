package com.amx.jax.postman.model;

import com.amx.jax.postman.model.File.PDFConverter;
import com.amx.jax.postman.model.ITemplates.ITemplate;

public enum TemplatesMX implements ITemplate {

	CONTACT_US("ContactForm"), REGISTRATION_OTP(""), FAILED_REGISTRATION("");

	String fileName;

	PDFConverter converter;

	String sampleJSON;

	boolean thymleaf = true;

	boolean thymleafJson = false;

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public String getJsonFileName() {
		return "json/" + fileName;
	}

	TemplatesMX(String fileName, PDFConverter converter, String sampleJSON) {
		this.fileName = fileName;
		this.converter = converter;
		this.sampleJSON = sampleJSON;
		if (this.fileName.startsWith("json/")) {
			this.thymleafJson = true;
		}
		if (this.converter == PDFConverter.JASPER) {
			this.thymleaf = false;
		}
	}

	TemplatesMX(String fileName, PDFConverter converter) {
		this(fileName, converter, null);
	}

	TemplatesMX(String fileName, String sampleJSON) {
		this(fileName, null, sampleJSON);
	}

	TemplatesMX(String fileName) {
		this(fileName, null, null);
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

	@Override
	public boolean isThymleafJson() {
		return thymleafJson;
	}

	public String toString() {
		return this.name();
	}

}
