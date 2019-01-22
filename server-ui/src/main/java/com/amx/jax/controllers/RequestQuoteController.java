
package com.amx.jax.controllers;

import java.io.IOException;
import java.math.BigDecimal;

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

import com.amx.jax.WebAppStatus.ApiWebAppStatus;
import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.DownloadImageModel;
import com.amx.jax.models.PersonalDetails;
import com.amx.jax.models.VehicleDetails;
import com.amx.jax.services.RequestQuoteService;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.ApiOperation;

@RestController
public class RequestQuoteController {
	private static final Logger logger = LoggerFactory.getLogger(RequestQuoteController.class);

	@Autowired
	public RequestQuoteService requestQuoteService;

	@ApiOperation(value = "returns vehicle details make meta data for dropdown ")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/vehicledetails/make", method = RequestMethod.GET)
	public AmxApiResponse<?, Object> getMake() {
		return requestQuoteService.getMake();
	}

	@ApiOperation(value = "returns vehicle details submake meta data for dropdown ", notes = "will return the data on basisi of make selected so selected make code need to enter.")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/vehicledetails/sub-make", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getModel(@RequestParam("make") String make) {
		return requestQuoteService.getModel(make);
	}

	@ApiOperation(value = "returns vehicle details fuel type meta data for dropdown")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/vehicledetails/fueltype", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getFuleType() {
		return requestQuoteService.getFuleType();
	}

	@ApiOperation(value = "returns vehicle details purpose meta data for dropdown")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/vehicledetails/purpose", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getPurpose() {
		return requestQuoteService.getPurpose();
	}

	@ApiOperation(value = "returns vehicle details shape meta data for dropdown")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/vehicledetails/shape", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getShape() {
		return requestQuoteService.getShape();
	}

	@ApiOperation(value = "returns vehicle details colour meta data for dropdown")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/vehicledetails/colour", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getColour() {
		return requestQuoteService.getColour();
	}

	@ApiOperation(value = "returns vehicle details vehicle condition meta data for dropdown")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/vehicledetails/vehicle-condition", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getVehicleCondition() {
		return requestQuoteService.getVehicleCondition();
	}

	@ApiOperation(value = "returns vehicle details model year meta data for dropdown")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/vehicledetails/model-year", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getMaxVehicleAgeAllowed() {
		return requestQuoteService.getMaxVehicleAgeAllowed();
	}

	@ApiOperation(value = "returns vehicle details vehicle policy duration meta data for dropdown")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/vehicledetails/policy-duartion", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getPolicyDuration() {
		return requestQuoteService.getPolicyDuration();
	}

	@ApiOperation(value = "returns request quote details", notes = "this api will return vehicle details , personal details , uploaded images , and insurance provider details")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/request-quote/get-requestquote-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getRequestQuoteDetails() {
		return requestQuoteService.getRequestQuoteDetails();
	}

	@ApiOperation(value = "submits updated vehicle details to server", notes = "for a new quote app sequence number will not present , "
			+ "but on the success of submit of vehicle details app sequence number will be generated in response and same "
			+ "will be mandatory and need to be entered after updating the vehicle details of same quote. If for the same quote the "
			+ "generated app sequence number not entered it will generate a new app sequence number and it will treated as new quote with new app sequence number ")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/request-quote/set-vehicle-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> setAppVehicleDetails(
			@RequestParam(name = "appSeqNumber", required = false) String appSeqNumber,
			@RequestBody VehicleDetails vehicleDetails) {
		BigDecimal appSeqNumberDet = null;
		if (null != appSeqNumber && !appSeqNumber.equals("") && !appSeqNumber.equalsIgnoreCase("null")) {
			appSeqNumberDet = ArgUtil.parseAsBigDecimal(appSeqNumber);
		}
		return requestQuoteService.setAppVehicleDetails(appSeqNumberDet, vehicleDetails, null);
	}

	@ApiOperation(value = "submits updated profile details to server", notes = "here to update personal details it is mandatory to enter app sequence number")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/request-quote/set-personal-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> setProfileDetails(@RequestParam(name = "appSeqNumber") String appSeqNumber,
			@RequestBody PersonalDetails personalDetails) {
		BigDecimal appSeqNumberDet = null;
		if (null != appSeqNumber && !appSeqNumber.equals("") && !appSeqNumber.equalsIgnoreCase("null")) {
			appSeqNumberDet = ArgUtil.parseAsBigDecimal(appSeqNumber, null);
		}
		return requestQuoteService.setProfileDetails(appSeqNumberDet, personalDetails);
	}

	@ApiOperation(value = "submit upadted vehicle image to server", notes = "image type can be only jpeg and png format while uploading image for the first time "
			+ "doc sequence number will be null , but after upload it will create a doc dequence number which will "
			+ "required to enter while updating the same image. Doc sequence number of uploaded image can be get from "
			+ "/api/request-quote/get-requestquote-details api.")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/request-quote/upload-vehicle-image", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> uploadVehicleImage(@RequestParam MultipartFile file,
			@RequestParam("appSeqNumber") String appSeqNumber, @RequestParam("docTypeCode") String docTypeCode,
			@RequestParam(name = "docSeqNumber", required = false) String docSeqNumber) throws IOException {
		BigDecimal appSeqNumberDet = null;
		if (null != appSeqNumber && !appSeqNumber.equals("") && !appSeqNumber.equalsIgnoreCase("null")) {
			appSeqNumberDet = ArgUtil.parseAsBigDecimal(appSeqNumber);
		}

		BigDecimal docSeqNumberDet = null;
		if (null != docSeqNumber && !docSeqNumber.equals("") && !docSeqNumber.equalsIgnoreCase("null")) {
			docSeqNumberDet = ArgUtil.parseAsBigDecimal(docSeqNumber);
		}
		return requestQuoteService.uploadVehicleImage(file, appSeqNumberDet, docTypeCode, docSeqNumberDet);
	}

	@ApiOperation(value = "returns uploaded vehicle image in byte array format of entered doc number")
	@RequestMapping(value = "/api/request-quote/downlaod-vehicle-images", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<byte[]> downloadVehicleImage(
			@RequestParam(name = "docSeqNumber", required = false) String docSeqNumber) throws IOException {
		BigDecimal docSeqNumberDet = null;
		if (null != docSeqNumber && !docSeqNumber.equals("") && !docSeqNumber.equalsIgnoreCase("null")) {
			docSeqNumberDet = ArgUtil.parseAsBigDecimal(docSeqNumber);

			DownloadImageModel downloadImageModel = requestQuoteService.downloadVehicleImage(docSeqNumberDet);
			byte[] imageByteArray = downloadImageModel.getImageByteArray();
			String imageType = downloadImageModel.getImageType();
			MediaType mediaType = null;
			if (imageType.contains("jpeg") || imageType.contains("jpg")) {
				mediaType = MediaType.IMAGE_JPEG;
			} else if (imageType.contains("png")) {
				mediaType = MediaType.IMAGE_PNG;
			}
			return ResponseEntity.ok().contentLength(imageByteArray.length).contentType(mediaType).body(imageByteArray);

		}
		return null;

	}

	@ApiOperation(value = "submits updated request quote details to server", notes = "after successfull submit of request quote a mail will trigger to the customer on his registered emial id of successful quote creation")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/request-quote/submit-request-quote", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> submitRequestQuote(@RequestParam String appSeqNumber,
			@RequestParam String insuranceCompCode) {
		BigDecimal appSeqNumberDet = null;
		if (null != appSeqNumber && !appSeqNumber.equals("") && !appSeqNumber.equalsIgnoreCase("null")) {
			appSeqNumberDet = ArgUtil.parseAsBigDecimal(appSeqNumber);
		}

		BigDecimal insuranceCompDet = null;
		if (null != insuranceCompCode && !insuranceCompCode.equals("") && !insuranceCompCode.equalsIgnoreCase("null")) {
			insuranceCompDet = ArgUtil.parseAsBigDecimal(insuranceCompCode, null);
		}
		return requestQuoteService.submitRequestQuote(appSeqNumberDet, insuranceCompDet);
	}

	/*
	 * @RequestMapping(value = "/api/requestquote/test", method = RequestMethod.GET)
	 * public AmxApiResponse<?, Object> test() { ArrayList<Purpose> purposeArray =
	 * new ArrayList<Purpose>(); String key = "key223";
	 * 
	 * Purpose purpose3 = new Purpose(); purpose3.setPurposeCode("key333");
	 * purpose3.setPurposeDesc("val333");
	 * 
	 * Purpose purpose2 = new Purpose(); purpose2.setPurposeCode("key222");
	 * purpose2.setPurposeDesc("val222");
	 * 
	 * Purpose purpose1 = new Purpose(); purpose1.setPurposeCode("key111");
	 * purpose1.setPurposeDesc("val111");
	 * 
	 * purposeArray.add(purpose1); purposeArray.add(purpose2);
	 * purposeArray.add(purpose3);
	 * 
	 * logger.info("RequestQuoteService :: getAppVehicleDetails :: getPurposeCode :"
	 * + key);
	 * 
	 * logger.info("RequestQuoteService :: getAppVehicleDetails :: returned key   :"
	 * + KeyAvaibility.purposeKeyAvaibility(key , purposeArray));
	 * 
	 * return null;
	 * 
	 * }
	 */

}
