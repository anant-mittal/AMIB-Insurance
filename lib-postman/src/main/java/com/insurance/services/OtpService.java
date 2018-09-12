package com.insurance.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.Message;
import com.amx.jax.constants.MessageKey;
import com.amx.jax.dao.CustomerRegistrationDao;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.RegSession;
import com.amx.jax.models.Validate;
import com.amx.utils.Random;
import com.insurance.model.ResponseOtpModel;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OtpService
{
	String TAG = "com.insurance.services :: OtpService :: ";

	private static final Logger logger = LoggerFactory.getLogger(OtpService.class);

	@Autowired
	RegSession regSession;

	@Autowired
	MetaData metaData;

	@Autowired
	EmailService emailNotification;

	@Autowired
	private SMService smservice;

	@Autowired
	CustomerRegistrationDao customerRegistrationDao;

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
		String emailIdFrom = regSession.getEmailFromConfigured();
		String emailITo = emailId;
		String Subject = "OTP Verification";
		String mailData = "Your Email OTP Generated From Almulla Insurance is :- " + emailOtpToSend;
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
		String mailData = "Your Mobile OTP Generated From Al Mulla Insurance is :- " + mobileOtpToSend;
		String emailIdFrom = regSession.getEmailFromConfigured();
		String emailITo = "almulla.insurance.1427@gmail.com";// for Demo
		String Subject = "Almulla Insurance Otp";
		emailNotification.sendEmail(emailIdFrom, emailITo, Subject, mailData);
		
		// smservice.sendMessage(mobileWithCode, mailData);
		return mobileOtpPrefix;
	}

	public AmxApiResponse<?, Object> initiateMobileEmailOtp(String emailId, String mobileNumber)
	{
		metaData.setEotp("");
		metaData.setMotp("");
		metaData.setmOtpMobileNumber("");
		metaData.seteOtpEmailId("");

		AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(regSession.getCivilId());
		AmxApiResponse<Validate, Object> isOtpEnabled = isOtpEnabled(regSession.getCivilId());
		if (isOtpEnabled.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE) && civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			return isOtpEnabled;
		}

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
		
		AmxApiResponse<Validate, Object> setOtpCount = setOtpCount(regSession.getCivilId());
		if (setOtpCount.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE) && civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			return setOtpCount;
		}
		
		return resp;
	}

	public AmxApiResponse<?, Object> validateDOTP(String eOtp, String mOtp, String emailId, String mobileNumber)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();

		if (null != mOtp && !mOtp.equals("") && null != eOtp && !eOtp.equals(""))
		{
			if (!metaData.geteOtpEmailId().equals(emailId) || !metaData.getmOtpMobileNumber().equals(mobileNumber))
			{
				return initiateMobileEmailOtp(emailId, mobileNumber);
			}

			if (!metaData.getMotp().equals(mOtp) || !metaData.getEotp().equals(eOtp))
			{
				resp.setError(Message.REG_INVALID_OTP);
				resp.setStatusKey(MessageKey.KEY_EMAIL_MOBILE_OTP_REQUIRED_INVALID);
				resp.setMessageKey(MessageKey.KEY_EMAIL_MOBILE_OTP_REQUIRED_INVALID);
				return resp;
			}
		}
		else
		{
			return initiateMobileEmailOtp(emailId, mobileNumber);
		}
		return null;
	}

	public AmxApiResponse<?, Object> initiateEmailOtp(String emailId)
	{
		metaData.setEotp("");
		metaData.setMotp("");
		metaData.setmOtpMobileNumber("");
		metaData.seteOtpEmailId("");

		AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(regSession.getCivilId());
		AmxApiResponse<Validate, Object> isOtpEnabled = isOtpEnabled(regSession.getCivilId());
		if (isOtpEnabled.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE) && civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			return isOtpEnabled;
		}
		
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();
		String eOtpPrefix = sendEmailOtp(emailId, "");
		responseOtpModel.setEotpPrefix(eOtpPrefix);
		responseOtpModel.setMotpPrefix(null);
		metaData.seteOtpEmailId(emailId);
		resp.setMeta(responseOtpModel);
		resp.setStatusKey(MessageKey.KEY_EMAIL_OTP_REQUIRED);
		resp.setMessageKey(MessageKey.KEY_EMAIL_OTP_REQUIRED);
		
		AmxApiResponse<Validate, Object> setOtpCount = setOtpCount(regSession.getCivilId());
		if (setOtpCount.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE) && civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			return setOtpCount;
		}
		return resp;
	}

	public AmxApiResponse<?, Object> validateEOTP(String eOtp, String emailId)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		if (null != eOtp && !eOtp.equals(""))
		{
			if (!metaData.geteOtpEmailId().equals(emailId))
			{
				return initiateEmailOtp(emailId);
			}
			if (!metaData.getEotp().equals(eOtp))
			{
				resp.setError(Message.REG_INVALID_OTP);
				resp.setStatusKey(MessageKey.KEY_EMAIL_OTP_REQUIRED_INVALID);
				resp.setMessageKey(MessageKey.KEY_EMAIL_OTP_REQUIRED_INVALID);
				return resp;
			}
		}
		else
		{
			return initiateEmailOtp(emailId);
		}
		return null;//
	}

	public AmxApiResponse<?, Object> initiateMobileOtp(String mobileNumber)
	{
		metaData.setEotp("");
		metaData.setMotp("");
		metaData.setmOtpMobileNumber("");
		metaData.seteOtpEmailId("");

		AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(regSession.getCivilId());
		AmxApiResponse<Validate, Object> isOtpEnabled = isOtpEnabled(regSession.getCivilId());
		if (isOtpEnabled.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE) && civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			return isOtpEnabled;
		}
		
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();
		String mOtpPrefix = sendMobileOtp(mobileNumber, "");
		responseOtpModel.setMotpPrefix(mOtpPrefix);
		responseOtpModel.setEotpPrefix(null);
		metaData.setmOtpMobileNumber(mobileNumber);
		resp.setMeta(responseOtpModel);
		resp.setStatusKey(MessageKey.KEY_MOBILE_OTP_REQUIRED);
		resp.setMessageKey(MessageKey.KEY_MOBILE_OTP_REQUIRED);
		
		AmxApiResponse<Validate, Object> setOtpCount = setOtpCount(regSession.getCivilId());
		if (setOtpCount.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE) && civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			return setOtpCount;
		}
		return resp;
	}

	public AmxApiResponse<?, Object> validateMOTP(String mOtp, String mobileNumber)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		if (null != mOtp && !mOtp.equals(""))
		{
			if (!metaData.getmOtpMobileNumber().equals(mobileNumber))
			{
				return initiateMobileOtp(mobileNumber);
			}
			if (!metaData.getMotp().equals(mOtp))
			{
				resp.setError(Message.REG_INVALID_OTP);
				resp.setStatusKey(MessageKey.KEY_MOBILE_OTP_REQUIRED_INVALID);
				resp.setMessageKey(MessageKey.KEY_MOBILE_OTP_REQUIRED_INVALID);
				return resp;
			}
		}
		else
		{
			return initiateMobileOtp(mobileNumber);
		}
		return null;
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

		String emailIdFrom = regSession.getEmailFromConfigured();
		String emailITo = emailId;
		String Subject = "OTP Verification";
		String mailData = "Your Email OTP Generated From Al Mulla Insurance is : " + emailOtpToSend + "          And Mobile Otp is :" + mobileOtpToSend + "";

		emailNotification.sendEmail(emailIdFrom, emailITo, Subject, mailData);

		return responseOtpModel;
	}

	public AmxApiResponse<Validate, Object> isOtpEnabled(String civilId)
	{
		logger.info(TAG + " isOtpEnabled :: civilId :" + civilId);
		AmxApiResponse<Validate, Object> resp = new AmxApiResponse<Validate, Object>();
		boolean isOtpEnable = customerRegistrationDao.isOtpEnabled(civilId);
		logger.info(TAG + " isOtpEnabled :: isOtpEnable :" + isOtpEnable);

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
		return resp;
	}

	public AmxApiResponse<Validate, Object> setOtpCount(String civilId)
	{
		AmxApiResponse<Validate, Object> resp = new AmxApiResponse<Validate, Object>();

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
}
