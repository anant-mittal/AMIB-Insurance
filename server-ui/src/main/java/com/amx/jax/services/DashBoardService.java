package com.amx.jax.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.dao.DashBoardDao;
import com.amx.jax.models.DashBoardDetails;
import com.amx.jax.models.IncompleteApplModel;
import com.amx.jax.models.RequestQuoteDetrails;
import com.amx.jax.ui.session.UserSession;

@Service
public class DashBoardService
{
	private static final Logger logger = LoggerFactory.getLogger(RequestQuoteService.class);

	String TAG = "RequestQuoteService :: ";

	@Autowired
	public DashBoardDao dashBoardDao;
	
	@Autowired
	UserSession userSession;
	
	public AmxApiResponse<DashBoardDetails, Object> getIncompleteApplication()
	{
		AmxApiResponse<DashBoardDetails, Object> resp = new AmxApiResponse<DashBoardDetails, Object>();
		RequestQuoteDetrails incompleteApplResponse = new RequestQuoteDetrails();
		DashBoardDetails dashBoardDetails = new DashBoardDetails();
		
		try
		{
			
			IncompleteApplModel incompleteApplModel = dashBoardDao.getIncompleteApplication(userSession.getCivilId() , HardCodedValues.USER_TYPE , userSession.getCustomerSequenceNumber());
			incompleteApplResponse.setAppSeqNumber(incompleteApplModel.getAppSeqNumber());
			incompleteApplResponse.setAppStage(incompleteApplModel.getAppStage());
			if (null == incompleteApplModel.getErrorCode())
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			}
			else
			{
				resp.setStatusKey(incompleteApplModel.getErrorCode());
			}
			dashBoardDetails.setRequestQuoteDetails(incompleteApplResponse);
			resp.setMessageKey(incompleteApplModel.getErrorCode());
			resp.setMessage(incompleteApplModel.getErrorMessage());
			resp.setData(dashBoardDetails);
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "getIncompleteApplication :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}
}
