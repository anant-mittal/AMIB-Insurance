




package com.insurance.user_registartion.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.insurance.response.ApiResponse;
import com.insurance.response.ResponseStatus;
import com.insurance.user_registartion.model.CustomerPersonalDetail;
import com.insurance.user_registartion.model.CustomerRegistrationDetails;
import com.insurance.user_registartion.services.CustomerRegistrationService;

public class CustomerRegistrationController
{
	String TAG = "com.insurance.user_registartion.controller :: CustomerRegistrationController :: ";

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationController.class);

	/*
	 * @RequestMapping(value = "/send-otp/", method = RequestMethod.POST) public
	 * ApiResponse sendOtp(@RequestBody CustomerPersonalDetail
	 * customerPersonalDetail) { logger.info("send otp request: " +
	 * customerPersonalDetail);
	 * 
	 * ApiResponse response = otpService.sendOtp(customerPersonalDetail);
	 * 
	 * return response; }
	 */

	@RequestMapping(value = "/customer-registration/", method = RequestMethod.POST)
	public ApiResponse addNewCustomer(@RequestBody CustomerRegistrationDetails customerRegistrationDetails)
	{
		ApiResponse apiResponse = null;

		try
		{
			logger.info(TAG + " saveNewCustomer :: customerRegistrationDetails :" + customerRegistrationDetails.toString());

			apiResponse = customerRegistrationService.addNewCustomer(customerRegistrationDetails);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			apiResponse.setResponseStatus(ResponseStatus.INTERNAL_ERROR);

		}

		return apiResponse;
	}

}
