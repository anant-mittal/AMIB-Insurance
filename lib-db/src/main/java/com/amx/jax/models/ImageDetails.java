package com.amx.jax.models;

import java.math.BigDecimal;

public class ImageDetails
{
	private String docTypeCode;

	private BigDecimal docSeqNumber;

	private String imageSubmittedDate;

	private String isImageMandatory;

	public String getIsImageMandatory()
	{
		return isImageMandatory;
	}

	public void setIsImageMandatory(String isImageMandatory)
	{
		this.isImageMandatory = isImageMandatory;
	}

	public String getImageSubmittedDate()
	{
		return imageSubmittedDate;
	}

	public void setImageSubmittedDate(String imageSubmittedDate)
	{
		this.imageSubmittedDate = imageSubmittedDate;
	}

	public String getDocTypeCode()
	{
		return docTypeCode;
	}

	public void setDocTypeCode(String docTypeCode)
	{
		this.docTypeCode = docTypeCode;
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
		return "ImageDetails [docTypeCode=" + docTypeCode + ", docSeqNumber=" + docSeqNumber + ", imageSubmittedDate=" + imageSubmittedDate + ", isImageMandatory=" + isImageMandatory + "]";
	}

}
