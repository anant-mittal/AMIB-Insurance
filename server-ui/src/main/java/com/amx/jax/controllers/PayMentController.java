package com.amx.jax.controllers;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	// public AmxApiResponse<Object, Object> createApplication(@RequestHeader(value
	// = "mOtp", required = false) String mOtpHeader, @RequestParam(required =
	// false) String mOtp, @RequestBody Object req, HttpServletRequest request)
	public AmxApiResponse<?, Object> createApplication(BigDecimal quoteSeqNum, HttpServletRequest request) {

		PaymentDetails paymentDetails;

		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try {
			Payment payment = new Payment();
			payment.setDocFinYear(null);
			// payment.setDocNo(paymentDetails.getPaySeqNum().toString());//PaySeqNum
			// payment.setMerchantTrackId(paymentDetails.getPaySeqNum().toString());//PaySeqNum
			payment.setDocNo("3");// PaySeqNum
			payment.setMerchantTrackId("3");// PaySeqNum
			payment.setNetPayableAmount(100);
			payment.setPgCode(PayGServiceCode.KNET);

			PgRedirectUrl pgRedirectUrl = new PgRedirectUrl();
			pgRedirectUrl.setRedirectUrl(payGService.getPaymentUrl(payment,
					"https://" + webConfig.getPaymentUrl() + "/app/landing/remittance"));
			resp.setData(pgRedirectUrl);
			resp.setStatusKey(ApiConstants.SUCCESS);

		} catch (Exception e) {
			e.printStackTrace();
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	@RequestMapping(value = "/remit/save-remittance", method = { RequestMethod.POST })
	public AmxApiResponse<PaymentResponseDto, Object> onPaymentCallback(
			@RequestBody PaymentResponseDto paymentResponse) {

		logger.info(TAG + " onPaymentCallback :: paymentResponse  :" + paymentResponse.toString());

		return null;
	}

	@RequestMapping(value = "/insert-payment", method = { RequestMethod.POST })
	public AmxApiResponse<?, Object> insertPaymentDetals(@RequestParam BigDecimal quoteSeqNum) {
		logger.info(TAG + " insertPaymentDetals :: quoteSeqNum :" + quoteSeqNum);
		return payMentService.insertPaymentDetals(quoteSeqNum);
	}

	@RequestMapping(value = "/update-payment", method = { RequestMethod.POST })
	public AmxApiResponse<?, Object> updatePaymentDetals(@RequestBody PaymentDetails paymentDetails) {
		return payMentService.updatePaymentDetals(paymentDetails);
	}

	@RequestMapping(value = "/update-create-amibcust", method = { RequestMethod.POST })
	public AmxApiResponse<?, Object> cretaeAmibCust() {
		return payMentService.cretaeAmibCust();
	}

	@RequestMapping(value = "/update-process-receipt", method = { RequestMethod.POST })
	public AmxApiResponse<?, Object> processReceipt(@RequestParam BigDecimal paySeqNum) {
		return payMentService.processReceipt(paySeqNum);
	}

	@RequestMapping(value = "/update-create-amibpolicy", method = { RequestMethod.POST })
	public AmxApiResponse<?, Object> createAmibPolicy(@RequestParam BigDecimal paySeqNum) {
		return payMentService.createAmibPolicy(paySeqNum);
	}

	@RequestMapping(value = "/update-prepare-printdata", method = { RequestMethod.POST })
	public AmxApiResponse<?, Object> preparePrintData(@RequestParam BigDecimal paySeqNum) {
		return payMentService.preparePrintData(paySeqNum);
	}

	@RequestMapping(value = "/api/payment-receipt-data", method = { RequestMethod.POST })
	public AmxApiResponse<?, Object> paymentReceiptData(@RequestParam BigDecimal paySeqNum) {
		return payMentService.paymentReceiptData(paySeqNum);
	}
}
