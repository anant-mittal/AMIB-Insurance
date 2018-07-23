




package com.insurance.user_registartion.services;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.insurance.user_registartion.dao.CustomerRegistrationDao;
import com.insurance.user_registartion.model.CivilIdOtpModel;
import com.insurance.user_registartion.model.CustomerPersonalDetail;
import com.insurance.user_registartion.model.PersonInfo;

@Service
public class CustomerRegistrationService
{

	@Autowired
	CustomerRegistrationDao customerRegistrationDao;

	
	public String addNewCustomer(CustomerPersonalDetail customerPersonalDetail)
	{
		try
		{
			return customerRegistrationDao.addNewCustomer(customerPersonalDetail);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList getCompanySetUp(int langind)
	{
		try
		{
			return customerRegistrationDao.getCompanySetUp(langind);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
}
