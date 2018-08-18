
package com.amx.jax.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.models.CustomerDetailResponse;
import com.amx.jax.models.CustomerProfileDetailResponse;
import com.amx.jax.models.CustomerProfileUpdateRequest;
import com.amx.jax.models.CustomerProfileUpdateResponse;
import com.amx.jax.services.PersonalDetailsService;
import com.insurance.generateotp.RequestOtpModel;

@RestController
public class PersonalDetailsController
{
	private static final Logger logger = LoggerFactory.getLogger(PersonalDetailsController.class);

	String TAG = "com.amx.jax.personaldetails.controller.PersonalDetailsCuntroller :- ";

	@Autowired
	public PersonalDetailsService personalDetailsService;

	@RequestMapping(value = "/api/personal/profiledetails", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<CustomerProfileDetailResponse, Object> getProfileDetails()
	{
		return personalDetailsService.getProfileDetails();
	}

	@RequestMapping(value = "/api/personal/update-profiledetails", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<CustomerProfileUpdateResponse, Object> updateProfileDetails(@RequestBody CustomerProfileUpdateRequest customerProfileUpdateRequest)
	{
		return personalDetailsService.updateProfileDetails(customerProfileUpdateRequest);
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
