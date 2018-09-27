package com.amx.jax.controllers;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.models.ActivePolicyModel;
import com.amx.jax.models.RequestQuoteDetrails;
import com.amx.jax.models.Validate;
import com.amx.jax.services.MyPolicyService;
import com.amx.utils.ArgUtil;
import com.amx.jax.api.AmxApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyPolicyController
{
	String TAG = "com.amx.jax.controllers :: MyPolicyController :: ";

	private static final Logger logger = LoggerFactory.getLogger(MyPolicyController.class);

	@Autowired
	private MyPolicyService myPolicyService;

	@RequestMapping(value = "/api/mypolicy/get-activepolicy", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<ActivePolicyModel, Object> getUserActivePolicy()
	{
		return myPolicyService.getUserActivePolicy();
	}
	
	@RequestMapping(value = "/api/mypolicy/renew-policy", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> renewInsuranceOldPolicy(@RequestParam(name = "renewAppDocNumber") String renewAppDocNumber)
	{
		BigDecimal renewAppDocNumberDet = null;
		if (null != renewAppDocNumber && !renewAppDocNumber.equals("") && !renewAppDocNumber.equalsIgnoreCase("null"))
		{
			renewAppDocNumberDet = ArgUtil.parseAsBigDecimal(renewAppDocNumber);
		}
		return myPolicyService.renewInsuranceOldPolicy(renewAppDocNumberDet);
	}
}
