package com.amx.jax.controllers;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.services.CustomizeQuoteService;
import com.amx.jax.services.MyQuotesService;
import com.amx.utils.ArgUtil;

@RestController
public class CustomizeQuoteController
{
	String TAG = "com.amx.jax.controllers :: CustomizeQuoteController :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomizeQuoteController.class);
	
	@Autowired
	private CustomizeQuoteService customizeQuoteService;
	
	@RequestMapping(value = "/api/customize-quote/get-quote-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getCustomizedQuoteDetails(@RequestParam(name = "appSeqNumber", required = false) String appSeqNumber)
	{
		BigDecimal appSeqNumberDet = null;
		if (null != appSeqNumber && !appSeqNumber.equals("") && !appSeqNumber.equalsIgnoreCase("null"))
		{
			appSeqNumberDet = ArgUtil.parseAsBigDecimal(appSeqNumber);
		}
		return customizeQuoteService.getCustomizedQuoteDetails(appSeqNumberDet);
	}

	@RequestMapping(value = "/api/customize-quote/get-replacement-list", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getReplacementTypeList(@RequestParam(name = "appSeqNumber") String appSeqNumber, @RequestParam(name = "addPolicyTypeCode") String addPolicyTypeCode)
	{
		BigDecimal appSeqNumberDet = null;
		if (null != appSeqNumber && !appSeqNumber.equals("") && !appSeqNumber.equalsIgnoreCase("null"))
		{
			appSeqNumberDet = ArgUtil.parseAsBigDecimal(appSeqNumber);
		}
		return customizeQuoteService.getReplacementTypeList(appSeqNumberDet, addPolicyTypeCode);
	}
}
