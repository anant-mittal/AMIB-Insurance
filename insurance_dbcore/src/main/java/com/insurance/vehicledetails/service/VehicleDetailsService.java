




package com.insurance.vehicledetails.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.insurance.response.ApiResponse;
import com.insurance.vehicledetails.dao.VehicleDetailsDao;

@Service
public class VehicleDetailsService
{
	@Autowired
	public VehicleDetailsDao vehicleDetailsDao;

	public ApiResponse getMake()
	{
		ApiResponse apiResponse = null;
		try
		{
			apiResponse = vehicleDetailsDao.getMake();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return apiResponse;
	}

	public ApiResponse getModel(String make)
	{
		ApiResponse apiResponse = null;
		try
		{
			apiResponse = vehicleDetailsDao.getModel(make);
			return apiResponse;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return apiResponse;
	}
	
	public ApiResponse getFuleType()
	{
		ApiResponse apiResponse = null;
		try
		{
			apiResponse = vehicleDetailsDao.getFuleType();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return apiResponse;
	}
	
	
	public ApiResponse getPurpose()
	{
		ApiResponse apiResponse = null;
		try
		{
			apiResponse = vehicleDetailsDao.getPurpose();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return apiResponse;
	}
	
	public ApiResponse getShape()
	{
		ApiResponse apiResponse = null;
		try
		{
			apiResponse = vehicleDetailsDao.getShape();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return apiResponse;
	}
	
	public ApiResponse getColour()
	{
		ApiResponse apiResponse = null;
		try
		{
			apiResponse = vehicleDetailsDao.getColour();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return apiResponse;
	}

}
