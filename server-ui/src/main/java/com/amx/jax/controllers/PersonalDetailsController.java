
package com.amx.jax.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.models.CustomerProfileDetailResponse;
import com.amx.jax.models.CustomerProfileUpdateRequest;
import com.amx.jax.services.PersonalDetailsService;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.ApiOperation;

@RestController
public class PersonalDetailsController
{
	private static final Logger logger = LoggerFactory.getLogger(PersonalDetailsController.class);
	
	@Autowired
	public PersonalDetailsService personalDetailsService;

	@ApiOperation(value = "returns customer personal details")
	@RequestMapping(value = "/api/personal/profiledetails", method = RequestMethod.POST)
	public AmxApiResponse<CustomerProfileDetailResponse, Object> getProfileDetails()
	{
		return personalDetailsService.getProfileDetails();
	}

	@ApiOperation(value = "update customer personal details")
	@RequestMapping(value = "/api/personal/update-profiledetails", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> updateProfileDetails(@RequestHeader(value = "mOtp", required = false) String mOtpHeader, @RequestHeader(value = "eOtp", required = false) String eOtpHeader, @RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp,
			@RequestBody CustomerProfileUpdateRequest customerProfileUpdateRequest)
	{
		mOtp = ArgUtil.ifNotEmpty(mOtp, mOtpHeader);
		eOtp = ArgUtil.ifNotEmpty(eOtp, eOtpHeader);
		return personalDetailsService.updateProfileDetails(mOtp, eOtp, customerProfileUpdateRequest);
	}

	@ApiOperation(value = "returns personal details profession meta info")
	@RequestMapping(value = "/api/personal/business", method = RequestMethod.GET)
	public AmxApiResponse<?, Object> getBusiness()
	{
		return personalDetailsService.getBusiness();
	}

	@ApiOperation(value = "returns personal details nationality meta info")
	@RequestMapping(value = "/api/personal/nationality", method = RequestMethod.GET)
	public AmxApiResponse<?, Object> getNationality()
	{
		return personalDetailsService.getNationality();
	}

	@ApiOperation(value = "returns personal details governorate meta info")
	@RequestMapping(value = "/api/personal/governorates", method = RequestMethod.GET)
	public AmxApiResponse<?, Object> getGovernorates()
	{
		return personalDetailsService.getGovernorates();
	}

	@ApiOperation(value = "returns personal details area meta info")
	@RequestMapping(value = "/api/personal/area", method = RequestMethod.GET)
	public AmxApiResponse<?, Object> getArea(@RequestParam("gov") String gov)
	{
		return personalDetailsService.getArea(gov);
	}

	@ApiOperation(value = "returns personal details gender meta info")
	@RequestMapping(value = "/api/personal/gender", method = RequestMethod.GET)
	public AmxApiResponse<?, Object> getGender()
	{
		return personalDetailsService.getGender();
	}
}
