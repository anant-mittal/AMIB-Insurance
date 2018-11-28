package com.amx.jax.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.services.DashBoardService;

@RestController
public class DashBoardController
{
	private static final Logger logger = LoggerFactory.getLogger(RequestQuoteController.class);

	@Autowired
	public DashBoardService dashBoardService;

	@RequestMapping(value = "/api/dashboard/getdetails", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getIncompleteApplication()
	{
		return dashBoardService.getIncompleteApplication();
	}
}
