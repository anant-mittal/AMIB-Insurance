
package com.amx.jax.services;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.DetailsConstants;
import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.constants.Message;
import com.amx.jax.constants.MessageKey;
import com.amx.jax.dao.CustomerRegistrationDao;
import com.amx.jax.dao.PersonalDetailsDao;
import com.amx.jax.models.ArrayResponseModel;
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
	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationService.class);

	static String TAG = "PersonalDetailsService :: ";
	
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
		try 
		{
			customerProfileDetailModel = personalDetailsDao.getProfileDetails(userSession.getCivilId() , HardCodedValues.USER_TYPE , userSession.getCustomerSequenceNumber(), userSession.getLanguageId());
			if (customerProfileDetailModel.getErrorCode() != null)
			{
				resp.setStatusKey(customerProfileDetailModel.getErrorCode());
				resp.setMessageKey(customerProfileDetailModel.getErrorCode());
				resp.setMessage(customerProfileDetailModel.getErrorCode());
				return resp;
			}
			
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
			
			resp.setData(customerProfileDetailResponse);
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
		}
		catch (Exception e) 
		{
			resp.setMessageKey(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "getProfileDetails :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}

	public AmxApiResponse<?, Object> updateProfileDetails(String mOtp, String eOtp, CustomerProfileUpdateRequest customerProfileUpdateRequest)
	{
		AmxApiResponse<?, Object> resp = new AmxApiResponse<CustomerProfileUpdateResponse, Object>();
		CustomerProfileDetailModel customerProfileDetailModel = new CustomerProfileDetailModel();
		CustomerProfileDetailModel customerProfileDetailModelCheck = new CustomerProfileDetailModel();
		CustomerProfileUpdateResponse customerProfileUpdateResponse = new CustomerProfileUpdateResponse();
		
		try 
		{
			ArrayList<Object> mandatoryCheck = new ArrayList<>();
			mandatoryCheck.add(customerProfileUpdateRequest.getEnglishName());
			mandatoryCheck.add(customerProfileUpdateRequest.getGenderCode());
			mandatoryCheck.add(customerProfileUpdateRequest.getIdExpiryDate());
			mandatoryCheck.add(customerProfileUpdateRequest.getBusinessCode());
			mandatoryCheck.add(customerProfileUpdateRequest.getNatyCode());
			mandatoryCheck.add(customerProfileUpdateRequest.getGovCode());
			mandatoryCheck.add(customerProfileUpdateRequest.getAreaCode());
			mandatoryCheck.add(customerProfileUpdateRequest.getEmail());
			mandatoryCheck.add(customerProfileUpdateRequest.getMobile());
			
			if(!checkMandatory(mandatoryCheck))
			{
				resp.setStatusEnum(WebAppStatusCodes.MANDATORY_FIELDS_MISSING);
				resp.setMessageKey(WebAppStatusCodes.MANDATORY_FIELDS_MISSING.toString());
				resp.setMessage(Message.MANDATORY_FIELDS_MISSING);
				return resp;
			}
			
			if (null != customerProfileUpdateRequest.getIdExpiryDate())
			{
				String dateFromDb = customerProfileUpdateRequest.getIdExpiryDate();
				if (DateFormats.checkExpiryDate(dateFromDb))
				{
					resp.setStatusEnum(WebAppStatusCodes.CIVIL_ID_EXPIRED);
					resp.setMessageKey(WebAppStatusCodes.CIVIL_ID_EXPIRED.toString());
					return resp;
				}
			}
			
			customerProfileDetailModelCheck = personalDetailsDao.getProfileDetails(userSession.getCivilId() , HardCodedValues.USER_TYPE , userSession.getCustomerSequenceNumber(),userSession.getLanguageId());
			if (customerProfileDetailModel.getErrorCode() != null)
			{
				resp.setStatusKey(customerProfileDetailModel.getErrorCode());
				resp.setMessageKey(customerProfileDetailModel.getErrorCode());
				resp.setMessage(customerProfileDetailModel.getErrorCode());
				return resp;
			}
			
			
			if (null != customerProfileDetailModelCheck.getMobile() && null != customerProfileDetailModelCheck.getEmail() && !customerProfileDetailModelCheck.getMobile().equals(customerProfileUpdateRequest.getMobile())
					&& !customerProfileDetailModelCheck.getEmail().equals(customerProfileUpdateRequest.getEmail()))

			{
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
					mobileNumberExists.setStatusEnum(WebAppStatusCodes.MOBILE_NUMBER_REGISTERED);
					return mobileNumberExists;
				}

				AmxApiResponse<ResponseInfo, Object> emailIdExists = customerRegistrationService.isEmailIdExist(customerProfileUpdateRequest.getEmail());
				if (emailIdExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
				{
					emailIdExists.setMessageKey(WebAppStatusCodes.EMAIL_ID_REGESTERED.toString());
					emailIdExists.setStatusEnum(WebAppStatusCodes.EMAIL_ID_REGESTERED);
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
					emailIdExists.setStatusEnum(WebAppStatusCodes.EMAIL_ID_REGESTERED);
					return emailIdExists;
				}
				
				AmxApiResponse<?, Object> validateEOTP = emailSmsService.validateEotp(eOtp, customerProfileUpdateRequest.getEmail() , DetailsConstants.UPDATE_PROFILE_OTP);
				if (null != validateEOTP)
				{
					return validateEOTP;
				}

			}
			else if (null != customerProfileDetailModelCheck.getMobile() && !customerProfileDetailModelCheck.getMobile().equals(customerProfileUpdateRequest.getMobile()))
			{
				AmxApiResponse<ResponseInfo, Object> isValidMobileNumber = customerRegistrationService.isValidMobileNumber(customerProfileUpdateRequest.getMobile());
				if (!isValidMobileNumber.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
				{
					return isValidMobileNumber;
				}

				AmxApiResponse<ResponseInfo, Object> mobileNumberExists = customerRegistrationService.isMobileNumberExist(customerProfileUpdateRequest.getMobile());
				if (mobileNumberExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
				{
					mobileNumberExists.setMessageKey(WebAppStatusCodes.MOBILE_NUMBER_REGISTERED.toString());
					mobileNumberExists.setStatusEnum(WebAppStatusCodes.MOBILE_NUMBER_REGISTERED);
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
			
			customerProfileDetailModel = personalDetailsDao.updateProfileDetails(customerProfileDetailModel , userSession.getCivilId() , HardCodedValues.USER_TYPE , userSession.getCustomerSequenceNumber(), userSession.getLanguageId());
			if (customerProfileDetailModel.getErrorCode() != null)
			{
				resp.setStatusKey(customerProfileDetailModel.getErrorCode());
				resp.setMessageKey(customerProfileDetailModel.getErrorCode());
				resp.setMessage(customerProfileDetailModel.getErrorCode());
				return resp;
			}
			
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
		}
		catch (Exception e) 
		{
			resp.setMessageKey(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "updateProfileDetails :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getBusiness()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			ArrayResponseModel businessDataArray =  personalDetailsDao.getBusiness(userSession.getLanguageId()); 
			if(null != businessDataArray.getErrorCode())
			{
				resp.setMessageKey(businessDataArray.getErrorCode());
				resp.setMessage(businessDataArray.getErrorMessage());
				return resp;
			}
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			resp.setResults(businessDataArray.getDataArray());

		}
		catch (Exception e)
		{
			
			resp.setMessageKey(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG+"getBusiness :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getNationality()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			ArrayResponseModel nationalityDataArray =  personalDetailsDao.getNationality(userSession.getLanguageId()); 
			if(null != nationalityDataArray.getErrorCode())
			{
				resp.setMessageKey(nationalityDataArray.getErrorCode());
				resp.setMessage(nationalityDataArray.getErrorMessage());
				return resp;
			}
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			resp.setResults(nationalityDataArray.getDataArray());

		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG+"getNationality :: exception :" + e);
			e.printStackTrace();
		}
		return resp;

	}

	public AmxApiResponse<?, Object> getGovernorates()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			
			ArrayResponseModel governateDataArray =  personalDetailsDao.getGovernorates(userSession.getLanguageId()); 
			if(null != governateDataArray.getErrorCode())
			{
				resp.setMessageKey(governateDataArray.getErrorCode());
				resp.setMessage(governateDataArray.getErrorMessage());
				return resp;
			}
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			resp.setResults(governateDataArray.getDataArray());
			
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG+"getGovernorates :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getArea(String gov)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			ArrayResponseModel areaDataArray =  personalDetailsDao.getArea(gov, userSession.getLanguageId()); 
			if(null != areaDataArray.getErrorCode())
			{
				resp.setMessageKey(areaDataArray.getErrorCode());
				resp.setMessage(areaDataArray.getErrorMessage());
				return resp;
			}
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			resp.setResults(areaDataArray.getDataArray());
			
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG+"getArea :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getGender()
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			ArrayResponseModel genderDataArray =  personalDetailsDao.getGender(userSession.getLanguageId()); 
			if(null != genderDataArray.getErrorCode())
			{
				resp.setMessageKey(genderDataArray.getErrorCode());
				resp.setMessage(genderDataArray.getErrorMessage());
				return resp;
			}
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			resp.setResults(genderDataArray.getDataArray());
			
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG+"getGender :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
		
	}
	
	public boolean checkMandatory(ArrayList<Object> details)
	{
		boolean mandatoryFlag = true;
		try
		{
			for(int i = 0 ; i < details.size() ; i++)
			{
				if(details.get(i) == null || details.get(i).toString().equals(""))
				{
					mandatoryFlag = false;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return mandatoryFlag;
	}
}
