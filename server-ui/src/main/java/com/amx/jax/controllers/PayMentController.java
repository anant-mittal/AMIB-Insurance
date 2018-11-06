package com.amx.jax.controllers;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AppConfig;
import com.amx.jax.WebConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.models.PaymentDetails;
import com.amx.jax.models.PaymentStatus;
import com.amx.jax.models.PgRedirectUrl;
import com.amx.jax.payg.PayGService;
import com.amx.jax.payg.Payment;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.services.PayMentService;

@RestController
public class PayMentController 
{
	/*String TAG = "com.amx.jax.services :: PayMentController :: ";

	private static final Logger logger = LoggerFactory.getLogger(PayMentController.class);

	@Autowired
	private PayGService payGService;

	@Autowired
	PayMentService payMentService;

	@Autowired
	private WebConfig webConfig;
	
	@Autowired
	private AppConfig appConfig;
	

	@RequestMapping(value = "/api/payment/pay", method = { RequestMethod.POST })
	public AmxApiResponse<?, Object> createApplication(@RequestParam BigDecimal quoteSeqNum, @RequestParam BigDecimal paymentAmount, HttpServletRequest request) {

		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try 
		{
			AmxApiResponse<PaymentDetails, Object> respInsertPayment = payMentService.insertPaymentDetals(quoteSeqNum,paymentAmount);
			PaymentDetails paymentDetails = respInsertPayment.getData();
			logger.info(TAG + " createApplication :: paymentDetails :" + paymentDetails.toString());
			
			Payment payment = new Payment();
			payment.setDocFinYear(null);
			payment.setDocNo(paymentDetails.getPaySeqNum().toString());// PaySeqNum
			payment.setMerchantTrackId(paymentDetails.getPaySeqNum().toString());// PaySeqNum
			payment.setNetPayableAmount(paymentAmount);
			payment.setPgCode(PayGServiceCode.KNET);
			
			String redirctUrl = payGService.getPaymentUrl(payment,"https://"+request.getServerName()+"/app/landing/myquotes/payment");
			resp.setData(redirctUrl);
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
	public void onPaymentCallback(@RequestBody PaymentResponseDto paymentResponse, HttpServletResponse response) 
	{
		logger.info(TAG + " onPaymentCallback :: paymentResponse  :" + paymentResponse.toString());

		try 
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

				logger.info(TAG + " onPaymentCallback :: paymentDetails  :" + paymentDetails.toString());
				
				AmxApiResponse<?, Object> updatePaymentResp = payMentService.updatePaymentDetals(paymentDetails);
				if (updatePaymentResp.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
				{
					 payMentService.cretaeAmibCust();
					 payMentService.processReceipt(paymentDetails.getPaySeqNum());
					 payMentService.createAmibPolicy(paymentDetails.getPaySeqNum());
					 payMentService.preparePrintData(paymentDetails.getPaySeqNum());
				}
				
			} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}

	
	@RequestMapping(value = "/api/payment-status", method = { RequestMethod.POST })
	public AmxApiResponse<?, Object> getPaymentStatus(@RequestParam BigDecimal paySeqNum) 
	{
		return payMentService.getPaymentStatus(paySeqNum);
	}
	
	
	@RequestMapping(value = "/api/payment-receipt-data", method = { RequestMethod.POST })
	public AmxApiResponse<?, Object> paymentReceiptData(@RequestParam BigDecimal paySeqNum) 
	{
		return payMentService.paymentReceiptData(paySeqNum);
	}*/
}
