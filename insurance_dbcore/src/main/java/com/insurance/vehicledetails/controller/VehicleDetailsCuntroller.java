




package com.insurance.vehicledetails.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.insurance.response.ApiResponse;
import com.insurance.vehicledetails.dao.VehicleDetailsDao;
import com.insurance.vehicledetails.service.VehicleDetailsService;

@RestController
public class VehicleDetailsCuntroller
{
	private static final Logger logger = LoggerFactory.getLogger(VehicleDetailsCuntroller.class);

	String TAG = "com.insurance.vehicledetails.controller.PersonalDetailsCuntroller :- ";

	@Autowired
	public VehicleDetailsService vehicleDetailsService;

	@RequestMapping(value = "/make", method = RequestMethod.GET)
	public ApiResponse getMake()
	{
		ApiResponse response = vehicleDetailsService.getMake();
		return response;
	}

	@RequestMapping(value = "/model", method = RequestMethod.GET)
	public ApiResponse getModel(@RequestParam("make") String make)
	{
		logger.info(TAG + " getModel :: make :" + make);
		ApiResponse response = vehicleDetailsService.getModel(make);
		return response;
	}
	
	@RequestMapping(value = "/fueltype", method = RequestMethod.GET)
	public ApiResponse getFuleType()
	{
		ApiResponse response = vehicleDetailsService.getFuleType();
		return response;
	}
	
	@RequestMapping(value = "/purpose", method = RequestMethod.GET)
	public ApiResponse getPurpose()
	{
		ApiResponse response = vehicleDetailsService.getPurpose();
		return response;
	}
	
	@RequestMapping(value = "/shape", method = RequestMethod.GET)
	public ApiResponse getShape()
	{
		ApiResponse response = vehicleDetailsService.getShape();
		return response;
	}
	
	@RequestMapping(value = "/colour", method = RequestMethod.GET)
	public ApiResponse getColour()
	{
		ApiResponse response = vehicleDetailsService.getColour();
		return response;
	}
	
	
	
	
	
}
