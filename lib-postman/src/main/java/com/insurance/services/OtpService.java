package com.insurance.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OtpService
{
	@Autowired
	RegSession regSession;

	@Autowired
	MetaData metaData;

	@Autowired
	EmailService emailNotification;

	@Autowired
	private SMService smservice;

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
		String emailITo = "abhishek.tiwari@mobicule.com";
		String Subject = "Almulla Insurance Otp";
		emailNotification.sendEmail(emailIdFrom, emailITo, Subject, mailData);

		// smservice.sendMessage(mobileWithCode, mailData);

		return mobileOtpPrefix;
	}

	public AmxApiResponse<?, Object> initiateMobileEmailOtp(String emailId, String mobileNumber)
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

	public AmxApiResponse<?, Object> validateDOTP(String eOtp, String mOtp, String emailId, String mobileNumber)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		if (null != mOtp && !mOtp.equals("") && null != eOtp && !eOtp.equals(""))
		{
			if (!metaData.getmOtpMobileNumber().equals(emailId) || !metaData.geteOtpEmailId().equals(mobileNumber))
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
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();
		String eOtpPrefix = sendEmailOtp(emailId, "");
		responseOtpModel.setEotpPrefix(eOtpPrefix);
		responseOtpModel.setMotpPrefix(null);
		metaData.seteOtpEmailId(emailId);
		resp.setMeta(responseOtpModel);
		resp.setStatusKey(MessageKey.KEY_EMAIL_OTP_REQUIRED);
		resp.setMessageKey(MessageKey.KEY_EMAIL_OTP_REQUIRED);
		return resp;
	}
	
	public AmxApiResponse<?, Object> validateEOTP(String eOtp , String emailId)
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
		return null;
	}
	

	public AmxApiResponse<?, Object> initiateMobileOtp(String mobileNumber)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();
		String mOtpPrefix = sendMobileOtp(mobileNumber, "");
		responseOtpModel.setMotpPrefix(mOtpPrefix);
		responseOtpModel.setEotpPrefix(null);
		metaData.setmOtpMobileNumber(mobileNumber);
		resp.setMeta(responseOtpModel);
		resp.setStatusKey(MessageKey.KEY_MOBILE_OTP_REQUIRED);
		resp.setMessageKey(MessageKey.KEY_MOBILE_OTP_REQUIRED);
		return resp;
	}
	
	public AmxApiResponse<?, Object> validateMOTP(String mOtp , String mobileNumber)
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
}
