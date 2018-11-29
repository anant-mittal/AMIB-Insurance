package com.amx.jax.meta;

import java.io.Serializable;

public interface IMetaService extends Serializable {

	public TenantProfile getTenantProfile();

	public UserDeviceInfos getUserDeviceInfo();
}
