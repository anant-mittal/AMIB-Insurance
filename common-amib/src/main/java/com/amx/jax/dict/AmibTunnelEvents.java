package com.amx.jax.dict;

import com.amx.jax.tunnel.ITunnelEventsDict;

public enum AmibTunnelEvents implements ITunnelEventsDict {
	TRNX_BENE_CREDIT, QUOTE_READY, RENEW_POLICY, PAYMENT_LINK;
	public static final class Names {
		public static final String TRNX_BENE_CREDIT = "TRNX_BENE_CREDIT";
		public static final String QUOTE_READY = "QUOTE_READY";
		public static final String RENEW_POLICY = "RENEW_POLICY";
		public static final String PAYMENT_LINK = "PAYMENT_LINK";
	}

	@Override
	public String getEventCode() {
		
		return this.name();
	}
}
