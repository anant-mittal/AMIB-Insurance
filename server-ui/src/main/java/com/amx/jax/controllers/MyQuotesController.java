package com.amx.jax.controllers;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.models.ActivePolicyModel;
import com.amx.jax.models.VehicleDetails;
import com.amx.jax.services.MyQuotesService;
import com.amx.utils.ArgUtil;

@RestController
public class MyQuotesController
{
	String TAG = "com.amx.jax.controllers :: MyQuotesController :: ";

	private static final Logger logger = LoggerFactory.getLogger(MyQuotesController.class);

	@Autowired
	private MyQuotesService myQuotesService;

	@RequestMapping(value = "/api/myquote/get-myquotes", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getUserQuote()
	{
		logger.info(TAG + " getUserQuote :: ");
		return myQuotesService.getUserQuote();
	}
}
