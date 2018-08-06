
package com.amx.jax.services;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.Message;
import com.amx.jax.constants.MessageKey;
import com.amx.jax.dao.CustomerRegistrationDao;
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
		validMobileNumber.setMessageKey(isValidMobileNumber.getErrorCode());//Error Code Coming From DB/Procedure

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

			Email email = emailNotification.sendEmail(emailOtpToSend, mobileOtpToSend, requestOtpModel.getEmailId());

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
		}
		else
		{
			resp.setStatusKey(ApiConstants.FAILURE);
		}

		resp.setMessage(customerRegistrationModel.getErrorMessage());
		resp.setError(customerRegistrationModel.getErrorCode());

		customerRegistrationResponse.setCivilid(customerRegistrationModel.getCivilid());
		customerRegistrationResponse.setUserSequenceNumber(customerRegistrationModel.getUserSequenceNumber());

		resp.setData(customerRegistrationResponse);

		return resp;

	}

	public ArrayList getCompanySetUp(int langId)
	{
		try
		{
			return customerRegistrationDao.getCompanySetUp(langId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
