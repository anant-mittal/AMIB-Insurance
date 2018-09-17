
package com.amx.jax.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.models.RequestQuoteModel;
import com.amx.jax.services.RequestQuoteService;
import com.amx.utils.ArgUtil;

@RestController
public class RequestQuoteController
{
	private static final Logger logger = LoggerFactory.getLogger(RequestQuoteController.class);

	String TAG = "com.amx.jax.controllers.RequestQuoteController :- ";

	@Autowired
	public RequestQuoteService requestQuoteService;

	@RequestMapping(value = "/api/request-quote/get-pending-policy", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getIncompleteApplication()
	{
		return requestQuoteService.getIncompleteApplication();
	}

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

	@RequestMapping(value = "/api/request-quote/get-vehicle-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getAppVehicleDetails(@RequestBody RequestQuoteModel requestQuoteModel)
	{
		return requestQuoteService.getAppVehicleDetails(requestQuoteModel);
	}

	@RequestMapping(value = "/api/request-quote/set-vehicle-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> setAppVehicleDetails(@RequestBody RequestQuoteModel requestQuoteModel)
	{
		return requestQuoteService.setAppVehicleDetails(requestQuoteModel);
	}

	@RequestMapping(value = "/api/request-quote/get-personal-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getProfileDetails(@RequestBody RequestQuoteModel requestQuoteModel)
	{
		return requestQuoteService.getProfileDetails(requestQuoteModel);
	}

	@RequestMapping(value = "/api/request-quote/set-personal-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> setProfileDetails(@RequestHeader(value = "mOtp", required = false) String mOtpHeader, @RequestHeader(value = "eOtp", required = false) String eOtpHeader, @RequestParam(required = false) String mOtp, @RequestParam(required = false) String eOtp,
			@RequestBody RequestQuoteModel requestQuoteModel)
	{
		mOtp = ArgUtil.ifNotEmpty(mOtp, mOtpHeader);
		eOtp = ArgUtil.ifNotEmpty(eOtp, eOtpHeader);
		return requestQuoteService.setProfileDetails(mOtp, eOtp, requestQuoteModel);
	}

	@RequestMapping(value = "/api/request-quote/get-image-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getImageDetails(@RequestParam("appSeqNumber") BigDecimal appSeqNumber)
	{
		return requestQuoteService.getImageDetails(appSeqNumber);
	}

	@RequestMapping(value = "/api/request-quote/downlaod-vehicle-images" , method = RequestMethod.GET , produces = MediaType.IMAGE_JPEG_VALUE)
	public @ResponseBody byte[] getUploadedImage(@RequestParam("appSeqNumber") BigDecimal appSeqNumber, @RequestParam("docTypeCode") String docTypeCode, @RequestParam(name = "docSeqNumber", required = false) BigDecimal docSeqNumber) throws IOException
	{
		return requestQuoteService.getUploadedImage(appSeqNumber, docTypeCode, docSeqNumber);
	}

	@RequestMapping(value = "/api/request-quote/set-image-upload", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> setUploadImage(@RequestParam MultipartFile file, @RequestParam("appSeqNumber") BigDecimal appSeqNumber, @RequestParam("docTypeCode") String docTypeCode, @RequestParam(name = "docSeqNumber", required = false) BigDecimal docSeqNumber) throws IOException
	{
		return requestQuoteService.setUploadImage(file, appSeqNumber, docTypeCode, docSeqNumber);
	}
}
