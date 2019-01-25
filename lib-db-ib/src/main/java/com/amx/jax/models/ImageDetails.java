package com.amx.jax.models;

import java.math.BigDecimal;

public class ImageDetails
{
	private String docTypeCode;
	
	private String docTypeDesc;

	private BigDecimal docSeqNumber;

	private String imageSubmittedDate;

	private String isImageMandatory;
	
	private BigDecimal displayOrder;
	
	private String status;
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

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
	
	public String getDocTypeDesc() {
		return docTypeDesc;
	}

	public void setDocTypeDesc(String docTypeDesc) {
		this.docTypeDesc = docTypeDesc;
	}

	public BigDecimal getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(BigDecimal displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Override
	public String toString() {
		return "ImageDetails [docTypeCode=" + docTypeCode + ", docTypeDesc=" + docTypeDesc + ", docSeqNumber="
				+ docSeqNumber + ", imageSubmittedDate=" + imageSubmittedDate + ", isImageMandatory=" + isImageMandatory
				+ ", displayOrder=" + displayOrder + ", status=" + status + "]";
	}
	
}
