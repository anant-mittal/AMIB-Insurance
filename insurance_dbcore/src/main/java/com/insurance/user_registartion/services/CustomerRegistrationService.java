




package com.insurance.user_registartion.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.util.concurrent.ExecutionError;
import com.insurance.error.GlobalException;
import com.insurance.error.JaxError;
import com.insurance.response.ApiResponse;
import com.insurance.services.AbstractService;
import com.insurance.user_registartion.dao.CustomerRegistrationDao;
import com.insurance.user_registartion.model.CustomerRegistrationDetails;

public class CustomerRegistrationService extends AbstractService
{

	@Autowired
	CustomerRegistrationDao customerRegistrationDao;

	@Override
	public String getModelType()
	{
		return null;
	}

	public ApiResponse addNewCustomer(CustomerRegistrationDetails customerRegistrationDetails)
	{
		try
		{
			ApiResponse apiResponse = customerRegistrationDao.addNewCustomer(customerRegistrationDetails);
			return apiResponse;
		}
		catch (Exception e)
		{
			throw new GlobalException("Customer Registration Failed Kindly try after soem time .", JaxError.EXCHANGE_RATE_NOT_FOUND);
		}
	}

}
