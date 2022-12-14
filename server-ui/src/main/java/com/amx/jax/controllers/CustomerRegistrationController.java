
package com.amx.jax.controllers;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.WebAppStatus.ApiWebAppStatus;
import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.Message;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.meta.IMetaService;
import com.amx.jax.models.ChangePasswordOtpRequest;
import com.amx.jax.models.ChangePasswordRequest;
import com.amx.jax.models.ChangePasswordResponse;
import com.amx.jax.models.CustomerDetailResponse;
import com.amx.jax.models.CustomerLoginRequest;
import com.amx.jax.models.CustomerRegistrationRequest;
import com.amx.jax.models.CustomerRegistrationResponse;
import com.amx.jax.models.RequestOtpModel;
import com.amx.jax.models.ResponseInfo;
import com.amx.jax.postman.client.GoogleService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.SupportEmail;
import com.amx.jax.services.CustomerRegistrationService;
import com.amx.jax.services.EmailSmsService;
import com.amx.jax.swagger.ApiMockParam;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.response.WebResponseStatus;
import com.amx.jax.ui.session.UserSession;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "User Auth APIs")
@RestController
public class CustomerRegistrationController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationController.class);

	@Autowired
	CommonHttpRequest httpService;

	@Autowired
	IMetaService metaService;

	@Autowired
	UserSession userSession;

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@Autowired
	private GoogleService googleService;

	@Autowired
	EmailSmsService emailSmsService;

	@ApiOperation(value = "api to validate valid civil id")
	@ApiWebAppStatus({ WebAppStatusCodes.CIVIL_ID_INVALID, WebAppStatusCodes.CIVIL_ID_VALID,
			WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@ApiMockParam(example = "284090301401", value = "customer civil id")

	@RequestMapping(value = "/pub/reg/civilid-valid", method = RequestMethod.POST)
	public AmxApiResponse<ResponseInfo, Object> isValidCivilId(@RequestParam("civilId") String civilid) {
		return customerRegistrationService.isValidCivilId(civilid);
	}

	@ApiOperation(value = "api to validate civil id already exist")
	@ApiWebAppStatus({ WebAppStatusCodes.CIVIL_ID_INVALID, WebAppStatusCodes.CIVIL_ID_VALID,
			WebAppStatusCodes.CIVIL_ID_ALREADY_REGISTERED, WebAppStatusCodes.CIVIL_ID_NOT_REGESTERED,
			WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@ApiMockParam(example = "284090301401", value = "customer civil id")
	@RequestMapping(value = "/pub/reg/civilid-exists", method = RequestMethod.POST)
	public AmxApiResponse<ResponseInfo, Object> isCivilIdExistCheck(@RequestParam("civilId") String civilid) {
		return customerRegistrationService.isCivilIdExistCheck(civilid);
	}

	@ApiOperation(value = "api to validate valid contact number")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@ApiMockParam(example = "98989892", value = "customer contact number")
	@RequestMapping(value = "/pub/reg/mobile-valid", method = RequestMethod.POST)
	public AmxApiResponse<ResponseInfo, Object> isValidMobileNumber(@RequestParam("mobile") String mobile) {
		return customerRegistrationService.isValidMobileNumber(mobile);
	}

	@ApiOperation(value = "api to validate valid email id")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@ApiMockParam(example = "amx@gmail.com", value = "customer email id")
	@RequestMapping(value = "/pub/reg/email-valid", method = RequestMethod.POST)
	public AmxApiResponse<ResponseInfo, Object> isValidEmailId(@RequestParam("emailId") String emailId) {
		return customerRegistrationService.isValidEmailId(emailId);
	}

	@ApiOperation(value = "api to verify email id and contact number through otp", notes = "email id and contact number enterd by customer while registration gets verified by sending otp")
	@ApiWebAppStatus({ WebAppStatusCodes.CIVIL_ID_INVALID, WebAppStatusCodes.CIVIL_ID_VALID,
			WebAppStatusCodes.CIVIL_ID_ALREADY_REGISTERED, WebAppStatusCodes.CIVIL_ID_NOT_REGESTERED,
			WebAppStatusCodes.EMAIL_ID_VALID, WebAppStatusCodes.EMAIL_ID_INVALID,
			WebAppStatusCodes.MOBILE_NUMBER_REGISTERED, WebAppStatusCodes.EMAIL_ID_REGESTERED,
			WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@ApiMockParam(example = "1223", value = "mobile otp")
	@RequestMapping(value = "/pub/reg/reg-otp", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> registrationOtpInitiate(
			@RequestHeader(value = "mOtp", required = false) String mOtpHeader,
			@RequestHeader(value = "eOtp", required = false) String eOtpHeader,
			@RequestBody RequestOtpModel requestOtpModel) {
		return customerRegistrationService.registrationOtp(eOtpHeader, mOtpHeader, requestOtpModel);
	}

	@ApiOperation(value = "api to set password and create new customer", notes = "after successful email id and contact number verfication through otp verification this api to set password and create new customer")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS,
			WebAppStatusCodes.REGISTER_FAILED })
	@RequestMapping(value = "/pub/reg/customer-registration", method = RequestMethod.POST)
	public AmxApiResponse<CustomerRegistrationResponse, Object> addNewCustomer(
			@RequestBody CustomerRegistrationRequest customerRegistrationRequest) {
		return customerRegistrationService.addNewCustomer(customerRegistrationRequest);
	}

	@ApiOperation(value = "api to validate customer id and password for successful login")
	@ApiWebAppStatus({ WebAppStatusCodes.EMPTY_PASSWORD, WebAppStatusCodes.CIVIL_ID_INVALID,
			WebAppStatusCodes.CIVIL_ID_VALID, WebAppStatusCodes.CIVIL_ID_ALREADY_REGISTERED,
			WebAppStatusCodes.CIVIL_ID_NOT_REGESTERED, WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS,
			WebAppStatusCodes.INVALID_USR_PWD })
	@RequestMapping(value = "/pub/login/validate-userlogin", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> validateUserLogin(@RequestParam(name = "languageId" , required = false) String languageId,
			@RequestBody CustomerLoginRequest customerLoginRequest) {
		BigDecimal languageIdDat = null;
		if (null != languageId && !languageId.equals("") && !languageId.equalsIgnoreCase("null")) {
			languageIdDat = ArgUtil.parseAsBigDecimal(languageId);
			userSession.setLanguageId(languageIdDat);
		}
		else
		{
			userSession.setLanguageId(new BigDecimal(0));
		}
		return customerRegistrationService.validateUserLogin(customerLoginRequest);
	}

	@ApiOperation(value = "api to verify email id and contact number while changing password non logged in user", notes = "this api is called when customer is not logged in and tries to change the password. Here customer emial id and contact number get validates through otp.")
	@ApiWebAppStatus({ WebAppStatusCodes.CIVIL_ID_INVALID, WebAppStatusCodes.CIVIL_ID_VALID,
			WebAppStatusCodes.CIVIL_ID_ALREADY_REGISTERED, WebAppStatusCodes.CIVIL_ID_NOT_REGESTERED,
			WebAppStatusCodes.CP_OTP_NOT_GENERATED, WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/pub/login/changepass-otpinitiate", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> changePasswordOtpInitiate(
			@RequestHeader(value = "mOtp", required = false) String mOtpHeader,
			@RequestHeader(value = "eOtp", required = false) String eOtpHeader,
			@RequestBody ChangePasswordOtpRequest changePasswordRequest) {
		return customerRegistrationService.changePasswordOtpInitiate(eOtpHeader, mOtpHeader, changePasswordRequest);
	}

	@ApiOperation(value = "api to update new password", notes = "while registration after successful verification of email id and contact number through otp , this api will called and updates the new password")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/pub/login/changepass", method = RequestMethod.POST)
	public AmxApiResponse<ChangePasswordResponse, Object> updatePassword(
			@RequestBody ChangePasswordRequest changePasswordRequest) {
		return customerRegistrationService.updatePassword(changePasswordRequest);
	}

	@ApiOperation(value = "api to return customer details", notes = "this api will return company details and if customer is logged in this api will return customer details and company details .")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS, WebAppStatusCodes.LIST_POP_ERROR,
			WebAppStatusCodes.INVLD_LOGIN_CATG, WebAppStatusCodes.INVALID_USR })
	@RequestMapping(value = "/pub/reg/userdetails", method = RequestMethod.POST)
	public AmxApiResponse<CustomerDetailResponse, Object> getUserDetails(
			@RequestParam(name = "languageId", required = false) String languageId) {
		BigDecimal languageIdDat = null;
		if (null != languageId && !languageId.equals("") && !languageId.equalsIgnoreCase("null")) {
			languageIdDat = ArgUtil.parseAsBigDecimal(languageId);
			userSession.setLanguageId(languageIdDat);
		}
		return customerRegistrationService.getCustomerDetails();
	}

	@ApiWebAppStatus({ WebAppStatusCodes.CIVIL_ID_INVALID, WebAppStatusCodes.CIVIL_ID_VALID,
			WebAppStatusCodes.CIVIL_ID_ALREADY_REGISTERED, WebAppStatusCodes.CIVIL_ID_NOT_REGESTERED,
			WebAppStatusCodes.CP_OTP_NOT_GENERATED, WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@ApiOperation(value = "api to verify email id and contact number while changing password for logged in user", notes = "this api will update the logged in user password.")
	@RequestMapping(value = "/pub/login/changepass-loggedin", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> changePasswordLogedInUser(
			@RequestHeader(value = "mOtp", required = false) String mOtpHeader,
			@RequestHeader(value = "eOtp", required = false) String eOtpHeader,
			@RequestBody ChangePasswordRequest changePasswordRequest) {
		return customerRegistrationService.changePasswordLogedInUser(eOtpHeader, mOtpHeader, changePasswordRequest);
	}

	@ApiOperation(value = "api to logg out customer")
	@RequestMapping(value = "/pub/login/log-out", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> logout() {
		AmxApiResponse<ResponseInfo, Object> resp = new AmxApiResponse<ResponseInfo, Object>();
		userSession.unauthorize();
		resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
		resp.setMessage(Message.LOGOUT_MESSAGE);
		resp.setMessageKey(Message.LOGOUT_MESSAGE);
		return resp;
	}

	@RequestMapping(value = "/pub/contact", method = { RequestMethod.POST })
	public ResponseWrapper<Email> contactUs(@RequestParam String name, @RequestParam String cemail,
			@RequestParam String cphone, @RequestParam String message, @RequestParam String verify) {
		ResponseWrapper<Email> wrapper = new ResponseWrapper<>();
		try {

			if (googleService.verifyCaptcha(verify, httpService.getIPAddress())) {

				logger.info("CustomerRegistrationController :: contactUs :: verify Done :" + verify);

				SupportEmail email = new SupportEmail();
				email.setCaptchaCode(verify);
				email.setVisitorName(name);
				email.setVisitorPhone(cphone);
				email.setVisitorEmail(cemail);
				email.setVisitorMessage(message);
				emailSmsService.sendEmailToSupprt(email);
				wrapper.setData(email);
			} else {

				logger.info("CustomerRegistrationController :: contactUs :: ERROR :");
				wrapper.setStatusKey(WebResponseStatus.ERROR);
			}

		} catch (Exception e) {
			e.printStackTrace();
			wrapper.setStatusKey(WebResponseStatus.ERROR);
		}
		return wrapper;
	}
}
