




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

	@Autowired
	private CustomerPersonalDetail customerPersonalDetail;

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationController.class);

	@RequestMapping(value = "/customer-registration/", method = RequestMethod.POST)
	public ApiResponse addNewCustomer(@RequestBody CustomerPersonalDetail customerPersonalDetail)
	{
		ApiResponse apiResponse = null;

		try
		{
			logger.info(TAG + " saveNewCustomer :: customerRegistrationDetails :" + customerPersonalDetail.toString());

			apiResponse = customerRegistrationService.addNewCustomer(customerPersonalDetail);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			apiResponse.setResponseStatus(ResponseStatus.INTERNAL_ERROR);

		}
		return apiResponse;
	}

	@RequestMapping(value = "/send-otp/", method = RequestMethod.GET)
	public ApiResponse sendOtp()
	{
		logger.info("in sendOtp Request");
		ApiResponse response = customerRegistrationService.sendOtpForCivilId(null);
		return response;
	}

}
