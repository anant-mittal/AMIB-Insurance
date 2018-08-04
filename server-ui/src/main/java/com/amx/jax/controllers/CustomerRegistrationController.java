
package com.amx.jax.controllers;

import java.util.ArrayList;

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
import com.amx.jax.models.CustomerRegistrationRequest;
import com.amx.jax.models.CustomerRegistrationResponse;
import com.amx.jax.models.Validate;
import com.amx.jax.services.CustomerRegistrationService;
import com.amx.jax.session.RegSession;
import com.amx.utils.ArgUtil;
import com.insurance.generateotp.RequestOtpModel;

@RestController
public class CustomerRegistrationController
{
	String TAG = "com.amx.jax.userregistration.controller :: CustomerRegistrationController :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationController.class);

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@Autowired
	RegSession regSession;

	@RequestMapping(value = "/pub/reg/civilid-exists", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<Validate, Object> isCivilIdExistCheck(@RequestParam("civilId") String civilid)
	{
		return customerRegistrationService.isCivilIdExistCheck(civilid);
	}

	@RequestMapping(value = "/pub/reg/mobile-exists", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<Validate, Object> isMobileNumberExistCheck(@RequestParam("mobile") String mobile)
	{
		return customerRegistrationService.isMobileNumberExistCheck(mobile);
	}
	
	@RequestMapping(value = "/pub/reg/email-exists", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<Validate, Object> isEmailIdExistCheck(@RequestParam("emailId") String emailId)
	{
		return customerRegistrationService.isEmailIdExistCheck(emailId);
	}
	
	@RequestMapping(value = "/pub/reg/userdetails", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> sendOtp(@RequestBody RequestOtpModel requestOtpModel)
	{
		return customerRegistrationService.sendOtp(requestOtpModel);
	}

	@RequestMapping(value = "/pub/reg/verifyuserdetails", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<Validate, Object> validateOtp(@RequestHeader(value = "mOtp", required = false) String mOtpHeader, @RequestHeader(value = "eOtp", required = false) String eOtpHeader, @RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp)
	{
		mOtp = ArgUtil.ifNotEmpty(mOtp, mOtpHeader);
		eOtp = ArgUtil.ifNotEmpty(eOtp, eOtpHeader);
		return customerRegistrationService.validateOtp(mOtp, eOtp);
	}

	@RequestMapping(value = "/pub/reg/customer-registration", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<CustomerRegistrationResponse, Object> addNewCustomer(@RequestBody CustomerRegistrationRequest customerRegistrationRequest)
	{
		return customerRegistrationService.addNewCustomer(customerRegistrationRequest);
	}

	
	@RequestMapping(value = "/pub/reg/companysetup", method = RequestMethod.GET, produces = "application/json")
	public ArrayList getCompanySetUp(@RequestParam("langId") int langId)
	{
		return customerRegistrationService.getCompanySetUp(langId);
	}
	
	/*@RequestMapping(value = "/pub/login/validate-user", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<Validate, Object> validateUserLogin(@RequestParam("civilId") String civilid)
	{
		return customerRegistrationService.validateUserLogin(civilid);
	}*/

}
