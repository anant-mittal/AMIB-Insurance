




package com.insurance.personaldetails.controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.insurance.personaldetails.service.PersonalDetailsService;

@RestController
public class PersonalDetailsCuntroller
{
	private static final Logger logger = LoggerFactory.getLogger(PersonalDetailsCuntroller.class);

	String TAG = "com.insurance.personaldetails.controller.PersonalDetailsCuntroller :- ";

	@Autowired
	public PersonalDetailsService personalDetailsService;

	@RequestMapping(value = "/business", method = RequestMethod.GET)
	public ArrayList getBusiness()
	{
		return personalDetailsService.getBusiness();
	}
	
	@RequestMapping(value = "/nationality", method = RequestMethod.GET)
	public ArrayList getNationality()
	{
		return personalDetailsService.getNationality();
	}
	
	@RequestMapping(value = "/governorates", method = RequestMethod.GET)
	public ArrayList getGovernorates()
	{
		return personalDetailsService.getGovernorates();
	}
	
	@RequestMapping(value = "/area", method = RequestMethod.GET)
	public ArrayList getArea(@RequestParam("gov") String gov)
	{
		return personalDetailsService.getArea(gov);
	}
	
}
