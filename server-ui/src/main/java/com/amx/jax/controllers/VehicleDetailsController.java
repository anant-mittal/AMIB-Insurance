




package com.amx.jax.controllers;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.models.Model;
import com.amx.jax.services.VehicleDetailsService;

@RestController
public class VehicleDetailsController
{
	private static final Logger logger = LoggerFactory.getLogger(VehicleDetailsController.class);

	String TAG = "com.amx.jax.vehicledetails.controller.VehicleDetailsController :- ";

	@Autowired
	public VehicleDetailsService vehicleDetailsService;

	@RequestMapping(value = "/make", method = RequestMethod.GET , produces = "application/json")
	public ArrayList getMake()
	{
		return vehicleDetailsService.getMake();

	}

	@RequestMapping(value = "/model", method = RequestMethod.GET , produces = "application/json")
	public AmxApiResponse<Model, Object> getModel(@RequestParam("make") String make)
	{
		logger.info(TAG + " getModel :: make :" + make);
		return vehicleDetailsService.getModel(make);
	}

	@RequestMapping(value = "/fueltype", method = RequestMethod.GET , produces = "application/json")
	public ArrayList getFuleType()
	{
		return vehicleDetailsService.getFuleType();
	}

	@RequestMapping(value = "/purpose", method = RequestMethod.GET , produces = "application/json")
	public ArrayList getPurpose()
	{
		return vehicleDetailsService.getPurpose();

	}

	@RequestMapping(value = "/shape", method = RequestMethod.GET , produces = "application/json")
	public ArrayList getShape()
	{
		return vehicleDetailsService.getShape();
	}

	@RequestMapping(value = "/colour", method = RequestMethod.GET , produces = "application/json")
	public ArrayList getColour()
	{
		logger.info(TAG + " getColour :: ");
		return vehicleDetailsService.getColour();
	}
		
}
