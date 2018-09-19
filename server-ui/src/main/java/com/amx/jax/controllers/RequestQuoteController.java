
package com.amx.jax.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.models.DownloadImageModel;
import com.amx.jax.models.PersonalDetails;
import com.amx.jax.models.VehicleDetails;
import com.amx.jax.services.RequestQuoteService;

@RestController
public class RequestQuoteController
{
	private static final Logger logger = LoggerFactory.getLogger(RequestQuoteController.class);

	String TAG = "com.amx.jax.controllers.RequestQuoteController :- ";

	@Autowired
	public RequestQuoteService requestQuoteService;

	@RequestMapping(value = "/api/vehicledetails/make", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getMake()
	{
		return requestQuoteService.getMake();
	}

	@RequestMapping(value = "/api/vehicledetails/sub-make", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getModel(@RequestParam("make") String make)
	{
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
		return requestQuoteService.getColour();
	}

	@RequestMapping(value = "/api/vehicledetails/vehicle-condition", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getVehicleCondition()
	{
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

	@RequestMapping(value = "/api/request-quote/get-requestquote-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getRequestQuoteDetails()
	{
		return requestQuoteService.getRequestQuoteDetails();
	}

	@RequestMapping(value = "/api/request-quote/set-vehicle-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> setAppVehicleDetails(@RequestParam(name = "appSeqNumber", required = false) BigDecimal appSeqNumber, @RequestBody VehicleDetails vehicleDetails)
	{
		return requestQuoteService.setAppVehicleDetails(appSeqNumber, vehicleDetails);
	}

	@RequestMapping(value = "/api/request-quote/set-personal-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> setProfileDetails(@RequestParam(name = "appSeqNumber", required = false) BigDecimal appSeqNumber, @RequestBody PersonalDetails personalDetails)
	{
		return requestQuoteService.setProfileDetails(appSeqNumber, personalDetails);
	}

	@RequestMapping(value = "/api/request-quote/upload-vehicle-image", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> uploadVehicleImage(@RequestParam MultipartFile file, @RequestParam("appSeqNumber") BigDecimal appSeqNumber, @RequestParam("docTypeCode") String docTypeCode, @RequestParam(name = "docSeqNumber", required = false) BigDecimal docSeqNumber) throws IOException
	{
		return requestQuoteService.uploadVehicleImage(file, appSeqNumber, docTypeCode, docSeqNumber);
	}

	@RequestMapping(value = "/api/request-quote/downlaod-vehicle-images", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> downloadVehicleImage(@RequestParam(name = "docSeqNumber", required = false) BigDecimal docSeqNumber) throws IOException
	{
		DownloadImageModel downloadImageModel = requestQuoteService.downloadVehicleImage(docSeqNumber);
		byte[] imageByteArray = downloadImageModel.getImageByteArray();
		String imageType = downloadImageModel.getImageType();
		MediaType mediaType = null;
		logger.info(TAG + " downloadVehicleImage :: imageType :" + imageType);
		if (imageType.contains("jpeg"))
		{
			mediaType = MediaType.IMAGE_JPEG;
		}
		else if (imageType.contains("png"))
		{
			mediaType = MediaType.IMAGE_PNG;
		}
		return ResponseEntity.ok().contentLength(imageByteArray.length).contentType(mediaType).body(imageByteArray);
	}
}
