
package com.amx.jax.services;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.dao.MyPolicyDao;
import com.amx.jax.models.ActivePolicyModel;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.PersonalDetails;
import com.amx.jax.models.PolicyReceiptDetails;
import com.amx.jax.models.RequestQuoteInfo;
import com.amx.jax.models.RequestQuoteModel;
import com.amx.jax.models.VehicleDetails;
import com.amx.jax.ui.session.UserSession;

@Service
public class MyPolicyService
{
	String TAG = "MyPolicyService :: ";

	private static final Logger logger = LoggerFactory.getLogger(MyPolicyService.class);

	@Autowired
	UserSession userSession;
	
	@Autowired
	private MyPolicyDao myPolicyDao;
	
	@Autowired
	public RequestQuoteService requestQuoteService;

	public AmxApiResponse<ActivePolicyModel, Object> getUserActivePolicy()
	{
		AmxApiResponse<ActivePolicyModel, Object> resp = new AmxApiResponse<ActivePolicyModel, Object>();
		try
		{
			
			ArrayResponseModel userActivePolicyDetails =  myPolicyDao.getUserActivePolicy(userSession.getUserAmibCustRef(), userSession.getCivilId() , HardCodedValues.USER_TYPE , userSession.getCustomerSequenceNumber(), userSession.getLanguageId()); 
			if(null != userActivePolicyDetails.getErrorCode())
			{
				resp.setMessageKey(userActivePolicyDetails.getErrorCode());
				resp.setMessage(userActivePolicyDetails.getErrorMessage());
				return resp;
			}
			
			if(null == userSession.getUserAmibCustRef())
			{
				if(null != userActivePolicyDetails.getData())
				{
					BigDecimal amibRef = new BigDecimal(userActivePolicyDetails.getData());
					logger.info(TAG + " getUserActivePolicy :: amibRef :" + amibRef);
					userSession.setUserAmibCustRef(amibRef);
				}
			}
			resp.setResults(userActivePolicyDetails.getDataArray());
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "getUserActivePolicy :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}
	
	
	public AmxApiResponse<?, Object> renewInsuranceOldPolicy(BigDecimal oldDocNumber)
	{
		BigDecimal insuranceCompCode = null;
		BigDecimal appSeqNumber = null;
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();

		try
		{
			AmxApiResponse<?, Object> respPersonalDetails = requestQuoteService.getProfileDetails();
			if (!respPersonalDetails.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				return respPersonalDetails;
			}
			else
			{
				ArrayResponseModel arrayResponseModel =  myPolicyDao.checkRenewableApplicable(oldDocNumber , userSession.getCivilId() , HardCodedValues.USER_TYPE , userSession.getCustomerSequenceNumber()); 
				if(null != arrayResponseModel.getErrorCode())
				{
					resp.setMessageKey(arrayResponseModel.getErrorCode());
					resp.setMessage(arrayResponseModel.getErrorMessage());
					return resp;
				}
				else
				{
					String checkRenewableApplicable = arrayResponseModel.getData();
					if(null != checkRenewableApplicable && !checkRenewableApplicable.equals(""))
					{
						resp.setStatusKey(checkRenewableApplicable);
						resp.setMessageKey(checkRenewableApplicable);
						return resp;
					}
				}
				
				
				PersonalDetails personalDetails = (PersonalDetails) respPersonalDetails.getData();
				if (null != personalDetails.getIdExpiryDate())
				{
					String dateFromDb = personalDetails.getIdExpiryDate();
					if (DateFormats.checkExpiryDate(dateFromDb))
					{
						resp.setStatusKey(WebAppStatusCodes.CIVIL_ID_EXPIRED.toString());
						resp.setMessageKey(WebAppStatusCodes.CIVIL_ID_EXPIRED.toString());
						return resp;
					}
				}

				AmxApiResponse<?, Object> getVehicleDetails = requestQuoteService.getRenewPolicyVehicleDetails(oldDocNumber);
				if (!getVehicleDetails.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
				{
					return getVehicleDetails;
				}
				else
				{
					if (null != getVehicleDetails.getMeta())
					{
						insuranceCompCode = (BigDecimal) getVehicleDetails.getMeta();
					}

					AmxApiResponse<?, Object> submitVehicleDetails = requestQuoteService.setAppVehicleDetails(appSeqNumber, (VehicleDetails) getVehicleDetails.getData(), oldDocNumber);
					if (!submitVehicleDetails.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
					{
						return submitVehicleDetails;
					}
					else
					{
						RequestQuoteModel requestQuoteModel = (RequestQuoteModel) submitVehicleDetails.getData();
						RequestQuoteInfo requestQuoteInfo = requestQuoteModel.getRequestQuoteInfo();
						appSeqNumber = requestQuoteInfo.getAppSeqNumber();
					}
					
					AmxApiResponse<?, Object> setPersonalDetails = requestQuoteService.setProfileDetails(appSeqNumber, (PersonalDetails) respPersonalDetails.getData());
					if (!setPersonalDetails.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
					{
						return setPersonalDetails;
					}
					
					AmxApiResponse<?, Object> updateInsuranceProvider = requestQuoteService.updateInsuranceProvider(appSeqNumber, insuranceCompCode , userSession.getCivilId());
					if (!updateInsuranceProvider.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
					{
						return updateInsuranceProvider;
					}
				}
			}
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "renewInsuranceOldPolicy :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}
	
	public ArrayResponseModel downloadPolicyReceipt(BigDecimal docNumber)
	{
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		try
		{
			arrayResponseModel = myPolicyDao.downloadPolicyReceipt(docNumber, userSession.getLanguageId());
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"downloadPolicyReceipt :: exception :" + e);
			e.printStackTrace();
		}
		return arrayResponseModel;
	}
}
