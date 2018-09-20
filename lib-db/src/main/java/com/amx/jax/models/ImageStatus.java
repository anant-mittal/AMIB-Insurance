package com.amx.jax.models;

import java.math.BigDecimal;

public class ImageStatus
{
	BigDecimal docSeqNumber;

	String imageDate;

	public BigDecimal getDocSeqNumber()
	{
		return docSeqNumber;
	}

	public void setDocSeqNumber(BigDecimal docSeqNumber)
	{
		this.docSeqNumber = docSeqNumber;
	}

	public String getImageDate()
	{
		return imageDate;
	}

	public void setImageDate(String imageDate)
	{
		this.imageDate = imageDate;
	}

}
