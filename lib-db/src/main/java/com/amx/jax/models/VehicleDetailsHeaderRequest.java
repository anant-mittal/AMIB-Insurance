package com.amx.jax.models;

import java.math.BigDecimal;

public class VehicleDetailsHeaderRequest
{
	/*P_CNTRYCD     IN     NUMBER,
    P_COMPCD      IN     NUMBER,
    P_APPLSEQNO   IN OUT NUMBER,
    P_APPLTYP     IN     VARCHAR2,//NU             'N'    'R'
    P_DOCCAT      IN     VARCHAR2,//NU  'M'
    P_CUSTSEQNO   IN     NUMBER,
    P_POLPERIOD   IN     NUMBER,
    P_OLDDOCNO    IN     NUMBER,//NU     if 'R' active docamo , 'N'--> null
    P_USERSEQNO   IN     NUMBER,
    P_ONLINELOCCD IN     VARCHAR2,//NU   //'L' Not yet Confirmed
    P_DEVICETYP   IN     VARCHAR2,
    P_DEVICE_ADDR IN     VARCHAR2,
    P_USER_BY     IN     VARCHAR2,*/
	//IRB_INSUPD_APPLICATION_HEADER
	
	
	
	
	
	
	
	
	
	
	
	
	BigDecimal appSeqNumber;

	String appType;

	String doccat;

	BigDecimal policyPeriod;

	String onlineLoccd;

	public BigDecimal getAppSeqNumber()
	{
		return appSeqNumber;
	}

	public void setAppSeqNumber(BigDecimal appSeqNumber)
	{
		this.appSeqNumber = appSeqNumber;
	}

	public String getAppType()
	{
		return appType;
	}

	public void setAppType(String appType)
	{
		this.appType = appType;
	}

	public String getDoccat()
	{
		return doccat;
	}

	public void setDoccat(String doccat)
	{
		this.doccat = doccat;
	}

	public BigDecimal getPolicyPeriod()
	{
		return policyPeriod;
	}

	public void setPolicyPeriod(BigDecimal policyPeriod)
	{
		this.policyPeriod = policyPeriod;
	}

	public String getOnlineLoccd()
	{
		return onlineLoccd;
	}

	public void setOnlineLoccd(String onlineLoccd)
	{
		this.onlineLoccd = onlineLoccd;
	}
}
