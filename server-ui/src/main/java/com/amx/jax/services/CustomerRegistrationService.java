
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
import com.amx.jax.service.HttpService;
import com.insurance.model.RequestOtpModel;
import com.insurance.model.ResponseOtpModel;
import com.insurance.services.EmailService;
import com.insurance.services.OtpService;
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
	private OtpService otpService;

	@Autowired
	HttpService httpService;

	public AmxApiResponse<CompanySetUp, Object> getCompanySetUp()
	{
		AmxApiResponse<CompanySetUp, Object> resp = new AmxApiResponse<CompanySetUp, Object>();
		try
		{
			BigDecimal languageId;
			if (null != regSession.getLanguageId())
			{
				languageId = regSession.getLanguageId();
			}
			else
			{
				languageId = new BigDecimal(0);
			}

			ArrayList<CompanySetUp> getCompanySetUp = customerRegistrationDao.getCompanySetUp(languageId);

			regSession.setCountryId(getCompanySetUp.get(0).getCntryCd());
			regSession.setCompCd(getCompanySetUp.get(0).getCompCd());
			regSession.setContactUsEmail(getCompanySetUp.get(0).getEmail());
			regSession.setContactUsHelpLineNumber(getCompanySetUp.get(0).getHelpLineNumber());
			regSession.setUserType("D");
			regSession.setDeviceId("12345678");
			regSession.setDeviceType("ONLINE");
			regSession.setEmailFromConfigured(webConfig.getConfigEmail());

			metaData.setCountryId(regSession.getCountryId());
			metaData.setCompCd(regSession.getCompCd());
			metaData.setUserType(regSession.getUserType());
			metaData.setLanguageId(regSession.getLanguageId());
			metaData.setDeviceType(regSession.getDeviceType());
			metaData.setDeviceId(regSession.getDeviceId());
			metaData.setContactUsEmail(regSession.getContactUsEmail());
			metaData.setContactUsHelpLineNumber(regSession.getContactUsHelpLineNumber());
			metaData.setEmailFromConfigured(webConfig.getConfigEmail());

			resp.setResults(getCompanySetUp);
			resp.setStatusKey(ApiConstants.SUCCESS);
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

	public AmxApiResponse<?, Object> registrationOtp(String eOtp, String mOtp, RequestOtpModel requestOtpModel)
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

			AmxApiResponse<?, Object> validateDOTP = otpService.validateDOTP(eOtp, mOtp, requestOtpModel.getEmailId(), requestOtpModel.getMobileNumber());
			if (null != validateDOTP)
			{
				return validateDOTP;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setData(null);
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<CustomerRegistrationResponse, Object> addNewCustomer(CustomerRegistrationRequest userRegistartionRequest)
	{
		handleSession();

		AmxApiResponse<CustomerRegistrationResponse, Object> resp = new AmxApiResponse<CustomerRegistrationResponse, Object>();
		CustomerRegistrationResponse customerRegistrationResponse = new CustomerRegistrationResponse();
		CustomerRegistrationModel customerRegistrationModel = new CustomerRegistrationModel();
		customerRegistrationModel.setPassword(userRegistartionRequest.getPassword());
		customerRegistrationModel.setCountryId(regSession.getCountryId());
		customerRegistrationModel.setCompCd(regSession.getCompCd());
		customerRegistrationModel.setUserType(regSession.getUserType());
		customerRegistrationModel.setMobile(regSession.getMobileNumber());
		customerRegistrationModel.setEmail(regSession.getEmailId());
		customerRegistrationModel.setLanguageId(regSession.getLanguageId());
		customerRegistrationModel.setCivilId(regSession.getCivilId());
		customerRegistrationModel.setCreatedDeviceId(regSession.getDeviceId());
		customerRegistrationModel.setDeviceType(regSession.getDeviceType());

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
		handleSession();
		CustomerLoginResponse customerLoginResponse = new CustomerLoginResponse();
		CustomerLoginModel customerLoginModel = new CustomerLoginModel();
		AmxApiResponse<CustomerLoginResponse, Object> resp = new AmxApiResponse<CustomerLoginResponse, Object>();
		AmxApiResponse<Validate, Object> validateCivilID = isValidCivilId(customerLoginRequest.getCivilId());
		AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(customerLoginRequest.getCivilId());

		if (validateCivilID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return validateCivilID;
		}

		if (civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return civilIdExistCheck;
		}

		if (civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
		{
			if (null == customerLoginRequest.getPassword() || customerLoginRequest.getPassword().equals(""))
			{
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessage(Message.EMPTY_PASSWORD);
				resp.setMessageKey(MessageKey.KEY_EMPTY_PASSWORD);
				return resp;
			}
		}

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
		regSession.setCivilId(customerLoginRequest.getCivilId());
	}

	public AmxApiResponse<?, Object> changePasswordOtpInitiate(String eOtp, String mOtp, ChangePasswordOtpRequest changePasswordOtpRequest)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();

		if (null == changePasswordOtpRequest.getCivilId() || changePasswordOtpRequest.getCivilId().equals(""))
		{
			changePasswordOtpRequest.setCivilId(metaData.getCivilId());
		}
		regSession.setCivilId(changePasswordOtpRequest.getCivilId());

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

		CustomerDetailModel customerDetailModel = customerRegistrationDao.getUserDetails(changePasswordOtpRequest.getCivilId());

		if (null == customerDetailModel || customerDetailModel.getErrorCode() != null)
		{
			resp.setMessageKey(customerDetailModel.getErrorCode());
			resp.setMessage(customerDetailModel.getErrorMessage());
			resp.setStatusKey(ApiConstants.FAILURE);
			return resp;
		}
		else
		{
			try
			{

				AmxApiResponse<?, Object> validateDOTP = otpService.validateDOTP(eOtp, mOtp, customerDetailModel.getEmail(), customerDetailModel.getMobile());
				if (null != validateDOTP)
				{
					return validateDOTP;
				}

				resp.setStatusKey(ApiConstants.SUCCESS);

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

	public AmxApiResponse<?, Object> changePasswordLogedInUser(String eOtp, String mOtp, ChangePasswordRequest changePasswordRequest)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();

		CustomerDetailModel customerDetailModel = customerRegistrationDao.getUserDetails(metaData.getCivilId());
		if (null == customerDetailModel || customerDetailModel.getErrorCode() != null)
		{
			resp.setMessageKey(customerDetailModel.getErrorCode());
			resp.setMessage(customerDetailModel.getErrorMessage());
			resp.setStatusKey(ApiConstants.FAILURE);
			return resp;
		}
		else
		{
			AmxApiResponse<?, Object> validateDOTP = otpService.validateDOTP(eOtp, mOtp, customerDetailModel.getEmail(), customerDetailModel.getMobile());
			if (null != validateDOTP)
			{
				return validateDOTP;
			}

			return updatePassword(changePasswordRequest);
		}
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
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setData(null);
		}
		else
		{
			resp.setMessageKey(customerDetailModel.getErrorCode());
			resp.setError(customerDetailModel.getErrorMessage());
			resp.setStatusKey(ApiConstants.FAILURE);
			resp.setData(null);
		}
		return resp;
	}

	public AmxApiResponse<ChangePasswordResponse, Object> updateUserPassword(String eOtp, String mOtp, ChangePasswordRequest changePasswordRequest)
	{
		AmxApiResponse<ChangePasswordResponse, Object> resp = new AmxApiResponse<ChangePasswordResponse, Object>();
		CustomerDetailModel customerDetailModel = new CustomerDetailModel();
		customerDetailModel.setPassword(changePasswordRequest.getNewPassword());
		customerDetailModel = customerRegistrationDao.updatePassword(customerDetailModel);
		if (customerDetailModel.getStatus())
		{
			resp.setMessageKey(customerDetailModel.getErrorCode());
			resp.setError(customerDetailModel.getErrorMessage());
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setData(null);
		}
		else
		{
			resp.setMessageKey(customerDetailModel.getErrorCode());
			resp.setError(customerDetailModel.getErrorMessage());
			resp.setStatusKey(ApiConstants.FAILURE);
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

	private AmxApiResponse<?, Object> validateForRegistration(RequestOtpModel requestOtpModel)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();

		AmxApiResponse<Validate, Object> validateCivilID = isValidCivilId(requestOtpModel.getCivilId());
		AmxApiResponse<Validate, Object> civilIdExistCheck = isCivilIdExist(requestOtpModel.getCivilId());
		AmxApiResponse<Validate, Object> isValidMobileNumber = isValidMobileNumber(requestOtpModel.getMobileNumber());
		AmxApiResponse<Validate, Object> mobileNumberExists = isMobileNumberExist(requestOtpModel.getMobileNumber());
		AmxApiResponse<Validate, Object> validateEmailID = isValidEmailId(requestOtpModel.getEmailId());
		AmxApiResponse<Validate, Object> emailIdExists = isEmailIdExist(requestOtpModel.getEmailId());

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
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setData(null);
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
			return resp;
		}
		return null;
	}

	private void handleSession()
	{
		if (httpService.getLanguage().toString().equalsIgnoreCase("EN"))
		{
			regSession.setLanguageId(new BigDecimal(0));
			getCompanySetUp();
		}
	}

}
