
package com.amx.jax.controllers;

import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.models.Model;
import com.amx.jax.services.VehicleDetailsService;

@RestController
public class VehicleDetailsController
{
	private static final Logger logger = LoggerFactory.getLogger(VehicleDetailsController.class);

	String TAG = "com.amx.jax.vehicledetails.controller.VehicleDetailsController :- ";

	@Autowired
	public VehicleDetailsService vehicleDetailsService;

	@RequestMapping(value = "/api/request-quote/pendingquote", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getPendingRequestQuote()
	{
		return vehicleDetailsService.getPendingRequestQuote();
	}

	@RequestMapping(value = "/api/vehicledetails/make", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getMake()
	{
		return vehicleDetailsService.getMake();

	}

	@RequestMapping(value = "/api/vehicledetails/model", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getModel(@RequestParam("make") String make)
	{
		logger.info(TAG + " getModel :: make :" + make);
		return vehicleDetailsService.getModel(make);
	}

	@RequestMapping(value = "/api/vehicledetails/fueltype", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getFuleType()
	{
		return vehicleDetailsService.getFuleType();
	}

	@RequestMapping(value = "/api/vehicledetails/purpose", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getPurpose()
	{
		return vehicleDetailsService.getPurpose();
	}

	@RequestMapping(value = "/api/vehicledetails/shape", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getShape()
	{
		return vehicleDetailsService.getShape();
	}

	@RequestMapping(value = "/api/vehicledetails/colour", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getColour()
	{
		logger.info(TAG + " getColour :: ");
		return vehicleDetailsService.getColour();
	}

	@RequestMapping(value = "/api/vehicledetails/vehicle-condition", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getVehicleCondition()
	{
		logger.info(TAG + " getVehicleCondition :: ");
		return vehicleDetailsService.getVehicleCondition();
	}

	@RequestMapping(value = "/api/vehicledetails/vehicle-agealowed", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getMaxVehicleAgeAllowed()
	{
		return vehicleDetailsService.getMaxVehicleAgeAllowed();
	}

	@RequestMapping(value = "/api/vehicledetails/policy-duartion", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getPolicyDuration()
	{
		return vehicleDetailsService.getPolicyDuration();
	}

}
