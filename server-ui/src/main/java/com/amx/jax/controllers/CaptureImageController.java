package com.amx.jax.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amx.jax.models.ImageMandatoryResponse;
import com.amx.jax.services.CaptureImageService;
import com.amx.jax.api.AmxApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CaptureImageController
{
	String TAG = "com.amx.jax.controllers :: RequestQuoteController :: ";

	private static final Logger logger = LoggerFactory.getLogger(CaptureImageController.class);

	@Autowired
	private  CaptureImageService captureImageService;

	@RequestMapping(value = "/api/reqquote/get-incomplete-policy", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<ImageMandatoryResponse, Object> getMandatoryImage()
	{
		return captureImageService.getMandatoryImage();
	}
}
