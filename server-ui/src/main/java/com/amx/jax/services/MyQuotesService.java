package com.amx.jax.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.dao.MyQuoteDao;
import com.amx.jax.models.ActivePolicyModel;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.RegSession;

@Service
public class MyQuotesService
{
	String TAG = "com.amx.jax.services :: MyQuotesService :: ";

	private static final Logger logger = LoggerFactory.getLogger(MyQuotesService.class);

	@Autowired
	RegSession regSession;

	@Autowired
	MetaData metaData;

	@Autowired
	private MyQuoteDao myQuoteDao;

	public AmxApiResponse<?, Object> getUserQuote()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			logger.info(TAG + " getUserQuote :: ");
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setData(myQuoteDao.getUserQuote());
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
