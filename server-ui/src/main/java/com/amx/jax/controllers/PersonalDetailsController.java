
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
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.models.CustomerDetailModel;
import com.amx.jax.models.CustomerDetailRequest;
import com.amx.jax.models.CustomerDetailResponse;
import com.amx.jax.models.CustomerProfileDetailRequest;
import com.amx.jax.models.Validate;
import com.amx.jax.services.CustomerRegistrationService;
import com.amx.jax.services.PersonalDetailsService;
import com.insurance.email.model.Email;
import com.insurance.generateotp.ResponseOtpModel;

@RestController
public class PersonalDetailsController
{
	private static final Logger logger = LoggerFactory.getLogger(PersonalDetailsController.class);

	String TAG = "com.amx.jax.personaldetails.controller.PersonalDetailsCuntroller :- ";

	@Autowired
	public PersonalDetailsService personalDetailsService;

	@RequestMapping(value = "/api/personal/userdetails", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<CustomerDetailResponse, Object> getUserDetails()
	{
		return personalDetailsService.getUserDetails();
	}

	@RequestMapping(value = "/api/personal/profiledetails", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<CustomerDetailResponse, Object> getUserProfileDetails()
	{
		return personalDetailsService.getUserProfileDetails();
	}

	@RequestMapping(value = "/api/personal/business", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getBusiness()
	{
		return personalDetailsService.getBusiness();
	}

	@RequestMapping(value = "/api/personal/nationality", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getNationality()
	{
		return personalDetailsService.getNationality();
	}

	@RequestMapping(value = "/api/personal/governorates", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getGovernorates()
	{
		return personalDetailsService.getGovernorates();
	}

	@RequestMapping(value = "/api/personal/area", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getArea(@RequestParam("gov") String gov)
	{
		return personalDetailsService.getArea(gov);
	}

	@RequestMapping(value = "/api/personal/gender", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getGender()
	{
		return personalDetailsService.getGender();
	}
}
