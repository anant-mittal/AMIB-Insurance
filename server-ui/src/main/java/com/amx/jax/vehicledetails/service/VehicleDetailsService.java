




package com.amx.jax.vehicledetails.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.insurance.vehicledetails.model.Model;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.insurance.vehicledetails.dao.VehicleDetailsDao;
import com.insurance.vehicledetails.interfaces.IVehicleDetails;

@Service
public class VehicleDetailsService implements IVehicleDetails
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

	public AmxApiResponse<Model, Object> getModel(String make)
	{
		AmxApiResponse<Model, Object> resp = new AmxApiResponse<Model, Object>();
		resp.setResults(vehicleDetailsDao.getModel(make));
		resp.setStatus(ApiConstants.Success);
		return resp;
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
