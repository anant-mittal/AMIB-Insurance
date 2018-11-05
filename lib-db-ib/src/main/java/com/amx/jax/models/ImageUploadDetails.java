package com.amx.jax.models;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

public class ImageUploadDetails
{
	String docCode;
	
	BigDecimal docSeqNumber;
	
	public String getDocCode()
	{
		return docCode;
	}

	public void setDocCode(String docCode)
	{
		this.docCode = docCode;
	}

	public BigDecimal getDocSeqNumber()
	{
		return docSeqNumber;
	}

	public void setDocSeqNumber(BigDecimal docSeqNumber)
	{
		this.docSeqNumber = docSeqNumber;
	}
	
	@Override
	public String toString()
	{
		return "ImageUploadDetails [docCode=" + docCode + ", docSeqNumber=" + docSeqNumber + "]";
	}
	
}
