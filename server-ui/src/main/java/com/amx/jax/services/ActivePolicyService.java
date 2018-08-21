package com.amx.jax.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.RegSession;
import com.amx.jax.models.Validate;
import com.amx.jax.dao.ActivePolicyDao;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ActivePolicyService
{
	String TAG = "com.amx.jax.services :: MyPolicyService :: ";

	private static final Logger logger = LoggerFactory.getLogger(ActivePolicyService.class);

	@Autowired
	RegSession regSession;

	@Autowired
	MetaData metaData;

	@Autowired
	private ActivePolicyDao activePolicyDao;

	public AmxApiResponse<Validate, Object> getUserActivePolicy()
	{
		return activePolicyDao.getUserActivePolicy();
	}

}
