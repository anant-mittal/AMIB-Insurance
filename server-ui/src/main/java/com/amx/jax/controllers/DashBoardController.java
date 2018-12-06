package com.amx.jax.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.WebAppStatus.ApiWebAppStatus;
import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.Message;
import com.amx.jax.services.DashBoardService;

import io.swagger.annotations.ApiOperation;

@RestController
public class DashBoardController {
	private static final Logger logger = LoggerFactory.getLogger(RequestQuoteController.class);

	@Autowired
	public DashBoardService dashBoardService;

	@ApiOperation(value = "returns incomplete quote details of the customer", notes = "this api can be excessed only after customer gets login, based on customer_sequence_number which you get from user details will return incomplete quote details, here customer sequence number is taken from session so no need to send it in api.")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/dashboard/getdetails", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getIncompleteApplication() 
	{
		logger.info("getIncompleteApplication :: Tets :");
		
		return dashBoardService.getIncompleteApplication();
	}
}
