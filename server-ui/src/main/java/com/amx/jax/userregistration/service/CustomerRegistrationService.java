




package com.amx.jax.userregistration.service;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.insurance.user_registartion.dao.CustomerRegistrationDao;
import com.insurance.user_registartion.interfaces.ICustomerRegistration;
import com.insurance.user_registartion.model.CustomerPersonalDetail;

@Service
public class CustomerRegistrationService implements ICustomerRegistration
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
