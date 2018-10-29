package com.amx.jax.ui.session;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class VehicleSession implements Serializable
{
	BigDecimal appSeqNumber;
	
	BigDecimal docNumber;

	public BigDecimal getDocNumber()
	{
		return docNumber;
	}

	public void setDocNumber(BigDecimal docNumber)
	{
		this.docNumber = docNumber;
	}

	public BigDecimal getAppSeqNumber()
	{
		return appSeqNumber;
	}

	public void setAppSeqNumber(BigDecimal appSeqNumber)
	{
		this.appSeqNumber = appSeqNumber;
	}
}
