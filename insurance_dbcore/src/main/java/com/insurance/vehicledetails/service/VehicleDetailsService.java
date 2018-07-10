package com.insurance.vehicledetails.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.insurance.error.GlobalException;
import com.insurance.response.ApiResponse;
import com.insurance.user_registartion.model.CustomerPersonalDetail;
import com.insurance.vehicledetails.dao.VehicleDetailsDao;

public class VehicleDetailsService
{
	@Autowired
	public VehicleDetailsDao vehicleDetailsDao;
	
	
	
	public ApiResponse getMake()
	{
		try
		{
			ApiResponse apiResponse = vehicleDetailsDao.getMake();
			return apiResponse;
		}
		catch (Exception e)
		{
			throw new GlobalException("Kindly try after soem time .");
		}
	}
	
	
	
	public ApiResponse getModel(String make)
	{
		try
		{
			if(null == make || make.equals(""))
			{
				throw new GlobalException("Kindly provide make first .");
			}
			
			ApiResponse apiResponse = vehicleDetailsDao.getModel(make);
			return apiResponse;
		}
		catch (Exception e)
		{
			throw new GlobalException("Kindly try after soem time .");
		}
	}
	
}
