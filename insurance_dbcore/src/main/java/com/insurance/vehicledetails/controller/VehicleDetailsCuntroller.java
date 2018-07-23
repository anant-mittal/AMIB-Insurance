




package com.insurance.vehicledetails.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.insurance.vehicledetails.service.VehicleDetailsService;

@RestController
public class VehicleDetailsCuntroller
{
	private static final Logger logger = LoggerFactory.getLogger(VehicleDetailsCuntroller.class);

	String TAG = "com.insurance.vehicledetails.controller.PersonalDetailsCuntroller :- ";

	@Autowired
	public VehicleDetailsService vehicleDetailsService;

	@RequestMapping(value = "/make", method = RequestMethod.GET)
	public ArrayList getMake()
	{
		return vehicleDetailsService.getMake();
		
	}

	@RequestMapping(value = "/model", method = RequestMethod.GET)
	public ArrayList getModel(@RequestParam("make") String make)
	{
		logger.info(TAG + " getModel :: make :" + make);
		return vehicleDetailsService.getModel(make);
		
	}
	
	@RequestMapping(value = "/fueltype", method = RequestMethod.GET)
	public ArrayList getFuleType()
	{
		return vehicleDetailsService.getFuleType();
	}
	
	@RequestMapping(value = "/purpose", method = RequestMethod.GET)
	public ArrayList getPurpose()
	{
		return vehicleDetailsService.getPurpose();
		
	}
	
	@RequestMapping(value = "/shape", method = RequestMethod.GET)
	public ArrayList getShape()
	{
		return vehicleDetailsService.getShape();
	}
	
	@RequestMapping(value = "/colour", method = RequestMethod.GET)
	public ArrayList getColour()
	{
		return vehicleDetailsService.getColour();
	}
	
	
	
	
	
}
