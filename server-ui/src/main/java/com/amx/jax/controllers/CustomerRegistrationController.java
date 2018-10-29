
package com.amx.jax.controllers;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.models.ChangePasswordOtpRequest;
import com.amx.jax.models.ChangePasswordRequest;
import com.amx.jax.models.ChangePasswordResponse;
import com.amx.jax.models.CustomerDetailResponse;
import com.amx.jax.models.CustomerLoginRequest;
import com.amx.jax.models.CustomerRegistrationRequest;
import com.amx.jax.models.CustomerRegistrationResponse;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.Person;
import com.amx.jax.models.RequestOtpModel;
import com.amx.jax.models.Validate;
import com.amx.jax.service.HttpService;
import com.amx.jax.services.CustomerRegistrationService;
import com.amx.jax.services.EmailSmsService;
import com.amx.jax.utility.CustomizeQuoteUtility;
import com.amx.utils.ArgUtil;

@RestController
public class CustomerRegistrationController
{
	String TAG = "com.amx.jax.userregistration.controller :: CustomerRegistrationController :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationController.class);
	
	@Autowired
	HttpService httpService;
	
	@Autowired
	MetaData metaData;

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
	public AmxApiResponse<Validate, Object> isValidCivilId(@RequestParam("civilId") String civilid)
	{
		handleSession();
		return customerRegistrationService.isValidCivilId(civilid);
	}

	@RequestMapping(value = "/pub/reg/civilid-exists", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<Validate, Object> isCivilIdExistCheck(@RequestParam("civilId") String civilid)
	{
		handleSession();
		return customerRegistrationService.isCivilIdExistCheck(civilid);
	}

	@RequestMapping(value = "/pub/reg/mobile-valid", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<Validate, Object> isValidMobileNumber(@RequestParam("mobile") String mobile)
	{
		handleSession();
		return customerRegistrationService.isValidMobileNumber(mobile);
	}

	@RequestMapping(value = "/pub/reg/mobile-exists", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<Validate, Object> isMobileNumberExistCheck(@RequestParam("mobile") String mobile)
	{
		handleSession();
		return customerRegistrationService.isMobileNumberExistCheck(mobile);
	}

	@RequestMapping(value = "/pub/reg/email-valid", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<Validate, Object> isValidEmailId(@RequestParam("emailId") String emailId)
	{
		handleSession();
		return customerRegistrationService.isValidEmailId(emailId);
	}

	@RequestMapping(value = "/pub/reg/email-exists", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<Validate, Object> isEmailIdExistCheck(@RequestParam("emailId") String emailId)
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

	@RequestMapping(value = "/pub/reg/sms-email-test", method = RequestMethod.POST, produces = "application/json")
	public String testEmailPostman()
	{
		handleSession();

		CustomerLoginRequest c = new CustomerLoginRequest();
		c.setCivilId("282071300105");
		c.setPassword("Amx@1234");
		customerRegistrationService.validateUserLogin(c);

		emailSmsService.sendEmailOtp("abhishektiwaribecse@gmail.com");
		emailSmsService.sendMobileOtp("8796589233");
		emailSmsService.emailTosuccessFullUserRegistration("abhishektiwaribecse@gmail.com");
		RequestOtpModel r = new RequestOtpModel();
		r.setCivilId("282071300105");
		r.setEmailId("abhishektiwaribecse@gmail.com");
		r.setMobileNumber("8796589233");
		emailSmsService.sendFailedRegEmail(r);
		// emailSmsService.emailToCustomerAndAmib("Make - Hona", "Submake -
		// kawasaki", "https://www.cricbuzz.com/");

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
