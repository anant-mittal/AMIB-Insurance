package com.amx.jax.models;

import java.math.BigDecimal;

public class PaymentDetails
{
	@Override
	public String toString()
	{
		return "PaymentDetails [appSeqNum=" + appSeqNum + ", quoteSeqNum=" + quoteSeqNum + ", qouteVerNum=" + qouteVerNum + ", custSeqNum=" + custSeqNum + ", paymentMethod=" + paymentMethod + ", paymentAmount=" + paymentAmount + ", paymentId=" + paymentId + ", paymentToken=" + paymentToken
				+ ", paySeqNum=" + paySeqNum + ", errorMessage=" + errorMessage + ", errorCode=" + errorCode + ", approvalNo=" + approvalNo + ", approvalDate=" + approvalDate + ", resultCd=" + resultCd + ", transId=" + transId + ", refId=" + refId + "]";
	}
	
	private BigDecimal appSeqNum;

	private BigDecimal quoteSeqNum;

	private BigDecimal qouteVerNum;

	private BigDecimal custSeqNum;

	private String paymentMethod;

	private BigDecimal paymentAmount;

	private String paymentId;

	private String paymentToken;

	private BigDecimal paySeqNum;

	private String errorMessage;

	private String errorCode;
	
	/****************************************************************************************/
	
	private String approvalNo;

	private java.sql.Date approvalDate;

	private String resultCd;

	private String transId;

	private String refId;
	
	/****************************************************************************************/

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	public BigDecimal getAppSeqNum()
	{
		return appSeqNum;
	}

	public void setAppSeqNum(BigDecimal appSeqNum)
	{
		this.appSeqNum = appSeqNum;
	}

	public BigDecimal getQuoteSeqNum()
	{
		return quoteSeqNum;
	}

	public void setQuoteSeqNum(BigDecimal quoteSeqNum)
	{
		this.quoteSeqNum = quoteSeqNum;
	}

	public BigDecimal getQouteVerNum()
	{
		return qouteVerNum;
	}

	public void setQouteVerNum(BigDecimal qouteVerNum)
	{
		this.qouteVerNum = qouteVerNum;
	}

	public BigDecimal getCustSeqNum()
	{
		return custSeqNum;
	}

	public void setCustSeqNum(BigDecimal custSeqNum)
	{
		this.custSeqNum = custSeqNum;
	}

	public String getPaymentMethod()
	{
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod)
	{
		this.paymentMethod = paymentMethod;
	}

	public BigDecimal getPaymentAmount()
	{
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount)
	{
		this.paymentAmount = paymentAmount;
	}

	public String getPaymentId()
	{
		return paymentId;
	}

	public void setPaymentId(String paymentId)
	{
		this.paymentId = paymentId;
	}

	public String getPaymentToken()
	{
		return paymentToken;
	}

	public void setPaymentToken(String paymentToken)
	{
		this.paymentToken = paymentToken;
	}

	public BigDecimal getPaySeqNum()
	{
		return paySeqNum;
	}

	public void setPaySeqNum(BigDecimal paySeqNum)
	{
		this.paySeqNum = paySeqNum;
	}

	
	
	
	
	
	
	
	/*****************************************************************************************************/

	

	public String getApprovalNo()
	{
		return approvalNo;
	}

	public void setApprovalNo(String approvalNo)
	{
		this.approvalNo = approvalNo;
	}

	public java.sql.Date getApprovalDate()
	{
		return approvalDate;
	}

	public void setApprovalDate(java.sql.Date approvalDate)
	{
		this.approvalDate = approvalDate;
	}

	public String getResultCd()
	{
		return resultCd;
	}

	public void setResultCd(String resultCd)
	{
		this.resultCd = resultCd;
	}

	public String getTransId()
	{
		return transId;
	}

	public void setTransId(String transId)
	{
		this.transId = transId;
	}

	public String getRefId()
	{
		return refId;
	}

	public void setRefId(String refId)
	{
		this.refId = refId;
	}
}
