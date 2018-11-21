package com.amx.jax.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.dao.MyQuoteDao;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.MyQuoteModel;
import com.amx.jax.ui.session.UserSession;

@Service
public class MyQuotesService
{
	String TAG = "com.amx.jax.services :: MyQuotesService :: ";

	private static final Logger logger = LoggerFactory.getLogger(MyQuotesService.class);

	@Autowired
	MetaData metaData;
	
	@Autowired
	UserSession userSession;

	@Autowired
	private MyQuoteDao myQuoteDao;

	public AmxApiResponse<?, Object> getUserQuote()
	{
		AmxApiResponse<MyQuoteModel, Object> resp = new AmxApiResponse<MyQuoteModel, Object>();
		try
		{
			logger.info(TAG + " getUserQuote :: getCivilId :" + userSession.getCivilId());
			logger.info(TAG + " getUserQuote :: getCustomerSequenceNumber :" + userSession.getCustomerSequenceNumber());
			
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setResults(myQuoteDao.getUserQuote(userSession.getCustomerSequenceNumber()));
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
