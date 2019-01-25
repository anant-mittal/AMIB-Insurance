package com.amx.jax.meta;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class UserDeviceInfos implements Serializable {
	
	private String deviceType;
	
	private String deviceId;

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
