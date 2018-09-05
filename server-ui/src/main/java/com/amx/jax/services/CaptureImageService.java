package com.amx.jax.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.controllers.ActivePolicyController;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.ImageMandatoryResponse;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.RegSession;
import com.amx.jax.dao.CaptureImageDao;
import com.amx.jax.dao.RequestQuoteDao;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CaptureImageService
{
	String TAG = "com.amx.jax.services :: RequestQuoteService :: ";

	private static final Logger logger = LoggerFactory.getLogger(ActivePolicyController.class);
	
	@Autowired
	RegSession regSession;

	@Autowired
	MetaData metaData;
	
	@Autowired
	private CaptureImageDao captureImageDao;
	
	public AmxApiResponse<ImageMandatoryResponse, Object> getMandatoryImage()
	{
		AmxApiResponse<ImageMandatoryResponse, Object> resp = new AmxApiResponse<ImageMandatoryResponse, Object>();
		try
		{
			ArrayResponseModel arrayResponseModel = captureImageDao.getMandatoryImage();
			if (null == arrayResponseModel.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}
			resp.setResults(arrayResponseModel.getDataArray());
			resp.setMessageKey(arrayResponseModel.getErrorCode());
			resp.setMessage(arrayResponseModel.getErrorCode());
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
