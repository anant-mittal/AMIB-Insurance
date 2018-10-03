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
import com.amx.jax.models.CustomizeQuoteModel;
import com.amx.jax.services.CustomizeQuoteService;
import com.amx.utils.ArgUtil;

@RestController
public class CustomizeQuoteController
{
	String TAG = "com.amx.jax.controllers :: CustomizeQuoteController :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomizeQuoteController.class);

	@Autowired
	private CustomizeQuoteService customizeQuoteService;

	@RequestMapping(value = "/api/customize-quote/get-quote-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getCustomizedQuoteDetails(@RequestParam(name = "appSeqNumber" , required = false) String appSeqNumber , @RequestParam(name = "quoteSeqNumber" , required = false) String quoteSeqNumber)
	{
		
		BigDecimal appSeqNumberDet = null;
		if (null != appSeqNumber && !appSeqNumber.equals("") && !appSeqNumber.equalsIgnoreCase("null"))
		{
			appSeqNumberDet = ArgUtil.parseAsBigDecimal(appSeqNumber);
		}
		
		BigDecimal quoteSeqNumberDet = null;
		if (null != quoteSeqNumber && !quoteSeqNumber.equals("") && !quoteSeqNumber.equalsIgnoreCase("null"))
		{
			quoteSeqNumberDet = ArgUtil.parseAsBigDecimal(quoteSeqNumber);
		}
		
		return customizeQuoteService.getCustomizedQuoteDetails(appSeqNumberDet , quoteSeqNumberDet);
	}

	@RequestMapping(value = "/api/customize-quote/calculate-quote", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> calculateCutomizeQuote(@RequestBody CustomizeQuoteModel customizeQuoteModel)
	{
		return customizeQuoteService.calculateCutomizeQuote(customizeQuoteModel);
	}

	@RequestMapping(value = "/api/customize-quote/get-quoteseq-list", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getQuoteSeqList()
	{
		return customizeQuoteService.getQuoteSeqList();
	}
	
	@RequestMapping(value = "/api/customize-quote/terms-condition", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getTermsAndCondition()
	{
		return customizeQuoteService.getTermsAndCondition();
	}
	
	@RequestMapping(value = "/api/customize-quote/submit-quote", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> saveCustomizeQuote(@RequestParam(name = "appSeqNumber") String appSeqNumber ,@RequestBody CustomizeQuoteModel customizeQuoteModel)
	{
		BigDecimal appSeqNumberDet = null;
		if (null != appSeqNumber && !appSeqNumber.equals("") && !appSeqNumber.equalsIgnoreCase("null"))
		{
			appSeqNumberDet = ArgUtil.parseAsBigDecimal(appSeqNumber);
		}
		return customizeQuoteService.saveCustomizeQuote(appSeqNumberDet,customizeQuoteModel);
	}
}
