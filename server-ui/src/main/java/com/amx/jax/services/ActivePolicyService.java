package com.amx.jax.services;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.models.ActivePolicyModel;
import com.amx.jax.models.IncompleteApplModel;
import com.amx.jax.models.IncompleteApplResponse;
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

	public AmxApiResponse<ActivePolicyModel, Object> getUserActivePolicy()
	{
		AmxApiResponse<ActivePolicyModel, Object> resp = new AmxApiResponse<ActivePolicyModel, Object>();
		try
		{
			
			
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setResults(activePolicyDao.getUserActivePolicy());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}
	
	
	public AmxApiResponse<IncompleteApplResponse, Object> getIncompleteApplication()
	{
		AmxApiResponse<IncompleteApplResponse, Object> resp = new AmxApiResponse<IncompleteApplResponse, Object>();
		IncompleteApplResponse incompleteApplResponse = new IncompleteApplResponse();
		try
		{
			IncompleteApplModel incompleteApplModel = activePolicyDao.getIncompleteApplication();
			
			incompleteApplResponse.setAppSeqNumber(incompleteApplModel.getAppSeqNumber());
			incompleteApplResponse.setAppStage(incompleteApplModel.getAppStage());
			resp.setData(incompleteApplResponse);
			resp.setStatusKey(ApiConstants.SUCCESS);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

}
