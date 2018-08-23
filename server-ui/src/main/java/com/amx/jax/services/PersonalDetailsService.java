
package com.amx.jax.services;

import java.math.BigDecimal;
import java.sql.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.DetailsConstants;
import com.amx.jax.constants.Message;
import com.amx.jax.constants.MessageKey;
import com.amx.jax.dao.CustomerRegistrationDao;
import com.amx.jax.dao.PersonalDetailsDao;
import com.amx.jax.models.CustomerDetailModel;
import com.amx.jax.models.CustomerDetailRequest;
import com.amx.jax.models.CustomerDetailResponse;
import com.amx.jax.models.CustomerProfileDetailModel;
import com.amx.jax.models.CustomerProfileDetailRequest;
import com.amx.jax.models.CustomerProfileDetailResponse;
import com.amx.jax.models.CustomerProfileUpdateRequest;
import com.amx.jax.models.CustomerProfileUpdateResponse;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.PersonalDetailsOtpRequest;
import com.amx.jax.models.RegSession;
import com.amx.jax.models.Validate;
import com.insurance.generateotp.RequestOtpModel;
import com.insurance.generateotp.ResponseOtpModel;

@Service
public class PersonalDetailsService
{
	String TAG = "com.amx.jax.services :: PersonalDetailsService :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationService.class);

	@Autowired
	public PersonalDetailsDao personalDetailsDao;

	@Autowired
	CustomerRegistrationDao customerRegistrationDao;

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@Autowired
	RegSession regSession;

	@Autowired
	MetaData metaData;

	public AmxApiResponse<CustomerProfileDetailResponse, Object> getProfileDetails()
	{
		AmxApiResponse<CustomerProfileDetailResponse, Object> resp = new AmxApiResponse<CustomerProfileDetailResponse, Object>();
		CustomerProfileDetailResponse customerProfileDetailResponse = new CustomerProfileDetailResponse();
		CustomerProfileDetailModel customerProfileDetailModel = new CustomerProfileDetailModel();

		customerProfileDetailModel = personalDetailsDao.getProfileDetails();

		customerProfileDetailResponse.setAreaCode(customerProfileDetailModel.getAreaCode());
		customerProfileDetailResponse.setAreaDesc(customerProfileDetailModel.getAreaDesc());
		customerProfileDetailResponse.setBusinessCode(customerProfileDetailModel.getBusinessCode());
		customerProfileDetailResponse.setBusinessDesc(customerProfileDetailModel.getBusinessDesc());
		customerProfileDetailResponse.setEmail(customerProfileDetailModel.getEmail());
		customerProfileDetailResponse.setEnglishName(customerProfileDetailModel.getEnglishName());
		customerProfileDetailResponse.setGenderCode(customerProfileDetailModel.getGenderCode());
		customerProfileDetailResponse.setGenderDesc(customerProfileDetailModel.getGenderDesc());
		customerProfileDetailResponse.setGovCode(customerProfileDetailModel.getGovCode());
		customerProfileDetailResponse.setGovDesc(customerProfileDetailModel.getGovDesc());
		customerProfileDetailResponse.setIdExpiryDate(customerProfileDetailModel.getIdExpiryDate());
		customerProfileDetailResponse.setLanguageId(customerProfileDetailModel.getLanguageId());
		customerProfileDetailResponse.setMobile(customerProfileDetailModel.getMobile());
		customerProfileDetailResponse.setNatyCode(customerProfileDetailModel.getNatyCode());
		customerProfileDetailResponse.setNatyDesc(customerProfileDetailModel.getNatyDesc());
		customerProfileDetailResponse.setNativeArabicName(customerProfileDetailModel.getNativeArabicName());

		logger.info(TAG + " getProfileDetails :: customerProfileDetailResponse :" + customerProfileDetailResponse);

		if (customerProfileDetailModel.getStatus())
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
		}

		resp.setData(customerProfileDetailResponse);
		resp.setMessageKey(customerProfileDetailModel.getErrorCode());
		resp.setMessage(customerProfileDetailModel.getErrorCode());

		return resp;
	}

	public AmxApiResponse<?, Object> updateProfileDetails(String mOtp, String eOtp, CustomerProfileUpdateRequest customerProfileUpdateRequest)
	{
		AmxApiResponse<?, Object> resp = new AmxApiResponse<CustomerProfileUpdateResponse, Object>();
		CustomerProfileDetailModel customerProfileDetailModel = new CustomerProfileDetailModel();
		CustomerProfileDetailModel customerProfileDetailModelCheck = new CustomerProfileDetailModel();
		CustomerProfileUpdateResponse customerProfileUpdateResponse = new CustomerProfileUpdateResponse();
		Validate validate = new Validate();

		customerProfileDetailModelCheck = personalDetailsDao.getProfileDetails();

		logger.info(TAG + " updateProfileDetails :: getMobile 1 :" + customerProfileDetailModelCheck.getMobile());
		logger.info(TAG + " updateProfileDetails :: getEmail  2 :" + customerProfileDetailModelCheck.getEmail());
		logger.info(TAG + " updateProfileDetails :: getMobile 3 :" + customerProfileUpdateRequest.getMobile());
		logger.info(TAG + " updateProfileDetails :: getEmail  4 :" + customerProfileUpdateRequest.getEmail());

		
		
		/* 
		if (!customerProfileDetailModelCheck.getMobile().equals(customerProfileUpdateRequest.getMobile()) && !customerProfileDetailModelCheck.getEmail().equals(customerProfileUpdateRequest.getEmail()))
		{
			logger.info(TAG + " updateProfileDetails :: Both Chnaged");
			logger.info(TAG + " updateProfileDetails :: mOtp :" + mOtp);
			logger.info(TAG + " updateProfileDetails :: eOtp :" + eOtp);

			if (null != mOtp && !mOtp.equals("") && null != eOtp && !eOtp.equals(""))
			{
				if (!metaData.getmOtpMobileNumber().equals(customerProfileUpdateRequest.getMobile()) || !metaData.geteOtpEmailId().equals(customerProfileUpdateRequest.getEmail()))
				{
					ResponseOtpModel responseOtpModel = customerRegistrationService.sendEmailOtpTemp(customerProfileUpdateRequest.getEmail());
					resp.setMeta(responseOtpModel);
					resp.setStatusKey(ApiConstants.FAILURE);
					resp.setMessageKey(MessageKey.KEY_EMAIL_MOBILE_OTP_REQUIRED);
					return resp;
				}

				if (!metaData.getMotp().equals(mOtp) || !metaData.getEotp().equals(eOtp))
				{
					validate.setValid(false);
					resp.setStatusKey(ApiConstants.FAILURE);
					resp.setMessage(Message.REG_INVALID_OTP);
					resp.setMessageKey(MessageKey.KEY_REG_VALIDATE_OTP);
					return resp;
				}
			}
			else
			{
				ResponseOtpModel responseOtpModel = customerRegistrationService.sendEmailOtpTemp(customerProfileUpdateRequest.getEmail());
				resp.setMeta(responseOtpModel);
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessageKey(MessageKey.KEY_EMAIL_MOBILE_OTP_REQUIRED);
				return resp;
			}
		}
		else if (!customerProfileDetailModelCheck.getEmail().equals(customerProfileUpdateRequest.getEmail()))
		{
			logger.info(TAG + " updateProfileDetails :: Email ");
			logger.info(TAG + " updateProfileDetails :: eOtp :" + eOtp);

			if (null != eOtp && !eOtp.equals(""))
			{
				if (!metaData.geteOtpEmailId().equals(customerProfileUpdateRequest.getEmail()))
				{
					ResponseOtpModel responseOtpModel = customerRegistrationService.sendEmailOtp(customerProfileUpdateRequest.getEmail());
					resp.setMeta(responseOtpModel);
					resp.setStatusKey(ApiConstants.FAILURE);
					resp.setMessageKey(MessageKey.KEY_EMAIL_OTP_REQUIRED);
					return resp;
				}

				if (!metaData.getEotp().equals(eOtp))
				{
					validate.setValid(false);
					resp.setStatusKey(ApiConstants.FAILURE);
					resp.setMessage(Message.REG_INVALID_OTP);
					resp.setMessageKey(MessageKey.KEY_VALIDATE_EMAIL_OTP);
					return resp;
				}
			}
			else
			{
				ResponseOtpModel responseOtpModel = customerRegistrationService.sendEmailOtpTemp(customerProfileUpdateRequest.getEmail());
				resp.setMeta(responseOtpModel);
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessageKey(MessageKey.KEY_EMAIL_OTP_REQUIRED);
				return resp;
			}
		}
		else if (!customerProfileDetailModelCheck.getMobile().equals(customerProfileUpdateRequest.getMobile()))
		{
			logger.info(TAG + " updateProfileDetails :: Mobile Chnaged");
			logger.info(TAG + " updateProfileDetails :: mOtp :" + mOtp);
		
			if (null != mOtp && !mOtp.equals(""))
			{
				if (!metaData.getmOtpMobileNumber().equals(customerProfileUpdateRequest.getMobile()))
				{
					ResponseOtpModel responseOtpModel = customerRegistrationService.sendEmailOtpTemp(customerProfileUpdateRequest.getEmail());
					resp.setMeta(responseOtpModel);
					resp.setStatusKey(ApiConstants.FAILURE);
					resp.setMessageKey(MessageKey.KEY_MOBILE_OTP_REQUIRED);
					return resp;
				}

				if (!metaData.getMotp().equals(mOtp))
				{
					validate.setValid(false);
					resp.setStatusKey(ApiConstants.FAILURE);
					resp.setMessage(Message.REG_INVALID_OTP);
					resp.setMessageKey(MessageKey.KEY_VALIDATE_MOBILE_OTP);
					return resp;
				}
			}
			else
			{
				ResponseOtpModel responseOtpModel = customerRegistrationService.sendEmailOtpTemp(customerProfileUpdateRequest.getEmail());
				resp.setMeta(responseOtpModel);
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessageKey(MessageKey.KEY_MOBILE_OTP_REQUIRED);
				return resp;
			}
		}*/
		
		

		logger.info(TAG + " updateProfileDetails :: Down ");

		customerProfileDetailModel.setEnglishName(customerProfileUpdateRequest.getEnglishName());
		customerProfileDetailModel.setNativeArabicName(customerProfileUpdateRequest.getNativeArabicName());
		customerProfileDetailModel.setGenderCode(customerProfileUpdateRequest.getGenderCode());
		customerProfileDetailModel.setIdExpiryDate(customerProfileUpdateRequest.getIdExpiryDate());
		customerProfileDetailModel.setBusinessCode(customerProfileUpdateRequest.getBusinessCode());
		customerProfileDetailModel.setNatyCode(customerProfileUpdateRequest.getNatyCode());
		customerProfileDetailModel.setGovCode(customerProfileUpdateRequest.getGovCode());
		customerProfileDetailModel.setAreaCode(customerProfileUpdateRequest.getAreaCode());
		customerProfileDetailModel.setMobile(customerProfileUpdateRequest.getMobile());
		customerProfileDetailModel.setEmail(customerProfileUpdateRequest.getEmail());

		customerProfileDetailModel = personalDetailsDao.updateProfileDetails(customerProfileDetailModel);

		customerProfileUpdateResponse.setStatus(customerProfileDetailModel.getStatus());
		customerProfileUpdateResponse.setErrorCode(customerProfileDetailModel.getErrorCode());
		customerProfileUpdateResponse.setErrorMessage(customerProfileDetailModel.getErrorMessage());

		logger.info(TAG + " updateProfileDetails :: customerProfileDetailModel :" + customerProfileDetailModel);

		if (customerProfileUpdateResponse.getStatus())
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
		}

		resp.setMessageKey(customerProfileUpdateResponse.getErrorCode());
		resp.setMessage(customerProfileUpdateResponse.getErrorCode());

		return resp;
	}

	public AmxApiResponse<?, Object> getBusiness()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setResults(personalDetailsDao.getBusiness());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getNationality()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setResults(personalDetailsDao.getNationality());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;

	}

	public AmxApiResponse<?, Object> getGovernorates()
	{

		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setResults(personalDetailsDao.getGovernorates());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getArea(String gov)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setResults(personalDetailsDao.getArea(gov));

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;

	}

	public AmxApiResponse<?, Object> getGender()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setResults(personalDetailsDao.getGender());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;

	}

	public AmxApiResponse<?, Object> emailMobileOtpInitiate(PersonalDetailsOtpRequest personalDetailsOtpRequest)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();
		RequestOtpModel requestOtpModel = new RequestOtpModel();
		requestOtpModel.setCivilId(metaData.getCivilId());
		requestOtpModel.setEmailId(personalDetailsOtpRequest.getEmailId());
		requestOtpModel.setMobileNumber(personalDetailsOtpRequest.getMobileNumber());

		try
		{
			AmxApiResponse<Validate, Object> isValidMobileNumber = customerRegistrationService.isValidMobileNumber(metaData.getCivilId());
			AmxApiResponse<Validate, Object> mobileNumberExists = customerRegistrationService.isMobileNumberExist(metaData.getCivilId());
			AmxApiResponse<Validate, Object> validateEmailID = customerRegistrationService.isValidEmailId(metaData.getCivilId());
			AmxApiResponse<Validate, Object> emailIdExists = customerRegistrationService.isEmailIdExist(metaData.getCivilId());
			AmxApiResponse<Validate, Object> isOtpEnabled = customerRegistrationService.isOtpEnabled(metaData.getCivilId());

			if (isValidMobileNumber.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return isValidMobileNumber;
			}

			if (validateEmailID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return validateEmailID;
			}

			if (mobileNumberExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS) && emailIdExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				customerRegistrationService.sendFailedRegistration(DetailsConstants.REG_INCOMPLETE_TYPE_MOBE_MAIL, requestOtpModel, mobileNumberExists.getMessage());
				mobileNumberExists.setMessageKey(MessageKey.KEY_MOBILE_OR_EMAIL_ALREADY_EXISTS);
				mobileNumberExists.setStatusKey(ApiConstants.FAILURE);
				return mobileNumberExists;
			}
			else if (mobileNumberExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				customerRegistrationService.sendFailedRegistration(DetailsConstants.REG_INCOMPLETE_TYPE_DUPLICATE_MOBILE, requestOtpModel, mobileNumberExists.getMessage());
				mobileNumberExists.setMessageKey(MessageKey.KEY_MOBILE_OR_EMAIL_ALREADY_EXISTS);
				mobileNumberExists.setStatusKey(ApiConstants.FAILURE);
				return mobileNumberExists;
			}
			else if (emailIdExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				customerRegistrationService.sendFailedRegistration(DetailsConstants.REG_INCOMPLETE_TYPE_DUPLICATE_EMAIL, requestOtpModel, emailIdExists.getMessage());
				emailIdExists.setMessageKey(MessageKey.KEY_MOBILE_OR_EMAIL_ALREADY_EXISTS);
				emailIdExists.setStatusKey(ApiConstants.FAILURE);
				return emailIdExists;
			}

			if (isOtpEnabled.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return isOtpEnabled;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setData(null);
			resp.setException(e.toString());
			resp.setStatus(ApiConstants.FAILURE);
			return resp;
		}

		try
		{

			AmxApiResponse<Validate, Object> setOtpCount = customerRegistrationService.setOtpCount(metaData.getCivilId());

			responseOtpModel = customerRegistrationService.sendEmailOtpTemp(requestOtpModel.getEmailId());

			/*
			 * Code Needed Here For Mobile Otp
			 * 
			 */

			resp.setData(responseOtpModel);
			resp.setStatus(ApiConstants.SUCCESS);

			if (setOtpCount.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return setOtpCount;
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setData(null);
			resp.setException(e.toString());
			resp.setStatus(ApiConstants.FAILURE);
		}
		return resp;
	}
	
}
