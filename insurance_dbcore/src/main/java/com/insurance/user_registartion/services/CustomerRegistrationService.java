




package com.insurance.user_registartion.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.insurance.constant.CommunicationChannel;
import com.insurance.response.ApiResponse;
import com.insurance.response.ResponseStatus;
import com.insurance.services.AbstractService;
import com.insurance.user_registartion.dao.CustomerRegistrationDao;
import com.insurance.user_registartion.model.CivilIdOtpModel;
import com.insurance.user_registartion.model.CustomerPersonalDetail;
import com.insurance.user_registartion.model.PersonInfo;

@Service
public class CustomerRegistrationService extends AbstractService
{

	@Autowired
	CustomerRegistrationDao customerRegistrationDao;

	@Override
	public String getModelType()
	{
		return null;
	}

	public ApiResponse addNewCustomer(CustomerPersonalDetail customerPersonalDetail)
	{
		ApiResponse apiResponse = null;
		
		try
		{
			apiResponse = customerRegistrationDao.addNewCustomer(customerPersonalDetail);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return apiResponse;
	}
	
	public ApiResponse getCompanySetUp(int langind)
	{
		ApiResponse apiResponse = null;
		
		try
		{
			apiResponse = customerRegistrationDao.getCompanySetUp(langind);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return apiResponse;
	}
	
}
