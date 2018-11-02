package com.amx.jax.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.payg.PaymentResponseDto;

import src.main.java.com.amx.jax.payg.PayGService;
import src.main.java.com.amx.jax.payg.Payment;

@RestController
public class PayMentController
{
	
	@Autowired
	private PayGService payGService;

	@RequestMapping(value = "/api/payment/pay", method = { RequestMethod.POST })
	public AmxApiResponse<Object, Object> createApplication(
			@RequestHeader(value = "mOtp", required = false) String mOtpHeader,
			@RequestParam(required = false) String mOtp,
			@RequestBody Object req, HttpServletRequest request) {

		// Noncompliant - exception is lost
				Payment payment = new Payment();
				payment.setDocFinYear(null);
				payment.setDocNo(null);
				payment.setMerchantTrackId(null);
				payment.setNetPayableAmount(null);
				payment.setPgCode(PayGServiceCode.KNET);
				wrapper.setRedirectUrl(payGService.getPaymentUrl(payment,
						"https://" + request.getServerName() + "/app/landing/remittance"));
			}

	}

	
	@RequestMapping(value = "/remit/save-remittance", method = { RequestMethod.POST })
	public AmxApiResponse<PaymentResponseDto, Object> onPaymentCallback(
			@RequestBody PaymentResponseDto paymentResponse) {
		
	}
}
