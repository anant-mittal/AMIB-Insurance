package com.insurance.vehicledetails.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.insurance.base.model.Business;
import com.insurance.response.ApiResponse;
import com.insurance.response.ResponseStatus;
import com.insurance.user_registartion.model.CustomerPersonalDetail;
import com.insurance.vehicledetails.service.VehicleDetailsService;

@RestController
public class VehicleDetailsCuntroller
{
	String TAG = "com.insurance.vehicledetails.controller.PersonalDetailsCuntroller :- ";
	
	@Autowired
	public VehicleDetailsService vehicleDetailsService;
	
	@RequestMapping(value = "/make", method = RequestMethod.GET)
	public ApiResponse getMake()
	{
		ApiResponse response = vehicleDetailsService.getMake();
		return response;
	}
	

}
