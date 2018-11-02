package com.amx.jax.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PayMentController
{
	@RequestMapping(value = "/api/payment/pay", method = { RequestMethod.POST })
	public ResponseWrapperM<RemittanceApplicationResponseModel, AuthResponseOTPprefix> createApplication(
			@RequestHeader(value = "mOtp", required = false) String mOtpHeader,
			@RequestParam(required = false) String mOtp,
			@RequestBody RemittanceTransactionRequestModel transactionRequestModel, HttpServletRequest request) {
		ResponseWrapperM<RemittanceApplicationResponseModel, AuthResponseOTPprefix> wrapper = new ResponseWrapperM<RemittanceApplicationResponseModel, AuthResponseOTPprefix>();

		// Noncompliant - exception is lost
				Payment payment = new Payment();
				payment.setDocFinYear(respTxMdl.getDocumentFinancialYear());
				payment.setDocNo(respTxMdl.getDocumentIdForPayment());
				payment.setMerchantTrackId(respTxMdl.getMerchantTrackId());
				payment.setNetPayableAmount(respTxMdl.getNetPayableAmount());
				payment.setPgCode(respTxMdl.getPgCode());

				wrapper.setRedirectUrl(payGService.getPaymentUrl(payment,
						"https://" + request.getServerName() + "/app/landing/remittance"));
			}

	}

	
	@RequestMapping(value = "/remit/save-remittance", method = { RequestMethod.POST })
	public AmxApiResponse<PaymentResponseDto, Object> onPaymentCallback(
			@RequestBody PaymentResponseDto paymentResponse) {
		
		
	}
}
