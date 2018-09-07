
package com.amx.jax.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.models.ImageMandatoryResponse;
import com.amx.jax.models.ImageUploadStatusResponse;
import com.amx.jax.models.IncompleteApplResponse;
import com.amx.jax.models.RequestQuoteModel;
import com.amx.jax.services.RequestQuoteService;

@RestController
public class RequestQuoteController
{
	private static final Logger logger = LoggerFactory.getLogger(RequestQuoteController.class);

	String TAG = "com.amx.jax.vehicledetails.controller.VehicleDetailsController :- ";

	@Autowired
	public RequestQuoteService requestQuoteService;

	/*
	 * @RequestMapping(value = "/api/request-quote/get-quote-Details", method =
	 * RequestMethod.POST, produces = "application/json") public
	 * AmxApiResponse<?, Object> getQuoteDetails(@RequestBody RequestQuoteModel
	 * requestQuoteModel) { return
	 * requestQuoteService.getQuoteDetails(requestQuoteModel); }
	 * 
	 * @RequestMapping(value = "/api/request-quote/set-quote-Details", method =
	 * RequestMethod.POST, produces = "application/json") public
	 * AmxApiResponse<?, Object> setQuoteDetails(@RequestBody RequestQuoteModel
	 * requestQuoteModel) { return
	 * requestQuoteService.setQuoteDetails(requestQuoteModel); }
	 */

	@RequestMapping(value = "/api/vehicledetails/make", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getMake()
	{
		return requestQuoteService.getMake();
	}

	@RequestMapping(value = "/api/vehicledetails/sub-make", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getModel(@RequestParam("make") String make)
	{
		logger.info(TAG + " getModel :: make :" + make);
		return requestQuoteService.getModel(make);
	}

	@RequestMapping(value = "/api/vehicledetails/fueltype", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getFuleType()
	{
		return requestQuoteService.getFuleType();
	}

	@RequestMapping(value = "/api/vehicledetails/purpose", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getPurpose()
	{
		return requestQuoteService.getPurpose();
	}

	@RequestMapping(value = "/api/vehicledetails/shape", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getShape()
	{
		return requestQuoteService.getShape();
	}

	@RequestMapping(value = "/api/vehicledetails/colour", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getColour()
	{
		logger.info(TAG + " getColour :: ");
		return requestQuoteService.getColour();
	}

	@RequestMapping(value = "/api/vehicledetails/vehicle-condition", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getVehicleCondition()
	{
		logger.info(TAG + " getVehicleCondition :: ");
		return requestQuoteService.getVehicleCondition();
	}

	@RequestMapping(value = "/api/vehicledetails/model-year", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getMaxVehicleAgeAllowed()
	{
		return requestQuoteService.getMaxVehicleAgeAllowed();
	}

	@RequestMapping(value = "/api/vehicledetails/policy-duartion", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getPolicyDuration()
	{
		return requestQuoteService.getPolicyDuration();
	}

	@RequestMapping(value = "/api/image/mandatory-image", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<ImageMandatoryResponse, Object> getMandatoryImage()
	{
		return requestQuoteService.getMandatoryImage();
	}

	@RequestMapping(value = "/api/image/image-upload-check", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<ImageUploadStatusResponse, Object> checkIfImageAlreadyUploaded(@RequestParam("docType") String docType)
	{
		return requestQuoteService.checkIfImageAlreadyUploaded(docType);
	}

	@RequestMapping(value = "/api/request-quote/get-pending-policy", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getIncompleteApplication()
	{
		return requestQuoteService.getIncompleteApplication();
	}

	@RequestMapping(value = "/api/request-quote/get-vehicle-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getAppVehicleDetails(@RequestBody RequestQuoteModel requestQuoteModel)
	{
		return requestQuoteService.getAppVehicleDetails(requestQuoteModel);
	}

	@RequestMapping(value = "/api/request-quote/update-vehicle-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> insUpdateVehicleDetails(@RequestBody RequestQuoteModel requestQuoteModel)
	{
		return requestQuoteService.insUpdateVehicleDetails(requestQuoteModel);
	}
	
	@RequestMapping(value = "/api/request-quote/get-personal-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getPersonalDetails(@RequestBody RequestQuoteModel requestQuoteModel)
	{
		return requestQuoteService.getPersonalDetails(requestQuoteModel);
	}
}
