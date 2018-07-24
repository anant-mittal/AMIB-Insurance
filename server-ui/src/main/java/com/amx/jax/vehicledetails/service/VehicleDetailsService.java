




package com.amx.jax.vehicledetails.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.insurance.vehicledetails.dao.VehicleDetailsDao;

@Service
public class VehicleDetailsService
{
	
	private static final Logger logger = LoggerFactory.getLogger(VehicleDetailsService.class);
	String TAG = "com.amx.jax.vehicledetails.service.PersonalDetailsCuntroller :- ";
	
	@Autowired
	public VehicleDetailsDao vehicleDetailsDao;

	public ArrayList getMake()
	{
		try
		{
			return vehicleDetailsDao.getMake();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList getModel(String make)
	{
		
		try
		{
			return vehicleDetailsDao.getModel(make);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList getFuleType()
	{
		
		try
		{
			return vehicleDetailsDao.getFuleType();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	public ArrayList getPurpose()
	{
		
		try
		{
			return vehicleDetailsDao.getPurpose();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList getShape()
	{
		
		try
		{
			return vehicleDetailsDao.getShape();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList getColour()
	{
		
		try
		{
			logger.info(TAG + " getColour :: ");
			return vehicleDetailsDao.getColour();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
