




package com.amx.jax.personaldetails.controller;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.amx.jax.personaldetails.service.PersonalDetailsService;
import com.insurance.personaldetails.interfaces.IPersonalDetails;


@RestController
public class PersonalDetailsController implements IPersonalDetails
{
	private static final Logger logger = LoggerFactory.getLogger(PersonalDetailsController.class);

	String TAG = "com.amx.jax.personaldetails.controller.PersonalDetailsCuntroller :- ";
	
	@Autowired
	public PersonalDetailsService personalDetailsService;

	@RequestMapping(value = "/business", method = RequestMethod.GET , produces = "application/json")
	public ArrayList getBusiness()
	{
		return personalDetailsService.getBusiness();
	}
	
	@RequestMapping(value = "/nationality", method = RequestMethod.GET , produces = "application/json")
	public ArrayList getNationality()
	{
		return personalDetailsService.getNationality();
	}
	
	@RequestMapping(value = "/governorates", method = RequestMethod.GET , produces = "application/json")
	public ArrayList getGovernorates()
	{
		return personalDetailsService.getGovernorates();
	}
	
	@RequestMapping(value = "/area", method = RequestMethod.GET , produces = "application/json")
	public ArrayList getArea(@RequestParam("gov") String gov)
	{
		return personalDetailsService.getArea(gov);
	}
	
}
