package com.amx.jax.session;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RegSession implements Serializable {

	private static final long serialVersionUID = 4265567700694960490L;
	private String motpPrefix;
	private String eotpPrefix;
	private String motp;
	private String eotp;

	public String getMotpPrefix() {
		return motpPrefix;
	}

	public void setMotpPrefix(String motpPrefix) {
		this.motpPrefix = motpPrefix;
	}

	public String getEotpPrefix() {
		return eotpPrefix;
	}

	public void setEotpPrefix(String eotpPrefix) {
		this.eotpPrefix = eotpPrefix;
	}

	public String getMotp() {
		return motp;
	}

	public void setMotp(String motp) {
		this.motp = motp;
	}

	public String getEotp() {
		return eotp;
	}

	public void setEotp(String eotp) {
		this.eotp = eotp;
	}

}
