
package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.WebConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.DatabaseErrorKey;
import com.amx.jax.constants.DetailsConstants;
import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.constants.Message;
import com.amx.jax.constants.MessageKey;
import com.amx.jax.dao.CustomerRegistrationDao;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.meta.IMetaService;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.ChangePasswordOtpRequest;
import com.amx.jax.models.ChangePasswordRequest;
import com.amx.jax.models.ChangePasswordResponse;
import com.amx.jax.models.CommonUtils;
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
import com.amx.jax.models.RequestOtpModel;
import com.amx.jax.models.ResponseInfo;
import com.amx.jax.models.ResponseOtpModel;
import com.amx.jax.ui.session.UserSession;

@Service
public class CustomerRegistrationService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationService.class);
	static String TAG = "CustomerRegistrationService :: ";

	@Autowired
	UserSession userSession;

	@Autowired
	private EmailSmsService emailSmsService;

	@Autowired
	CommonHttpRequest httpService;

	@Autowired
	private WebConfig webConfig;

	@Autowired
	IMetaService metaService;

	@Autowired
	private CustomerRegistrationDao customerRegistrationDao;

	public AmxApiResponse<CompanySetUp, Object> getCompanySetUp() 
	{
		AmxApiResponse<CompanySetUp, Object> resp = new AmxApiResponse<CompanySetUp, Object>();
		try {
			
			ArrayResponseModel arrayResponseModel = customerRegistrationDao.getCompanySetUp(userSession.getLanguageId(),webConfig.getAppCompCode());
			if (arrayResponseModel.getErrorCode() == null) {
				ArrayList<CompanySetUp> getCompanySetUp = arrayResponseModel.getDataArray();
				metaService.getTenantProfile().setCountryId(getCompanySetUp.get(0).getCntryCd());
				metaService.getTenantProfile().setCompCd(getCompanySetUp.get(0).getCompCd());
				metaService.getTenantProfile().setContactUsHelpLineNumber(getCompanySetUp.get(0).getHelpLineNumber());
				metaService.getTenantProfile().setContactUsEmail(getCompanySetUp.get(0).getEmail());
				metaService.getTenantProfile().setAmibWebsiteLink(getCompanySetUp.get(0).getWebSite());
				metaService.getTenantProfile().setDecplc(getCompanySetUp.get(0).getDecimalPlaceUpTo());
				metaService.getTenantProfile().setCompanyName(getCompanySetUp.get(0).getCompanyName());
				metaService.getTenantProfile().setCurrency(getCompanySetUp.get(0).getCurrency());
				resp.setResults(getCompanySetUp);
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			} else {
				resp.setMessage(arrayResponseModel.getErrorMessage());
				resp.setMessageKey(arrayResponseModel.getErrorCode());
			}
		} catch (Exception e) {
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "getCompanySetUp :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}

	public AmxApiResponse<ResponseInfo, Object> isValidCivilId(String civilid) {
		AmxApiResponse<ResponseInfo, Object> resp = new AmxApiResponse<ResponseInfo, Object>();
		try {
			ArrayResponseModel arrayResponseModel = customerRegistrationDao.isValidCivilId(civilid);
			if (null != arrayResponseModel.getErrorCode()
					&& arrayResponseModel.getErrorCode().equals(ApiConstants.ERROR_OCCURRED_ON_SERVER)) {
				resp.setMessage(arrayResponseModel.getErrorMessage());
				resp.setMessageKey(arrayResponseModel.getErrorCode());
				return resp;
			}
			
			if (arrayResponseModel.getData() == null) 
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				resp.setMessage(Message.CIVILID_VALID);
				resp.setMessageKey(WebAppStatusCodes.CIVIL_ID_VALID.toString());
			} else {
				resp.setStatusEnum(WebAppStatusCodes.CIVIL_ID_INVALID);
				resp.setMessage(Message.CIVILID_INVALID);
				resp.setMessageKey(WebAppStatusCodes.CIVIL_ID_INVALID.toString());
			}

			
		} catch (Exception e) {
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "isValidCivilId :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}

	public AmxApiResponse<ResponseInfo, Object> isCivilIdExist(String civilid) {

		AmxApiResponse<ResponseInfo, Object> resp = new AmxApiResponse<ResponseInfo, Object>();
		try {
			ArrayResponseModel arrayResponseModel = customerRegistrationDao.isCivilIdExist(civilid,
					HardCodedValues.USER_TYPE);
			if (null != arrayResponseModel.getErrorCode()
					&& arrayResponseModel.getErrorCode().equals(ApiConstants.ERROR_OCCURRED_ON_SERVER)) {
				resp.setMessage(arrayResponseModel.getErrorMessage());
				resp.setMessageKey(arrayResponseModel.getErrorCode());
				return resp;
			}
			
			if (arrayResponseModel.getData().equalsIgnoreCase("Y")) {
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				resp.setMessage(Message.CIVILID_ALREDAY_REGISTER);
				resp.setMessageKey(WebAppStatusCodes.CIVIL_ID_ALREADY_REGISTERED.toString());
			} else {
				resp.setStatusEnum(WebAppStatusCodes.CIVIL_ID_NOT_REGESTERED);
				resp.setMessage(Message.CIVILID_ALREDAY_NOT_REGISTER);
				resp.setMessageKey(WebAppStatusCodes.CIVIL_ID_NOT_REGESTERED.toString());
			}

			

		} catch (Exception e) {
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "isCivilIdExist :: exception :" + e);
			e.printStackTrace();
		}

		return resp;
	}

	public AmxApiResponse<ResponseInfo, Object> isValidMobileNumber(String mobileNumber) {
		AmxApiResponse<ResponseInfo, Object> validMobileNumber = new AmxApiResponse<ResponseInfo, Object>();

		try {
			if (null != mobileNumber && !mobileNumber.equals("")) {
				if (!CommonUtils.isNumeric(mobileNumber)) {
					validMobileNumber.setStatusEnum(WebAppStatusCodes.INVALID_MOBILE);
					validMobileNumber.setMessageKey(WebAppStatusCodes.INVALID_MOBILE.toString());
					return validMobileNumber;
				}
			}

			ResponseInfo isValidMobileNumber = customerRegistrationDao.isValidMobileNumber(mobileNumber);
			logger.info(TAG + "isValidMobileNumber :: getErrorCode :" + isValidMobileNumber.getErrorCode());
			
			
			if (isValidMobileNumber.getErrorCode() == null) {
				validMobileNumber.setStatusEnum(WebAppStatusCodes.SUCCESS);
			} else {
				validMobileNumber.setStatusKey(isValidMobileNumber.getErrorCode());
			}
			validMobileNumber.setMessage(isValidMobileNumber.getErrorMessage());
			validMobileNumber.setMessageKey(isValidMobileNumber.getErrorCode());
		} catch (Exception e) {
			validMobileNumber.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			validMobileNumber.setMessage(e.toString());
			logger.info(TAG + "isValidMobileNumber :: exception :" + e);
			e.printStackTrace();
		}
		return validMobileNumber;
	}

	public AmxApiResponse<ResponseInfo, Object> isMobileNumberExist(String mobileNumber) {

		AmxApiResponse<ResponseInfo, Object> validMobileNumber = new AmxApiResponse<ResponseInfo, Object>();

		try {
			ArrayResponseModel arrayResponseModel = customerRegistrationDao.isMobileNumberExist(mobileNumber,
					HardCodedValues.USER_TYPE);
			if (null != arrayResponseModel.getErrorCode()
					&& arrayResponseModel.getErrorCode().equals(ApiConstants.ERROR_OCCURRED_ON_SERVER)) {
				validMobileNumber.setMessage(arrayResponseModel.getErrorMessage());
				validMobileNumber.setMessageKey(arrayResponseModel.getErrorCode());
				return validMobileNumber;
			}
			
			if (null != arrayResponseModel.getData() && arrayResponseModel.getData().equalsIgnoreCase("Y")) {
				validMobileNumber.setStatusEnum(WebAppStatusCodes.SUCCESS);
				validMobileNumber.setMessage(Message.MOBILE_NO_ALREDAY_REGISTER);
				validMobileNumber.setMessageKey(WebAppStatusCodes.MOBILE_NUMBER_REGISTERED.toString());
			} else {
				validMobileNumber.setStatusEnum(WebAppStatusCodes.MOBILE_NUMBER_NOT_REGISTERED);
				validMobileNumber.setMessage(Message.MOBILE_NO_NOT_REGISTER);
				validMobileNumber.setMessageKey(WebAppStatusCodes.MOBILE_NUMBER_NOT_REGISTERED.toString());

				ResponseInfo validate = new ResponseInfo();
				validate.setContactUsHelpLineNumber(metaService.getTenantProfile().getContactUsHelpLineNumber());
				validate.setContactUsEmail(metaService.getTenantProfile().getContactUsEmail());
			}
		} catch (Exception e) {
			validMobileNumber.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			validMobileNumber.setMessage(e.toString());
			logger.info(TAG + "isMobileNumberExist :: exception :" + e);
			e.printStackTrace();
		}
		return validMobileNumber;
	}

	public AmxApiResponse<ResponseInfo, Object> isValidEmailId(String emailId) {
		AmxApiResponse<ResponseInfo, Object> resp = new AmxApiResponse<ResponseInfo, Object>();

		try 
		{
			ResponseInfo validate = new ResponseInfo();
			if (validateEmail(emailId)) {
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				resp.setMessage(Message.EMAIL_VALID);
				resp.setMessageKey(WebAppStatusCodes.EMAIL_ID_VALID.toString());
			} else {
				resp.setStatusEnum(WebAppStatusCodes.EMAIL_ID_INVALID);
				resp.setMessage(Message.EMAIL_INVALID);
				resp.setMessageKey(WebAppStatusCodes.EMAIL_ID_INVALID.toString());
			}
			resp.setData(validate);
		} catch (Exception e) {
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "isValidEmailId :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}

	public static boolean validateEmail(String emailStr) {
		Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}

	public AmxApiResponse<ResponseInfo, Object> isEmailIdExist(String emailId) {
		AmxApiResponse<ResponseInfo, Object> resp = new AmxApiResponse<ResponseInfo, Object>();

		try {
			ArrayResponseModel arrayResponseModel = customerRegistrationDao.isEmailIdExist(emailId,
					HardCodedValues.USER_TYPE);
			if (null != arrayResponseModel.getErrorCode()
					&& arrayResponseModel.getErrorCode().equals(ApiConstants.ERROR_OCCURRED_ON_SERVER)) {
				resp.setMessage(arrayResponseModel.getErrorMessage());
				resp.setMessageKey(arrayResponseModel.getErrorCode());
				return resp;
			}

			if (null != arrayResponseModel.getErrorCode() && arrayResponseModel.getErrorCode().equalsIgnoreCase("Y")) {
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				resp.setMessage(Message.EMAIL_ALREDAY_REGISTER);
				resp.setMessageKey(WebAppStatusCodes.EMAIL_ID_REGESTERED.toString());
			} else {
				resp.setStatusEnum(WebAppStatusCodes.EMAIL_ID_NOT_REGESTERED);
				resp.setMessage(Message.EMAIL_ALREDAY_NOT_REGISTER);
				resp.setMessageKey(WebAppStatusCodes.EMAIL_ID_NOT_REGESTERED.toString());

				ResponseInfo validate = new ResponseInfo();
				validate.setContactUsHelpLineNumber(metaService.getTenantProfile().getContactUsHelpLineNumber());
				validate.setContactUsEmail(metaService.getTenantProfile().getContactUsEmail());
				resp.setData(validate);
			}

			
		} catch (Exception e) {
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "isEmailIdExist :: exception :" + e);
			e.printStackTrace();
		}

		return resp;
	}

	public AmxApiResponse<CustomerDetailResponse, Object> getCustomerDetails() {
		AmxApiResponse<CustomerDetailResponse, Object> resp = new AmxApiResponse<CustomerDetailResponse, Object>();
		try {
			CustomerDetailResponse customerDetailResponse = new CustomerDetailResponse();
			CustomerDetails customerDetails = new CustomerDetails();

			if (null != userSession.getCivilId() && !userSession.getCivilId().equals("")) {
				CustomerDetailModel customerDetailModel = customerRegistrationDao.getUserDetails(
						userSession.getCivilId(), HardCodedValues.USER_TYPE, userSession.getUserSequenceNumber(),
						userSession.getLanguageId());

				if (customerDetailModel.getErrorCode() == null) {
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
				} else {
					resp.setMessage(customerDetailModel.getErrorMessage());
					resp.setMessageKey(customerDetailModel.getErrorCode());
					return resp;
				}
			} else {
				customerDetailResponse.setCustomerDetails(null);
			}

			ArrayResponseModel arrayResponseModel = customerRegistrationDao.getCompanySetUp(userSession.getLanguageId(),
					webConfig.getAppCompCode());
			if (arrayResponseModel.getErrorCode() == null) {
				ArrayList<CompanySetUp> getCompanySetUp = arrayResponseModel.getDataArray();
				
				CompanySetUp companySetUp = getCompanySetUp.get(0);
				customerDetailResponse.setCompanySetUp(companySetUp);

				resp.setData(customerDetailResponse);
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			} else {
				resp.setMessage(arrayResponseModel.getErrorMessage());
				resp.setMessageKey(arrayResponseModel.getErrorCode());
			}

			resp.setData(customerDetailResponse);
		} catch (Exception e) {
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "getCustomerDetails :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}

	public AmxApiResponse<ResponseInfo, Object> isCivilIdExistCheck(String civilid) {

		AmxApiResponse<ResponseInfo, Object> validateCivilID = isValidCivilId(civilid);
		if (!validateCivilID.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {
			return validateCivilID;
		}
		return isCivilIdExist(civilid);
	}

	public AmxApiResponse<ResponseInfo, Object> isMobileNumberExistCheck(String mobilenumber) {

		AmxApiResponse<ResponseInfo, Object> isValidMobileNumber = isValidMobileNumber(mobilenumber);
		if (!isValidMobileNumber.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {
			return isValidMobileNumber;
		}
		return isMobileNumberExist(mobilenumber);
	}

	public AmxApiResponse<ResponseInfo, Object> isEmailIdExistCheck(String emailId) {

		AmxApiResponse<ResponseInfo, Object> isValidEmailId = isValidEmailId(emailId);
		if (!isValidEmailId.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {
			return isValidEmailId;
		}
		return isEmailIdExist(emailId);
	}

	public AmxApiResponse<?, Object> registrationOtp(String eOtp, String mOtp, RequestOtpModel requestOtpModel) {
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();

		try {
			AmxApiResponse<?, Object> validateForRegistration = validateForRegistration(requestOtpModel);
			if (null != validateForRegistration) {
				return validateForRegistration;
			}
			userSession.setCivilId(requestOtpModel.getCivilId());
			userSession.setCustomerEmailId(requestOtpModel.getEmailId());
			userSession.setCustomerMobileNumber(requestOtpModel.getMobileNumber());

			AmxApiResponse<?, Object> validateDOTP = emailSmsService.validateDOTP(eOtp, mOtp,
					requestOtpModel.getEmailId(), requestOtpModel.getMobileNumber(), DetailsConstants.REGISTRATION_OTP);

			if (null != validateDOTP) {
				
				return validateDOTP;
			}
		} catch (Exception e) {
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "registrationOtp :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}

	public AmxApiResponse<CustomerRegistrationResponse, Object> addNewCustomer(
			CustomerRegistrationRequest userRegistartionRequest) {
		AmxApiResponse<CustomerRegistrationResponse, Object> resp = new AmxApiResponse<CustomerRegistrationResponse, Object>();

		try {

			if (null == userRegistartionRequest.getPassword() || userRegistartionRequest.getPassword().equals("")) {
				resp.setStatusEnum(WebAppStatusCodes.EMPTY_PASSWORD);
				resp.setMessage(Message.EMPTY_PASSWORD);
				resp.setMessageKey(WebAppStatusCodes.EMPTY_PASSWORD.toString());
				return resp;
			}

			if (null == userSession.getCustomerEmailId() || userSession.getCustomerEmailId().equals("")) {
				resp.setStatusEnum(WebAppStatusCodes.CUSTOMER_MOBILE_EMAIL_NOT_VALIDATED);
				resp.setMessage(Message.CUSTOMER_MOBILE_EMAIL_NOT_VALIDATED);
				resp.setMessageKey(WebAppStatusCodes.CUSTOMER_MOBILE_EMAIL_NOT_VALIDATED.toString());
				return resp;
			}

			CustomerRegistrationResponse customerRegistrationResponse = new CustomerRegistrationResponse();
			CustomerRegistrationModel customerRegistrationModel = new CustomerRegistrationModel();
			customerRegistrationModel.setPassword(userRegistartionRequest.getPassword());
			customerRegistrationModel.setCountryId(metaService.getTenantProfile().getCountryId());
			customerRegistrationModel.setCompCd(metaService.getTenantProfile().getCompCd());
			customerRegistrationModel.setUserType(HardCodedValues.USER_TYPE);
			customerRegistrationModel.setMobile(userSession.getCustomerMobileNumber());
			customerRegistrationModel.setEmail(userSession.getCustomerEmailId());
			customerRegistrationModel.setLanguageId(userSession.getLanguageId());
			customerRegistrationModel.setCivilId(userSession.getCivilId());
			customerRegistrationModel.setCreatedDeviceId(metaService.getUserDeviceInfo().getDeviceId());
			customerRegistrationModel.setDeviceType(metaService.getUserDeviceInfo().getDeviceType());

			customerRegistrationModel = customerRegistrationDao.addNewCustomer(customerRegistrationModel,
					userSession.getLanguageId());

			if (customerRegistrationModel.getErrorCode() == null) {
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				emailSmsService.emailTosuccessFullUserRegistration(userSession.getCustomerEmailId());
			} else {
				resp.setMessageKey(customerRegistrationModel.getErrorCode());
				resp.setMessage(customerRegistrationModel.getErrorMessage());
			}

			resp.setMessage(customerRegistrationModel.getErrorMessage());
			resp.setMessageKey(customerRegistrationModel.getErrorCode());
			customerRegistrationResponse.setCivilid(customerRegistrationModel.getCivilid());
			customerRegistrationResponse.setUserSequenceNumber(customerRegistrationModel.getUserSequenceNumber());
			resp.setData(customerRegistrationResponse);
		} catch (Exception e) {
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "addNewCustomer :: exception :" + e);
			e.printStackTrace();
		}

		return resp;

	}

	public AmxApiResponse<?, Object> validateUserLogin(CustomerLoginRequest customerLoginRequest) {
		AmxApiResponse<CustomerLoginResponse, Object> resp = new AmxApiResponse<CustomerLoginResponse, Object>();

		try {
			if (null == customerLoginRequest.getCivilId() || customerLoginRequest.getCivilId().equals("")) {
				resp.setStatusEnum(WebAppStatusCodes.EMPTY_CIVIL_ID);
				resp.setMessage(Message.EMPTY_CIVIL_ID);
				resp.setMessageKey(WebAppStatusCodes.EMPTY_CIVIL_ID.toString());
				return resp;
			}

			CustomerLoginResponse customerLoginResponse = new CustomerLoginResponse();
			CustomerLoginModel customerLoginModel = new CustomerLoginModel();
			AmxApiResponse<ResponseInfo, Object> validateCivilID = isValidCivilId(customerLoginRequest.getCivilId());
			AmxApiResponse<ResponseInfo, Object> civilIdExistCheck = isCivilIdExist(customerLoginRequest.getCivilId());

			if (!validateCivilID.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {
				return validateCivilID;
			}

			if (!civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {
				return civilIdExistCheck;
			}

			if (civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {
				if (null == customerLoginRequest.getPassword() || customerLoginRequest.getPassword().equals("")) {
					resp.setStatusEnum(WebAppStatusCodes.EMPTY_PASSWORD);
					resp.setMessageKey(WebAppStatusCodes.EMPTY_PASSWORD.toString());
					return resp;
				}
			}

			customerLoginModel.setCivilId(customerLoginRequest.getCivilId());
			customerLoginModel.setPassword(customerLoginRequest.getPassword());
			customerLoginModel = customerRegistrationDao.validateUserLogin(customerLoginModel,
					HardCodedValues.USER_TYPE, userSession.getLanguageId());

			if (customerLoginModel.getErrorCode() == null) {
				onSuccessLogin(customerLoginRequest, customerLoginModel);
				getCustomerDetails();
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				userSession.authorize(customerLoginRequest.getCivilId(), customerLoginRequest.getPassword());
				resp.setRedirectUrl(userSession.getReturnUrl());
			} else {
				resp.setStatusKey(customerLoginModel.getErrorCode());
			}

			if (null != customerLoginModel.getErrorMessage() && customerLoginModel.getErrorCode().toString()
					.equalsIgnoreCase(DatabaseErrorKey.INVALID_USER_LOGIN)) {
				String countData = "";
				int count = Integer.parseInt(customerLoginModel.getErrorMessage());
				int userInvalidCountRemaining = 3 - count;
				if (userInvalidCountRemaining == 0) {
					countData = " No ";
				} else {
					countData = "" + userInvalidCountRemaining;
				}
				resp.setMessageKey(customerLoginModel.getErrorCode());
				resp.setMessage(countData);
			} else if (null != customerLoginModel.getErrorCode()
					&& customerLoginModel.getErrorCode().equalsIgnoreCase(DatabaseErrorKey.USER_ACCOUNT_LOCK)) {
				logger.info("validateUserLogin :: Lock Account :");
				resp.setMessageKey(customerLoginModel.getErrorCode());
				resp.setMessage(customerLoginModel.getErrorMessage());
				customerLoginResponse
						.setContactUsHelpLineNumber(metaService.getTenantProfile().getContactUsHelpLineNumber());
				customerLoginResponse.setContactUsEmail(metaService.getTenantProfile().getContactUsEmail());
			} else {
				resp.setMessageKey(customerLoginModel.getErrorCode());
				resp.setMessage(customerLoginModel.getErrorMessage());
			}

			customerLoginResponse.setAmibRef(customerLoginModel.getAmibRef());
			customerLoginResponse.setUserSeqNum(customerLoginModel.getUserSeqNum());
			resp.setData(customerLoginResponse);
		} catch (Exception e) {
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "validateUserLogin :: exception :" + e);
			e.printStackTrace();
		}

		return resp;
	}

	public void onSuccessLogin(CustomerLoginRequest customerLoginRequest, CustomerLoginModel customerLoginModel) 
	{
		userSession.setCivilId(customerLoginRequest.getCivilId());
		userSession.setUserSequenceNumber(customerLoginModel.getUserSeqNum());
		userSession.setUserAmibCustRef(customerLoginModel.getAmibRef());
		userSession.setCivilId(customerLoginRequest.getCivilId());
	}

	public AmxApiResponse<?, Object> changePasswordOtpInitiate(String eOtp, String mOtp,
			ChangePasswordOtpRequest changePasswordOtpRequest) {
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();

		try {

			if (null == changePasswordOtpRequest.getCivilId() || changePasswordOtpRequest.getCivilId().equals("")) {
				resp.setStatusEnum(WebAppStatusCodes.EMPTY_CIVIL_ID);
				resp.setMessage(Message.EMPTY_CIVIL_ID);
				resp.setMessageKey(WebAppStatusCodes.EMPTY_CIVIL_ID.toString());
				return resp;
			}

			userSession.setCivilId(changePasswordOtpRequest.getCivilId());

			AmxApiResponse<ResponseInfo, Object> validateCivilID = isValidCivilId(
					changePasswordOtpRequest.getCivilId());
			if (!validateCivilID.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {
				validateCivilID.setStatusKey(validateCivilID.getMessageKey());
				return validateCivilID;
			}

			AmxApiResponse<ResponseInfo, Object> civilIdExistCheck = isCivilIdExist(
					changePasswordOtpRequest.getCivilId());
			if (!civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {
				civilIdExistCheck.setStatusKey(civilIdExistCheck.getMessageKey());
				return civilIdExistCheck;
			}

			CustomerDetailModel customerDetailModel = customerRegistrationDao.getUserDetails(
					changePasswordOtpRequest.getCivilId(), HardCodedValues.USER_TYPE,
					userSession.getUserSequenceNumber(), userSession.getLanguageId());
			if (null == customerDetailModel || customerDetailModel.getErrorCode() != null) {
				resp.setMessageKey(customerDetailModel.getErrorCode());
				resp.setMessage(customerDetailModel.getErrorMessage());
				resp.setStatusKey(customerDetailModel.getErrorCode());
				return resp;
			} else {
				try {

					AmxApiResponse<?, Object> validateDOTP = emailSmsService.validateDOTP(eOtp, mOtp,
							customerDetailModel.getEmail(), customerDetailModel.getMobile(),
							DetailsConstants.RESET_PASSOWRD_OTP);
					if (null != validateDOTP) {
						return validateDOTP;
					}
					resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				} catch (Exception e) {
					e.printStackTrace();
					resp.setStatusEnum(WebAppStatusCodes.CP_OTP_NOT_GENERATED);
					resp.setMessage(Message.CP_EMAIL_NOT_SENT);
					resp.setMessageKey(WebAppStatusCodes.CP_OTP_NOT_GENERATED.toString());
				}
			}
		} catch (Exception e) {
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "changePasswordOtpInitiate :: exception :" + e);
			e.printStackTrace();
		}

		return resp;
	}

	public AmxApiResponse<?, Object> changePasswordLogedInUser(String eOtp, String mOtp,
			ChangePasswordRequest changePasswordRequest) {
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();

		try {
			if (null == changePasswordRequest.getNewPassword() || changePasswordRequest.getNewPassword().equals("")) {
				resp.setStatusEnum(WebAppStatusCodes.EMPTY_PASSWORD);
				resp.setMessage(Message.EMPTY_PASSWORD);
				resp.setMessageKey(WebAppStatusCodes.EMPTY_PASSWORD.toString());
				return resp;
			}

			CustomerDetailModel customerDetailModel = customerRegistrationDao.getUserDetails(userSession.getCivilId(),
					HardCodedValues.USER_TYPE, userSession.getUserSequenceNumber(), userSession.getLanguageId());
			if (null == customerDetailModel || customerDetailModel.getErrorCode() != null) {
				resp.setMessageKey(customerDetailModel.getErrorCode());
				resp.setMessage(customerDetailModel.getErrorMessage());
				resp.setStatusKey(customerDetailModel.getErrorCode());
				return resp;
			} 
			else 
			{
				AmxApiResponse<?, Object> validateDOTP = emailSmsService.validateDOTP(eOtp, mOtp,
						customerDetailModel.getEmail(), customerDetailModel.getMobile(),
						DetailsConstants.RESET_PASSOWRD_OTP);
				if (null != validateDOTP) {
					return validateDOTP;
				}

				return updatePassword(changePasswordRequest);
			}
		} catch (Exception e) {
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "changePasswordLogedInUser :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}

	public AmxApiResponse<ChangePasswordResponse, Object> updatePassword(ChangePasswordRequest changePasswordRequest) {
		AmxApiResponse<ChangePasswordResponse, Object> resp = new AmxApiResponse<ChangePasswordResponse, Object>();

		try {
			CustomerDetailModel customerDetailModel = new CustomerDetailModel();
			customerDetailModel.setPassword(changePasswordRequest.getNewPassword());

			customerDetailModel = customerRegistrationDao.updatePassword(customerDetailModel, userSession.getCivilId(),
					HardCodedValues.USER_TYPE);

			if (customerDetailModel.getErrorCode() == null) {
				resp.setMessageKey(customerDetailModel.getErrorCode());
				resp.setMessage(customerDetailModel.getErrorMessage());
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				resp.setData(null);
			} else {
				resp.setMessageKey(customerDetailModel.getErrorCode());
				resp.setMessage(customerDetailModel.getErrorMessage());
				resp.setStatusKey(customerDetailModel.getErrorCode());
				resp.setData(null);
			}
		} catch (Exception e) {
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "updatePassword :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}

	public AmxApiResponse<ChangePasswordResponse, Object> updateUserPassword(String eOtp, String mOtp,
			ChangePasswordRequest changePasswordRequest) {
		AmxApiResponse<ChangePasswordResponse, Object> resp = new AmxApiResponse<ChangePasswordResponse, Object>();
		try {
			CustomerDetailModel customerDetailModel = new CustomerDetailModel();
			customerDetailModel.setPassword(changePasswordRequest.getNewPassword());
			customerDetailModel = customerRegistrationDao.updatePassword(customerDetailModel, userSession.getCivilId(),
					HardCodedValues.USER_TYPE);
			if (customerDetailModel.getErrorCode() == null) {
				resp.setMessageKey(customerDetailModel.getErrorCode());
				resp.setMessage(customerDetailModel.getErrorMessage());
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
				resp.setData(null);
			} else {
				resp.setMessageKey(customerDetailModel.getErrorCode());
				resp.setMessage(customerDetailModel.getErrorMessage());
				resp.setStatusKey(customerDetailModel.getErrorCode());
				resp.setData(null);
			}
		} catch (Exception e) {
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "updateUserPassword :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}

	public void sendFailedRegistration(String type, RequestOtpModel requestOtpModel, String exceptionMessage) {
		FailureException failureException = new FailureException();
		failureException.setCivilId(requestOtpModel.getCivilId());
		failureException.setMobileNumber(requestOtpModel.getMobileNumber());
		failureException.setEmailId(requestOtpModel.getEmailId());
		failureException.setCountryId(metaService.getTenantProfile().getCountryId());
		failureException.setCompCd(metaService.getTenantProfile().getCompCd());
		failureException.setDeviceId(metaService.getUserDeviceInfo().getDeviceId());
		failureException.setDeviceType(metaService.getUserDeviceInfo().getDeviceType());
		failureException.setLanguageId(userSession.getLanguageId());
		failureException.setExceptionType("REGISTER");
		failureException.setUserType(HardCodedValues.USER_TYPE);
		failureException.setExceptionMsg(exceptionMessage);
		emailSmsService.sendFailedRegEmail(requestOtpModel);
		customerRegistrationDao.setFailedException(type, failureException);

	}

	private AmxApiResponse<?, Object> validateForRegistration(RequestOtpModel requestOtpModel) {
		AmxApiResponse<ResponseOtpModel, Object> resp = new AmxApiResponse<ResponseOtpModel, Object>();

		try {

			AmxApiResponse<ResponseInfo, Object> validateCivilID = isValidCivilId(requestOtpModel.getCivilId());
			
			AmxApiResponse<ResponseInfo, Object> civilIdExistCheck = isCivilIdExist(requestOtpModel.getCivilId());
			
			AmxApiResponse<ResponseInfo, Object> isValidMobileNumber = isValidMobileNumber(requestOtpModel.getMobileNumber());
			
			AmxApiResponse<ResponseInfo, Object> mobileNumberExists = isMobileNumberExist(requestOtpModel.getMobileNumber());
			
			AmxApiResponse<ResponseInfo, Object> validateEmailID = isValidEmailId(requestOtpModel.getEmailId());
			
			AmxApiResponse<ResponseInfo, Object> emailIdExists = isEmailIdExist(requestOtpModel.getEmailId());

			try {
				if (null == requestOtpModel.getCivilId() || requestOtpModel.getCivilId().toString().equals("")) {
					requestOtpModel.setCivilId(userSession.getCivilId());
				}

				if (!validateCivilID.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {
					validateCivilID.setStatusKey(validateCivilID.getMessageKey());
					return validateCivilID;
				}
				
				if (civilIdExistCheck.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {
					civilIdExistCheck.setStatusKey(civilIdExistCheck.getMessageKey());
					return civilIdExistCheck;
				}
				
				if (!validateEmailID.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {

					return validateEmailID;
				}
				
				if (emailIdExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {
					sendFailedRegistration(DetailsConstants.REG_INCOMPLETE_TYPE_DUPLICATE_EMAIL, requestOtpModel,
							emailIdExists.getMessage());
					resp.setMessageKey(WebAppStatusCodes.EMAIL_ID_REGESTERED.toString());
					resp.setStatusEnum(WebAppStatusCodes.EMAIL_ID_REGESTERED);
					return resp;
				}
				
				if (!isValidMobileNumber.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {
					return isValidMobileNumber;
				}
				
				if (mobileNumberExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {
					sendFailedRegistration(DetailsConstants.REG_INCOMPLETE_TYPE_DUPLICATE_MOBILE, requestOtpModel,
							mobileNumberExists.getMessage());
					resp.setMessageKey(WebAppStatusCodes.MOBILE_NUMBER_REGISTERED.toString());
					resp.setStatusEnum(WebAppStatusCodes.MOBILE_NUMBER_REGISTERED);
					return resp;
				}
				
				if (mobileNumberExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)
						&& emailIdExists.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {
					sendFailedRegistration(DetailsConstants.REG_INCOMPLETE_TYPE_MOBE_MAIL, requestOtpModel,
							mobileNumberExists.getMessage());
					resp.setStatusEnum(WebAppStatusCodes.MOBILE_OR_EMAIL_ALREADY_EXISTS);
					resp.setMessageKey(WebAppStatusCodes.MOBILE_OR_EMAIL_ALREADY_EXISTS.toString());
					return resp;
				}

			} catch (Exception e) {
				resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
				resp.setMessage(e.toString());
				logger.info(TAG + "validateForRegistration :: exception :" + e);
				e.printStackTrace();
			}
		} catch (Exception e) {
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "validateForRegistration :: exception :" + e);
			e.printStackTrace();
		}

		return null;
	}
}
