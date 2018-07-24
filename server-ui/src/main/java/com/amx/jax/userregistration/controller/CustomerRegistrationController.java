




package com.amx.jax.userregistration.controller;

import java.util.ArrayList;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.amx.jax.userregistration.service.CustomerRegistrationService;
import com.insurance.user_registartion.model.CustomerPersonalDetail;


@RestController
public class CustomerRegistrationController
{
	String TAG = "com.insurance.user_registartion.controller :: CustomerRegistrationController :: ";

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationController.class);

	@RequestMapping(value = "/customer-registration", method = RequestMethod.POST)
	public String addNewCustomer(@RequestBody CustomerPersonalDetail customerPersonalDetail)
	{
		return customerRegistrationService.addNewCustomer(dummyUserInfo());
		
	}
	
	@RequestMapping(value = "/companysetup", method = RequestMethod.GET)
	public ArrayList getCompanySetUp(@RequestParam("langind") int langind)
	{
		return customerRegistrationService.getCompanySetUp(langind);
	}
	
	public CustomerPersonalDetail dummyUserInfo()
	{
		CustomerPersonalDetail customerPersonalDetail = new CustomerPersonalDetail();
		Date now = new Date();
		customerPersonalDetail.setCountryId(91);
		customerPersonalDetail.setCompCd(01);
		customerPersonalDetail.setUserSeqNumber(02);
		customerPersonalDetail.setUserType("D");
		customerPersonalDetail.setCivilId("1427ABHI");
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
