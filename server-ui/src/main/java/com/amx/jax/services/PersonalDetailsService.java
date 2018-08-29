
package com.amx.jax.services;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.common.DateFormats;
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
import com.insurance.model.RequestOtpModel;
import com.insurance.model.ResponseOtpModel;
import com.insurance.services.OtpService;

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

	@Autowired
	private OtpService otpService;

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

		customerProfileDetailModelCheck = personalDetailsDao.getProfileDetails();

		logger.info(TAG + " updateProfileDetails :: getMobile 1 :" + customerProfileDetailModelCheck.getMobile());
		logger.info(TAG + " updateProfileDetails :: getEmail  2 :" + customerProfileDetailModelCheck.getEmail());
		logger.info(TAG + " updateProfileDetails :: getMobile 3 :" + customerProfileUpdateRequest.getMobile());
		logger.info(TAG + " updateProfileDetails :: getEmail  4 :" + customerProfileUpdateRequest.getEmail());

		if (null != customerProfileUpdateRequest.getIdExpiryDate())
		{
			String dateFromDb = customerProfileUpdateRequest.getIdExpiryDate().toString();
			if (checkExpiryDate(dateFromDb))
			{
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessageKey(MessageKey.KEY_CIVIL_ID_EXPIRED);
				return resp;
			}
		}

		if (null != customerProfileDetailModelCheck.getMobile() && null != customerProfileDetailModelCheck.getEmail() && !customerProfileDetailModelCheck.getMobile().equals(customerProfileUpdateRequest.getMobile())
				&& !customerProfileDetailModelCheck.getEmail().equals(customerProfileUpdateRequest.getEmail()))

		{
			logger.info(TAG + " updateProfileDetails :: Both Chnaged");

			AmxApiResponse<Validate, Object> isValidMobileNumber = customerRegistrationService.isValidMobileNumber(customerProfileUpdateRequest.getMobile());
			if (isValidMobileNumber.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return isValidMobileNumber;
			}

			AmxApiResponse<Validate, Object> validateEmailID = customerRegistrationService.isValidEmailId(customerProfileUpdateRequest.getEmail());
			if (validateEmailID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return validateEmailID;
			}

			AmxApiResponse<Validate, Object> mobileNumberExists = customerRegistrationService.isMobileNumberExist(customerProfileUpdateRequest.getMobile());
			if (mobileNumberExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				mobileNumberExists.setMessageKey(MessageKey.KEY_MOBILE_NO_ALREADY_REGISTER);
				mobileNumberExists.setStatusKey(ApiConstants.FAILURE);
				return mobileNumberExists;
			}

			AmxApiResponse<Validate, Object> emailIdExists = customerRegistrationService.isEmailIdExist(customerProfileUpdateRequest.getEmail());
			if (emailIdExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				emailIdExists.setMessageKey(MessageKey.KEY_EMAID_ALREADY_REGISTER);
				emailIdExists.setStatusKey(ApiConstants.FAILURE);
				return emailIdExists;
			}

			AmxApiResponse<?, Object> validateDOTP = otpService.validateDOTP(eOtp, mOtp, customerProfileUpdateRequest.getEmail(), customerProfileUpdateRequest.getMobile());
			if (null != validateDOTP)
			{
				return validateDOTP;
			}
		}
		else if (null != customerProfileDetailModelCheck.getEmail() && !customerProfileDetailModelCheck.getEmail().equals(customerProfileUpdateRequest.getEmail()))
		{
			logger.info(TAG + " updateProfileDetails :: Email ");

			AmxApiResponse<Validate, Object> validateEmailID = customerRegistrationService.isValidEmailId(customerProfileUpdateRequest.getEmail());
			if (validateEmailID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return validateEmailID;
			}

			AmxApiResponse<Validate, Object> emailIdExists = customerRegistrationService.isEmailIdExist(customerProfileUpdateRequest.getEmail());
			if (emailIdExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				emailIdExists.setMessageKey(MessageKey.KEY_EMAID_ALREADY_REGISTER);
				emailIdExists.setStatusKey(ApiConstants.FAILURE);
				return emailIdExists;
			}

			AmxApiResponse<?, Object> validateEOTP = otpService.validateEOTP(eOtp, customerProfileDetailModelCheck.getEmail());
			if (null != validateEOTP)
			{
				return validateEOTP;
			}

		}
		else if (null != customerProfileDetailModelCheck.getMobile() && !customerProfileDetailModelCheck.getMobile().equals(customerProfileUpdateRequest.getMobile()))
		{
			logger.info(TAG + " updateProfileDetails :: Mobile Chnaged");

			AmxApiResponse<Validate, Object> isValidMobileNumber = customerRegistrationService.isValidMobileNumber(customerProfileUpdateRequest.getMobile());
			if (isValidMobileNumber.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return isValidMobileNumber;
			}

			AmxApiResponse<Validate, Object> mobileNumberExists = customerRegistrationService.isMobileNumberExist(customerProfileUpdateRequest.getMobile());
			if (mobileNumberExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				mobileNumberExists.setMessageKey(MessageKey.KEY_MOBILE_NO_ALREADY_REGISTER);
				mobileNumberExists.setStatusKey(ApiConstants.FAILURE);
				return mobileNumberExists;
			}

			AmxApiResponse<?, Object> validateMOTP = otpService.validateMOTP(mOtp, customerProfileDetailModelCheck.getMobile());
			if (null != validateMOTP)
			{
				return validateMOTP;
			}
		}

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

	public boolean checkExpiryDate(String idExpiryDate)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

			java.util.Date todays = sdf.parse(DateFormats.formatType1(new java.util.Date().toString()));
			java.util.Date idExpDateFormatted = sdf.parse(DateFormats.formatType2(idExpiryDate));

			if (idExpDateFormatted.before(todays))
			{
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
}
