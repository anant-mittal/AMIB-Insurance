/**  AlMulla Exchange
  *
  */
package com.amx.jax.sample;

import com.amx.jax.AppConstants;
import io.swagger.annotations.Api;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.amx.jax.sample.SampleConstant.SAMPLE_API_ENDPOINT;

@RestController
@RequestMapping(SAMPLE_API_ENDPOINT)
@Api(value = "Sample APIs")
public class SampleController {

	private Logger logger = Logger.getLogger(SampleController.class);

	@RequestMapping(value = "/tranx", method = RequestMethod.GET)
	public String unlockCustomer(@RequestParam(AppConstants.TRANX_ID_XKEY) String tranxId,
			 @RequestParam(required = false) String message) {
			return message;
	}

}
