
package com.amx.jax.services;

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
import com.amx.jax.models.Validate;
import com.amx.jax.session.RegSession;
import com.amx.utils.Random;
import com.insurance.email.dao.EmailNotification;
import com.insurance.email.model.Email;
import com.insurance.generateotp.CreateOtpToken;
import com.insurance.generateotp.RequestOtpModel;
import com.insurance.generateotp.ResponseOtpModel;

@Service
public class CustomerRegistrationService
{
	String TAG = "com.amx.jax.userregistration.service :: CustomerRegistrationService :: ";

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
	private WebConfig webConfig;

	public AmxApiResponse<Validate, Object> getCompanySetUp(int languageId, String deviceId)
	{
		AmxApiResponse<Validate, Object> resp = new AmxApiResponse<Validate, Object>();

		try
		{
			ArrayList<CompanySetUp> getCompanySetUp = customerRegistrationDao.getCompanySetUp(languageId);

			logger.info(TAG + " getCompanySetUp :: Init After getCompanySetUp :");

			regSession.setCountryId(getCompanySetUp.get(0).getCntryCd());
			regSession.setCompCd(getCompanySetUp.get(0).getCompCd());
			regSession.setUserType("D");
			regSession.setDeviceId(deviceId);
			regSession.setDeviceType("ONLINE");

			logger.info(TAG + " getCompanySetUp :: getCountryId   :" + regSession.getCountryId());
			logger.info(TAG + " getCompanySetUp :: getCompCd      :" + regSession.getCompCd());
			logger.info(TAG + " getCompanySetUp :: getUserType    :" + regSession.getUserType());
			logger.info(TAG + " getCompanySetUp :: getDeviceId    :" + regSession.getDeviceId());
			logger.info(TAG + " getCompanySetUp :: getDeviceType  :" + regSession.getDeviceType());

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
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
			resp.setMessage(Message.CIVILID_INVALID);
		}
		resp.setMessageKey(MessageKey.KEY_CIVIL_ID_INVALID);
		return resp;

	}

	public AmxApiResponse<Validate, Object> isCivilIdExist(String civilid)
	{
		boolean civilIdExistCheck = customerRegistrationDao.isCivilIdExist(civilid);

		AmxApiResponse<Validate, Object> resp = new AmxApiResponse<Validate, Object>();

		if (civilIdExistCheck)
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setMessage(Message.CIVILID_ALREDAY_REGISTER);
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
			resp.setMessage(Message.CIVILID_ALREDAY_NOT_REGISTER);
		}
		resp.setMessageKey(MessageKey.KEY_CIVIL_ID_ALREADY_REGISTER);
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
		}
		else
		{
			validMobileNumber.setStatusKey(ApiConstants.FAILURE);
			validMobileNumber.setMessage(Message.MOBILE_NO_NOT_REGISTER);
		}
		validMobileNumber.setMessageKey(MessageKey.KEY_MOBILE_NO_ALREADY_REGISTER);
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

		}
		else
		{
			validate.setValid(false);
			resp.setStatusKey(ApiConstants.FAILURE);
			resp.setMessage(Message.EMAIL_INVALID);
		}
		resp.setMessageKey(MessageKey.KEY_EMAID_INVALID);
		resp.setData(validate);
		return resp;
	}

	public CustomerDetailModel userDetails(String civilId)
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

	public AmxApiResponse<Validate, Object> isEmailIdExistCheck(String mobileNumber)
	{
		AmxApiResponse<Validate, Object> isValidEmailId = isValidEmailId(mobileNumber);

		if (isValidEmailId.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return isValidEmailId;
		}
		return null;
	}

	public AmxApiResponse<?, Object> sendOtp(RequestOtpModel requestOtpModel)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();

		try
		{
			AmxApiResponse<Validate, Object> validateCivilID = isValidCivilId(requestOtpModel.getCivilId());
			if (validateCivilID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return validateCivilID;
			}

			AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(requestOtpModel.getCivilId());
			if (civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				civilIdExistCheck.setStatusKey(ApiConstants.FAILURE);
				return civilIdExistCheck;
			}

			AmxApiResponse<Validate, Object> isValidMobileNumber = isValidMobileNumber(requestOtpModel.getMobileNumber());
			if (isValidMobileNumber.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return isValidMobileNumber;
			}

			AmxApiResponse<Validate, Object> mobileNumberExists = isMobileNumberExist(requestOtpModel.getMobileNumber());
			if (mobileNumberExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				mobileNumberExists.setStatusKey(ApiConstants.FAILURE);
				return mobileNumberExists;
			}

			AmxApiResponse<Validate, Object> validateEmailID = isValidEmailId(requestOtpModel.getEmailId());

			if (validateEmailID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
			{
				return validateEmailID;
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

			Email email = emailNotification.sendEmail(emailIdFrom, emailITo, Subject, mailData);

			if (email.getEmailSentStatus())
			{
				resp.setData(responseOtpModel);
				resp.setStatus(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setData(null);
				resp.setException(email.getEmailSendingException());
				resp.setStatus(ApiConstants.FAILURE);
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

	public AmxApiResponse<CustomerLoginResponse, Object> validateUserLogin(CustomerLoginRequest customerLoginRequest)
	{
		AmxApiResponse<CustomerLoginResponse, Object> resp = new AmxApiResponse<CustomerLoginResponse, Object>();

		CustomerLoginModel customerLoginModel = new CustomerLoginModel();
		customerLoginModel.setCountryId(regSession.getCountryId());
		customerLoginModel.setCompCd(regSession.getCompCd());
		customerLoginModel.setUserType(regSession.getUserType());
		customerLoginModel.setCivilId(customerLoginRequest.getCivilId());
		customerLoginModel.setPassword(customerLoginRequest.getPassword());

		customerLoginModel = customerRegistrationDao.validateUserLogin(customerLoginModel);

		if (customerLoginModel.getStatus())
		{
			resp.setStatusKey(ApiConstants.SUCCESS);

		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
		}

		resp.setMessageKey(customerLoginModel.getErrorCode());
		resp.setMessage(customerLoginModel.getErrorCode());

		CustomerLoginResponse customerLoginResponse = new CustomerLoginResponse();
		customerLoginResponse.setAmibRef(customerLoginModel.getAmibRef());
		customerLoginResponse.setUserSeqNum(customerLoginModel.getUserSeqNum());

		resp.setData(customerLoginResponse);

		logger.info(TAG + " validateUserLogin :: customerLoginModel :" + customerLoginModel);

		return resp;
	}

	public AmxApiResponse<?, Object> changePasswordOtpInitiate(ChangePasswordOtpRequest changePasswordOtpRequest)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();

		AmxApiResponse<Validate, Object> validateCivilID = isValidCivilId(changePasswordOtpRequest.getCivilId());
		if (validateCivilID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return validateCivilID;
		}

		AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(changePasswordOtpRequest.getCivilId());
		if (civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return civilIdExistCheck;
		}

		CustomerDetailModel customerDetailModel = userDetails(changePasswordOtpRequest.getCivilId());

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
				String Subject = "Almulla Insurance Registartion Otp";
				String mailData = "Your Email OTP Generted For Change Password of Almulla Insurance is : " + emailOtpToSend + "          And Mobile Otp is :" + mobileOtpToSend + "";

				Email email = emailNotification.sendEmail(emailIdFrom, emailITo, Subject, mailData);

				if (email.getEmailSentStatus())
				{
					resp.setData(responseOtpModel);
					resp.setStatus(ApiConstants.SUCCESS);
				}
				else
				{
					resp.setStatusKey(ApiConstants.FAILURE);
					resp.setMessage(Message.CP_EMAIL_NOT_SENT);
					resp.setMessageKey(MessageKey.KEY_CP_OTP_NOT_GENERATED);
				}

				/*
				 * Code Needed Here For Mobile Otp
				 * 
				 * 
				 * 
				 * Code Needed Here For Mobile Otp
				 * 
				 */

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
		customerDetailModel.setOtp(changePasswordRequest.getChangePasswordOtp());
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
}
