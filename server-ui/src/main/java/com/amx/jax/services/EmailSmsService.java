package com.amx.jax.services;

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
import com.amx.jax.models.MetaData;
import com.amx.jax.models.RegSession;
import com.amx.jax.models.RequestOtpModel;
import com.amx.jax.models.ResponseOtpModel;
import com.amx.jax.models.Validate;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.Notipy.Channel;
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
	RegSession regSession;

	@Autowired
	MetaData metaData;

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
		String emailFrom = regSession.getEmailFromConfigured();

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

		Map<String, Object> model = new HashMap<String, Object>();
		model.put(DetailsConstants.CUSTOMER_NAME, "Customer");
		model.put(DetailsConstants.CONTACT_US_EMAIL, metaData.getContactUsEmail());
		model.put(DetailsConstants.EMAIL_OTP, emailOtpToSend);

		ArrayList<String> emailTo = new ArrayList<String>();
		emailTo.add(EmailTo);

		Email email = new Email();
		email.setFrom(emailFrom);
		email.setTo(emailTo);
		email.setSubject(DetailsConstants.REG_OTP_EMAIL_SUBJECT);
		email.setModel(model);

		email.setMessage("Al Mulla Insurance One Time OTP : " + emailOtpToSend);
		email.setHtml(false);

		// email.setITemplate(TemplatesIB.REG_EMAIL_OTP);
		// email.setHtml(true);

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
		String mobileWithCode = mobileNumber;
		String mobileOtpPrefix = Random.randomAlpha(3);
		String mobileOtp = Random.randomNumeric(6);
		String mobileOtpToSend = mobileOtpPrefix + "-" + mobileOtp;
		responseOtpModel.setEotpPrefix(null);
		responseOtpModel.setMotpPrefix(mobileOtpPrefix);
		regSession.setMotpPrefix(mobileOtpPrefix);
		regSession.setMotp(mobileOtp);
		metaData.setMotpPrefix(mobileOtpPrefix);
		metaData.setMotp(mobileOtp);

		System.out.println(TAG + " sendMobileOtp :: mobileOtpPrefix  :" + mobileOtpPrefix);
		System.out.println(TAG + " sendMobileOtp :: mobileOtp        :" + mobileOtp);

		try
		{
			SMS sms = new SMS();
			sms.addTo(mobileWithCode);
			sms.getModel().put(DetailsConstants.MOBILE_OTP, mobileOtpToSend);
			sms.setITemplate(TemplatesIB.REG_MOBILE_OTP);

			System.out.println(TAG + " sendMobileOtp :: !appConfig.isProdMode()        :" + !appConfig.isProdMode());

			if (!appConfig.isProdMode())
			{
				System.out.println(TAG + " sendMobileOtp :: To Slack ");
				sendToSlack("mobile", sms.getTo().get(0), mobileOtpPrefix, mobileOtp);
			}
			else
			{
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
	public void emailTosuccessFullUserRegistration()
	{

		String emailIdTo = regSession.getCustomerEmailId().toString();
		String emailIdFrom = regSession.getEmailFromConfigured();

		Map<String, Object> model = new HashMap<String, Object>();
		model.put(DetailsConstants.CUSTOMER_NAME, "Customer");
		model.put(DetailsConstants.CONTACT_US_EMAIL, regSession.getContactUsEmail());
		model.put(DetailsConstants.AMIB_WEBSITE_LINK, metaData.getAmibWebsiteLink());
		model.put(DetailsConstants.CONTACT_US_EMAIL, metaData.getContactUsEmail());

		ArrayList<String> emailTo = new ArrayList<String>();
		emailTo.add(emailIdTo);

		Email email = new Email();
		email.setFrom(emailIdFrom);
		email.setTo(emailTo);
		email.setSubject(DetailsConstants.REG_SUCCESS_EMAIL);
		email.setModel(model);

		email.setMessage("Al Mulla Insurance Registration Completed Successfully.");
		email.setHtml(false);

		// email.setITemplate(TemplatesIB.REG_SUCCESS_MAIL);
		// email.setHtml(true);

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
		String emailIdTo = regSession.getContactUsEmail();
		String emailIdFrom = regSession.getEmailFromConfigured();

		Map<String, Object> model = new HashMap<String, Object>();
		model.put(DetailsConstants.CUSTOMER_CIVIL_ID, requestOtpModel.getCivilId());
		model.put(DetailsConstants.CUSTOMER_EMAIL_ID, requestOtpModel.getEmailId());
		model.put(DetailsConstants.CUSTOMER_MOBILE_NO, requestOtpModel.getMobileNumber());

		ArrayList<String> emailTo = new ArrayList<String>();
		emailTo.add(emailIdTo);

		Email email = new Email();
		email.setFrom(emailIdFrom);
		email.setTo(emailTo);
		email.setSubject(DetailsConstants.FAILURE_REG_EMAIL_SUBJECT);
		email.setModel(model);

		StringBuffer sb = new StringBuffer();
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
		
		email.setHtml(false);
		email.setMessage(sb.toString());

		//email.setITemplate(TemplatesIB.REG_FAILED_EMAIL);
		//email.setHtml(true);

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
	 * REQUEST QUOTE SUBMIT SUCCESS MAIL TO USER AND AMIB
	 ********/
	public void emailToCustomerAndAmib()
	{
		String emailIdFrom = metaData.getEmailFromConfigured();
		String customerEmailId = metaData.getCustomerEmailId();
		String customerMobileNumber = metaData.getCustomerMobileNumber();
		String amibEmailId = metaData.getContactUsEmail();
		String civilId = metaData.getCivilId();

		Map<String, Object> model = new HashMap<String, Object>();
		model.put(DetailsConstants.CUSTOMER_CIVIL_ID, civilId);
		model.put(DetailsConstants.CUSTOMER_EMAIL_ID, customerEmailId);
		model.put(DetailsConstants.CUSTOMER_MOBILE_NO, customerMobileNumber);

		ArrayList<String> emailTo = new ArrayList<String>();
		emailTo.add(customerEmailId);
		emailTo.add(amibEmailId);

		Email email = new Email();
		email.setFrom(emailIdFrom);
		email.setTo(emailTo);
		email.setSubject(DetailsConstants.FAILURE_REG_EMAIL_SUBJECT);
		email.setModel(model);
		
		email.setSubject("AL Mulla Insurance Quotation Request : ");
		email.setHtml(false);
		email.setMessage("AL Mulla Insurance Application Request Quotation has Submited Successfully.");
		
		//email.setITemplate(TemplatesIB.REQ_QUOTE_SUBMITTED);
		//email.setHtml(true);
		
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

		String eOtpPrefix = sendEmailOtp(emailId);
		String mOtpPrefix = sendMobileOtp(mobileNumber);

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
			if (!metaData.geteOtpEmailId().equals(emailId) || !metaData.getmOtpMobileNumber().equals(mobileNumber))
			{
				return initiateDotp(emailId, mobileNumber);
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
		String eOtpPrefix = sendEmailOtp(emailId);
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
			if (!metaData.geteOtpEmailId().equals(emailId))
			{
				return initiateEotp(emailId);
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
		String mOtpPrefix = sendMobileOtp(mobileNumber);
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
			if (!metaData.getmOtpMobileNumber().equals(mobileNumber))
			{
				return initiateMotp(mobileNumber);
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

	public void sendToSlack(String channel, String to, String prefix, String otp)
	{
		System.out.println(TAG + " sendToSlack :: prefix :" + prefix);
		System.out.println(TAG + " sendToSlack :: otp    :" + otp);

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
			logger.error("error in SlackNotify", e);
		}
	}
}
