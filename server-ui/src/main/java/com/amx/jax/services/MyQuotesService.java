package com.amx.jax.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.dao.MyQuoteDao;
import com.amx.jax.models.MyQuoteModel;
import com.amx.jax.ui.session.UserSession;

@Service
public class MyQuotesService
{
	private static final Logger logger = LoggerFactory.getLogger(MyQuotesService.class);
	
	@Autowired
	UserSession userSession;

	@Autowired
	private MyQuoteDao myQuoteDao;

	public AmxApiResponse<?, Object> getUserQuote()
	{
		AmxApiResponse<MyQuoteModel, Object> resp = new AmxApiResponse<MyQuoteModel, Object>();
		try
		{
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			resp.setResults(myQuoteDao.getUserQuote(userSession.getCustomerSequenceNumber(), userSession.getLanguageId()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;
	}
}
