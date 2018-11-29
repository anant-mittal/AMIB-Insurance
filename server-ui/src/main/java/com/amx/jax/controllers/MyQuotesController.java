package com.amx.jax.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.services.MyQuotesService;

@RestController
public class MyQuotesController
{
	private static final Logger logger = LoggerFactory.getLogger(MyQuotesController.class);

	@Autowired
	private MyQuotesService myQuotesService;

	@RequestMapping(value = "/api/myquote/get-myquotes", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getUserQuote()
	{
		return myQuotesService.getUserQuote();
	}
}
