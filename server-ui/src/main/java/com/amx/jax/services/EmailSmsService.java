package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Templates;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.DetailsConstants;
import com.amx.jax.constants.Message;
import com.amx.jax.constants.MessageKey;
import com.amx.jax.dao.CustomerRegistrationDao;
import com.amx.jax.dict.Language;
import com.amx.jax.models.CustomerDetailModel;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.RequestOtpModel;
import com.amx.jax.models.ResponseOtpModel;
import com.amx.jax.models.Validate;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.Notipy.Channel;
import com.amx.jax.ui.session.UserSession;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesIB;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.utils.Random;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class EmailSmsService
{
	String TAG = "com.insurance.services :: EmailSmsService :: ";

	private static final Logger logger = LoggerFactory.getLogger(EmailSmsService.class);

	@Autowired
	MetaData metaData;
	
	@Autowired
	UserSession userSession;

	@Autowired
	private PostManClient postManClient;

	@Autowired
	private AppConfig appConfig;

	@Autowired
	CustomerRegistrationDao customerRegistrationDao;

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */

	/************* EMAIL OTP **********/
	public String sendEmailOtp(String EmailTo)
	{
		String emailFrom = metaData.getEmailFromConfigured();

		ResponseOtpModel responseOtpModel = new ResponseOtpModel();
		String emailOtpPrefix = Random.randomAlpha(3);
		String emailOtp = Random.randomNumeric(6);
		String emailOtpToSend = emailOtpPrefix + "-" + emailOtp;
		responseOtpModel.setEotpPrefix(emailOtpPrefix);
		responseOtpModel.setMotpPrefix(null);
		userSession.setEotpPrefix(emailOtpPrefix);
		userSession.setEotp(emailOtp);

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(DetailsConstants.CUSTOMER_NAME, customerName());
		model.put(DetailsConstants.CONTACT_US_EMAIL, metaData.getContactUsEmail());
		model.put(DetailsConstants.AMIB_WEBSITE_LINK, metaData.getAmibWebsiteLink());
		model.put(DetailsConstants.EMAIL_OTP, emailOtpToSend);
		model.put(DetailsConstants.COMPANY_NAME, metaData.getCompanyName());
		model.put(DetailsConstants.COUNTRY_NAME, "KUWAIT");
		wrapper.put("data", model);
		
		ArrayList<String> emailTo = new ArrayList<String>();
		emailTo.add(EmailTo);

		Email email = new Email();
		email.setFrom(emailFrom);
		email.setTo(emailTo);
		email.setSubject(DetailsConstants.REG_OTP_EMAIL_SUBJECT);
		email.setModel(wrapper);
		email.setITemplate(TemplatesIB.OTP_EMAIL);
		email.setHtml(true);
		email.setLang(Language.EN);//TODO : LANGUAGE IS PASSED HARD CODED HERE NEED TO CONFIGURE
		
		postManClient.sendEmail(email);

		return emailOtpPrefix;
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/************* MOBILE OTP **********/
	public String sendMobileOtp(String mobileNumber)
	{

		ResponseOtpModel responseOtpModel = new ResponseOtpModel();
		String mobileOtpPrefix = Random.randomAlpha(3);
		String mobileOtp = Random.randomNumeric(6);
		String mobileOtpToSend = mobileOtpPrefix + "-" + mobileOtp;
		responseOtpModel.setEotpPrefix(null);
		responseOtpModel.setMotpPrefix(mobileOtpPrefix);
		userSession.setMotpPrefix(mobileOtpPrefix);
		userSession.setMotp(mobileOtp);
		try
		{
			Map<String, Object> wrapper = new HashMap<String, Object>();
			Map<String, Object> model = new HashMap<String, Object>();
			
			SMS sms = new SMS();
			sms.addTo(mobileNumber);
			if (!appConfig.isProdMode())
			{
				sendToSlack("mobile", sms.getTo().get(0), mobileOtpPrefix, mobileOtp);
			}
			else
			{
				model.put(DetailsConstants.MOBILE_OTP, mobileOtpToSend);
				wrapper.put("data", model);
				sms.setModel(wrapper);
				sms.setITemplate(TemplatesIB.OTP_SMS);
				sms.setLang(Language.EN);//TODO : LANGUAGE IS PASSED HARD CODED HERE NEED TO CONFIGURE
				postManClient.sendSMS(sms);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("error in sendOtpSms", e);
		}

		return mobileOtpPrefix;
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/************* EMAIL TO CUSTOMER FOR SUCCESS FULL REGISTARTION **********/
	public void emailTosuccessFullUserRegistration(String emailIdTo)
	{

		String emailIdFrom = metaData.getEmailFromConfigured();

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(DetailsConstants.CUSTOMER_NAME, customerName());
		model.put(DetailsConstants.CONTACT_US_EMAIL, metaData.getContactUsEmail());
		model.put(DetailsConstants.AMIB_WEBSITE_LINK, metaData.getAmibWebsiteLink());
		model.put(DetailsConstants.CONTACT_US_EMAIL, metaData.getContactUsEmail());
		model.put(DetailsConstants.COMPANY_NAME, metaData.getCompanyName());
		model.put(DetailsConstants.COUNTRY_NAME, "KUWAIT");
		wrapper.put("data", model);
		
		ArrayList<String> emailTo = new ArrayList<String>();
		emailTo.add(emailIdTo);

		Email email = new Email();
		email.setFrom(emailIdFrom);
		email.setTo(emailTo);
		email.setSubject(DetailsConstants.REG_SUCCESS_EMAIL);
		email.setModel(wrapper);
		email.setMessage("Al Mulla Insurance Registration Completed Successfully.");
		email.setITemplate(TemplatesIB.REG_SUCCESS_EMAIL);
		email.setHtml(true);
		email.setLang(Language.EN);//TODO : LANGUAGE IS PASSED HARD CODED HERE NEED TO CONFIGURE
		postManClient.sendEmail(email);

	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/************* USER REG FAILED EMAIL TO AMIB **********/
	public void sendFailedRegEmail(RequestOtpModel requestOtpModel)
	{
		String emailIdTo = metaData.getContactUsEmail();
		String emailIdFrom = metaData.getEmailFromConfigured();

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(DetailsConstants.CUSTOMER_NAME, customerName());
		model.put(DetailsConstants.CUSTOMER_CIVIL_ID, requestOtpModel.getCivilId());
		model.put(DetailsConstants.CUSTOMER_EMAIL_ID, requestOtpModel.getEmailId());
		model.put(DetailsConstants.CUSTOMER_MOBILE_NO, requestOtpModel.getMobileNumber());
		model.put(DetailsConstants.CONTACT_US_EMAIL, metaData.getContactUsEmail());
		model.put(DetailsConstants.AMIB_WEBSITE_LINK, metaData.getAmibWebsiteLink());
		model.put(DetailsConstants.COMPANY_NAME, metaData.getCompanyName());
		model.put(DetailsConstants.COUNTRY_NAME, "KUWAIT");
		wrapper.put("data", model);
		
		ArrayList<String> emailTo = new ArrayList<String>();
		emailTo.add(emailIdTo);

		Email email = new Email();
		email.setFrom(emailIdFrom);
		email.setTo(emailTo);
		email.setSubject(DetailsConstants.FAILURE_REG_EMAIL_SUBJECT);
		email.setModel(wrapper);
		email.setITemplate(TemplatesIB.REG_INCOMPLETE_EMAIL);
		email.setHtml(true);
		email.setLang(Language.EN);//TODO : LANGUAGE IS PASSED HARD CODED HERE NEED TO CONFIGURE
		postManClient.sendEmail(email);

	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/*********
	 * REQUEST QUOTE SUBMIT SUCCESS MAIL TO USER
	 ********/
	public void emailToCustomerOnCompilitionRequestQuote(String makeDesc, String subMakeDesc, BigDecimal appSeqNumber)
	{
		logger.info(TAG + " emailToCustomerAndAmib :: makeDesc :" + makeDesc);
		logger.info(TAG + " emailToCustomerAndAmib :: subMakeDesc :" + subMakeDesc);
		logger.info(TAG + " emailToCustomerAndAmib :: appSeqNumber :" + appSeqNumber);
		
		String emailIdFrom = metaData.getEmailFromConfigured();
		String emailIdTo = userSession.getCustomerEmailId();
		String customerMobileNumber = userSession.getCustomerMobileNumber();
		String amibEmailId = metaData.getContactUsEmail();
		String civilId = userSession.getCivilId();

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(DetailsConstants.CUSTOMER_NAME, customerName());
		model.put(DetailsConstants.CUSTOMER_CIVIL_ID, civilId);
		model.put(DetailsConstants.CUSTOMER_EMAIL_ID, emailIdTo);
		model.put(DetailsConstants.CUSTOMER_MOBILE_NO, customerMobileNumber);
		model.put(DetailsConstants.CONTACT_US_EMAIL, metaData.getContactUsEmail());
		model.put(DetailsConstants.CONTACT_US_MOBILE, metaData.getContactUsHelpLineNumber());
		model.put(DetailsConstants.AMIB_WEBSITE_LINK, metaData.getAmibWebsiteLink());
		model.put(DetailsConstants.COMPANY_NAME, metaData.getCompanyName());
		model.put(DetailsConstants.COUNTRY_NAME, "KUWAIT");
		model.put(DetailsConstants.MAKE_DESC, makeDesc);
		model.put(DetailsConstants.SUB_MAKE_DESC, subMakeDesc);
		model.put(DetailsConstants.APPLICATION_ID, appSeqNumber);
		wrapper.put("data", model);
		
		ArrayList<String> emailTo = new ArrayList<String>();
		emailTo.add(emailIdTo);
		emailTo.add(amibEmailId);

		Email email = new Email();
		email.setFrom(emailIdFrom);
		email.setTo(emailTo);
		email.setSubject("Quote Request - "+appSeqNumber);
		email.setModel(wrapper);
		email.setITemplate(TemplatesIB.QUOTE_SUBMIT_EMAIL_TO_UESR);
		email.setHtml(true);
		email.setLang(Language.EN);//TODO : LANGUAGE IS PASSED HARD CODED HERE NEED TO CONFIGURE
		postManClient.sendEmail(email);

	}
	
	
	
	
	
	
	
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/*********
	 * REQUEST QUOTE SUBMIT SUCCESS MAIL TO AMIB
	 ********/
	public void emailToAmibOnCompilitionRequestQuote(String makeDesc, String subMakeDesc, BigDecimal appSeqNumber)
	{
		logger.info(TAG + " emailToCustomerAndAmib :: makeDesc :" + makeDesc);
		logger.info(TAG + " emailToCustomerAndAmib :: subMakeDesc :" + subMakeDesc);
		logger.info(TAG + " emailToCustomerAndAmib :: appSeqNumber :" + appSeqNumber);
		
		String emailIdFrom = metaData.getEmailFromConfigured();
		String emailIdTo = userSession.getCustomerEmailId();
		String customerMobileNumber = userSession.getCustomerMobileNumber();
		String amibEmailId = metaData.getContactUsEmail();
		String civilId = userSession.getCivilId();

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(DetailsConstants.CUSTOMER_NAME, customerName());
		model.put(DetailsConstants.CUSTOMER_CIVIL_ID, civilId);
		model.put(DetailsConstants.CUSTOMER_EMAIL_ID, emailIdTo);
		model.put(DetailsConstants.CUSTOMER_MOBILE_NO, customerMobileNumber);
		model.put(DetailsConstants.CONTACT_US_EMAIL, metaData.getContactUsEmail());
		model.put(DetailsConstants.CONTACT_US_MOBILE, metaData.getContactUsHelpLineNumber());
		model.put(DetailsConstants.AMIB_WEBSITE_LINK, metaData.getAmibWebsiteLink());
		model.put(DetailsConstants.COMPANY_NAME, metaData.getCompanyName());
		model.put(DetailsConstants.COUNTRY_NAME, "KUWAIT");
		model.put(DetailsConstants.MAKE_DESC, makeDesc);
		model.put(DetailsConstants.SUB_MAKE_DESC, subMakeDesc);
		model.put(DetailsConstants.APPLICATION_ID, appSeqNumber);
		wrapper.put("data", model);
		
		ArrayList<String> emailTo = new ArrayList<String>();
		emailTo.add(emailIdTo);
		emailTo.add(amibEmailId);

		Email email = new Email();
		email.setFrom(emailIdFrom);
		email.setTo(emailTo);
		email.setSubject("Customer Quote Request - "+appSeqNumber);
		email.setModel(wrapper);
		email.setITemplate(TemplatesIB.QUOTE_SUBMIT_EMAIL_TO_UESR);
		email.setHtml(true);
		email.setLang(Language.EN);//TODO : LANGUAGE IS PASSED HARD CODED HERE NEED TO CONFIGURE
		postManClient.sendEmail(email);

	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/************* INITIATE D OTP **********/
	public AmxApiResponse<?, Object> initiateDotp(String emailId, String mobileNumber)
	{
		userSession.setEotp("");
		userSession.setMotp("");
		userSession.setmOtpMobileNumber("");
		userSession.seteOtpEmailId("");

		AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(userSession.getCivilId());
		AmxApiResponse<Validate, Object> isOtpEnabled = isOtpEnabled(userSession.getCivilId());
		if (isOtpEnabled.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE) && civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			return isOtpEnabled;
		}

		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();

		String eOtpPrefix = sendEmailOtp(emailId);
		String mOtpPrefix = sendMobileOtp(mobileNumber);

		responseOtpModel.setEotpPrefix(eOtpPrefix);
		responseOtpModel.setMotpPrefix(mOtpPrefix);
		userSession.setmOtpMobileNumber(mobileNumber);
		userSession.seteOtpEmailId(emailId);
		resp.setMeta(responseOtpModel);
		resp.setStatusKey(MessageKey.KEY_EMAIL_MOBILE_OTP_REQUIRED);
		resp.setMessageKey(MessageKey.KEY_EMAIL_MOBILE_OTP_REQUIRED);

		AmxApiResponse<Validate, Object> setOtpCount = setOtpCount(userSession.getCivilId());
		if (setOtpCount.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE) && civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			return setOtpCount;
		}
		return resp;
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/************* VALIDATE D OTP **********/
	public AmxApiResponse<?, Object> validateDOTP(String eOtp, String mOtp, String emailId, String mobileNumber)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();

		if (null != mOtp && !mOtp.equals("") && null != eOtp && !eOtp.equals(""))
		{
			if (!userSession.geteOtpEmailId().equals(emailId) || !userSession.getmOtpMobileNumber().equals(mobileNumber))
			{
				return initiateDotp(emailId, mobileNumber);
			}

			if (!userSession.getMotp().equals(mOtp) || !userSession.getEotp().equals(eOtp))
			{
				resp.setError(Message.REG_INVALID_OTP);
				resp.setStatusKey(MessageKey.KEY_EMAIL_MOBILE_OTP_REQUIRED_INVALID);
				resp.setMessageKey(MessageKey.KEY_EMAIL_MOBILE_OTP_REQUIRED_INVALID);
				return resp;
			}
		}
		else
		{
			return initiateDotp(emailId, mobileNumber);
		}
		return null;
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/************* INITIATE E OTP **********/
	public AmxApiResponse<?, Object> initiateEotp(String emailId)
	{
		userSession.setEotp("");
		userSession.setMotp("");
		userSession.setmOtpMobileNumber("");
		userSession.seteOtpEmailId("");

		AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(userSession.getCivilId());
		AmxApiResponse<Validate, Object> isOtpEnabled = isOtpEnabled(userSession.getCivilId());
		if (isOtpEnabled.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE) && civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			return isOtpEnabled;
		}

		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();
		String eOtpPrefix = sendEmailOtp(emailId);
		responseOtpModel.setEotpPrefix(eOtpPrefix);
		responseOtpModel.setMotpPrefix(null);
		userSession.seteOtpEmailId(emailId);
		resp.setMeta(responseOtpModel);
		resp.setStatusKey(MessageKey.KEY_EMAIL_OTP_REQUIRED);
		resp.setMessageKey(MessageKey.KEY_EMAIL_OTP_REQUIRED);

		AmxApiResponse<Validate, Object> setOtpCount = setOtpCount(userSession.getCivilId());
		if (setOtpCount.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE) && civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			return setOtpCount;
		}
		return resp;
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/************* VALIDATE E OTP **********/
	public AmxApiResponse<?, Object> validateEotp(String eOtp, String emailId)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		if (null != eOtp && !eOtp.equals(""))
		{
			if (!userSession.geteOtpEmailId().equals(emailId))
			{
				return initiateEotp(emailId);
			}
			if (!userSession.getEotp().equals(eOtp))
			{
				resp.setError(Message.REG_INVALID_OTP);
				resp.setStatusKey(MessageKey.KEY_EMAIL_OTP_REQUIRED_INVALID);
				resp.setMessageKey(MessageKey.KEY_EMAIL_OTP_REQUIRED_INVALID);
				return resp;
			}
		}
		else
		{
			return initiateEotp(emailId);
		}
		return null;
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/************* INITIATE M OTP **********/
	public AmxApiResponse<?, Object> initiateMotp(String mobileNumber)
	{
		userSession.setEotp("");
		userSession.setMotp("");
		userSession.setmOtpMobileNumber("");
		userSession.seteOtpEmailId("");

		AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(userSession.getCivilId());
		AmxApiResponse<Validate, Object> isOtpEnabled = isOtpEnabled(userSession.getCivilId());
		if (isOtpEnabled.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE) && civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			return isOtpEnabled;
		}

		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();
		String mOtpPrefix = sendMobileOtp(mobileNumber);
		responseOtpModel.setMotpPrefix(mOtpPrefix);
		responseOtpModel.setEotpPrefix(null);
		userSession.setmOtpMobileNumber(mobileNumber);
		resp.setMeta(responseOtpModel);
		resp.setStatusKey(MessageKey.KEY_MOBILE_OTP_REQUIRED);
		resp.setMessageKey(MessageKey.KEY_MOBILE_OTP_REQUIRED);

		AmxApiResponse<Validate, Object> setOtpCount = setOtpCount(userSession.getCivilId());
		if (setOtpCount.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE) && civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			return setOtpCount;
		}
		return resp;
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/************* VALIDATE M OTP **********/
	public AmxApiResponse<?, Object> validateMotp(String mOtp, String mobileNumber)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		if (null != mOtp && !mOtp.equals(""))
		{
			if (!userSession.getmOtpMobileNumber().equals(mobileNumber))
			{
				return initiateMotp(mobileNumber);
			}
			if (!userSession.getMotp().equals(mOtp))
			{
				resp.setError(Message.REG_INVALID_OTP);
				resp.setStatusKey(MessageKey.KEY_MOBILE_OTP_REQUIRED_INVALID);
				resp.setMessageKey(MessageKey.KEY_MOBILE_OTP_REQUIRED_INVALID);
				return resp;
			}
		}
		else
		{
			return initiateMotp(mobileNumber);
		}
		return null;
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/************* CHECK IF OTP IS ENABLED FOR THIS PERTICULAR USER **********/
	public AmxApiResponse<Validate, Object> isOtpEnabled(String civilId)
	{
		AmxApiResponse<Validate, Object> resp = new AmxApiResponse<Validate, Object>();
		boolean isOtpEnable = customerRegistrationDao.isOtpEnabled(civilId , userSession.getUserType());
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
			validate.setContactUsHelpLineNumber(metaData.getContactUsHelpLineNumber());
			validate.setContactUsEmail(metaData.getContactUsEmail());
			resp.setData(validate);
		}
		return resp;
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/************* MAINTAIN COUNT OF OTP SENT BY USRE IN A DAY **********/
	public AmxApiResponse<Validate, Object> setOtpCount(String civilId)
	{
		AmxApiResponse<Validate, Object> resp = new AmxApiResponse<Validate, Object>();

		Validate setOtpCount = customerRegistrationDao.setOtpCount(civilId , userSession.getUserType());

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

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/************* CHECK IF CIVIL ID EXIST **********/
	public AmxApiResponse<Validate, Object> isCivilIdExist(String civilid)
	{
		boolean civilIdExistCheck = customerRegistrationDao.isCivilIdExist(civilid , userSession.getUserType());
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

	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/************* SEND SMS TO SLACK **********/
	public void sendToSlack(String channel, String to, String prefix, String otp)
	{
		Notipy msg = new Notipy();
		msg.setMessage(String.format("%s = %s", channel, to));
		msg.addLine(String.format("OTP = %s-%s", prefix, otp));
		msg.setChannel(Channel.NOTIPY);
		try
		{
			postManClient.notifySlack(msg);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	/************* GET CUSTOMER NAME FROM CIVIL ID **********/
	
	public String customerName()
	{
		String civilId = userSession.getCivilId();
		if (null != civilId && !civilId.equals(""))
		{
			CustomerDetailModel customerDetailModel = customerRegistrationDao.getUserDetails(userSession.getCivilId() , userSession.getUserType() , userSession.getUserSequenceNumber());
			if (null != customerDetailModel.getUserName() && !customerDetailModel.getUserName().equals(""))
			{
				return customerDetailModel.getUserName();
			}
		}
		return "Customer";
	}
}
