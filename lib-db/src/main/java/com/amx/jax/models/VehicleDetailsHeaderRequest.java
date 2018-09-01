package com.amx.jax.models;

import java.math.BigDecimal;

public class VehicleDetailsHeaderRequest
{
	/*P_CNTRYCD     IN     NUMBER,
    P_COMPCD      IN     NUMBER,
    P_APPLSEQNO   IN OUT NUMBER,
    P_APPLTYP     IN     VARCHAR2,//NU
    P_DOCCAT      IN     VARCHAR2,//NU
    P_CUSTSEQNO   IN     NUMBER,
    P_POLPERIOD   IN     NUMBER,
    P_OLDDOCNO    IN     NUMBER,//NU
    P_USERSEQNO   IN     NUMBER,
    P_ONLINELOCCD IN     VARCHAR2,//NU
    P_DEVICETYP   IN     VARCHAR2,
    P_DEVICE_ADDR IN     VARCHAR2,
    P_USER_BY     IN     VARCHAR2,*/
	
	
    BigDecimal appSeqNumber;
    String appType;
    String doccat;
    BigDecimal policyPeriod;
    String onlineLoccd;
    
}
