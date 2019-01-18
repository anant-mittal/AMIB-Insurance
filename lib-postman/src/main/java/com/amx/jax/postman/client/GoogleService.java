package com.amx.jax.postman.client;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.amx.jax.rest.RestService;
import com.amx.utils.ArgUtil;

@Component
public class GoogleService {

	private static final Logger logger = LoggerFactory.getLogger(GoogleService.class);

	// private String googleSecret = "6LeK33AUAAAAANWoO_wM5_3FxJ0DoPjPZp_n7pVz";
	private String googleSecret = "6LdtFEMUAAAAACbe0JJ2MDSrsLPseUTCjb659uHD";

	@Autowired
	RestService restService;

	public Boolean verifyCaptcha(String responseKey, String remoteIP) {
		logger.info("GoogleService :: verifyCaptcha :: remoteIP :" + remoteIP);

		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> resp = restService.ajax("https://www.google.com/recaptcha/api/siteverify").acceptJson()
					.field("secret", googleSecret).field("response", responseKey).field("remoteip", remoteIP).postForm()
					.as(Map.class);

			logger.info("GoogleService :: verifyCaptcha :: responseKey :" + responseKey);
			logger.info("GoogleService :: verifyCaptcha :: resp :" + resp);
			logger.info("GoogleService :: verifyCaptcha :: resp.toString() :" + resp.toString());
			logger.info("GoogleService :: verifyCaptcha :: resp.keySet() :" + resp.keySet());
			logger.info("GoogleService :: verifyCaptcha :: resp.values() :" + resp.values());
			
			if (resp != null) {
				return ArgUtil.parseAsBoolean(resp.get("success"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
}
