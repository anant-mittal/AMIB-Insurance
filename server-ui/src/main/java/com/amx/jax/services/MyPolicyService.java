
package com.amx.jax.services;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.dao.MyPolicyDao;
import com.amx.jax.models.ActivePolicyModel;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.PersonalDetails;
import com.amx.jax.models.RequestQuoteInfo;
import com.amx.jax.models.RequestQuoteModel;
import com.amx.jax.models.VehicleDetails;
import com.amx.jax.ui.session.UserSession;

@Service
public class MyPolicyService
{
	String TAG = "com.amx.jax.services :: MyPolicyService :: ";

	private static final Logger logger = LoggerFactory.getLogger(MyPolicyService.class);

	@Autowired
	MetaData metaData;

	@Autowired
	UserSession userSession;
	
	@Autowired
	private MyPolicyDao myPolicyDao;
	
	@Autowired
	public RequestQuoteService requestQuoteService;

	public AmxApiResponse<ActivePolicyModel, Object> getUserActivePolicy()
	{
		logger.info(TAG + " MyPolicyService ::");
		
		AmxApiResponse<ActivePolicyModel, Object> resp = new AmxApiResponse<ActivePolicyModel, Object>();
		try
		{
			resp.setResults(myPolicyDao.getUserActivePolicy(userSession.getUserAmibCustRef(), userSession.getCivilId() , userSession.getUserType() , userSession.getCustomerSequenceNumber()));
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
	
	
	public AmxApiResponse<?, Object> renewInsuranceOldPolicy(BigDecimal oldDocNumber)
	{
		BigDecimal insuranceCompCode = null;
		BigDecimal appSeqNumber = null;
		logger.info(TAG + " getRenewPolicyDetails :: oldDocNumber :" + oldDocNumber);

		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();
		try
		{
			AmxApiResponse<?, Object> respPersonalDetails = requestQuoteService.getProfileDetails();
			logger.info(TAG + " getRenewPolicyDetails :: respPersonalDetails :" + respPersonalDetails.getStatusKey());
			if (respPersonalDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return respPersonalDetails;
			}
			else
			{
				
				
				
				
				
				
				
				
				
				
				
				AmxApiResponse<?, Object> getVehicleDetails = requestQuoteService.getRenewPolicyVehicleDetails(oldDocNumber);
				logger.info(TAG + " getRenewPolicyDetails :: getVehicleDetails :" + getVehicleDetails.getStatusKey());
				if (getVehicleDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
				{
					return getVehicleDetails;
				}
				else
				{
					if (null != getVehicleDetails.getMeta())
					{
						insuranceCompCode = (BigDecimal) getVehicleDetails.getMeta();
						logger.info(TAG + " getRenewPolicyDetails :: insuranceCompCode :" + insuranceCompCode);
					}

					AmxApiResponse<?, Object> submitVehicleDetails = requestQuoteService.setAppVehicleDetails(appSeqNumber, (VehicleDetails) getVehicleDetails.getData(), oldDocNumber);
					logger.info(TAG + " getRenewPolicyDetails :: submitVehicleDetails :" + submitVehicleDetails.getStatusKey());
					if (submitVehicleDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
					{
						return submitVehicleDetails;
					}
					else
					{
						RequestQuoteModel requestQuoteModel = (RequestQuoteModel) submitVehicleDetails.getData();
						RequestQuoteInfo requestQuoteInfo = requestQuoteModel.getRequestQuoteInfo();
						appSeqNumber = requestQuoteInfo.getAppSeqNumber();
						logger.info(TAG + " getRenewPolicyDetails :: appSeqNumber1 :" + appSeqNumber);
					}
					
					AmxApiResponse<?, Object> setPersonalDetails = requestQuoteService.setProfileDetails(appSeqNumber, (PersonalDetails) respPersonalDetails.getData());
					logger.info(TAG + " getRenewPolicyDetails :: setPersonalDetails :" + setPersonalDetails.getStatusKey());
					if (setPersonalDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
					{
						return setPersonalDetails;
					}
					
					AmxApiResponse<?, Object> updateInsuranceProvider = requestQuoteService.updateInsuranceProvider(appSeqNumber, insuranceCompCode , userSession.getCivilId());
					logger.info(TAG + " getRenewPolicyDetails :: updateInsuranceProvider :" + updateInsuranceProvider.getStatusKey());
					if (updateInsuranceProvider.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
					{
						return updateInsuranceProvider;
					}
				}
				
				
				
				
				
				
				
				
				
				
				
				
				
			}
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setMessage(e.toString());
			return resp;
		}
		return resp;
	}
}
