
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
import com.amx.jax.constants.DatabaseErrorKey;
import com.amx.jax.constants.DetailsConstants;
import com.amx.jax.constants.Message;
import com.amx.jax.constants.MessageKey;
import com.amx.jax.dao.CustomerRegistrationDao;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.models.ChangePasswordOtpRequest;
import com.amx.jax.models.ChangePasswordRequest;
import com.amx.jax.models.ChangePasswordResponse;
import com.amx.jax.models.CompanySetUp;
import com.amx.jax.models.CustomerDetailModel;
import com.amx.jax.models.CustomerDetailResponse;
import com.amx.jax.models.CustomerDetails;
import com.amx.jax.models.CustomerLoginModel;
import com.amx.jax.models.CustomerLoginRequest;
import com.amx.jax.models.CustomerLoginResponse;
import com.amx.jax.models.CustomerRegistrationModel;
import com.amx.jax.models.CustomerRegistrationRequest;
import com.amx.jax.models.CustomerRegistrationResponse;
import com.amx.jax.models.FailureException;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.RequestOtpModel;
import com.amx.jax.models.ResponseOtpModel;
import com.amx.jax.models.ResponseInfo;
import com.amx.jax.ui.session.UserSession;

@Service
public class CustomerRegistrationService
{
	String TAG = "com.amx.jax.services :: CustomerRegistrationService :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationService.class);

	@Autowired
	private MetaData metaData;
	
	@Autowired
	UserSession userSession;

	@Autowired
	private WebConfig webConfig;

	@Autowired
	private EmailSmsService emailSmsService;

	@Autowired
	CommonHttpRequest httpService;
	
	@Autowired
	private CustomerRegistrationDao customerRegistrationDao;
	
	public AmxApiResponse<CompanySetUp, Object> getCompanySetUp()
	{
		logger.info(TAG + " getCompanySetUp ::");
		
		AmxApiResponse<CompanySetUp, Object> resp = new AmxApiResponse<CompanySetUp, Object>();
		try
		{
			ArrayList<CompanySetUp> getCompanySetUp = customerRegistrationDao.getCompanySetUp(metaData.getLanguageId());
			
			metaData.setCountryId(getCompanySetUp.get(0).getCntryCd());
			metaData.setCompCd(getCompanySetUp.get(0).getCompCd());
			metaData.setContactUsHelpLineNumber(getCompanySetUp.get(0).getHelpLineNumber());
			metaData.setContactUsEmail(getCompanySetUp.get(0).getEmail());
			metaData.setAmibWebsiteLink(getCompanySetUp.get(0).getWebSite());
			metaData.setDecplc(getCompanySetUp.get(0).getDecimalPlaceUpTo());
			metaData.setCompanyName(getCompanySetUp.get(0).getCompanyName());
			metaData.setDeviceType("ONLINE");
			metaData.setDeviceId(httpService.getDeviceId());
			metaData.setCurrency(getCompanySetUp.get(0).getCurrency());
			
			userSession.setUserType("D");
			
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

	public AmxApiResponse<ResponseInfo, Object> isValidCivilId(String civilid)
	{
		boolean isValidCivilId = customerRegistrationDao.isValidCivilId(civilid);
		AmxApiResponse<ResponseInfo, Object> resp = new AmxApiResponse<ResponseInfo, Object>();
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

	public AmxApiResponse<ResponseInfo, Object> isCivilIdExist(String civilid)
	{
		boolean civilIdExistCheck = customerRegistrationDao.isCivilIdExist(civilid , userSession.getUserType());
		logger.info(TAG + " isCivilIdExist :: civilIdExistCheck :" + civilIdExistCheck);
		
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

	public AmxApiResponse<ResponseInfo, Object> isValidMobileNumber(String mobileNumber)
	{
		ResponseInfo isValidMobileNumber = customerRegistrationDao.isValidMobileNumber(mobileNumber);
		AmxApiResponse<ResponseInfo, Object> validMobileNumber = new AmxApiResponse<ResponseInfo, Object>();
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

	public AmxApiResponse<ResponseInfo, Object> isMobileNumberExist(String mobileNumber)
	{
		boolean mobileNumberExists = customerRegistrationDao.isMobileNumberExist(mobileNumber , userSession.getUserType());
		AmxApiResponse<ResponseInfo, Object> validMobileNumber = new AmxApiResponse<ResponseInfo, Object>();
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
			ResponseInfo validate = new ResponseInfo();
			validate.setContactUsHelpLineNumber(metaData.getContactUsHelpLineNumber());
			validate.setContactUsEmail(metaData.getContactUsEmail());
		}
		return validMobileNumber;
	}

	public AmxApiResponse<ResponseInfo, Object> isValidEmailId(String emailId)
	{
		AmxApiResponse<ResponseInfo, Object> resp = new AmxApiResponse<ResponseInfo, Object>();
		ResponseInfo validate = new ResponseInfo();
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

	public AmxApiResponse<ResponseInfo, Object> isEmailIdExist(String emailId)
	{
		boolean emailIdExistCheck = customerRegistrationDao.isEmailIdExist(emailId , userSession.getUserType());
		logger.info(TAG + " isCivilIdExist :: civilIdExistCheck :" + emailIdExistCheck);
		AmxApiResponse<ResponseInfo, Object> resp = new AmxApiResponse<ResponseInfo, Object>();
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
			ResponseInfo validate = new ResponseInfo();
			validate.setContactUsHelpLineNumber(metaData.getContactUsHelpLineNumber());
			validate.setContactUsEmail(metaData.getContactUsEmail());
		}
		return resp;
	}

	public AmxApiResponse<CustomerDetailResponse, Object> getCustomerDetails()
	{
		AmxApiResponse<CustomerDetailResponse, Object> resp = new AmxApiResponse<CustomerDetailResponse, Object>();
		CustomerDetailResponse customerDetailResponse = new CustomerDetailResponse();
		CustomerDetails customerDetails = new CustomerDetails();

		if (null != userSession.getCivilId() && !userSession.getCivilId().equals(""))
		{
			CustomerDetailModel customerDetailModel = customerRegistrationDao.getUserDetails(userSession.getCivilId() , userSession.getUserType() , userSession.getUserSequenceNumber());
			customerDetails.setCivilId(userSession.getCivilId());
			customerDetails.setCustSeqNumber(customerDetailModel.getCustSequenceNumber());
			customerDetails.setDeviceId(customerDetailModel.getDeviceId());
			customerDetails.setDeviceType(customerDetailModel.getDeviceType());
			customerDetails.setEmail(customerDetailModel.getEmail());
			customerDetails.setLanguageId(customerDetailModel.getLanguageId());
			customerDetails.setLastLogin(customerDetailModel.getLastLogin());
			customerDetails.setMailVerify(customerDetailModel.getMailVerify());
			customerDetails.setMobile(customerDetailModel.getMobile());
			customerDetails.setMobileVerify(customerDetailModel.getMobileVerify());
			customerDetails.setUserName(customerDetailModel.getUserName());

			userSession.setCustomerEmailId(customerDetailModel.getEmail());
			userSession.setCustomerSequenceNumber(customerDetailModel.getCustSequenceNumber());
			userSession.setCustomerMobileNumber(customerDetailModel.getMobile());

			customerDetailResponse.setCustomerDetails(customerDetails);
		}
		else
		{
			customerDetailResponse.setCustomerDetails(null);
		}

		ArrayList<CompanySetUp> getCompanySetUp = customerRegistrationDao.getCompanySetUp(metaData.getLanguageId());
		CompanySetUp companySetUp = getCompanySetUp.get(0);

		customerDetailResponse.setCompanySetUp(companySetUp);

		resp.setData(customerDetailResponse);
		return resp;
	}

	public AmxApiResponse<ResponseInfo, Object> isCivilIdExistCheck(String civilid)
	{
		AmxApiResponse<ResponseInfo, Object> validateCivilID = isValidCivilId(civilid);

		if (validateCivilID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return validateCivilID;
		}
		return isCivilIdExist(civilid);
	}

	public AmxApiResponse<ResponseInfo, Object> isMobileNumberExistCheck(String mobilenumber)
	{
		AmxApiResponse<ResponseInfo, Object> isValidMobileNumber = isValidMobileNumber(mobilenumber);

		if (isValidMobileNumber.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return isValidMobileNumber;
		}
		return isMobileNumberExist(mobilenumber);
	}

	public AmxApiResponse<ResponseInfo, Object> isEmailIdExistCheck(String emailId)
	{
		AmxApiResponse<ResponseInfo, Object> isValidEmailId = isValidEmailId(emailId);

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
			
			userSession.setCivilId(requestOtpModel.getCivilId());
			userSession.setCustomerEmailId(requestOtpModel.getEmailId());
			userSession.setCustomerMobileNumber(requestOtpModel.getMobileNumber());

			AmxApiResponse<?, Object> validateDOTP = emailSmsService.validateDOTP(eOtp, mOtp, requestOtpModel.getEmailId(), requestOtpModel.getMobileNumber());
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
		AmxApiResponse<CustomerRegistrationResponse, Object> resp = new AmxApiResponse<CustomerRegistrationResponse, Object>();
		CustomerRegistrationResponse customerRegistrationResponse = new CustomerRegistrationResponse();
		CustomerRegistrationModel customerRegistrationModel = new CustomerRegistrationModel();
		customerRegistrationModel.setPassword(userRegistartionRequest.getPassword());
		customerRegistrationModel.setCountryId(metaData.getCountryId());
		customerRegistrationModel.setCompCd(metaData.getCompCd());
		customerRegistrationModel.setUserType(userSession.getUserType());
		customerRegistrationModel.setMobile(userSession.getCustomerMobileNumber());
		customerRegistrationModel.setEmail(userSession.getCustomerEmailId());
		customerRegistrationModel.setLanguageId(metaData.getLanguageId());
		customerRegistrationModel.setCivilId(userSession.getCivilId());
		customerRegistrationModel.setCreatedDeviceId(metaData.getDeviceId());
		customerRegistrationModel.setDeviceType(metaData.getDeviceType());
		customerRegistrationModel = customerRegistrationDao.addNewCustomer(customerRegistrationModel);

		if (customerRegistrationModel.getStatus())
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			emailSmsService.emailTosuccessFullUserRegistration(userSession.getCivilId());
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
		AmxApiResponse<ResponseInfo, Object> validateCivilID = isValidCivilId(customerLoginRequest.getCivilId());
		AmxApiResponse<ResponseInfo, Object> civilIdExistCheck = isCivilIdExist(customerLoginRequest.getCivilId());

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
		customerLoginModel = customerRegistrationDao.validateUserLogin(customerLoginModel , userSession.getUserType());

		if (customerLoginModel.getStatus())
		{

			onSuccessLogin(customerLoginRequest, customerLoginModel);
			getCustomerDetails();
			resp.setStatusKey(ApiConstants.SUCCESS);
			userSession.authorize(customerLoginRequest.getCivilId(), customerLoginRequest.getPassword());
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
			customerLoginResponse.setContactUsHelpLineNumber(metaData.getContactUsHelpLineNumber());
			customerLoginResponse.setContactUsEmail(metaData.getContactUsEmail());
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
		userSession.setCivilId(customerLoginRequest.getCivilId());
		userSession.setUserSequenceNumber(customerLoginModel.getUserSeqNum());
		userSession.setUserAmibCustRef(customerLoginModel.getAmibRef());
		userSession.setCivilId(customerLoginRequest.getCivilId());
	}

	public AmxApiResponse<?, Object> changePasswordOtpInitiate(String eOtp, String mOtp, ChangePasswordOtpRequest changePasswordOtpRequest)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();
		
		logger.info(TAG + " changePasswordOtpInitiate :: getCivilId :"+changePasswordOtpRequest.getCivilId());
		userSession.setCivilId(changePasswordOtpRequest.getCivilId());
		
		
		AmxApiResponse<ResponseInfo, Object> validateCivilID = isValidCivilId(changePasswordOtpRequest.getCivilId());
		if (validateCivilID.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return validateCivilID;
		}

		AmxApiResponse<ResponseInfo, Object> civilIdExistCheck = isCivilIdExist(changePasswordOtpRequest.getCivilId());
		logger.info(TAG + " changePasswordOtpInitiate :: civilIdExistCheck :"+civilIdExistCheck.getStatus());
		if (civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
		{
			return civilIdExistCheck;
		}

		CustomerDetailModel customerDetailModel = customerRegistrationDao.getUserDetails(changePasswordOtpRequest.getCivilId() , userSession.getUserType() , userSession.getUserSequenceNumber());
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

				AmxApiResponse<?, Object> validateDOTP = emailSmsService.validateDOTP(eOtp, mOtp, customerDetailModel.getEmail(), customerDetailModel.getMobile());
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

		CustomerDetailModel customerDetailModel = customerRegistrationDao.getUserDetails(userSession.getCivilId() , userSession.getUserType() , userSession.getUserSequenceNumber());
		if (null == customerDetailModel || customerDetailModel.getErrorCode() != null)
		{
			resp.setMessageKey(customerDetailModel.getErrorCode());
			resp.setMessage(customerDetailModel.getErrorMessage());
			resp.setStatusKey(ApiConstants.FAILURE);
			return resp;
		}
		else
		{
			AmxApiResponse<?, Object> validateDOTP = emailSmsService.validateDOTP(eOtp, mOtp, customerDetailModel.getEmail(), customerDetailModel.getMobile());
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

		customerDetailModel = customerRegistrationDao.updatePassword(customerDetailModel , userSession.getCivilId() , userSession.getUserType());

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
		customerDetailModel = customerRegistrationDao.updatePassword(customerDetailModel , userSession.getCivilId() , userSession.getUserType());
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
		FailureException failureException = new FailureException();
		failureException.setCivilId(requestOtpModel.getCivilId());
		failureException.setMobileNumber(requestOtpModel.getMobileNumber());
		failureException.setEmailId(requestOtpModel.getEmailId());
		failureException.setCountryId(metaData.getCountryId());
		failureException.setCompCd(metaData.getCompCd());
		failureException.setDeviceId(metaData.getDeviceId());
		failureException.setDeviceType(metaData.getDeviceType());
		failureException.setLanguageId(metaData.getLanguageId());
		failureException.setExceptionType("REGISTER");
		failureException.setUserType(userSession.getUserType());
		failureException.setExceptionMsg(exceptionMessage);

		emailSmsService.sendFailedRegEmail(requestOtpModel);

		customerRegistrationDao.setFailedException(type, failureException);

	}

	private AmxApiResponse<?, Object> validateForRegistration(RequestOtpModel requestOtpModel)
	{
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();

		AmxApiResponse<ResponseInfo, Object> validateCivilID = isValidCivilId(requestOtpModel.getCivilId());
		AmxApiResponse<ResponseInfo, Object> civilIdExistCheck = isCivilIdExist(requestOtpModel.getCivilId());
		AmxApiResponse<ResponseInfo, Object> isValidMobileNumber = isValidMobileNumber(requestOtpModel.getMobileNumber());
		AmxApiResponse<ResponseInfo, Object> mobileNumberExists = isMobileNumberExist(requestOtpModel.getMobileNumber());
		AmxApiResponse<ResponseInfo, Object> validateEmailID = isValidEmailId(requestOtpModel.getEmailId());
		AmxApiResponse<ResponseInfo, Object> emailIdExists = isEmailIdExist(requestOtpModel.getEmailId());

		try
		{
			if (null == requestOtpModel.getCivilId() || requestOtpModel.getCivilId().toString().equals(""))
			{
				requestOtpModel.setCivilId(userSession.getCivilId());
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
}
