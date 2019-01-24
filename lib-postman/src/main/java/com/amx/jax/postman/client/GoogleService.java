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
	private String googleSecret = "6LedCIsUAAAAANqAW4gzuhCJzl4d4krpw8BJfBNe";

	@Autowired
	RestService restService;

	public Boolean verifyCaptcha(String responseKey, String remoteIP) {
		logger.info("GoogleService :: verifyCaptcha :: remoteIP :" + remoteIP);
		logger.info("googleSecret :: " + googleSecret);

		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> resp = restService.ajax("https://www.google.com/recaptcha/api/siteverify").acceptJson()
					.field("secret", googleSecret).field("response", responseKey).field("remoteip", remoteIP).postForm()
					.as(Map.class);
			if (resp != null) {
				return ArgUtil.parseAsBoolean(resp.get("success"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
}
