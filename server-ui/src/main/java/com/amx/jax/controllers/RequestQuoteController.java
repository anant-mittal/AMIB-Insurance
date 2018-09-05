package com.amx.jax.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amx.jax.models.IncompleteApplResponse;
import com.amx.jax.services.RequestQuoteService;
import com.amx.jax.api.AmxApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestQuoteController
{
	String TAG = "com.amx.jax.controllers :: RequestQuoteController :: ";

	private static final Logger logger = LoggerFactory.getLogger(ActivePolicyController.class);

	@Autowired
	private RequestQuoteService requestQuoteService;

	@RequestMapping(value = "/api/reqquote/get-incomplete-policy", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<IncompleteApplResponse, Object> getIncompleteApplication()
	{
		return requestQuoteService.getIncompleteApplication();
	}
}
