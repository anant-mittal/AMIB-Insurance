




package com.insurance.personaldetails.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.insurance.personaldetails.dao.PersonalDetailsDao;
import com.insurance.personaldetails.service.PersonalDetailsService;
import com.insurance.response.ApiResponse;

@RestController
public class PersonalDetailsCuntroller
{
	private static final Logger logger = LoggerFactory.getLogger(PersonalDetailsCuntroller.class);

	String TAG = "com.insurance.personaldetails.controller.PersonalDetailsCuntroller :- ";

	@Autowired
	public PersonalDetailsService personalDetailsService;

	@RequestMapping(value = "/business", method = RequestMethod.GET)
	public ApiResponse getBusiness()
	{
		ApiResponse response = personalDetailsService.getBusiness();
		return response;
	}
	
	@RequestMapping(value = "/nationality", method = RequestMethod.GET)
	public ApiResponse getNationality()
	{
		ApiResponse response = personalDetailsService.getNationality();
		return response;
	}
	
	@RequestMapping(value = "/governorates", method = RequestMethod.GET)
	public ApiResponse getGovernorates()
	{
		ApiResponse response = personalDetailsService.getGovernorates();
		return response;
	}
	
	@RequestMapping(value = "/area", method = RequestMethod.GET)
	public ApiResponse getArea(@RequestParam("gov") String gov)
	{
		ApiResponse response = personalDetailsService.getArea(gov);
		return response;
	}
	
}
