package com.amx.jax.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.dao.MyQuoteDao;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.MyQuoteModel;
import com.amx.jax.ui.session.UserSession;

@Service
public class MyQuotesService
{
	private static final Logger logger = LoggerFactory.getLogger(MyQuotesService.class);
	
	String TAG = "MyQuotesService :: ";
	
	@Autowired
	UserSession userSession;

	@Autowired
	private MyQuoteDao myQuoteDao;

	public AmxApiResponse<?, Object> getUserQuote()
	{
		AmxApiResponse<MyQuoteModel, Object> resp = new AmxApiResponse<MyQuoteModel, Object>();
		try
		{
			ArrayResponseModel arrayResponseModel = myQuoteDao.getUserQuote(userSession.getCustomerSequenceNumber(), userSession.getLanguageId());
			if(arrayResponseModel.getErrorCode() == null)
			{
				resp.setResults(arrayResponseModel.getDataArray());
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			}
			else
			{
				resp.setMessageKey(arrayResponseModel.getErrorCode());
				resp.setMessage(arrayResponseModel.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG+"getUserQuote :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}
}
