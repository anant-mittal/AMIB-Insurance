package com.insurance.user_registartion.interfaces;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.insurance.user_registartion.model.CustomerPersonalDetail;

public interface ICustomerRegistration
{
	public String addNewCustomer(@RequestBody CustomerPersonalDetail customerPersonalDetail);
	
	public ArrayList getCompanySetUp(@RequestParam("langind") int langind);
	
}
