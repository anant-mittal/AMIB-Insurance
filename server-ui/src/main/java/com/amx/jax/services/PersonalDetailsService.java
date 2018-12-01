
package com.amx.jax.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.DetailsConstants;
import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.constants.MessageKey;
import com.amx.jax.dao.CustomerRegistrationDao;
import com.amx.jax.dao.PersonalDetailsDao;
import com.amx.jax.models.CustomerProfileDetailModel;
import com.amx.jax.models.CustomerProfileDetailResponse;
import com.amx.jax.models.CustomerProfileUpdateRequest;
import com.amx.jax.models.CustomerProfileUpdateResponse;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.ResponseInfo;
import com.amx.jax.ui.session.UserSession;

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
	UserSession userSession;

	@Autowired
	private EmailSmsService emailSmsService;

	public AmxApiResponse<CustomerProfileDetailResponse, Object> getProfileDetails()
	{
		AmxApiResponse<CustomerProfileDetailResponse, Object> resp = new AmxApiResponse<CustomerProfileDetailResponse, Object>();
		CustomerProfileDetailResponse customerProfileDetailResponse = new CustomerProfileDetailResponse();
		CustomerProfileDetailModel customerProfileDetailModel = new CustomerProfileDetailModel();

		customerProfileDetailModel = personalDetailsDao.getProfileDetails(userSession.getCivilId() , HardCodedValues.USER_TYPE , userSession.getCustomerSequenceNumber());

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
		customerProfileDetailResponse.setIdExpiryDate(DateFormats.uiFormattedDate(customerProfileDetailModel.getIdExpiryDate()));
		customerProfileDetailResponse.setLanguageId(customerProfileDetailModel.getLanguageId());
		customerProfileDetailResponse.setMobile(customerProfileDetailModel.getMobile());
		customerProfileDetailResponse.setNatyCode(customerProfileDetailModel.getNatyCode());
		customerProfileDetailResponse.setNatyDesc(customerProfileDetailModel.getNatyDesc());
		customerProfileDetailResponse.setNativeArabicName(customerProfileDetailModel.getNativeArabicName());
		
		if (customerProfileDetailModel.getErrorCode() == null)
		{
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
		}
		else
		{
			resp.setStatusKey(customerProfileDetailModel.getErrorCode());
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
		
		customerProfileDetailModelCheck = personalDetailsDao.getProfileDetails(userSession.getCivilId() , HardCodedValues.USER_TYPE , userSession.getCustomerSequenceNumber());
		if (null != customerProfileUpdateRequest.getIdExpiryDate())
		{
			String dateFromDb = customerProfileUpdateRequest.getIdExpiryDate();
			if (DateFormats.checkExpiryDate(dateFromDb))
			{
				resp.setStatusKey(WebAppStatusCodes.CIVIL_ID_EXPIRED.toString());
				resp.setMessageKey(WebAppStatusCodes.CIVIL_ID_EXPIRED.toString());
				return resp;
			}
		}

		if (null != customerProfileDetailModelCheck.getMobile() && null != customerProfileDetailModelCheck.getEmail() && !customerProfileDetailModelCheck.getMobile().equals(customerProfileUpdateRequest.getMobile())
				&& !customerProfileDetailModelCheck.getEmail().equals(customerProfileUpdateRequest.getEmail()))

		{
			logger.info(TAG + " updateProfileDetails :: Both Chnaged");

			AmxApiResponse<ResponseInfo, Object> isValidMobileNumber = customerRegistrationService.isValidMobileNumber(customerProfileUpdateRequest.getMobile());
			if (!isValidMobileNumber.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				return isValidMobileNumber;
			}

			AmxApiResponse<ResponseInfo, Object> validateEmailID = customerRegistrationService.isValidEmailId(customerProfileUpdateRequest.getEmail());
			if (!validateEmailID.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				return validateEmailID;
			}

			AmxApiResponse<ResponseInfo, Object> mobileNumberExists = customerRegistrationService.isMobileNumberExist(customerProfileUpdateRequest.getMobile());
			if (mobileNumberExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				mobileNumberExists.setMessageKey(WebAppStatusCodes.MOBILE_NUMBER_REGISTERED.toString());
				mobileNumberExists.setStatusKey(WebAppStatusCodes.MOBILE_NUMBER_REGISTERED.toString());
				return mobileNumberExists;
			}

			AmxApiResponse<ResponseInfo, Object> emailIdExists = customerRegistrationService.isEmailIdExist(customerProfileUpdateRequest.getEmail());
			if (emailIdExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				emailIdExists.setMessageKey(WebAppStatusCodes.EMAIL_ID_REGESTERED.toString());
				emailIdExists.setStatusKey(WebAppStatusCodes.EMAIL_ID_REGESTERED.toString());
				return emailIdExists;
			}

			AmxApiResponse<?, Object> validateDOTP = emailSmsService.validateDOTP(eOtp, mOtp, customerProfileUpdateRequest.getEmail(), customerProfileUpdateRequest.getMobile() , DetailsConstants.UPDATE_PROFILE_OTP);
			if (null != validateDOTP)
			{
				return validateDOTP;
			}
		}
		else if (null != customerProfileDetailModelCheck.getEmail() && !customerProfileDetailModelCheck.getEmail().equals(customerProfileUpdateRequest.getEmail()))
		{
			AmxApiResponse<ResponseInfo, Object> validateEmailID = customerRegistrationService.isValidEmailId(customerProfileUpdateRequest.getEmail());
			if (!validateEmailID.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				return validateEmailID;
			}

			AmxApiResponse<ResponseInfo, Object> emailIdExists = customerRegistrationService.isEmailIdExist(customerProfileUpdateRequest.getEmail());
			if (emailIdExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				emailIdExists.setMessageKey(WebAppStatusCodes.EMAIL_ID_REGESTERED.toString());
				emailIdExists.setStatusKey(WebAppStatusCodes.EMAIL_ID_REGESTERED.toString());
				return emailIdExists;
			}
			
			logger.info(TAG + " updateProfileDetails :: Email "+customerProfileDetailModelCheck.getEmail());
			AmxApiResponse<?, Object> validateEOTP = emailSmsService.validateEotp(eOtp, customerProfileUpdateRequest.getEmail() , DetailsConstants.UPDATE_PROFILE_OTP);
			if (null != validateEOTP)
			{
				return validateEOTP;
			}

		}
		else if (null != customerProfileDetailModelCheck.getMobile() && !customerProfileDetailModelCheck.getMobile().equals(customerProfileUpdateRequest.getMobile()))
		{
			logger.info(TAG + " updateProfileDetails :: Mobile Chnaged");

			AmxApiResponse<ResponseInfo, Object> isValidMobileNumber = customerRegistrationService.isValidMobileNumber(customerProfileUpdateRequest.getMobile());
			if (!isValidMobileNumber.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				return isValidMobileNumber;
			}

			AmxApiResponse<ResponseInfo, Object> mobileNumberExists = customerRegistrationService.isMobileNumberExist(customerProfileUpdateRequest.getMobile());
			if (mobileNumberExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				mobileNumberExists.setMessageKey(WebAppStatusCodes.MOBILE_NUMBER_REGISTERED.toString());
				mobileNumberExists.setStatusKey(WebAppStatusCodes.MOBILE_NUMBER_REGISTERED.toString());
				return mobileNumberExists;
			}

			AmxApiResponse<?, Object> validateMOTP = emailSmsService.validateMotp(mOtp, customerProfileUpdateRequest.getMobile());
			if (null != validateMOTP)
			{
				return validateMOTP;
			}
		}

		customerProfileDetailModel.setEnglishName(customerProfileUpdateRequest.getEnglishName());
		customerProfileDetailModel.setNativeArabicName(customerProfileUpdateRequest.getNativeArabicName());
		customerProfileDetailModel.setGenderCode(customerProfileUpdateRequest.getGenderCode());
		customerProfileDetailModel.setIdExpiryDate(DateFormats.setDbSqlFormatDate(customerProfileUpdateRequest.getIdExpiryDate().toString()));
		customerProfileDetailModel.setBusinessCode(customerProfileUpdateRequest.getBusinessCode());
		customerProfileDetailModel.setNatyCode(customerProfileUpdateRequest.getNatyCode());
		customerProfileDetailModel.setGovCode(customerProfileUpdateRequest.getGovCode());
		customerProfileDetailModel.setAreaCode(customerProfileUpdateRequest.getAreaCode());
		customerProfileDetailModel.setMobile(customerProfileUpdateRequest.getMobile());
		customerProfileDetailModel.setEmail(customerProfileUpdateRequest.getEmail());
		
		customerProfileDetailModel = personalDetailsDao.updateProfileDetails(customerProfileDetailModel , userSession.getCivilId() , HardCodedValues.USER_TYPE , userSession.getCustomerSequenceNumber());
		logger.info(TAG + " updateProfileDetails :: getCivilId :" + customerProfileDetailModel.getCustSequenceNumber());
		userSession.setCustomerSequenceNumber(customerProfileDetailModel.getCustSequenceNumber());
		
		customerProfileUpdateResponse.setStatus(customerProfileDetailModel.getStatus());
		customerProfileUpdateResponse.setErrorCode(customerProfileDetailModel.getErrorCode());
		customerProfileUpdateResponse.setErrorMessage(customerProfileDetailModel.getErrorMessage());
		if (customerProfileUpdateResponse.getErrorCode() == null)
		{
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
		}
		else
		{
			resp.setStatusKey(customerProfileUpdateResponse.getErrorCode().toString());
		}
		resp.setMessageKey(customerProfileUpdateResponse.getErrorCode());
		resp.setMessage(customerProfileUpdateResponse.getErrorMessage());

		return resp;
	}

	public AmxApiResponse<?, Object> getBusiness()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			resp.setResults(personalDetailsDao.getBusiness());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getNationality()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			resp.setResults(personalDetailsDao.getNationality());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;

	}

	public AmxApiResponse<?, Object> getGovernorates()
	{

		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			resp.setResults(personalDetailsDao.getGovernorates());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getArea(String gov)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			resp.setResults(personalDetailsDao.getArea(gov));

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;

	}

	public AmxApiResponse<?, Object> getGender()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try
		{
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			resp.setResults(personalDetailsDao.getGender());

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
		}
		return resp;

	}
}
