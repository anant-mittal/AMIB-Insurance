
package com.amx.jax.services;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.constants.MessageKey;
import com.amx.jax.dao.MyPolicyDao;
import com.amx.jax.models.ActivePolicyModel;
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
	String TAG = "com.amx.jax.services :: MyPolicyService :: ";

	private static final Logger logger = LoggerFactory.getLogger(MyPolicyService.class);

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
			resp.setResults(myPolicyDao.getUserActivePolicy(userSession.getUserAmibCustRef(), userSession.getCivilId() , HardCodedValues.USER_TYPE , userSession.getCustomerSequenceNumber()));
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
		logger.info(TAG + " getRenewPolicyDetails :: oldDocNumber :" + oldDocNumber);
		BigDecimal insuranceCompCode = null;
		BigDecimal appSeqNumber = null;
		AmxApiResponse<RequestQuoteModel, Object> resp = new AmxApiResponse<RequestQuoteModel, Object>();

		try
		{
			AmxApiResponse<?, Object> respPersonalDetails = requestQuoteService.getProfileDetails();
			if (respPersonalDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return respPersonalDetails;
			}
			else
			{
				String checkRenewableApplicable = myPolicyDao.checkRenewableApplicable(oldDocNumber , userSession.getCivilId() , HardCodedValues.USER_TYPE , userSession.getCustomerSequenceNumber());
				logger.info(TAG + " renewInsuranceOldPolicy :: checkRenewableApplicable :" + checkRenewableApplicable);
				if(null != checkRenewableApplicable && !checkRenewableApplicable.equals(""))
				{
					resp.setStatusKey(ApiConstants.FAILURE);
					resp.setMessageKey(checkRenewableApplicable);
					return resp;
				}
				
				PersonalDetails personalDetails = (PersonalDetails) respPersonalDetails.getData();
				if (null != personalDetails.getIdExpiryDate())
				{
					String dateFromDb = personalDetails.getIdExpiryDate();
					if (DateFormats.checkExpiryDate(dateFromDb))
					{
						resp.setStatusKey(ApiConstants.FAILURE);
						resp.setMessageKey(MessageKey.KEY_CIVIL_ID_EXPIRED);
						return resp;
					}
				}

				AmxApiResponse<?, Object> getVehicleDetails = requestQuoteService.getRenewPolicyVehicleDetails(oldDocNumber);
				if (getVehicleDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
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
					if (submitVehicleDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
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
					if (setPersonalDetails.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
					{
						return setPersonalDetails;
					}
					
					AmxApiResponse<?, Object> updateInsuranceProvider = requestQuoteService.updateInsuranceProvider(appSeqNumber, insuranceCompCode , userSession.getCivilId());
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
	
	public PolicyReceiptDetails downloadPolicyReceipt(BigDecimal docNumber)
	{
		PolicyReceiptDetails policyReceiptDetails = null;
		try
		{
			policyReceiptDetails = myPolicyDao.downloadPolicyReceipt(docNumber);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return policyReceiptDetails;
	}
	
}
