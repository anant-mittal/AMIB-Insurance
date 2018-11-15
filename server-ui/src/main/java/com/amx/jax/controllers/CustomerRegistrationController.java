
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

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.Message;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.models.ChangePasswordOtpRequest;
import com.amx.jax.models.ChangePasswordRequest;
import com.amx.jax.models.ChangePasswordResponse;
import com.amx.jax.models.CustomerDetailResponse;
import com.amx.jax.models.CustomerLoginRequest;
import com.amx.jax.models.CustomerRegistrationRequest;
import com.amx.jax.models.CustomerRegistrationResponse;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.RequestOtpModel;
import com.amx.jax.models.ResponseInfo;
import com.amx.jax.services.CustomerRegistrationService;
import com.amx.jax.services.EmailSmsService;
import com.amx.jax.ui.session.UserSession;
import com.amx.utils.ArgUtil;

@RestController
public class CustomerRegistrationController
{
	String TAG = "com.amx.jax.userregistration.controller :: CustomerRegistrationController :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationController.class);
	
	@Autowired
	CommonHttpRequest httpService;
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	UserSession userSession;

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@Autowired
	EmailSmsService emailSmsService;

	@RequestMapping(value = "/pub/reg/companysetup", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getCompanySetUp()
	{
		return customerRegistrationService.getCompanySetUp();
	}

	@RequestMapping(value = "/pub/reg/civilid-valid", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<ResponseInfo, Object> isValidCivilId(@RequestParam("civilId") String civilid)
	{
		handleSession();
		return customerRegistrationService.isValidCivilId(civilid);
	}

	@RequestMapping(value = "/pub/reg/civilid-exists", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<ResponseInfo, Object> isCivilIdExistCheck(@RequestParam("civilId") String civilid)
	{
		handleSession();
		return customerRegistrationService.isCivilIdExistCheck(civilid);
	}

	@RequestMapping(value = "/pub/reg/mobile-valid", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<ResponseInfo, Object> isValidMobileNumber(@RequestParam("mobile") String mobile)
	{
		handleSession();
		return customerRegistrationService.isValidMobileNumber(mobile);
	}

	@RequestMapping(value = "/pub/reg/mobile-exists", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<ResponseInfo, Object> isMobileNumberExistCheck(@RequestParam("mobile") String mobile)
	{
		handleSession();
		return customerRegistrationService.isMobileNumberExistCheck(mobile);
	}

	@RequestMapping(value = "/pub/reg/email-valid", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<ResponseInfo, Object> isValidEmailId(@RequestParam("emailId") String emailId)
	{
		handleSession();
		return customerRegistrationService.isValidEmailId(emailId);
	}

	@RequestMapping(value = "/pub/reg/email-exists", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<ResponseInfo, Object> isEmailIdExistCheck(@RequestParam("emailId") String emailId)
	{
		handleSession();
		return customerRegistrationService.isEmailIdExistCheck(emailId);
	}

	@RequestMapping(value = "/pub/reg/reg-otp", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> registrationOtpInitiate(@RequestHeader(value = "mOtp", required = false) String mOtpHeader, @RequestHeader(value = "eOtp", required = false) String eOtpHeader, @RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp,
			@RequestBody RequestOtpModel requestOtpModel)
	{
		handleSession();
		mOtp = ArgUtil.ifNotEmpty(mOtp, mOtpHeader);
		eOtp = ArgUtil.ifNotEmpty(eOtp, eOtpHeader);
		return customerRegistrationService.registrationOtp(eOtp, mOtp, requestOtpModel);
	}

	@RequestMapping(value = "/pub/reg/customer-registration", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<CustomerRegistrationResponse, Object> addNewCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest)
	{
		handleSession();
		return customerRegistrationService.addNewCustomer(customerRegistrationRequest);
	}

	@RequestMapping(value = "/pub/login/validate-userlogin", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> validateUserLogin(@RequestBody CustomerLoginRequest customerLoginRequest)
	{
		handleSession();
		return customerRegistrationService.validateUserLogin(customerLoginRequest);
	}

	@RequestMapping(value = "/pub/login/changepass-otpinitiate", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> changePasswordOtpInitiate(@RequestHeader(value = "mOtp", required = false) String mOtpHeader, @RequestHeader(value = "eOtp", required = false) String eOtpHeader, @RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp,
			@RequestBody ChangePasswordOtpRequest changePasswordRequest)
	{
		handleSession();
		mOtp = ArgUtil.ifNotEmpty(mOtp, mOtpHeader);
		eOtp = ArgUtil.ifNotEmpty(eOtp, eOtpHeader);
		return customerRegistrationService.changePasswordOtpInitiate(eOtp, mOtp, changePasswordRequest);
	}

	@RequestMapping(value = "/pub/login/changepass", method = RequestMethod.POST)
	public AmxApiResponse<ChangePasswordResponse, Object> updatePassword(@RequestBody ChangePasswordRequest changePasswordRequest)
	{
		handleSession();
		return customerRegistrationService.updatePassword(changePasswordRequest);
	}

	@RequestMapping(value = "/pub/reg/userdetails", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<CustomerDetailResponse, Object> getUserDetails()
	{
		handleSession();
		return customerRegistrationService.getCustomerDetails();
	}

	@RequestMapping(value = "/pub/login/changepass-loggedin", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> changePasswordLogedInUser(@RequestHeader(value = "mOtp", required = false) String mOtpHeader, @RequestHeader(value = "eOtp", required = false) String eOtpHeader, @RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp,
			@RequestBody ChangePasswordRequest changePasswordRequest)
	{
		handleSession();
		mOtp = ArgUtil.ifNotEmpty(mOtp, mOtpHeader);
		eOtp = ArgUtil.ifNotEmpty(eOtp, eOtpHeader);
		return customerRegistrationService.changePasswordLogedInUser(eOtp, mOtp, changePasswordRequest);
	}
	
	@RequestMapping(value = "/pub/login/log-out", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> logout()
	{
		handleSession();
		userSession.unauthorize();
		
		AmxApiResponse<ResponseInfo, Object> resp = new AmxApiResponse<ResponseInfo, Object>();
		resp.setStatusKey(ApiConstants.SUCCESS);
		resp.setMessage(Message.LOGOUT_MESSAGE);
		resp.setMessageKey(Message.LOGOUT_MESSAGE);
		handleSession();
		
		return resp;
	}
	
	

	@RequestMapping(value = "/pub/reg/sms-email-test", method = RequestMethod.POST, produces = "application/json")
	public String testEmailPostman()
	{
		handleSession();
		CustomerLoginRequest c = new CustomerLoginRequest();
		c.setCivilId("282071300105");
		c.setPassword("Amx@1234");
		customerRegistrationService.validateUserLogin(c);

		/*emailSmsService.sendEmailOtp("abhishektiwaribecse@gmail.com");
		emailSmsService.sendMobileOtp("8796589233");
		emailSmsService.emailToCustomerOnCompilitionRequestQuote("Make1","SubMake2",new BigDecimal("1"));
		emailSmsService.emailToAmibOnCompilitionRequestQuote("Make1","SubMake2",new BigDecimal("2"));
		emailSmsService.emailToCustomerAfterSuccessPg("100","1234","5678",new BigDecimal(80));*/

		/*emailSmsService.emailTosuccessFullUserRegistration("abhishektiwaribecse@gmail.com");
		RequestOtpModel r = new RequestOtpModel();
		r.setCivilId("282071300105");
		r.setEmailId("abhishektiwaribecse@gmail.com");
		r.setMobileNumber("8796589233");
		emailSmsService.sendFailedRegEmail(r);*/
		
		return "Done";
	}

	
	private void handleSession()
	{
		if(null == metaData.getCountryId())
		{
			if (httpService.getLanguage().toString().equalsIgnoreCase("EN"))
			{
				metaData.setLanguageId(new BigDecimal(0));
			}
			getCompanySetUp();
		}
	}
	
	
}
