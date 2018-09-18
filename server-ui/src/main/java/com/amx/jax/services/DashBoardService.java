package com.amx.jax.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.dao.DashBoardDao;
import com.amx.jax.models.DashBoardDetails;
import com.amx.jax.models.IncompleteApplModel;
import com.amx.jax.models.RequestQuoteDetrails;

@Service
public class DashBoardService
{
	private static final Logger logger = LoggerFactory.getLogger(RequestQuoteService.class);

	String TAG = "com.amx.jax.services.RequestQuoteService :- ";

	@Autowired
	public DashBoardDao dashBoardDao;

	public AmxApiResponse<DashBoardDetails, Object> getIncompleteApplication()
	{
		AmxApiResponse<DashBoardDetails, Object> resp = new AmxApiResponse<DashBoardDetails, Object>();
		RequestQuoteDetrails incompleteApplResponse = new RequestQuoteDetrails();
		DashBoardDetails dashBoardDetails = new DashBoardDetails();
		
		try
		{
			IncompleteApplModel incompleteApplModel = dashBoardDao.getIncompleteApplication();
			incompleteApplResponse.setAppSeqNumber(incompleteApplModel.getAppSeqNumber());
			incompleteApplResponse.setAppStage(incompleteApplModel.getAppStage());
			if (null == incompleteApplModel.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			dashBoardDetails.setRequestQuoteDetrails(incompleteApplResponse);
			resp.setMessageKey(incompleteApplModel.getErrorCode());
			resp.setMessage(incompleteApplModel.getErrorMessage());
			resp.setData(dashBoardDetails);
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
