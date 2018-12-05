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
import com.amx.jax.services.MyQuotesService;
import com.amx.jax.swagger.ApiMockParam;

import io.swagger.annotations.ApiOperation;

@RestController
public class MyQuotesController
{
	private static final Logger logger = LoggerFactory.getLogger(MyQuotesController.class);

	@Autowired
	private MyQuotesService myQuotesService;

	@ApiOperation(value = "return the list of my quotes created by the customer")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/myquote/get-myquotes", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getUserQuote()
	{
		return myQuotesService.getUserQuote();
	}
}
