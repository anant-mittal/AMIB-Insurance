package com.amx.jax.payg;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.AppContext;
import com.amx.jax.AppContextUtil;
import com.amx.jax.controllers.PayMentController;
import com.amx.utils.URLBuilder;

@Component
public class PayGService {

	String TAG = "com.amx.jax.services :: PayGService :: ";

	private static final Logger logger = LoggerFactory.getLogger(PayGService.class);
	
	@Autowired
	private AppConfig appConfig;

	public String getPaymentUrl(Payment payment, String callback) throws MalformedURLException, URISyntaxException {
		AppContext context = AppContextUtil.getContext();

		logger.info(TAG + " getPaymentUrl :: callback  :" + callback);
		
		URLBuilder builder = new URLBuilder(appConfig.getPaygURL());
		
		logger.info(TAG + " getPaymentUrl :: builder  :" + builder);
		
		String callbackUrl = callback + "?docNo=" + payment.getDocNo() + "&docFy=" + payment.getDocFinYear();
		
		logger.info(TAG + " getPaymentUrl :: callbackUrl  :" + callbackUrl);
		
		String callbackd = Base64.getEncoder().encodeToString(callbackUrl.getBytes());

		logger.info(TAG + " getPaymentUrl :: callbackd  :" + callbackd);
		
		builder.setPath("app/payment").addParameter("amount", payment.getNetPayableAmount())
				.addParameter("trckid", payment.getMerchantTrackId()).addParameter("pg", payment.getPgCode())
				.addParameter("docFy", payment.getDocFinYear()).addParameter("docNo", payment.getDocNo())
				.addParameter("tnt", context.getTenant()).addParameter("callbackd", callbackd)
				.addParameter(AppConstants.TRACE_ID_XKEY, context.getTraceId());
		
		logger.info(TAG + " getPaymentUrl :: builder.getURL()  :" + builder.getURL());
		
		return builder.getURL();
	}

}
