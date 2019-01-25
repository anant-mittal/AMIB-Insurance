
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

import com.amx.jax.WebAppStatus.ApiWebAppStatus;
import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.models.CustomerProfileDetailResponse;
import com.amx.jax.models.CustomerProfileUpdateRequest;
import com.amx.jax.services.PersonalDetailsService;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.ApiOperation;

@RestController
public class PersonalDetailsController {
	private static final Logger logger = LoggerFactory.getLogger(PersonalDetailsController.class);

	@Autowired
	public PersonalDetailsService personalDetailsService;

	@ApiOperation(value = "returns customer personal details", notes = "this api can be excessed only after customer gets login, this will return personal details of customer")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/personal/profiledetails", method = RequestMethod.POST)
	public AmxApiResponse<CustomerProfileDetailResponse, Object> getProfileDetails() {
		return personalDetailsService.getProfileDetails();
	}

	@ApiOperation(value = "update customer personal details", notes = "here customer details is updated. Now if customer tries to enter or update email id and mobile number it "
			+ "will throw a DOTP_REQUIRED_INVALID messageKey , if customer tries to enter or update only email id it will throw a EOTP_REQUIRED messageKey and if customer tries "
			+ "to enter or update only mobile number it will throw a MOTP_REQUIRED messageKey "
			+ "Same API has to hit again with required otp to update customer details")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/personal/update-profiledetails", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> updateProfileDetails(
			@RequestHeader(value = "mOtp", required = false) String mOtpHeader,
			@RequestHeader(value = "eOtp", required = false) String eOtpHeader,
			@RequestBody CustomerProfileUpdateRequest customerProfileUpdateRequest) {
		return personalDetailsService.updateProfileDetails(mOtpHeader, eOtpHeader, customerProfileUpdateRequest);
	}

	@ApiOperation(value = "returns personal details profession meta data for dropdown")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/personal/business", method = RequestMethod.GET)
	public AmxApiResponse<?, Object> getBusiness() {
		return personalDetailsService.getBusiness();
	}

	@ApiOperation(value = "returns personal details nationality meta data for dropdown")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/personal/nationality", method = RequestMethod.GET)
	public AmxApiResponse<?, Object> getNationality() {
		return personalDetailsService.getNationality();
	}

	@ApiOperation(value = "returns personal details governorate meta data for dropdown")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/personal/governorates", method = RequestMethod.GET)
	public AmxApiResponse<?, Object> getGovernorates() {
		return personalDetailsService.getGovernorates();
	}

	@ApiOperation(value = "returns personal details area meta data for dropdown")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/personal/area", method = RequestMethod.GET)
	public AmxApiResponse<?, Object> getArea(@RequestParam("gov") String gov) {
		return personalDetailsService.getArea(gov);
	}

	@ApiOperation(value = "returns personal details gender meta data for dropdown")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/personal/gender", method = RequestMethod.GET)
	public AmxApiResponse<?, Object> getGender() {
		return personalDetailsService.getGender();
	}
}
