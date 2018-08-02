
package com.amx.jax.userregistration.controller;

import java.util.ArrayList;
import java.util.Date;

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
import com.amx.jax.userregistration.service.CustomerRegistrationService;
import com.amx.utils.ArgUtil;
import com.insurance.generateotp.RequestOtpModel;
import com.insurance.user_registartion.interfaces.ICustomerRegistration;
import com.insurance.user_registartion.model.CustomerPersonalDetail;
import com.insurance.user_registartion.model.Validate;

@RestController
public class CustomerRegistrationController implements ICustomerRegistration {
	String TAG = "com.amx.jax.userregistration.controller :: CustomerRegistrationController :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationController.class);

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@RequestMapping(value = "/validate-civilid", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<Validate, Object> isValidCivilId(@RequestParam("civilId") String civilid) {
		return customerRegistrationService.isValidCivilId(civilid);
	}

	@RequestMapping(value = "/civilid-exists", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<Validate, Object> isCivilIdExist(@RequestParam("civilId") String civilid) {
		return customerRegistrationService.isCivilIdExist(civilid);
	}

	@RequestMapping(value = "/validate-mobile", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<Validate, Object> isValidMobileNumber(@RequestParam("mobileNumber") String mobileNumber) {
		return customerRegistrationService.isValidMobileNumber(mobileNumber);
	}

	@RequestMapping(value = "/validate-emailId", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<Validate, Object> isValidEmailId(@RequestParam("emailId") String emailId) {
		return customerRegistrationService.isValidEmailId(emailId);
	}

	@RequestMapping(value = "/pub/reg/userdetails", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> sendOtp(@RequestBody RequestOtpModel requestOtpModel) {
		logger.info(TAG + " sendOtp :: requestOtpModel :" + requestOtpModel);

		AmxApiResponse<Validate, Object> validateCivilID = customerRegistrationService
				.isValidCivilId(requestOtpModel.getCivilId());

		if (validateCivilID.getStatusKey().equalsIgnoreCase(ApiConstants.Failure)) {
			return validateCivilID;
		}

		AmxApiResponse<Validate, Object> civilIdExist = customerRegistrationService
				.isCivilIdExist(requestOtpModel.getCivilId());

		if (civilIdExist.getStatusKey().equalsIgnoreCase(ApiConstants.Failure)) {
			return civilIdExist;
		}

		AmxApiResponse<Validate, Object> validateMobileNumber = customerRegistrationService
				.isValidMobileNumber(requestOtpModel.getMobileNumber());

		if (validateMobileNumber.getStatusKey().equalsIgnoreCase(ApiConstants.Failure)) {
			return validateMobileNumber;
		}

		AmxApiResponse<Validate, Object> validateEmailID = customerRegistrationService
				.isValidEmailId(requestOtpModel.getEmailId());

		if (validateEmailID.getStatusKey().equalsIgnoreCase(ApiConstants.Failure)) {
			return validateEmailID;
		}

		return customerRegistrationService.sendOtp(requestOtpModel);

	}

	@RequestMapping(value = "/pub/reg/verifyuserdetails", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<CustomerPersonalDetail, Object> validateOtp(

			@RequestHeader(value = "mOtp", required = false) String mOtpHeader,
			@RequestHeader(value = "eOtp", required = false) String eOtpHeader,
			@RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp

	) {

		mOtp = ArgUtil.ifNotEmpty(mOtp, mOtpHeader);
		eOtp = ArgUtil.ifNotEmpty(eOtp, eOtpHeader);

		return null; // customerRegistrationService.validateOtp(null);
	}

	@RequestMapping(value = "/customer-registration", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<CustomerPersonalDetail, Object> addNewCustomer(
			@RequestBody CustomerPersonalDetail customerPersonalDetail) {
		return customerRegistrationService.addNewCustomer(customerPersonalDetail);
	}

	@RequestMapping(value = "/companysetup", method = RequestMethod.GET, produces = "application/json")
	public ArrayList getCompanySetUp(@RequestParam("langId") int langId) {
		return customerRegistrationService.getCompanySetUp(langId);
	}

	public CustomerPersonalDetail dummyUserInfo() {
		CustomerPersonalDetail customerPersonalDetail = new CustomerPersonalDetail();
		Date now = new Date();
		customerPersonalDetail.setCountryId(1);
		customerPersonalDetail.setCompCd(10);
		customerPersonalDetail.setUserSeqNumber(02);
		customerPersonalDetail.setUserType("D");
		customerPersonalDetail.setCivilId("234234232");
		customerPersonalDetail.setPassword("1427ABHIPASS");
		customerPersonalDetail.setMobile("8796589233");
		customerPersonalDetail.setEmail("abhishek.tiwari@almullaexcahnge.com");
		customerPersonalDetail.setMobVerificationCode("m1427");
		customerPersonalDetail.setEmailVerificationCode("e1427");
		customerPersonalDetail.setDeviceType("Web");
		customerPersonalDetail.setCreatedDeviceId("createdDevId_1427");

		return customerPersonalDetail;
	}

}
