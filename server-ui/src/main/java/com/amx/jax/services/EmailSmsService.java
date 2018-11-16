package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.amx.jax.models.PaymentReceipt;
import com.amx.jax.models.PaymentStatus;
import com.amx.jax.models.RequestOtpModel;
import com.amx.jax.models.ResponseOtpModel;
import com.amx.jax.models.ResponseInfo;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.Notipy.Channel;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesIB;
import com.amx.jax.ui.session.UserSession;
import com.amx.utils.Random;

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
	
	@Autowired
	private PostManService postManService;
	
	@Autowired
	PayMentService payMentService;
	

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
	public String sendEmailOtp(String EmailTo ,String otpType)
	{
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
		model.put(DetailsConstants.COMPANY_NAME, getCompanyName());
		model.put(DetailsConstants.COUNTRY_NAME, "KUWAIT");
		
		if(otpType.equalsIgnoreCase(DetailsConstants.REGISTRATION_OTP))
		{
			model.put(DetailsConstants.PROCESS, "registration process");
			model.put(DetailsConstants.OTP_USED_FOR, "registration");
		}
		else if(otpType.equalsIgnoreCase(DetailsConstants.RESET_PASSOWRD_OTP))
		{
			model.put(DetailsConstants.PROCESS, "reset password process");
			model.put(DetailsConstants.OTP_USED_FOR, "password reset");
		}
		else if(otpType.equalsIgnoreCase(DetailsConstants.UPDATE_PROFILE_OTP))
		{
			model.put(DetailsConstants.PROCESS, "update profile");
			model.put(DetailsConstants.OTP_USED_FOR, "updating details");
		}
		else
		{
			model.put(DetailsConstants.PROCESS, "");
			model.put(DetailsConstants.OTP_USED_FOR, "");
		}
		
		wrapper.put("data", model);
		
		ArrayList<String> emailTo = new ArrayList<String>();
		emailTo.add(EmailTo);

		Email email = new Email();
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

		logger.info(TAG + " emailTosuccessFullUserRegistration :: civilId :" + userSession.getCivilId());

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(DetailsConstants.CUSTOMER_NAME, customerName());
		model.put(DetailsConstants.CONTACT_US_EMAIL, metaData.getContactUsEmail());
		model.put(DetailsConstants.AMIB_WEBSITE_LINK, metaData.getAmibWebsiteLink());
		model.put(DetailsConstants.CONTACT_US_EMAIL, metaData.getContactUsEmail());
		model.put(DetailsConstants.COMPANY_NAME, getCompanyName());
		model.put(DetailsConstants.COUNTRY_NAME, "KUWAIT");
		wrapper.put("data", model);
		
		ArrayList<String> emailTo = new ArrayList<String>();
		emailTo.add(emailIdTo);

		Email email = new Email();
		//email.setFrom(emailIdFrom);
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
		//String emailIdFrom = metaData.getEmailFromConfigured();

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(DetailsConstants.CUSTOMER_NAME, customerName());
		model.put(DetailsConstants.CUSTOMER_CIVIL_ID, requestOtpModel.getCivilId());
		model.put(DetailsConstants.CUSTOMER_EMAIL_ID, requestOtpModel.getEmailId());
		model.put(DetailsConstants.CUSTOMER_MOBILE_NO, requestOtpModel.getMobileNumber());
		model.put(DetailsConstants.CONTACT_US_EMAIL, metaData.getContactUsEmail());
		model.put(DetailsConstants.AMIB_WEBSITE_LINK, metaData.getAmibWebsiteLink());
		model.put(DetailsConstants.COMPANY_NAME, getCompanyName());
		model.put(DetailsConstants.COUNTRY_NAME, "KUWAIT");
		wrapper.put("data", model);
		
		ArrayList<String> emailTo = new ArrayList<String>();
		emailTo.add(emailIdTo);

		Email email = new Email();
		//email.setFrom(emailIdFrom);
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
		
		//String emailIdFrom = metaData.getEmailFromConfigured();
		String customerEmailId = userSession.getCustomerEmailId();
		String customerMobileNumber = userSession.getCustomerMobileNumber();
		String civilId = userSession.getCivilId();

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(DetailsConstants.CUSTOMER_NAME, customerName());
		model.put(DetailsConstants.CUSTOMER_CIVIL_ID, civilId);
		model.put(DetailsConstants.CUSTOMER_EMAIL_ID, customerEmailId);
		model.put(DetailsConstants.CUSTOMER_MOBILE_NO, customerMobileNumber);
		model.put(DetailsConstants.CONTACT_US_EMAIL, metaData.getContactUsEmail());
		model.put(DetailsConstants.CONTACT_US_MOBILE, metaData.getContactUsHelpLineNumber());
		model.put(DetailsConstants.AMIB_WEBSITE_LINK, metaData.getAmibWebsiteLink());
		model.put(DetailsConstants.COMPANY_NAME, getCompanyName());
		model.put(DetailsConstants.COUNTRY_NAME, "KUWAIT");
		model.put(DetailsConstants.MAKE_DESC, makeDesc);
		model.put(DetailsConstants.SUB_MAKE_DESC, subMakeDesc);
		model.put(DetailsConstants.APPLICATION_ID, appSeqNumber);
		model.put(DetailsConstants.URL_DETAILS, "");
		
		wrapper.put("data", model);
		
		ArrayList<String> emailTo = new ArrayList<String>();
		emailTo.add(customerEmailId);
		
		Email email = new Email();
		//email.setFrom(emailIdFrom);
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
		
		String customerEmailId = userSession.getCustomerEmailId();
		String customerMobileNumber = userSession.getCustomerMobileNumber();
		String amibEmailId = metaData.getContactUsEmail();
		String civilId = userSession.getCivilId();

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(DetailsConstants.CUSTOMER_NAME, customerName());
		model.put(DetailsConstants.CUSTOMER_CIVIL_ID, civilId);
		model.put(DetailsConstants.CUSTOMER_EMAIL_ID, customerEmailId);
		model.put(DetailsConstants.CUSTOMER_MOBILE_NO, customerMobileNumber);
		model.put(DetailsConstants.CONTACT_US_EMAIL, metaData.getContactUsEmail());
		model.put(DetailsConstants.CONTACT_US_MOBILE, metaData.getContactUsHelpLineNumber());
		model.put(DetailsConstants.AMIB_WEBSITE_LINK, metaData.getAmibWebsiteLink());
		model.put(DetailsConstants.COMPANY_NAME, getCompanyName());
		model.put(DetailsConstants.COUNTRY_NAME, "KUWAIT");
		model.put(DetailsConstants.MAKE_DESC, makeDesc);
		model.put(DetailsConstants.SUB_MAKE_DESC, subMakeDesc);
		model.put(DetailsConstants.APPLICATION_ID, appSeqNumber);
		model.put(DetailsConstants.URL_DETAILS, "");
		wrapper.put("data", model);
		
		
		ArrayList<String> emailTo = new ArrayList<String>();
		emailTo.add(amibEmailId);

		Email email = new Email();
		email.setTo(emailTo);
		email.setSubject("Customer Quote Request - "+appSeqNumber);
		email.setModel(wrapper);
		email.setITemplate(TemplatesIB.QUOTE_SUBMIT_EMAIL_TO_AMIB);
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
	 * EMAIL TO CUSTOMER AFTER SUCCESSFULL PG TRANSACTION
	 ********/
	public void emialToCustonSuccessPg(BigDecimal amount , String transecionId , BigDecimal policyAppNo ,ArrayList<Map> receiptData)
	{
		logger.info(TAG + " emailToCustomerAfterSuccessPg :: amount  :" + amount);
		String customerEmailId = userSession.getCustomerEmailId();
		String customerMobileNumber = userSession.getCustomerMobileNumber();
		String civilId = userSession.getCivilId();

		Map<String, Object> wrapper = new HashMap<String, Object>();
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(DetailsConstants.CUSTOMER_NAME, customerName());
		model.put(DetailsConstants.CUSTOMER_CIVIL_ID, civilId);
		model.put(DetailsConstants.CUSTOMER_EMAIL_ID, customerEmailId);
		model.put(DetailsConstants.CUSTOMER_MOBILE_NO, customerMobileNumber);
		model.put(DetailsConstants.CONTACT_US_EMAIL, metaData.getContactUsEmail());
		model.put(DetailsConstants.CONTACT_US_MOBILE, metaData.getContactUsHelpLineNumber());
		model.put(DetailsConstants.AMIB_WEBSITE_LINK, metaData.getAmibWebsiteLink());
		model.put(DetailsConstants.COMPANY_NAME, getCompanyName());
		model.put(DetailsConstants.COUNTRY_NAME, "KUWAIT");
		model.put(DetailsConstants.URL_DETAILS, "");//TODO
		model.put(DetailsConstants.POLICY_AMOUNT, amount);
		model.put(DetailsConstants.TRANSACTION_ID, transecionId);
		model.put(DetailsConstants.POLICY_APP_NO, policyAppNo);
		wrapper.put("data", model);
		
		ArrayList<String> emailTo = new ArrayList<String>();
		emailTo.add(customerEmailId);

		File file = new File();
		file.setITemplate(TemplatesIB.TRNX_RECEIPT);
		file.setType(File.Type.PDF);
		file.getModel().put("results", receiptData);
		
		Email email = new Email();
		email.setTo(emailTo);
		email.setSubject("Al Mulla Insurance Brokerage Payment Success");
		email.setModel(wrapper);
		email.setITemplate(TemplatesIB.KNET_SUCCESS_EMAIL);
		email.setHtml(true);
		email.addFile(file);
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
	 * FAILED PROCEDURE WHILE POST PG
	 ********/
	public void failedPGProcedureAfterCapture(PaymentStatus paymentStatus, String messageKey , String message , String type , String paySeqNum)
	{
		logger.info(TAG + " emailToCustomerAndAmib :: paymentStatus :" + paymentStatus.toString());
		logger.info(TAG + " emailToCustomerAndAmib :: messageKey :" + messageKey);
		logger.info(TAG + " emailToCustomerAndAmib :: message :" + message);
		logger.info(TAG + " emailToCustomerAndAmib :: type :" + type);
		
		String emailIdAshokSir = "ashok.kalal@almullaexchange.com";
		String amibEmailId = metaData.getContactUsEmail();
		String civilId = userSession.getCivilId();
		
		ArrayList<String> emailTo = new ArrayList<String>();
		emailTo.add(amibEmailId);
		emailTo.add(emailIdAshokSir);
		
		StringBuffer sb = new StringBuffer();
		sb.append("\n");
		sb.append("\n USER PAYMENT GATEWAY INFO :");
		sb.append("\n");
		sb.append("\n CIVIL ID :: " + civilId);
		sb.append("\n");
		sb.append("\n PROCEDURE FAILED :: " + type);
		sb.append("\n");
		sb.append("\n MESSAGE :: " + message);
		sb.append("\n");
		sb.append("\n MESSAGE KEY :: "+ messageKey);
		sb.append("\n");
		sb.append("\n APP SEQUENCE NUMBER :: "+ paymentStatus.getAppSeqNumber());
		sb.append("\n");
		sb.append("\n PAY ID :: "+ paymentStatus.getPayId());
		sb.append("\n");
		sb.append("\n PAYMENT SEQUENCE NUMBER :: "+ paySeqNum);
		sb.append("\n");
		String mailData = sb.toString();

		Email email = new Email();
		email.setTo(emailTo);
		email.setSubject("INSURANCE FAILED PROCEDURE POST PG");
		email.setHtml(false);
		email.setLang(Language.EN);//TODO : LANGUAGE IS PASSED HARD CODED HERE NEED TO CONFIGURE
		email.setMessage(mailData);
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
	public AmxApiResponse<?, Object> initiateDotp(String emailId, String mobileNumber , String otpType)
	{
		userSession.setEotp("");
		userSession.setMotp("");
		userSession.setmOtpMobileNumber("");
		userSession.seteOtpEmailId("");

		AmxApiResponse<ResponseInfo, Object> civilIdExistCheck = isCivilIdExist(userSession.getCivilId());
		AmxApiResponse<ResponseInfo, Object> isOtpEnabled = isOtpEnabled(userSession.getCivilId());
		if (isOtpEnabled.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE) && civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			return isOtpEnabled;
		}

		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();

		String eOtpPrefix = sendEmailOtp(emailId , otpType);
		String mOtpPrefix = sendMobileOtp(mobileNumber);

		responseOtpModel.setEotpPrefix(eOtpPrefix);
		responseOtpModel.setMotpPrefix(mOtpPrefix);
		userSession.setmOtpMobileNumber(mobileNumber);
		userSession.seteOtpEmailId(emailId);
		resp.setMeta(responseOtpModel);
		resp.setStatusKey(MessageKey.KEY_EMAIL_MOBILE_OTP_REQUIRED);
		resp.setMessageKey(MessageKey.KEY_EMAIL_MOBILE_OTP_REQUIRED);

		AmxApiResponse<ResponseInfo, Object> setOtpCount = setOtpCount(userSession.getCivilId());
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
	public AmxApiResponse<?, Object> validateDOTP(String eOtp, String mOtp, String emailId, String mobileNumber,String otpType)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();

		if (null != mOtp && !mOtp.equals("") && null != eOtp && !eOtp.equals(""))
		{
			if (!userSession.geteOtpEmailId().equals(emailId) || !userSession.getmOtpMobileNumber().equals(mobileNumber))
			{
				return initiateDotp(emailId, mobileNumber , otpType);
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
			return initiateDotp(emailId, mobileNumber , otpType);
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
	public AmxApiResponse<?, Object> initiateEotp(String emailId , String otpType)
	{
		userSession.setEotp("");
		userSession.setMotp("");
		userSession.setmOtpMobileNumber("");
		userSession.seteOtpEmailId("");

		AmxApiResponse<ResponseInfo, Object> civilIdExistCheck = isCivilIdExist(userSession.getCivilId());
		AmxApiResponse<ResponseInfo, Object> isOtpEnabled = isOtpEnabled(userSession.getCivilId());
		if (isOtpEnabled.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE) && civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			return isOtpEnabled;
		}

		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();
		String eOtpPrefix = sendEmailOtp(emailId , otpType);
		responseOtpModel.setEotpPrefix(eOtpPrefix);
		responseOtpModel.setMotpPrefix(null);
		userSession.seteOtpEmailId(emailId);
		resp.setMeta(responseOtpModel);
		resp.setStatusKey(MessageKey.KEY_EMAIL_OTP_REQUIRED);
		resp.setMessageKey(MessageKey.KEY_EMAIL_OTP_REQUIRED);

		AmxApiResponse<ResponseInfo, Object> setOtpCount = setOtpCount(userSession.getCivilId());
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
	public AmxApiResponse<?, Object> validateEotp(String eOtp, String emailId , String otpType)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		if (null != eOtp && !eOtp.equals(""))
		{
			if (!userSession.geteOtpEmailId().equals(emailId))
			{
				return initiateEotp(emailId , otpType);
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
			return initiateEotp(emailId , otpType);
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

		AmxApiResponse<ResponseInfo, Object> civilIdExistCheck = isCivilIdExist(userSession.getCivilId());
		AmxApiResponse<ResponseInfo, Object> isOtpEnabled = isOtpEnabled(userSession.getCivilId());
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

		AmxApiResponse<ResponseInfo, Object> setOtpCount = setOtpCount(userSession.getCivilId());
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
	public AmxApiResponse<ResponseInfo, Object> isOtpEnabled(String civilId)
	{
		AmxApiResponse<ResponseInfo, Object> resp = new AmxApiResponse<ResponseInfo, Object>();
		boolean isOtpEnable = customerRegistrationDao.isOtpEnabled(civilId , metaData.getUserType());
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
			ResponseInfo validate = new ResponseInfo();
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
	public AmxApiResponse<ResponseInfo, Object> setOtpCount(String civilId)
	{
		AmxApiResponse<ResponseInfo, Object> resp = new AmxApiResponse<ResponseInfo, Object>();

		ResponseInfo setOtpCount = customerRegistrationDao.setOtpCount(civilId , metaData.getUserType());

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
	public AmxApiResponse<ResponseInfo, Object> isCivilIdExist(String civilid)
	{
		boolean civilIdExistCheck = customerRegistrationDao.isCivilIdExist(civilid , metaData.getUserType());
		AmxApiResponse<ResponseInfo, Object> resp = new AmxApiResponse<ResponseInfo, Object>();
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
			CustomerDetailModel customerDetailModel = customerRegistrationDao.getUserDetails(userSession.getCivilId() , metaData.getUserType() , userSession.getUserSequenceNumber());
			if (null != customerDetailModel.getUserName() && !customerDetailModel.getUserName().equals(""))
			{
				return customerDetailModel.getUserName();
			}
		}
		return "Customer";
	}
	
	public String getCompanyName()
	{
		if(null != metaData.getCompanyName() && metaData.getCompanyName().toString().equals(""))
		{
			return metaData.getCompanyName().toLowerCase();
		}
		return "";
	}
}
