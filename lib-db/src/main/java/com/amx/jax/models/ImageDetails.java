package com.amx.jax.models;

import java.math.BigDecimal;

public class ImageDetails
{
	private String docTypeCode;

	public String getDocTypeCode()
	{
		return docTypeCode;
	}

	public void setDocTypeCode(String docTypeCode)
	{
		this.docTypeCode = docTypeCode;
	}

	private BigDecimal docSeqNumber;

	public BigDecimal getDocSeqNumber()
	{
		return docSeqNumber;
	}

	public void setDocSeqNumber(BigDecimal docSeqNumber)
	{
		this.docSeqNumber = docSeqNumber;
	}

}
