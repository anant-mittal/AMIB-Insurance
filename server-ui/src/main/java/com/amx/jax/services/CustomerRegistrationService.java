




package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amx.jax.WebConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.DetailsConstants;
import com.amx.jax.constants.ErrorKey;
import com.amx.jax.constants.Message;
import com.amx.jax.constants.MessageKey;
import com.amx.jax.dao.CustomerRegistrationDao;
import com.amx.jax.models.ChangePasswordOtpRequest;
import com.amx.jax.models.ChangePasswordOtpResponse;
import com.amx.jax.models.ChangePasswordRequest;
import com.amx.jax.models.ChangePasswordResponse;
import com.amx.jax.models.CompanySetUp;
import com.amx.jax.models.CompanySetupRequest;
import com.amx.jax.models.CustomerDetailModel;
import com.amx.jax.models.CustomerLoginModel;
import com.amx.jax.models.CustomerLoginRequest;
import com.amx.jax.models.CustomerLoginResponse;
import com.amx.jax.models.CustomerRegistrationModel;
import com.amx.jax.models.CustomerRegistrationRequest;
import com.amx.jax.models.CustomerRegistrationResponse;
import com.amx.jax.models.FailureException;
import com.amx.jax.models.Validate;
import com.amx.jax.session.RegSession;
import com.amx.jax.session.UserSession;
import com.amx.utils.Random;
import com.insurance.email.dao.EmailNotification;
import com.insurance.email.model.Email;
import com.insurance.generateotp.CreateOtpToken;
import com.insurance.generateotp.RequestOtpModel;
import com.insurance.generateotp.ResponseOtpModel;

@Service
public class CustomerRegistrationService
{
	String TAG = "com.amx.jax.services :: CustomerRegistrationService :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationService.class);

	@Autowired
	CustomerRegistrationDao customerRegistrationDao;

	@Autowired
	EmailNotification emailNotification;

	@Autowired
	CreateOtpToken createOtpToken;

	@Autowired
	RegSession regSession;

	@Autowired
	UserSession userSession;

	@Autowired
	private WebConfig webConfig;

	public AmxApiResponse<Validate, Object> getCompanySetUp(int languageId, String deviceId)
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
			regSession.setDeviceId(deviceId);
			regSession.setDeviceType("ONLINE");

			DetailsConstants.CNTRYCD = new BigDecimal(regSession.getCountryId());
			DetailsConstants.COMPCD = new BigDecimal(regSession.getCompCd());
			DetailsConstants.USER_TYPE = regSession.getUserType();
			DetailsConstants.CONTACT_US_EMAIL_ID = regSession.getContactUsEmail();
			DetailsConstants.CONTACT_US_HELPLINE_NO = regSession.getContactUsHelpLineNumber();
			DetailsConstants.DEVICE_ID = regSession.getDeviceId();
			DetailsConstants.DEVICE_TYPE = regSession.getDeviceType();
			DetailsConstants.LANGUAGE_ID = new BigDecimal(languageId);

			logger.info(TAG + " getCompanySetUp :: getCountryId                :" + regSession.getCountryId());
			logger.info(TAG + " getCompanySetUp :: getCompCd                   :" + regSession.getCompCd());
			logger.info(TAG + " getCompanySetUp :: getUserType                 :" + regSession.getUserType());
			logger.info(TAG + " getCompanySetUp :: getDeviceId                 :" + regSession.getDeviceId());
			logger.info(TAG + " getCompanySetUp :: getDeviceType               :" + regSession.getDeviceType());
			logger.info(TAG + " getCompanySetUp :: setContactUsEmail           :" + regSession.getContactUsEmail());
			logger.info(TAG + " getCompanySetUp :: setContactUsHelpLineNumber  :" + regSession.getContactUsHelpLineNumber());

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
		}
		return resp;
	}

	public AmxApiResponse<Validate, Object> isOtpEnabled(String civilId)
	{
		boolean isOtpEnable = customerRegistrationDao.isOtpEnabled(civilId);

		logger.info(TAG + " isOtpEnable :: isOtpEnable :" + isOtpEnable);

		AmxApiResponse<Validate, Object> resp = new AmxApiResponse<Validate, Object>();

		if (isOtpEnable)
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setMessage(Message.CUST_OTP_ENABLED);
			resp.setMessageKey(MessageKey.KEY_CUST_OTP_ENABLED);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
			resp.setMessage(Message.CUST_OTP_NOT_ENABLED);
			resp.setMessageKey(MessageKey.KEY_CUST_OTP_NOT_ENABLED);
		}
		return resp;
	}

	public AmxApiResponse<Validate, Object> setOtpCount(String civilId)
	{
		Validate setOtpCount = customerRegistrationDao.setOtpCount(civilId);

		logger.info(TAG + " setOtpCount :: setOtpCount :" + setOtpCount);

		AmxApiResponse<Validate, Object> resp = new AmxApiResponse<Validate, Object>();

		if (setOtpCount.isValid())
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
			resp.setMessage(setOtpCount.getErrorMessage());
			resp.setMessageKey(setOtpCount.getErrorCode());
		}
		return resp;
	}

	public CustomerDetailModel getUserDetails(String civilId)
	{
		CustomerDetailModel customerDetailModel = customerRegistrationDao.getUserDetails(civilId);

		logger.info(TAG + " userDetails :: customerDetailModel :" + customerDetailModel.toString());

		if (customerDetailModel.getStatus())
		{
			return customerDetailModel;
		}
		return null;
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

	public AmxApiResponse<?, Object> sendOtp(RequestOtpModel requestOtpModel)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();

		try
		{
			AmxApiResponse<Validate, Object> validateCivilID = isValidCivilId(requestOtpModel.getCivilId());
			AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(requestOtpModel.getCivilId());
			AmxApiResponse<Validate, Object> isValidMobileNumber = isValidMobileNumber(requestOtpModel.getMobileNumber());
			AmxApiResponse<Validate, Object> mobileNumberExists = isMobileNumberExist(requestOtpModel.getMobileNumber());
			AmxApiResponse<Validate, Object> validateEmailID = isValidEmailId(requestOtpModel.getEmailId());
			AmxApiResponse<Validate, Object> emailIdExists = isEmailIdExist(requestOtpModel.getEmailId());
			AmxApiResponse<Validate, Object> isOtpEnabled = setOtpCount(requestOtpModel.getCivilId());

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
				mobileNumberExists.setMessageKey(MessageKey.KEY_MOBILE_OR_EMAIL_ALREADY_EXISTS);
				mobileNumberExists.setStatusKey(ApiConstants.FAILURE);
				return mobileNumberExists;
			}
			else if (emailIdExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				sendFailedRegistration(DetailsConstants.REG_INCOMPLETE_TYPE_DUPLICATE_EMAIL, requestOtpModel, emailIdExists.getMessage());
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
			AmxApiResponse<Validate, Object> setOtpCount = isOtpEnabled(requestOtpModel.getCivilId());

			regSession.setCivilId(requestOtpModel.getCivilId());
			regSession.setEmailId(requestOtpModel.getEmailId());
			regSession.setMobileNumber(requestOtpModel.getMobileNumber());

			String emailOtpPrefix = Random.randomAlpha(3);
			String mobileOtpPrefix = Random.randomAlpha(3);

			String emailOtp = Random.randomNumeric(6);
			String mobileOtp = Random.randomNumeric(6);

			String emailOtpToSend = emailOtpPrefix + "-" + emailOtp;
			String mobileOtpToSend = mobileOtpPrefix + "-" + mobileOtp;

			responseOtpModel.setEotpPrefix(emailOtpPrefix);
			responseOtpModel.setMotpPrefix(mobileOtpPrefix);

			regSession.setMotpPrefix(responseOtpModel.getMotpPrefix());
			regSession.setEotpPrefix(responseOtpModel.getEotpPrefix());
			regSession.setEotp(emailOtp);
			regSession.setMotp(mobileOtp);

			String emailIdFrom = webConfig.getConfigEmail();
			String emailITo = requestOtpModel.getEmailId();
			String Subject = "Almulla Insurance Registartion Otp";
			String mailData = "Your Email OTP Generted For Registration of Almulla Insurance is : " + emailOtpToSend + "          And Mobile Otp is :" + mobileOtpToSend + "";

			emailNotification.sendEmail(emailIdFrom, emailITo, Subject, mailData);

			/*
			 * Code Needed Here For Mobile Otp
			 * 
			 * 
			 * 
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
		}
		resp.setData(validate);
		resp.setMessageKey(MessageKey.KEY_REG_VALIDATE_OTP);

		return resp;
	}

	public AmxApiResponse<CustomerRegistrationResponse, Object> addNewCustomer(CustomerRegistrationRequest userRegistartionRequest)
	{
		AmxApiResponse<CustomerRegistrationResponse, Object> resp = new AmxApiResponse<CustomerRegistrationResponse, Object>();
		CustomerRegistrationResponse customerRegistrationResponse = new CustomerRegistrationResponse();
		CustomerRegistrationModel customerRegistrationModel = new CustomerRegistrationModel();

		customerRegistrationModel.setCountryId(1);
		customerRegistrationModel.setCompCd(10);
		customerRegistrationModel.setUserType(userRegistartionRequest.getUserType());
		customerRegistrationModel.setMobile(regSession.getMobileNumber());
		customerRegistrationModel.setEmail(regSession.getEmailId());
		customerRegistrationModel.setLanguageId(0);
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
			String Subject = "Almulla Insurance Registartion Confirmation";
			String mailData = "Almulla Insurance Registartion Completed Successfuill.";

			Email email = emailNotification.sendEmail(emailIdFrom, emailITo, Subject, mailData);
			if (email.getEmailSentStatus())
			{
			}

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

			customerLoginModel.setCountryId(regSession.getCountryId());
			customerLoginModel.setCompCd(regSession.getCompCd());
			customerLoginModel.setUserType(regSession.getUserType());
			customerLoginModel.setCivilId(customerLoginRequest.getCivilId());
			customerLoginModel.setPassword(customerLoginRequest.getPassword());
			customerLoginModel.setDeviceId(regSession.getDeviceId());
			customerLoginModel.setDeviceType(regSession.getDeviceType());

			customerLoginModel = customerRegistrationDao.validateUserLogin(customerLoginModel);

			if (customerLoginModel.getStatus())
			{
				userSession.setCivilId(customerLoginRequest.getCivilId());
				userSession.setCustomerSequenceNumber(new BigDecimal(customerLoginModel.getUserSeqNum()));
				userSession.setCountryId(new BigDecimal(regSession.getCountryId()));
				userSession.setCompCd(new BigDecimal(regSession.getCompCd()));
				userSession.setUserType(regSession.getUserType());
				userSession.setLanguageId(new BigDecimal(regSession.getLanguageId()));
				userSession.setDeviceType(regSession.getDeviceType());
				userSession.setContactUsEmail(regSession.getContactUsEmail());
				userSession.setContactUsHelpLineNumber(regSession.getContactUsHelpLineNumber());

				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
			}

			resp.setMessageKey(customerLoginModel.getErrorCode());
			resp.setMessage(customerLoginModel.getErrorMessage());

			if (null != customerLoginModel.getErrorCode() && customerLoginModel.getErrorCode().equalsIgnoreCase(ErrorKey.CUSTOMER_ACCOUNT_LOCK))
			{
				customerLoginResponse.setContactUsHelpLineNumber(regSession.getContactUsHelpLineNumber());
				customerLoginResponse.setContactUsEmail(regSession.getContactUsEmail());
			}

			customerLoginResponse.setAmibRef(customerLoginModel.getAmibRef());
			customerLoginResponse.setUserSeqNum(customerLoginModel.getUserSeqNum());

			resp.setData(customerLoginResponse);

			logger.info(TAG + " validateUserLogin :: customerLoginResponse :" + customerLoginResponse.toString());
		}
		return resp;
	}

	public AmxApiResponse<?, Object> changePasswordOtpInitiate(ChangePasswordOtpRequest changePasswordOtpRequest)
	{
		logger.info(TAG + " changePasswordOtpInitiate :: getCivilId :" + changePasswordOtpRequest.getCivilId());
		
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

		CustomerDetailModel customerDetailModel = getUserDetails(changePasswordOtpRequest.getCivilId());

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

				String emailOtpPrefix = Random.randomAlpha(3);
				String mobileOtpPrefix = Random.randomAlpha(3);

				String emailOtp = Random.randomNumeric(6);
				String mobileOtp = Random.randomNumeric(6);

				String emailOtpToSend = emailOtpPrefix + "-" + emailOtp;
				String mobileOtpToSend = mobileOtpPrefix + "-" + mobileOtp;

				responseOtpModel.setEotpPrefix(emailOtpPrefix);
				responseOtpModel.setMotpPrefix(mobileOtpPrefix);

				regSession.setMotpPrefix(responseOtpModel.getMotpPrefix());
				regSession.setEotpPrefix(responseOtpModel.getEotpPrefix());
				regSession.setEotp(emailOtp);
				regSession.setMotp(mobileOtp);

				String emailIdFrom = webConfig.getConfigEmail();
				String emailITo = customerDetailModel.getEmail();
				logger.info(TAG + " changePasswordOtpInitiate :: civilIdExistCheck :" + customerDetailModel.getEmail());
				String Subject = "Almulla Insurance Registartion Otp";
				String mailData = "Your Email OTP Generted For Change Password of Almulla Insurance is : " + emailOtpToSend + "          And Mobile Otp is :" + mobileOtpToSend + "";

				emailNotification.sendEmail(emailIdFrom, emailITo, Subject, mailData);

				/*
				 * Code Needed Here For Mobile Otp
				 * 
				 * 
				 * 
				 * Code Needed Here For Mobile Otp
				 * 
				 */

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
		customerDetailModel.setCivilId(regSession.getCivilId());
		customerDetailModel.setCountryId(regSession.getCountryId());
		customerDetailModel.setCompCd(regSession.getCompCd());
		customerDetailModel.setUserType(regSession.getUserType());
		customerDetailModel.setDeviceId(regSession.getDeviceId());
		customerDetailModel.setDeviceType(regSession.getDeviceType());

		logger.info(TAG + " updatePassword :: getCountryId :" + customerDetailModel.getCountryId());
		logger.info(TAG + " updatePassword :: getCompCd :" + customerDetailModel.getCompCd());
		logger.info(TAG + " updatePassword :: getUserType :" + customerDetailModel.getUserType());
		logger.info(TAG + " updatePassword :: getCivilId :" + customerDetailModel.getCivilId());
		logger.info(TAG + " updatePassword :: getPassword :" + customerDetailModel.getPassword());
		logger.info(TAG + " updatePassword :: getDeviceId :" + customerDetailModel.getDeviceId());

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
		failureException.setCountryId(new BigDecimal(regSession.getCountryId()));
		failureException.setCompCd(new BigDecimal(regSession.getCompCd()));
		failureException.setDeviceId(regSession.getDeviceId());
		failureException.setDeviceType(regSession.getDeviceType());
		failureException.setLanguageId(new BigDecimal(regSession.getLanguageId()));
		failureException.setUserType(regSession.getUserType());
		failureException.setExceptionType("REGISTER");
		failureException.setExceptionMsg(exceptionMessage);

		emailNotification.sendEmail(emailIdFrom, emailITo, Subject, mailData);

		customerRegistrationDao.setFailedException(type, failureException);

	}

}
