
package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amx.jax.WebConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.DetailsConstants;
import com.amx.jax.constants.DatabaseErrorKey;
import com.amx.jax.constants.Message;
import com.amx.jax.constants.MessageKey;
import com.amx.jax.dao.CustomerRegistrationDao;
import com.amx.jax.models.ChangePasswordOtpRequest;
import com.amx.jax.models.ChangePasswordRequest;
import com.amx.jax.models.ChangePasswordResponse;
import com.amx.jax.models.CompanySetUp;
import com.amx.jax.models.CustomerDetailModel;
import com.amx.jax.models.CustomerDetailResponse;
import com.amx.jax.models.CustomerLoginModel;
import com.amx.jax.models.CustomerLoginRequest;
import com.amx.jax.models.CustomerLoginResponse;
import com.amx.jax.models.CustomerRegistrationModel;
import com.amx.jax.models.CustomerRegistrationRequest;
import com.amx.jax.models.CustomerRegistrationResponse;
import com.amx.jax.models.FailureException;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.RegSession;
import com.amx.jax.models.Validate;
import com.amx.utils.Random;
import com.insurance.model.Email;
import com.insurance.model.RequestOtpModel;
import com.insurance.model.ResponseOtpModel;
import com.insurance.services.EmailService;
import com.insurance.services.SMService;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CustomerRegistrationService
{
	String TAG = "com.amx.jax.services :: CustomerRegistrationService :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationService.class);

	@Autowired
	CustomerRegistrationDao customerRegistrationDao;

	@Autowired
	EmailService emailNotification;

	@Autowired
	RegSession regSession;

	@Autowired
	MetaData metaData;

	@Autowired
	private WebConfig webConfig;

	@Autowired
	private SMService smservice;

	public AmxApiResponse<Validate, Object> getCompanySetUp(BigDecimal languageId, String deviceId)
	{
		AmxApiResponse<Validate, Object> resp = new AmxApiResponse<Validate, Object>();

		try
		{
			ArrayList<CompanySetUp> getCompanySetUp = customerRegistrationDao.getCompanySetUp(languageId);

			regSession.setCountryId(getCompanySetUp.get(0).getCntryCd());
			regSession.setCompCd(getCompanySetUp.get(0).getCompCd());
			regSession.setContactUsEmail(getCompanySetUp.get(0).getEmail());
			regSession.setContactUsHelpLineNumber(getCompanySetUp.get(0).getHelpLineNumber());
			regSession.setUserType("D");
			regSession.setDeviceId("12345678");
			regSession.setDeviceType("ONLINE");
			regSession.setEmailFromConfigured(webConfig.getConfigEmail());

			logger.info(TAG + " getCompanySetUp :: getCountryId                :" + regSession.getCountryId());
			logger.info(TAG + " getCompanySetUp :: getCompCd                   :" + regSession.getCompCd());
			logger.info(TAG + " getCompanySetUp :: getUserType                 :" + regSession.getUserType());
			logger.info(TAG + " getCompanySetUp :: getDeviceId                 :" + regSession.getDeviceId());
			logger.info(TAG + " getCompanySetUp :: getDeviceType               :" + regSession.getDeviceType());
			logger.info(TAG + " getCompanySetUp :: setContactUsEmail           :" + regSession.getContactUsEmail());
			logger.info(TAG + " getCompanySetUp :: setContactUsHelpLineNumber  :" + regSession.getContactUsHelpLineNumber());

			metaData.setCountryId(regSession.getCountryId());
			metaData.setCompCd(regSession.getCompCd());
			metaData.setUserType(regSession.getUserType());
			metaData.setLanguageId(regSession.getLanguageId());
			metaData.setDeviceType(regSession.getDeviceType());
			metaData.setDeviceId(regSession.getDeviceId());
			metaData.setContactUsEmail(regSession.getContactUsEmail());
			metaData.setContactUsHelpLineNumber(regSession.getContactUsHelpLineNumber());
			metaData.setEmailFromConfigured(webConfig.getConfigEmail());

			resp.setData(null);
			resp.setStatus(ApiConstants.SUCCESS);
		}
		catch (Exception e)
		{
			resp.setException(e.toString());
			e.printStackTrace();
		}
		return resp;
	}

	public static boolean validate(String emailStr)
	{
		Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}

	public AmxApiResponse<Validate, Object> isValidCivilId(String civilid)
	{

		boolean isValidCivilId = customerRegistrationDao.isValidCivilId(civilid);

		AmxApiResponse<Validate, Object> resp = new AmxApiResponse<Validate, Object>();

		if (isValidCivilId)
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setMessage(Message.CIVILID_VALID);
			resp.setMessageKey(MessageKey.KEY_CIVIL_ID_VALID);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
			resp.setMessage(Message.CIVILID_INVALID);
			resp.setMessageKey(MessageKey.KEY_CIVIL_ID_INVALID);
		}

		return resp;

	}

	public AmxApiResponse<Validate, Object> isCivilIdExist(String civilid)
	{
		boolean civilIdExistCheck = customerRegistrationDao.isCivilIdExist(civilid);

		logger.info(TAG + " isCivilIdExist :: civilIdExistCheck :" + civilIdExistCheck);

		AmxApiResponse<Validate, Object> resp = new AmxApiResponse<Validate, Object>();

		if (civilIdExistCheck)
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setMessage(Message.CIVILID_ALREDAY_REGISTER);
			resp.setMessageKey(MessageKey.KEY_CIVIL_ID_ALREADY_REGISTER);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
			resp.setMessage(Message.CIVILID_ALREDAY_NOT_REGISTER);
			resp.setMessageKey(MessageKey.KEY_CIVIL_ID_NOT_REGISTERED);// Commit
		}

		return resp;

	}

	public AmxApiResponse<Validate, Object> isValidMobileNumber(String mobileNumber)
	{
		Validate isValidMobileNumber = customerRegistrationDao.isValidMobileNumber(mobileNumber);

		AmxApiResponse<Validate, Object> validMobileNumber = new AmxApiResponse<Validate, Object>();

		if (isValidMobileNumber.isValid())
		{
			validMobileNumber.setStatusKey(ApiConstants.SUCCESS);
		}
		else
		{
			validMobileNumber.setStatusKey(ApiConstants.FAILURE);
		}
		validMobileNumber.setMessage(isValidMobileNumber.getErrorMessage());
		validMobileNumber.setError(isValidMobileNumber.getErrorCode());
		validMobileNumber.setMessageKey(isValidMobileNumber.getErrorCode());

		return validMobileNumber;
	}

	public AmxApiResponse<Validate, Object> isMobileNumberExist(String mobileNumber)
	{
		boolean mobileNumberExists = customerRegistrationDao.isMobileNumberExist(mobileNumber);

		AmxApiResponse<Validate, Object> validMobileNumber = new AmxApiResponse<Validate, Object>();

		if (mobileNumberExists)
		{
			validMobileNumber.setStatusKey(ApiConstants.SUCCESS);
			validMobileNumber.setMessage(Message.MOBILE_NO_ALREDAY_REGISTER);
			validMobileNumber.setMessageKey(MessageKey.KEY_MOBILE_NO_ALREADY_REGISTER);
		}
		else
		{
			validMobileNumber.setStatusKey(ApiConstants.FAILURE);
			validMobileNumber.setMessage(Message.MOBILE_NO_NOT_REGISTER);
			validMobileNumber.setMessageKey(MessageKey.KEY_MOBILE_NO_NOT_REGISTER);

			Validate validate = new Validate();
			validate.setContactUsHelpLineNumber(regSession.getContactUsHelpLineNumber());
			validate.setContactUsEmail(regSession.getContactUsEmail());
		}

		return validMobileNumber;
	}

	public AmxApiResponse<Validate, Object> isValidEmailId(String emailId)
	{
		AmxApiResponse<Validate, Object> resp = new AmxApiResponse<Validate, Object>();

		Validate validate = new Validate();

		if (validate(emailId))
		{
			validate.setValid(true);
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setMessage(Message.EMAIL_VALID);
			resp.setMessageKey(MessageKey.KEY_EMAID_VALID);

		}
		else
		{
			validate.setValid(false);
			resp.setStatusKey(ApiConstants.FAILURE);
			resp.setMessage(Message.EMAIL_INVALID);
			resp.setMessageKey(MessageKey.KEY_EMAID_INVALID);
		}

		resp.setData(validate);
		return resp;
	}

	public AmxApiResponse<Validate, Object> isEmailIdExist(String emailId)
	{
		boolean emailIdExistCheck = customerRegistrationDao.isEmailIdExist(emailId);

		logger.info(TAG + " isCivilIdExist :: civilIdExistCheck :" + emailIdExistCheck);

		AmxApiResponse<Validate, Object> resp = new AmxApiResponse<Validate, Object>();

		if (emailIdExistCheck)
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setMessage(Message.EMAIL_ALREDAY_REGISTER);
			resp.setMessageKey(MessageKey.KEY_EMAID_ALREADY_REGISTER);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
			resp.setMessage(Message.EMAIL_ALREDAY_NOT_REGISTER);
			resp.setMessageKey(MessageKey.KEY_EMAID_NOT_REGISTER);

			Validate validate = new Validate();
			validate.setContactUsHelpLineNumber(regSession.getContactUsHelpLineNumber());
			validate.setContactUsEmail(regSession.getContactUsEmail());

		}
		return resp;
	}

	public AmxApiResponse<Validate, Object> isOtpEnabled(String civilId)
	{
		if (regSession.getCivilId() == null)
		{
			regSession.setCivilId(civilId);
		}

		if (regSession.getOtpCount() < 1 || !regSession.getCivilId().equals(civilId))
		{
			regSession.setOtpCount(1);
		}

		AmxApiResponse<Validate, Object> resp = new AmxApiResponse<Validate, Object>();

		AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(civilId);

		if (civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			boolean isOtpEnable = customerRegistrationDao.isOtpEnabled(civilId);

			if (isOtpEnable)
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
				resp.setMessage(Message.CUST_OTP_ENABLED);
				resp.setMessageKey(MessageKey.KEY_USER_OTP_ENABLED);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessage(Message.CUST_OTP_NOT_ENABLED);
				resp.setMessageKey(MessageKey.KEY_USER_OTP_NOT_ENABLED);

				Validate validate = new Validate();
				validate.setContactUsHelpLineNumber(regSession.getContactUsHelpLineNumber());
				validate.setContactUsEmail(regSession.getContactUsEmail());
				resp.setData(validate);
			}
		}
		else
		{
			logger.info(TAG + " otpSessionHandling :: regSession.getOtpCount() :" + regSession.getOtpCount());

			if (regSession.getOtpCount() > 3)
			{
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessage(Message.CUST_OTP_NOT_ENABLED);
				resp.setMessageKey(MessageKey.KEY_USER_OTP_NOT_ENABLED);

				Validate validate = new Validate();
				validate.setContactUsHelpLineNumber(regSession.getContactUsHelpLineNumber());
				validate.setContactUsEmail(regSession.getContactUsEmail());
				resp.setData(validate);
			}
			else
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
				resp.setMessage(Message.CUST_OTP_ENABLED);
				resp.setMessageKey(MessageKey.KEY_USER_OTP_ENABLED);
			}
			int getOtpCount = regSession.getOtpCount();
			getOtpCount++;
			regSession.setOtpCount(getOtpCount);

		}

		return resp;
	}

	public AmxApiResponse<Validate, Object> setOtpCount(String civilId)
	{
		AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(civilId);

		AmxApiResponse<Validate, Object> resp = new AmxApiResponse<Validate, Object>();

		if (civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			Validate setOtpCount = customerRegistrationDao.setOtpCount(civilId);

			if (setOtpCount.isValid())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessage(setOtpCount.getErrorMessage());
				resp.setMessageKey(setOtpCount.getErrorCode());

				logger.info(TAG + " setOtpCount :: setOtpCount.getErrorMessage() :" + setOtpCount.getErrorMessage());
				logger.info(TAG + " setOtpCount :: setOtpCount.getErrorCode() :" + setOtpCount.getErrorCode());
			}
		}
		else
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
		}
		return resp;
	}

	public AmxApiResponse<CustomerDetailResponse, Object> getUserDetails()
	{
		AmxApiResponse<CustomerDetailResponse, Object> resp = new AmxApiResponse<CustomerDetailResponse, Object>();
		CustomerDetailResponse customerDetailResponse = new CustomerDetailResponse();

		CustomerDetailModel customerDetailModel = customerRegistrationDao.getUserDetails(metaData.getCivilId());
		customerDetailResponse.setCivilId(metaData.getCivilId());
		customerDetailResponse.setCustSeqNumber(customerDetailModel.getCustSequenceNumber());
		customerDetailResponse.setDeviceId(customerDetailModel.getDeviceId());
		customerDetailResponse.setDeviceType(customerDetailModel.getDeviceType());
		customerDetailResponse.setEmail(customerDetailModel.getEmail());
		customerDetailResponse.setLanguageId(customerDetailModel.getLanguageId());
		customerDetailResponse.setLastLogin(customerDetailModel.getLastLogin());
		customerDetailResponse.setMailVerify(customerDetailModel.getMailVerify());
		customerDetailResponse.setMobile(customerDetailModel.getMobile());
		customerDetailResponse.setMobileVerify(customerDetailModel.getMobileVerify());
		customerDetailResponse.setUserName(customerDetailModel.getUserName());

		logger.info(TAG + " getUserDetails :: customerDetailModel :" + customerDetailModel.getCustSequenceNumber());

		metaData.setCustomerSequenceNumber(customerDetailModel.getCustSequenceNumber());

		resp.setData(customerDetailResponse);

		if (customerDetailModel.getStatus())
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<Validate, Object> isCivilIdExistCheck(String civilid)
	{
		AmxApiResponse<Validate, Object> validateCivilID = isValidCivilId(civilid);

		if (validateCivilID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return validateCivilID;
		}
		return isCivilIdExist(civilid);
	}

	public AmxApiResponse<Validate, Object> isMobileNumberExistCheck(String mobilenumber)
	{
		AmxApiResponse<Validate, Object> isValidMobileNumber = isValidMobileNumber(mobilenumber);

		if (isValidMobileNumber.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return isValidMobileNumber;
		}
		return isMobileNumberExist(mobilenumber);
	}

	public AmxApiResponse<Validate, Object> isEmailIdExistCheck(String emailId)
	{
		AmxApiResponse<Validate, Object> isValidEmailId = isValidEmailId(emailId);

		if (isValidEmailId.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return isValidEmailId;
		}
		return isEmailIdExist(emailId);
	}

	public AmxApiResponse<?, Object> registrationOtpInitiate(RequestOtpModel requestOtpModel)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();

		try
		{
			if (null == requestOtpModel.getCivilId() || requestOtpModel.getCivilId().toString().equals(""))
			{
				requestOtpModel.setCivilId(metaData.getCivilId());
			}

			AmxApiResponse<Validate, Object> validateCivilID = isValidCivilId(requestOtpModel.getCivilId());
			AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(requestOtpModel.getCivilId());
			AmxApiResponse<Validate, Object> isValidMobileNumber = isValidMobileNumber(requestOtpModel.getMobileNumber());
			AmxApiResponse<Validate, Object> mobileNumberExists = isMobileNumberExist(requestOtpModel.getMobileNumber());
			AmxApiResponse<Validate, Object> validateEmailID = isValidEmailId(requestOtpModel.getEmailId());
			AmxApiResponse<Validate, Object> emailIdExists = isEmailIdExist(requestOtpModel.getEmailId());
			AmxApiResponse<Validate, Object> isOtpEnabled = isOtpEnabled(requestOtpModel.getCivilId());

			if (validateCivilID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return validateCivilID;
			}

			if (isValidMobileNumber.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return isValidMobileNumber;
			}

			if (validateEmailID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return validateEmailID;
			}

			if (civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				civilIdExistCheck.setStatusKey(ApiConstants.FAILURE);
				return civilIdExistCheck;
			}

			if (mobileNumberExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS) && emailIdExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				sendFailedRegistration(DetailsConstants.REG_INCOMPLETE_TYPE_MOBE_MAIL, requestOtpModel, mobileNumberExists.getMessage());
				mobileNumberExists.setMessageKey(MessageKey.KEY_MOBILE_OR_EMAIL_ALREADY_EXISTS);
				mobileNumberExists.setStatusKey(ApiConstants.FAILURE);
				return mobileNumberExists;
			}
			else if (mobileNumberExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				sendFailedRegistration(DetailsConstants.REG_INCOMPLETE_TYPE_DUPLICATE_MOBILE, requestOtpModel, mobileNumberExists.getMessage());
				mobileNumberExists.setMessageKey(MessageKey.KEY_MOBILE_NO_ALREADY_REGISTER);
				mobileNumberExists.setStatusKey(ApiConstants.FAILURE);
				return mobileNumberExists;
			}
			else if (emailIdExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				sendFailedRegistration(DetailsConstants.REG_INCOMPLETE_TYPE_DUPLICATE_EMAIL, requestOtpModel, emailIdExists.getMessage());
				emailIdExists.setMessageKey(MessageKey.KEY_EMAID_ALREADY_REGISTER);
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

			regSession.setCivilId(requestOtpModel.getCivilId());
			regSession.setEmailId(requestOtpModel.getEmailId());
			regSession.setMobileNumber(requestOtpModel.getMobileNumber());

			AmxApiResponse<Validate, Object> setOtpCount = setOtpCount(requestOtpModel.getCivilId());

			//responseOtpModel = sendEmailOtpTemp(requestOtpModel.getEmailId());

			String eOtpPrefix = sendEmailOtp(requestOtpModel.getEmailId(), "");
			String mOtpPrefix = sendMobileOtp(requestOtpModel.getMobileNumber(), "");
			responseOtpModel.setEotpPrefix(eOtpPrefix);
			responseOtpModel.setMotpPrefix(mOtpPrefix);

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

	public AmxApiResponse<?, Object> registrationOtp(String mOtp, String eOtp, RequestOtpModel requestOtpModel)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();

		try
		{
			AmxApiResponse<?, Object> validateForRegistration = validateForRegistration(requestOtpModel);
			if (null != validateForRegistration)
			{
				return validateForRegistration;
			}

			regSession.setCivilId(requestOtpModel.getCivilId());
			regSession.setEmailId(requestOtpModel.getEmailId());
			regSession.setMobileNumber(requestOtpModel.getMobileNumber());
			metaData.setCivilId(requestOtpModel.getCivilId());
			metaData.setEmailId(requestOtpModel.getEmailId());
			metaData.setMobileNumber(requestOtpModel.getMobileNumber());

			if (null != mOtp && !mOtp.equals("") && null != eOtp && !eOtp.equals(""))
			{
				if (!metaData.getmOtpMobileNumber().equals(requestOtpModel.getMobileNumber()) || !metaData.geteOtpEmailId().equals(requestOtpModel.getEmailId()))
				{
					AmxApiResponse<?, Object> initiateUserOtp = initiateMobileEmailOtp(requestOtpModel.getEmailId(),requestOtpModel.getMobileNumber());
					if (null != initiateUserOtp)
					{
						return initiateUserOtp;
					}
				}

				if (!metaData.getMotp().equals(mOtp) || !metaData.getEotp().equals(eOtp))
				{
					resp.setError(Message.REG_INVALID_OTP);
					resp.setStatusKey(MessageKey.KEY_EMAIL_MOBILE_OTP_REQUIRED);
					resp.setMessageKey(MessageKey.KEY_EMAIL_MOBILE_OTP_REQUIRED_INVALID);
					return resp;
				}
				else
				{
					resp.setStatusKey(ApiConstants.SUCCESS);
					return resp;
				}
			}
			else
			{
				AmxApiResponse<?, Object> initiateUserOtp = initiateMobileEmailOtp(requestOtpModel.getEmailId(),requestOtpModel.getMobileNumber());
				if (null != initiateUserOtp)
				{
					return initiateUserOtp;
				}

				AmxApiResponse<Validate, Object> setOtpCount = setOtpCount(requestOtpModel.getCivilId());
				if (setOtpCount.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
				{
					return setOtpCount;
				}
				return resp;
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

	public AmxApiResponse<Validate, Object> validateOtp(String mOtp, String eOtp)
	{
		AmxApiResponse<Validate, Object> resp = new AmxApiResponse<Validate, Object>();
		Validate validate = new Validate();

		if (regSession.getMotp().equals(mOtp) && regSession.getEotp().equals(eOtp))
		{
			validate.setValid(true);
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setMessage(Message.REG_VALID_OTP);
		}
		else
		{
			validate.setValid(false);
			resp.setStatusKey(ApiConstants.FAILURE);
			resp.setMessage(Message.REG_INVALID_OTP);
			resp.setMessageKey(MessageKey.KEY_REG_VALIDATE_OTP);
		}
		resp.setData(validate);

		return resp;
	}

	public AmxApiResponse<CustomerRegistrationResponse, Object> addNewCustomer(CustomerRegistrationRequest userRegistartionRequest)
	{
		AmxApiResponse<CustomerRegistrationResponse, Object> resp = new AmxApiResponse<CustomerRegistrationResponse, Object>();
		CustomerRegistrationResponse customerRegistrationResponse = new CustomerRegistrationResponse();
		CustomerRegistrationModel customerRegistrationModel = new CustomerRegistrationModel();

		customerRegistrationModel.setCountryId(regSession.getCountryId());
		customerRegistrationModel.setCompCd(regSession.getCompCd());
		customerRegistrationModel.setUserType(regSession.getUserType());
		customerRegistrationModel.setMobile(regSession.getMobileNumber());
		customerRegistrationModel.setEmail(regSession.getEmailId());
		customerRegistrationModel.setLanguageId(regSession.getLanguageId());
		customerRegistrationModel.setCivilId(userRegistartionRequest.getCivilId());
		customerRegistrationModel.setPassword(userRegistartionRequest.getPassword());
		customerRegistrationModel.setCreatedDeviceId(userRegistartionRequest.getCreatedDeviceId());
		customerRegistrationModel.setDeviceType(userRegistartionRequest.getDeviceType());

		customerRegistrationModel = customerRegistrationDao.addNewCustomer(customerRegistrationModel);

		if (customerRegistrationModel.getStatus())
		{
			resp.setStatusKey(ApiConstants.SUCCESS);

			String emailIdFrom = webConfig.getConfigEmail();
			String emailITo = regSession.getEmailId();
			String Subject = "Al Mulla Insurance Registartion Confirmation";
			String mailData = "Al Mulla Insurance Registration Completed Successfully.";

			emailNotification.sendEmail(emailIdFrom, emailITo, Subject, mailData);
			
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
		}

		resp.setMessage(customerRegistrationModel.getErrorMessage());
		resp.setMessageKey(customerRegistrationModel.getErrorCode());

		customerRegistrationResponse.setCivilid(customerRegistrationModel.getCivilid());
		customerRegistrationResponse.setUserSequenceNumber(customerRegistrationModel.getUserSequenceNumber());

		resp.setData(customerRegistrationResponse);

		return resp;

	}

	public AmxApiResponse<?, Object> validateUserLogin(CustomerLoginRequest customerLoginRequest)
	{
		CustomerLoginResponse customerLoginResponse = new CustomerLoginResponse();
		CustomerLoginModel customerLoginModel = new CustomerLoginModel();
		AmxApiResponse<CustomerLoginResponse, Object> resp = new AmxApiResponse<CustomerLoginResponse, Object>();
		AmxApiResponse<Validate, Object> validateCivilID = isValidCivilId(customerLoginRequest.getCivilId());
		AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(customerLoginRequest.getCivilId());

		if (validateCivilID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return validateCivilID;
		}
		else if (civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return civilIdExistCheck;
		}
		else
		{

			customerLoginModel.setCivilId(customerLoginRequest.getCivilId());
			customerLoginModel.setPassword(customerLoginRequest.getPassword());

			customerLoginModel = customerRegistrationDao.validateUserLogin(customerLoginModel);

			if (customerLoginModel.getStatus())
			{
				onSuccessLogin(customerLoginRequest, customerLoginModel);
				getUserDetails();

				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}

			logger.info(TAG + " validateUserLogin :: getErrorMessage :" + customerLoginModel.getErrorMessage());
			logger.info(TAG + " validateUserLogin :: getErrorCode :" + customerLoginModel.getErrorCode());

			if (null != customerLoginModel.getErrorMessage() && customerLoginModel.getErrorCode().toString().equalsIgnoreCase(DatabaseErrorKey.INVALID_USER_LOGIN))
			{
				logger.info(TAG + " validateUserLogin :: Invalid Attempts :");

				String countData = "";
				int count = Integer.parseInt(customerLoginModel.getErrorMessage());
				int userInvalidCountRemaining = 3 - count;
				if (userInvalidCountRemaining == 0)
				{
					countData = " No ";
				}
				else
				{
					countData = "" + userInvalidCountRemaining;
				}
				resp.setMessageKey(customerLoginModel.getErrorCode());
				resp.setMessage(countData);
			}
			else if (null != customerLoginModel.getErrorCode() && customerLoginModel.getErrorCode().equalsIgnoreCase(DatabaseErrorKey.USER_ACCOUNT_LOCK))
			{
				logger.info(TAG + " validateUserLogin :: Lock Account :");
				resp.setMessageKey(customerLoginModel.getErrorCode());
				resp.setMessage(customerLoginModel.getErrorCode());
				customerLoginResponse.setContactUsHelpLineNumber(regSession.getContactUsHelpLineNumber());
				customerLoginResponse.setContactUsEmail(regSession.getContactUsEmail());
			}
			else
			{
				resp.setMessageKey(customerLoginModel.getErrorCode());
				resp.setMessage(customerLoginModel.getErrorCode());
			}

			customerLoginResponse.setAmibRef(customerLoginModel.getAmibRef());
			customerLoginResponse.setUserSeqNum(customerLoginModel.getUserSeqNum());
			resp.setData(customerLoginResponse);
			logger.info(TAG + " validateUserLogin :: customerLoginResponse :" + customerLoginResponse.toString());
		}
		return resp;
	}

	public void onSuccessLogin(CustomerLoginRequest customerLoginRequest, CustomerLoginModel customerLoginModel)
	{
		metaData.setCivilId(customerLoginRequest.getCivilId());
		metaData.setUserSequenceNumber(customerLoginModel.getUserSeqNum());
		metaData.setUserAmibCustRef(customerLoginModel.getAmibRef());
		metaData.setCountryId(regSession.getCountryId());
		metaData.setCompCd(regSession.getCompCd());
		metaData.setUserType(regSession.getUserType());
		metaData.setLanguageId(regSession.getLanguageId());
		metaData.setDeviceType(regSession.getDeviceType());
		metaData.setContactUsEmail(regSession.getContactUsEmail());
		metaData.setContactUsHelpLineNumber(regSession.getContactUsHelpLineNumber());

	}

	public AmxApiResponse<?, Object> changePasswordOtpInitiate(ChangePasswordOtpRequest changePasswordOtpRequest)
	{
		logger.info(TAG + " changePasswordOtpInitiate :: getCivilId :" + changePasswordOtpRequest.getCivilId());

		if (null == changePasswordOtpRequest.getCivilId() || changePasswordOtpRequest.getCivilId().toString().equals(""))
		{
			changePasswordOtpRequest.setCivilId(metaData.getCivilId());
		}

		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();
		regSession.setCivilId(changePasswordOtpRequest.getCivilId());

		AmxApiResponse<Validate, Object> validateCivilID = isValidCivilId(changePasswordOtpRequest.getCivilId());
		if (validateCivilID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return validateCivilID;
		}

		AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(changePasswordOtpRequest.getCivilId());
		logger.info(TAG + " changePasswordOtpInitiate :: civilIdExistCheck :" + civilIdExistCheck.getStatusKey());
		if (civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return civilIdExistCheck;
		}

		AmxApiResponse<Validate, Object> isOtpEnabled = isOtpEnabled(changePasswordOtpRequest.getCivilId());
		logger.info(TAG + " changePasswordOtpInitiate :: isOtpEnabled :" + isOtpEnabled.getStatusKey());
		if (isOtpEnabled.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return isOtpEnabled;
		}

		CustomerDetailModel customerDetailModel = customerRegistrationDao.getUserDetails(changePasswordOtpRequest.getCivilId());

		if (null == customerDetailModel || customerDetailModel.getErrorCode() != null)
		{
			resp.setMessageKey(customerDetailModel.getErrorCode());
			resp.setMessage(customerDetailModel.getErrorMessage());
			resp.setStatus(ApiConstants.FAILURE);
			return resp;
		}
		else
		{
			try
			{
				AmxApiResponse<Validate, Object> setOtpCount = setOtpCount(changePasswordOtpRequest.getCivilId());

				//responseOtpModel = sendEmailOtpTemp(customerDetailModel.getEmail());

				String eOtpPrefix = sendEmailOtp(customerDetailModel.getEmail(), "");
				String mOtpPrefix = sendMobileOtp(customerDetailModel.getMobile(), "");
				responseOtpModel.setEotpPrefix(eOtpPrefix);
				responseOtpModel.setMotpPrefix(mOtpPrefix);
				
				if (setOtpCount.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
				{
					return setOtpCount;
				}

				resp.setData(responseOtpModel);
				resp.setStatus(ApiConstants.SUCCESS);

			}
			catch (Exception e)
			{
				e.printStackTrace();
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessage(Message.CP_EMAIL_NOT_SENT);
				resp.setMessageKey(MessageKey.KEY_CP_OTP_NOT_GENERATED);
			}

		}

		return resp;
	}

	public AmxApiResponse<ChangePasswordResponse, Object> updatePassword(ChangePasswordRequest changePasswordRequest)
	{
		AmxApiResponse<ChangePasswordResponse, Object> resp = new AmxApiResponse<ChangePasswordResponse, Object>();

		CustomerDetailModel customerDetailModel = new CustomerDetailModel();
		customerDetailModel.setPassword(changePasswordRequest.getNewPassword());

		customerDetailModel = customerRegistrationDao.updatePassword(customerDetailModel);

		if (customerDetailModel.getStatus())
		{
			resp.setMessageKey(customerDetailModel.getErrorCode());
			resp.setError(customerDetailModel.getErrorMessage());
			resp.setStatus(ApiConstants.SUCCESS);
			resp.setData(null);
		}
		else
		{
			resp.setMessageKey(customerDetailModel.getErrorCode());
			resp.setError(customerDetailModel.getErrorMessage());
			resp.setStatus(ApiConstants.FAILURE);
			resp.setData(null);
		}

		return resp;
	}

	public void sendFailedRegistration(String type, RequestOtpModel requestOtpModel, String exceptionMessage)
	{
		String emailIdFrom = webConfig.getConfigEmail();
		String emailITo = requestOtpModel.getEmailId();
		String Subject = "User Registration Failure";
		String mailData = "";
		StringBuffer sb = new StringBuffer();

		if (type.equalsIgnoreCase(DetailsConstants.REG_INCOMPLETE_TYPE_MOBE_MAIL))
		{
			sb.append("\n");
			sb.append("\n USER REGISTRTATION FAILED INFO :");
			sb.append("\n");
			sb.append("\n CIVIL ID   ::  " + requestOtpModel.getCivilId());
			sb.append("\n");
			sb.append("\n EMAIL ID  ::  " + requestOtpModel.getEmailId());
			sb.append("\n");
			sb.append("\n MOBILE    ::  " + requestOtpModel.getMobileNumber());
			sb.append("\n");
			sb.append("\n ISSUE       ::  Duplicate Email Id and Mobile Number");
			sb.append("\n");
			sb.append("\n");
			mailData = sb.toString();
		}
		else if (type.equalsIgnoreCase(DetailsConstants.REG_INCOMPLETE_TYPE_DUPLICATE_EMAIL))
		{
			sb.append("\n");
			sb.append("\n USER REGISTRTATION FAILED INFO :");
			sb.append("\n");
			sb.append("\n CIVIL ID    ::  " + requestOtpModel.getCivilId());
			sb.append("\n");
			sb.append("\n EMAIL ID  ::  " + requestOtpModel.getEmailId());
			sb.append("\n");
			sb.append("\n MOBILE    ::  " + requestOtpModel.getMobileNumber());
			sb.append("\n");
			sb.append("\n ISSUE       ::  Duplicate Email Id");
			sb.append("\n");
			sb.append("\n");
			mailData = sb.toString();
		}
		else if (type.equalsIgnoreCase(DetailsConstants.REG_INCOMPLETE_TYPE_DUPLICATE_MOBILE))
		{
			sb.append("\n");
			sb.append("\n USER REGISTRTATION FAILED INFO :");
			sb.append("\n");
			sb.append("\n CIVIL ID   ::  " + requestOtpModel.getCivilId());
			sb.append("\n");
			sb.append("\n EMAIL ID  ::  " + requestOtpModel.getEmailId());
			sb.append("\n");
			sb.append("\n MOBILE    ::  " + requestOtpModel.getMobileNumber());
			sb.append("\n");
			sb.append("\n ISSUE       ::  Duplicate Mobile Number");
			sb.append("\n");
			sb.append("\n");
			mailData = sb.toString();
		}

		FailureException failureException = new FailureException();
		failureException.setCivilId(requestOtpModel.getCivilId());
		failureException.setMobileNumber(requestOtpModel.getMobileNumber());
		failureException.setEmailId(requestOtpModel.getEmailId());
		failureException.setCountryId(regSession.getCountryId());
		failureException.setCompCd(regSession.getCompCd());
		failureException.setDeviceId(regSession.getDeviceId());
		failureException.setDeviceType(regSession.getDeviceType());
		failureException.setLanguageId(regSession.getLanguageId());
		failureException.setUserType(regSession.getUserType());
		failureException.setExceptionType("REGISTER");
		failureException.setExceptionMsg(exceptionMessage);

		emailNotification.sendEmail(emailIdFrom, emailITo, Subject, mailData);
		customerRegistrationDao.setFailedException(type, failureException);

	}

	public ResponseOtpModel sendEmailOtpTemp(String emailId)
	{
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();

		String emailOtpPrefix = Random.randomAlpha(3);
		String mobileOtpPrefix = Random.randomAlpha(3);

		String emailOtp = Random.randomNumeric(6);
		String mobileOtp = Random.randomNumeric(6);

		String emailOtpToSend = emailOtpPrefix + "-" + emailOtp;
		String mobileOtpToSend = mobileOtpPrefix + "-" + mobileOtp;

		responseOtpModel.setEotpPrefix(emailOtpPrefix);
		responseOtpModel.setMotpPrefix(mobileOtpPrefix);

		regSession.setMotpPrefix(mobileOtpPrefix);
		regSession.setEotpPrefix(emailOtpPrefix);
		regSession.setEotp(emailOtp);
		regSession.setMotp(mobileOtp);

		metaData.setMotpPrefix(mobileOtpPrefix);
		metaData.setEotpPrefix(emailOtpPrefix);
		metaData.setEotp(emailOtp);
		metaData.setMotp(mobileOtp);

		String emailIdFrom = webConfig.getConfigEmail();
		String emailITo = emailId;
		String Subject = "OTP Verification";
		String mailData = "Your Email OTP Generated From Al Mulla Insurance is : " + emailOtpToSend + "          And Mobile Otp is :" + mobileOtpToSend + "";

		emailNotification.sendEmail(emailIdFrom, emailITo, Subject, mailData);

		return responseOtpModel;
	}

	public String sendEmailOtp(String emailId, String data)
	{
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();

		String emailOtpPrefix = Random.randomAlpha(3);
		String emailOtp = Random.randomNumeric(6);
		String emailOtpToSend = emailOtpPrefix + "-" + emailOtp;

		responseOtpModel.setEotpPrefix(emailOtpPrefix);
		responseOtpModel.setMotpPrefix(null);

		regSession.setEotpPrefix(emailOtpPrefix);
		regSession.setEotp(emailOtp);

		metaData.setEotpPrefix(emailOtpPrefix);
		metaData.setEotp(emailOtp);

		String emailIdFrom = webConfig.getConfigEmail();
		String emailITo = emailId;
		String Subject = "OTP Verification";
		String mailData = "Your Email OTP Generated From Almulla Insurance is : " + emailOtpToSend;

		emailNotification.sendEmail(emailIdFrom, emailITo, Subject, mailData);

		return emailOtpPrefix;
	}

	public String sendMobileOtp(String mobileNumber, String data)
	{
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();

		String mobileWithCode = "965" + mobileNumber;

		String mobileOtpPrefix = Random.randomAlpha(3);
		String mobileOtp = Random.randomNumeric(6);
		String mobileOtpToSend = mobileOtpPrefix + "-" + mobileOtp;

		responseOtpModel.setEotpPrefix(null);
		responseOtpModel.setMotpPrefix(mobileOtpPrefix);

		regSession.setMotpPrefix(mobileOtpPrefix);
		regSession.setMotp(mobileOtp);

		metaData.setMotpPrefix(mobileOtpPrefix);
		metaData.setMotp(mobileOtp);
		String mailData = "Your Mobile OTP Generated From Al Mulla Insurance is : " + mobileOtpToSend;

		/*String emailIdFrom = webConfig.getConfigEmail();
		String emailITo = "abhishek.tiwari@mobicule.com";
		String Subject = "Almulla Insurance Otp";
		emailNotification.sendEmail(emailIdFrom, emailITo, Subject, mailData);*/

		smservice.sendMessage(mobileWithCode, mailData);

		return mobileOtpPrefix;
	}

	private AmxApiResponse<?, Object> initiateMobileEmailOtp(String emailId , String mobileNumber)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();
		String eOtpPrefix = sendEmailOtp(emailId, "");
		String mOtpPrefix = sendMobileOtp(mobileNumber, "");
		responseOtpModel.setEotpPrefix(eOtpPrefix);
		responseOtpModel.setMotpPrefix(mOtpPrefix);
		metaData.setmOtpMobileNumber(mobileNumber);
		metaData.seteOtpEmailId(emailId);
		resp.setMeta(responseOtpModel);
		resp.setStatusKey(MessageKey.KEY_EMAIL_MOBILE_OTP_REQUIRED);
		resp.setMessageKey(MessageKey.KEY_EMAIL_MOBILE_OTP_REQUIRED);
		return resp;
	}

	private AmxApiResponse<?, Object> validateForRegistration(RequestOtpModel requestOtpModel)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();

		AmxApiResponse<Validate, Object> validateCivilID = isValidCivilId(requestOtpModel.getCivilId());
		AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(requestOtpModel.getCivilId());
		AmxApiResponse<Validate, Object> isValidMobileNumber = isValidMobileNumber(requestOtpModel.getMobileNumber());
		AmxApiResponse<Validate, Object> mobileNumberExists = isMobileNumberExist(requestOtpModel.getMobileNumber());
		AmxApiResponse<Validate, Object> validateEmailID = isValidEmailId(requestOtpModel.getEmailId());
		AmxApiResponse<Validate, Object> emailIdExists = isEmailIdExist(requestOtpModel.getEmailId());
		AmxApiResponse<Validate, Object> isOtpEnabled = isOtpEnabled(requestOtpModel.getCivilId());

		try
		{
			if (null == requestOtpModel.getCivilId() || requestOtpModel.getCivilId().toString().equals(""))
			{
				requestOtpModel.setCivilId(metaData.getCivilId());
			}

			if (validateCivilID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return validateCivilID;
			}

			if (isValidMobileNumber.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return isValidMobileNumber;
			}

			if (validateEmailID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return validateEmailID;
			}

			if (civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				civilIdExistCheck.setStatusKey(ApiConstants.FAILURE);
				return civilIdExistCheck;
			}

			if (mobileNumberExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS) && emailIdExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				sendFailedRegistration(DetailsConstants.REG_INCOMPLETE_TYPE_MOBE_MAIL, requestOtpModel, mobileNumberExists.getMessage());
				mobileNumberExists.setMessageKey(MessageKey.KEY_MOBILE_OR_EMAIL_ALREADY_EXISTS);
				mobileNumberExists.setStatusKey(ApiConstants.FAILURE);
				return mobileNumberExists;
			}
			else if (mobileNumberExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				sendFailedRegistration(DetailsConstants.REG_INCOMPLETE_TYPE_DUPLICATE_MOBILE, requestOtpModel, mobileNumberExists.getMessage());
				mobileNumberExists.setMessageKey(MessageKey.KEY_MOBILE_NO_ALREADY_REGISTER);
				mobileNumberExists.setStatusKey(ApiConstants.FAILURE);
				return mobileNumberExists;
			}
			else if (emailIdExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				sendFailedRegistration(DetailsConstants.REG_INCOMPLETE_TYPE_DUPLICATE_EMAIL, requestOtpModel, emailIdExists.getMessage());
				emailIdExists.setMessageKey(MessageKey.KEY_EMAID_ALREADY_REGISTER);
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
		return null;
	}

}
