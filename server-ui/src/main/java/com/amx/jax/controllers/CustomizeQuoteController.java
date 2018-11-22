package com.amx.jax.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.DetailsConstants;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.models.CustomizeQuoteModel;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.PaymentDetails;
import com.amx.jax.models.PaymentReceipt;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.TemplatesIB;
import com.amx.jax.services.CustomerRegistrationService;
import com.amx.jax.services.CustomizeQuoteService;
import com.amx.jax.services.PayMentService;
import com.amx.jax.ui.session.UserSession;
import com.amx.jax.utility.Utility;
import com.amx.utils.ArgUtil;

@RestController
public class CustomizeQuoteController
{
	String TAG = "com.amx.jax.controllers :: CustomizeQuoteController :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomizeQuoteController.class);

	@Autowired
	private CustomizeQuoteService customizeQuoteService;
	
	@Autowired
	PayMentService payMentService;
	
	@Autowired
	CommonHttpRequest httpService;
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	UserSession userSession;
	
	@Autowired
	private PostManService postManService;
	
	@Autowired
	private CustomerRegistrationService customerRegistrationService;
	
	@Autowired
	private HttpServletResponse response;
	

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
	public PaymentResponseDto onPaymentCallback(@RequestBody PaymentResponseDto paymentResponse) 
	{
		logger.info(TAG + " onPaymentCallback :: userSession  :" + userSession.toString());
		logger.info(TAG + " onPaymentCallback :: metaData     :" + metaData.toString());
		logger.info(TAG + " onPaymentCallback :: paymentResponse  :" + paymentResponse.toString());
		
		try 
		{
			metaDataSetup();
			
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
				BigDecimal paySeqNumber = new BigDecimal(paymentResponse.getTrackId().toString());
				logger.info(TAG + " onPaymentCallback :: paySeqNumber  :" + paySeqNumber);
				paymentDetails.setPaySeqNum(paySeqNumber);
				paymentDetails.setPaymentToken(paySeqNumber.toString());
			} 
			else 
			{
				paymentDetails.setPaySeqNum(null);
			}

			logger.info(TAG + " onPaymentCallback :: paymentDetails  :" + paymentDetails.toString());
			PaymentDetails updateStatus = payMentService.updatePaymentDetals(paymentDetails);
			logger.info(TAG + " onPaymentCallback :: updateStatus  :" + updateStatus.toString());
			
		} 
				
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return paymentResponse;
	}
	
	@RequestMapping(value = "/api/payment-status", method = { RequestMethod.POST })
	public AmxApiResponse<?, Object> getPaymentStatus(@RequestParam String paySeqNum) 
	{
		logger.info(TAG + " getPaymentStatus :: paySeqNum  :" + paySeqNum);
		
		BigDecimal paySeqNumDet = null;
		if (null != paySeqNum && !paySeqNum.equals("") && !paySeqNum.equalsIgnoreCase("null"))
		{
			paySeqNumDet = ArgUtil.parseAsBigDecimal(paySeqNum, null);
		}
		
		return payMentService.getPaymentStatus(paySeqNumDet);
	}
	
	
	@RequestMapping(value = "/api/payment-receipt-data", method = { RequestMethod.GET })
	public String paymentReceiptDataExt(@RequestParam String paySeqNum) 
	{
		File file = null;
		PaymentReceipt paymentReceipt = null;
		try
		{
			BigDecimal paySeqNumDet = null;
			if (null != paySeqNum && !paySeqNum.equals("") && !paySeqNum.equalsIgnoreCase("null"))
			{
				paySeqNumDet = ArgUtil.parseAsBigDecimal(paySeqNum, null);
			}
			
			AmxApiResponse<?, Object> receiptData  = payMentService.paymentReceiptData(paySeqNumDet);
			if (receiptData.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
			{
				paymentReceipt = (PaymentReceipt) receiptData.getData();
				logger.info(TAG + " getPaymentStatus :: paymentReceipt  :" + paymentReceipt.toString());
			}
			
			Map<String, Object> wrapper = new HashMap<String, Object>();
			Map<String, Object> model = new HashMap<String, Object>();
			ArrayList<Map> dataList = new ArrayList<>();
			
			model.put(DetailsConstants.applicationId, paymentReceipt.getApplicationId());
			model.put(DetailsConstants.customerId, paymentReceipt.getCustomerId());
			model.put(DetailsConstants.paymentDate, paymentReceipt.getPaymentDate());
			model.put(DetailsConstants.paymentMode, paymentReceipt.getPaymentMode());
			
			BigDecimal amountValue = Utility.getNumericValue(paymentReceipt.getAmountPaidNumber());
			BigDecimal amountWithDecimal = Utility.round(amountValue ,new BigDecimal(3));
			String amountWithCurrency = metaData.getCurrency()+" "+amountWithDecimal.toString();
			logger.info(TAG + " paymentReceiptDataExt :: amountValue  :" + amountValue);
			logger.info(TAG + " paymentReceiptDataExt :: amountWithDecimal  :" + amountWithDecimal);
			logger.info(TAG + " paymentReceiptDataExt :: amountWithCurrency  :" + amountWithCurrency);
			
			model.put(DetailsConstants.amountPaidNumber, amountWithCurrency);
			
			model.put(DetailsConstants.amountPaidWord, paymentReceipt.getAmountPaidWord());
			model.put(DetailsConstants.paymentId, paymentReceipt.getPaymentId());
			model.put(DetailsConstants.customerName, paymentReceipt.getCustomerName());
			model.put(DetailsConstants.civilId, paymentReceipt.getCivilId());
			model.put(DetailsConstants.mobileNumber, paymentReceipt.getMobileNumber());
			model.put(DetailsConstants.emialId, paymentReceipt.getEmialId());
			
			model.put(DetailsConstants.policyDuration, paymentReceipt.getPolicyDuration());
			
			model.put(DetailsConstants.governate, paymentReceipt.getGovernate());
			model.put(DetailsConstants.areaDesc, paymentReceipt.getAreaDesc());
			model.put(DetailsConstants.address, paymentReceipt.getAddress());
			model.put(DetailsConstants.make, paymentReceipt.getMake());
			model.put(DetailsConstants.subMake, paymentReceipt.getSubMake());
			model.put(DetailsConstants.ktNumber, paymentReceipt.getKtNumber());
			model.put(DetailsConstants.chasisNumber, paymentReceipt.getChasisNumber());
			model.put(DetailsConstants.modelYear, paymentReceipt.getModelYear());
			model.put(DetailsConstants.trnsReceiptRef, paymentReceipt.getTrnsReceiptRef());
			
			dataList.add(model);
			wrapper.put("results", dataList);
			
			file = postManService.processTemplate(new File(TemplatesIB.TRNX_RECEIPT, wrapper, File.Type.PDF)).getResult();
			file.create(response, true);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void metaDataSetup()
	{
		if(null == metaData.getCountryId())
		{
			if (httpService.getLanguage().toString().equalsIgnoreCase("EN"))
			{
				metaData.setLanguageId(new BigDecimal(0));
				customerRegistrationService.getCompanySetUp();
			}
		}
	}
}
