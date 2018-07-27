




package com.amx.jax.userregistration.controller;

import java.util.ArrayList;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.userregistration.service.CustomerRegistrationService;
import com.insurance.email.model.Email;
import com.insurance.user_registartion.interfaces.ICustomerRegistration;
import com.insurance.user_registartion.model.CustomerPersonalDetail;

@RestController
public class CustomerRegistrationController implements ICustomerRegistration
{
	String TAG = "com.amx.jax.userregistration.controller :: CustomerRegistrationController :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationController.class);

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@RequestMapping(value = "/validate-civilid", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<BoolRespModel, Object> isValidCivilId(@RequestParam("civilId") String civilid)
	{
		return customerRegistrationService.isValidCivilId(civilid);
	}

	@RequestMapping(value = "/civilid-exists", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<BoolRespModel, Object> isCivilIdExist(@RequestParam("civilId") String civilid)
	{
		return customerRegistrationService.isCivilIdExist(civilid);
	}

	@RequestMapping(value = "/validate-mobile", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<BoolRespModel, Object> isValidMobileNumber(@RequestParam("mobileNumber") String mobileNumber)
	{
		return customerRegistrationService.isValidMobileNumber(mobileNumber);
	}

	@RequestMapping(value = "/validate-emailId", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<BoolRespModel, Object> isValidEmailId(@RequestParam("emailId") String emailId)
	{
		return customerRegistrationService.isValidEmailId(emailId);
	}

	@RequestMapping(value = "/customer-registration", method = RequestMethod.POST, produces = "application/json")
	public String addNewCustomer(@RequestBody CustomerPersonalDetail customerPersonalDetail)
	{
		return customerRegistrationService.addNewCustomer(dummyUserInfo());
	}

	@RequestMapping(value = "/companysetup", method = RequestMethod.GET, produces = "application/json")
	public ArrayList getCompanySetUp(@RequestParam("langId") int langId)
	{
		return customerRegistrationService.getCompanySetUp(langId);
	}

	@RequestMapping(value = "/sendemail", method = RequestMethod.POST)
	public void sendEmail(@RequestBody Email email)
	{
		logger.info(TAG + " sendEmail :: email :" + email);

		//String emailIdFrom = "almulla.insurance.1427@gmail.com";
		//String emailIdTo = "almulla.insurance.1427@gmail.com";
		//String subject = "Emial Test Second";
		//String message = "My Second Email Notification Demo";

		//email.setEmailIdFrom(emailIdFrom);
		//email.setEmailIdTo(emailIdTo);
		//email.setSubject(subject);
		//email.setMessage(message);

		customerRegistrationService.sendEmail(email);
	}

	public CustomerPersonalDetail dummyUserInfo()
	{
		CustomerPersonalDetail customerPersonalDetail = new CustomerPersonalDetail();
		Date now = new Date();
		customerPersonalDetail.setCountryId(1);
		customerPersonalDetail.setCompCd(10);
		customerPersonalDetail.setUserSeqNumber(02);
		customerPersonalDetail.setUserType("D");
		customerPersonalDetail.setCivilId("93467136135887");
		customerPersonalDetail.setPassword("1427ABHIPASS");
		customerPersonalDetail.setMobile("8796589233");
		customerPersonalDetail.setEmail("abhishek.tiwari@almullaexcahnge.com");
		customerPersonalDetail.setMobVerificationCode("m1427");
		customerPersonalDetail.setEmailVerificationCode("e1427");
		customerPersonalDetail.setMobileVerified("Y");
		customerPersonalDetail.setEmailVerified("Y");
		customerPersonalDetail.setRefAmibcd("ABCD");
		customerPersonalDetail.setStatus("T");
		customerPersonalDetail.setDeviceType("Web");
		customerPersonalDetail.setDate(now);
		customerPersonalDetail.setCreatedDeviceId("createdDevId_1427");
		customerPersonalDetail.setCreatedBy("createdBy_1427");
		customerPersonalDetail.setUpdateOn(now);
		customerPersonalDetail.setUpdateDeviceId("upDateDevId_1427");

		return customerPersonalDetail;
	}
}
