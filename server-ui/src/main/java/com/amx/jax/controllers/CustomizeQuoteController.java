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
import com.amx.jax.models.CustomizeQuoteModel;
import com.amx.jax.models.PaymentDetails;
import com.amx.jax.payg.PayGService;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.services.CustomizeQuoteService;
import com.amx.jax.services.PayMentService;
import com.amx.utils.ArgUtil;

@RestController
public class CustomizeQuoteController
{
	String TAG = "com.amx.jax.controllers :: CustomizeQuoteController :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomizeQuoteController.class);

	@Autowired
	private CustomizeQuoteService customizeQuoteService;
	
	@Autowired
	private PayGService payGService;

	@Autowired
	PayMentService payMentService;

	@Autowired
	private WebConfig webConfig;
	
	@Autowired
	private AppConfig appConfig;
	

	@RequestMapping(value = "/api/customize-quote/get-quote-details", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> getCustomizedQuoteDetails(@RequestParam(name = "quoteSeqNumber", required = false) String quoteSeqNumber)
	{
		BigDecimal quoteSeqNumberDet = null;
		if (null != quoteSeqNumber && !quoteSeqNumber.equals("") && !quoteSeqNumber.equalsIgnoreCase("null"))
		{
			quoteSeqNumberDet = ArgUtil.parseAsBigDecimal(quoteSeqNumber);
		}
		return customizeQuoteService.getCustomizedQuoteDetails(quoteSeqNumberDet);
	}

	@RequestMapping(value = "/api/customize-quote/calculate-quote", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> calculateCutomizeQuote(@RequestBody CustomizeQuoteModel customizeQuoteModel)
	{
		return customizeQuoteService.calculateCutomizeQuote(customizeQuoteModel);
	}

	@RequestMapping(value = "/api/customize-quote/get-quoteseq-list", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getQuoteSeqList()
	{
		return customizeQuoteService.getQuoteSeqList();
	}

	@RequestMapping(value = "/api/customize-quote/terms-condition", method = RequestMethod.GET, produces = "application/json")
	public AmxApiResponse<?, Object> getTermsAndCondition()
	{
		return customizeQuoteService.getTermsAndCondition();
	}

	@RequestMapping(value = "/api/customize-quote/submit-quote", method = RequestMethod.POST, produces = "application/json")
	public AmxApiResponse<?, Object> saveCustomizeQuote(@RequestBody CustomizeQuoteModel customizeQuoteModel , HttpServletRequest request)
	{
		return customizeQuoteService.saveCustomizeQuote(customizeQuoteModel , request);
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
				logger.info(TAG + " onPaymentCallback :: getPostDate  :" + paymentResponse.getPostDate());
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
				
				PaymentDetails updateStatus = payMentService.updatePaymentDetals(paymentDetails);
				
				logger.info(TAG + " onPaymentCallback :: updateStatus  :" + updateStatus.toString());
				
				if (updateStatus.getErrorCode() == null)
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
		logger.info(TAG + " getPaymentStatus :: paySeqNum  :" + paySeqNum);
		return payMentService.getPaymentStatus(paySeqNum);
	}
	
	
	@RequestMapping(value = "/api/payment-receipt-data", method = { RequestMethod.POST })
	public AmxApiResponse<?, Object> paymentReceiptData(@RequestParam BigDecimal paySeqNum) 
	{
		logger.info(TAG + " paymentReceiptData :: paySeqNum  :" + paySeqNum);
		return payMentService.paymentReceiptData(paySeqNum);
	}
}
