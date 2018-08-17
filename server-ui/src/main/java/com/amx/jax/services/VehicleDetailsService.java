
package com.amx.jax.services;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.dao.VehicleDetailsDao;
import com.amx.jax.models.Model;

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

	public AmxApiResponse<Model, Object> getModel(String make)
	{
		AmxApiResponse<Model, Object> resp = new AmxApiResponse<Model, Object>();
		resp.setResults(vehicleDetailsDao.getModel(make));
		resp.setStatus(ApiConstants.SUCCESS);
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
