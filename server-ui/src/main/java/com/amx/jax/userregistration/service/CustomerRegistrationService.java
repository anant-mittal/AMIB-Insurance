




package com.amx.jax.userregistration.service;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.userregistration.controller.CustomerRegistrationController;
import com.insurance.email.dao.EmailNotification;
import com.insurance.email.model.Email;
import com.insurance.user_registartion.dao.CustomerRegistrationDao;
import com.insurance.user_registartion.interfaces.ICustomerRegistration;
import com.insurance.user_registartion.model.CustomerPersonalDetail;
import com.insurance.vehicledetails.model.Model;

@Service
public class CustomerRegistrationService implements ICustomerRegistration
{
	String TAG = "com.amx.jax.userregistration.service :: CustomerRegistrationService :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationService.class);

	@Autowired
	CustomerRegistrationDao customerRegistrationDao;

	@Autowired
	EmailNotification emailNotification;

	public AmxApiResponse<BoolRespModel, Object> isValidCivilId(String civilid)
	{
		AmxApiResponse<BoolRespModel, Object> resp = new AmxApiResponse<BoolRespModel, Object>();
		resp.build(new BoolRespModel(customerRegistrationDao.isValidCivilId(civilid)));
		resp.setStatus(ApiConstants.Success);
		return resp;
	}
	
	public AmxApiResponse<BoolRespModel, Object> isCivilIdExist(String civilid)
	{
		AmxApiResponse<BoolRespModel, Object> resp = new AmxApiResponse<BoolRespModel, Object>();
		resp.build(new BoolRespModel(customerRegistrationDao.isCivilIdExist(civilid)));
		resp.setStatus(ApiConstants.Success);
		return resp;
			
	}

	public AmxApiResponse<BoolRespModel, Object> isValidEmailId(String emailId)
	{
		AmxApiResponse<BoolRespModel, Object> resp = new AmxApiResponse<BoolRespModel, Object>();
		resp.build(new BoolRespModel(validate(emailId)));
		resp.setStatus(ApiConstants.Success);
		return resp;
	}

	public static boolean validate(String emailStr)
	{
		Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}

	public AmxApiResponse<BoolRespModel, Object> isValidMobileNumber(String mobileNumber)
	{
		AmxApiResponse<BoolRespModel, Object> resp = new AmxApiResponse<BoolRespModel, Object>();
		resp.build(new BoolRespModel(customerRegistrationDao.isValidMobileNumber(mobileNumber)));
		resp.setStatus(ApiConstants.Success);
		return resp;
	}

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

	public ArrayList getCompanySetUp(int langId)
	{
		try
		{
			return customerRegistrationDao.getCompanySetUp(langId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public void sendEmail(Email email)
	{
		try
		{
			logger.info(TAG + " sendEmail :: email :" + email);
			emailNotification.sendEmail(email);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
