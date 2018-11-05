package com.amx.jax.controllers;

import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.WebConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.models.PaymentDetails;
import com.amx.jax.models.PgRedirectUrl;
import com.amx.jax.payg.PayGService;
import com.amx.jax.payg.Payment;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.services.PayMentService;

@RestController
public class PayMentController {
	String TAG = "com.amx.jax.services :: PayMentController :: ";

	private static final Logger logger = LoggerFactory.getLogger(PayMentController.class);

	@Autowired
	private PayGService payGService;

	@Autowired
	PayMentService payMentService;

	@Autowired
	private WebConfig webConfig;
	
	@RequestMapping(value = "/api/payment/pay", method = { RequestMethod.POST })
	public AmxApiResponse<?, Object> createApplication(BigDecimal quoteSeqNum, HttpServletRequest request) {

		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try 
		{
			//AmxApiResponse<PaymentDetails, Object> respInsertPayment = payMentService.insertPaymentDetals(quoteSeqNum);
			//PaymentDetails paymentDetails = respInsertPayment.getData();
			//logger.info(TAG + " createApplication :: paymentDetails  :" + paymentDetails.toString());
			
			Payment payment = new Payment();
			payment.setDocFinYear(null);
			//payment.setDocNo(paymentDetails.getPaySeqNum().toString());// PaySeqNum
			//payment.setMerchantTrackId(paymentDetails.getPaySeqNum().toString());// PaySeqNum
			payment.setDocNo("3");// PaySeqNum
			payment.setMerchantTrackId("3");// PaySeqNum
			payment.setNetPayableAmount(100);
			payment.setPgCode(PayGServiceCode.KNET);

			PgRedirectUrl pgRedirectUrl = new PgRedirectUrl();
			pgRedirectUrl.setRedirectUrl(payGService.getPaymentUrl(payment,"https://" + webConfig.getPaymentUrl() + "/app/landing/remittance"));
			resp.setData(pgRedirectUrl);
			resp.setStatusKey(ApiConstants.SUCCESS);

		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	@RequestMapping(value = "/remit/save-remittance", method = { RequestMethod.POST })
	public String onPaymentCallback(@RequestBody PaymentResponseDto paymentResponse , HttpServletResponse response) 
	{
		String redirectUrl;
		
		logger.info(TAG + " onPaymentCallback :: paymentResponse  :" + paymentResponse.toString());
		
		if(paymentResponse.getResultCode().equalsIgnoreCase("CAPTURED"))
		{
			PaymentDetails paymentDetails = new PaymentDetails();
			paymentDetails.setPaymentId(paymentResponse.getPaymentId());
			paymentDetails.setApprovalNo(paymentResponse.getAuth_appNo());
			paymentDetails.setApprovalDate(null);
			paymentDetails.setResultCd(paymentResponse.getResultCode());
			paymentDetails.setTransId(paymentResponse.getTransactionId());
			paymentDetails.setRefId(paymentResponse.getReferenceId());

			if (null != paymentResponse.getTrackId()) 
			{
				BigDecimal appSeqNum = new BigDecimal(paymentResponse.getTrackId().toString());
				paymentDetails.setPaySeqNum(appSeqNum);
			} 
			else 
			{
				paymentDetails.setPaySeqNum(null);
			}
			//payMentService.updatePaymentDetals(paymentDetails);
			//payMentService.cretaeAmibCust();
			//payMentService.processReceipt(paymentDetails.getPaySeqNum());
			//payMentService.createAmibPolicy(paymentDetails.getPaySeqNum());
			//payMentService.preparePrintData(paymentDetails.getPaySeqNum());
			
			
			logger.info(TAG + " onPaymentCallback :: getAppUrl  :" + webConfig.getAppUrl());
			logger.info(TAG + " onPaymentCallback :: getAppName  :" + webConfig.getAppName());
			logger.info(TAG + " onPaymentCallback :: getAppCompCode()  :" + webConfig.getAppCompCode());
			logger.info(TAG + " onPaymentCallback :: getAppTitle  :" + webConfig.getAppTitle());
			logger.info(TAG + " onPaymentCallback :: paymentResponse  :" + paymentResponse.toString());
			
			
			redirectUrl = "https://amib.amxremit.com/app/landing/myquotes";
			
			logger.info(TAG + " onPaymentCallback :: redirectUrl  :" + redirectUrl);
			
			try 
			{
				response.sendRedirect(redirectUrl);
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			return redirectUrl;

		}
		return null;
	}
	
	/*@RequestMapping(value = "/api/payment-receipt-data", method = { RequestMethod.POST })
	public AmxApiResponse<?, Object> paymentReceiptData(@RequestParam BigDecimal paySeqNum) {
		return payMentService.paymentReceiptData(paySeqNum);
	}*/
}
