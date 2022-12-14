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

import com.amx.jax.WebAppStatus.ApiWebAppStatus;
import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.DetailsConstants;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.meta.IMetaService;
import com.amx.jax.models.CustomizeQuoteModel;
import com.amx.jax.models.PaymentDetails;
import com.amx.jax.models.PaymentReceipt;
import com.amx.jax.models.PolicyReceiptDetails;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.TemplatesIB;
import com.amx.jax.services.CustomerRegistrationService;
import com.amx.jax.services.CustomizeQuoteService;
import com.amx.jax.services.PayMentService;
import com.amx.jax.swagger.ApiMockParam;
import com.amx.jax.ui.session.UserSession;
import com.amx.jax.utility.Utility;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.HttpUtils;
import com.amx.utils.JsonUtil;

import io.swagger.annotations.ApiOperation;

@RestController
public class CustomizeQuoteController {
	private static final Logger logger = LoggerFactory.getLogger(CustomizeQuoteController.class);

	@Autowired
	private CustomizeQuoteService customizeQuoteService;

	@Autowired
	PayMentService payMentService;

	@Autowired
	CommonHttpRequest httpService;

	@Autowired
	IMetaService metaService;

	@Autowired
	UserSession userSession;

	@Autowired
	private PostManService postManService;

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@Autowired
	private HttpServletResponse response;

	@ApiOperation(value = "returns customer created quote", notes = "this api returns quote details created by customer on customize quote page")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@ApiMockParam(example = "124", value = "unique quote sequence number")
	@RequestMapping(value = "/api/customize-quote/get-quote-details", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> getCustomizedQuoteDetails(
			@RequestParam(name = "quoteSeqNumber") String quoteSeqNumber) {
		BigDecimal quoteSeqNumberDet = null;
		if (null != quoteSeqNumber && !quoteSeqNumber.equals("") && !quoteSeqNumber.equalsIgnoreCase("null")) {
			quoteSeqNumberDet = ArgUtil.parseAsBigDecimal(quoteSeqNumber);
		}
		return customizeQuoteService.getCustomizedQuoteDetails(quoteSeqNumberDet);
	}

	@ApiOperation(value = "returns calculated customize quote", notes = "this api calculate the quote details on the selection of premium")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/customize-quote/calculate-quote", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> calculateCutomizeQuote(@RequestBody CustomizeQuoteModel customizeQuoteModel) {
		return customizeQuoteService.calculateCutomizeQuote(customizeQuoteModel);
	}

	@ApiOperation(value = "returns the list of quotation sequence number", notes = "this will return you the quote sequence number of all the quote that belongs to customer")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/customize-quote/get-quoteseq-list", method = RequestMethod.GET)
	public AmxApiResponse<?, Object> getQuoteSeqList() {
		return customizeQuoteService.getQuoteSeqList();
	}

	@ApiOperation(value = "return terms and conditions of AMIB for payment gateway")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/customize-quote/terms-condition", method = RequestMethod.GET)
	public AmxApiResponse<?, Object> getTermsAndCondition() {
		return customizeQuoteService.getTermsAndCondition();
	}

	@ApiOperation(value = "submits the calculated quote and calls payment gateway", notes = "this Api will first submit the customize quote details to server and on success of it, it will return a redirect url which will have Payment gateway url , Application redirect url and also required pay sequence number which will be required for getting payment status and downloading payment transection receipt ")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/customize-quote/submit-quote", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> saveCustomizeQuote(@RequestBody CustomizeQuoteModel customizeQuoteModel,
			HttpServletRequest request) {
		String paygRedirectUrl = HttpUtils.getServerName(request)+"/app/landing/myquotes/quote";
		return customizeQuoteService.saveCustomizeQuote(customizeQuoteModel, request, paygRedirectUrl);
	}

	@ApiOperation(value = "submits payment info to server", notes = "this api is not going to be consumed by ui end. this is internal called api for payment gateway")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/callback/payg/payment/capture", method = { RequestMethod.POST })
	public PaymentResponseDto onPaymentCallback(@RequestBody PaymentResponseDto paymentResponse) {
		try {
			setMetaData();

			logger.info(" onPaymentCallbackNew :: paymentResponse :" + JsonUtil.toJson(paymentResponse));

			PaymentDetails paymentDetails = new PaymentDetails();
			paymentDetails.setPaymentId(paymentResponse.getPaymentId());
			paymentDetails.setApprovalNo(paymentResponse.getAuth_appNo());
			paymentDetails.setApprovalDate(null);
			
			if(paymentResponse.getResultCode().equalsIgnoreCase(Constants.NEW_KNET_NOTCAPTURED))
			{
				paymentDetails.setResultCd(Constants.NEW_KNET_MODIFIED_RESPONSE);
				logger.info(" onPaymentCallbackNew :: paymentResponse2 :" + paymentDetails.toString());
			}else {
				paymentDetails.setResultCd(paymentResponse.getResultCode());
			}
						
			paymentDetails.setTransId(paymentResponse.getTransactionId());
			paymentDetails.setRefId(paymentResponse.getReferenceId());

			if (null != paymentResponse.getTrackId()) {
				BigDecimal paySeqNumber = new BigDecimal(paymentResponse.getTrackId().toString());
				logger.info(" onPaymentCallbackNew :: paySeqNumber  :" + paySeqNumber);
				paymentDetails.setPaySeqNum(paySeqNumber);
				paymentDetails.setPaymentToken(paySeqNumber.toString());
			} else {
				paymentDetails.setPaySeqNum(null);
			}

			PaymentDetails updateStatus = payMentService.updatePaymentDetals(paymentDetails);
			logger.info(" onPaymentCallbackNew :: updateStatus  :" + updateStatus.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return paymentResponse;
	}

	@ApiOperation(value = "return the payment status after payment through payment gateway", notes = "this api is called after payment "
			+ "gets done by payment gateway and it will return the status of payment done, it will trigger an email to the customer of successfull payment transaction with transaction receipt pdf")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@ApiMockParam(example = "123", value = "pay sequence number of transaction")
	@RequestMapping(value = "/api/payment-status", method = { RequestMethod.POST })
	public AmxApiResponse<?, Object> getPaymentStatus(@RequestParam String paySeqNum) {
		logger.info("getPaymentStatus :: paySeqNum  :" + paySeqNum);
		logger.info("getPaymentStatus :: userSession :" + userSession.toString());
		logger.info("getPaymentStatus :: metaService :" + metaService.getTenantProfile().toString());

		BigDecimal paySeqNumDet = null;
		if (null != paySeqNum && !paySeqNum.equals("") && !paySeqNum.equalsIgnoreCase("null")) {
			paySeqNumDet = ArgUtil.parseAsBigDecimal(paySeqNum, null);
		}

		return payMentService.getPaymentStatus(paySeqNumDet);
	}

	@ApiOperation(value = "api to download payment receipt after successfull paymnet")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@ApiMockParam(example = "123", value = "pay-sequence number of payment transaction")
	@RequestMapping(value = "/api/payment-receipt-data", method = { RequestMethod.GET })
	public String paymentReceiptDataExt(@RequestParam String paySeqNum) {
		File file = null;
		PaymentReceipt paymentReceipt = null;
		try {
			BigDecimal paySeqNumDet = null;
			if (null != paySeqNum && !paySeqNum.equals("") && !paySeqNum.equalsIgnoreCase("null")) {
				paySeqNumDet = ArgUtil.parseAsBigDecimal(paySeqNum, null);
			}

			AmxApiResponse<PolicyReceiptDetails, Object> receiptData = payMentService.paymentReceiptData(paySeqNumDet);
			if (receiptData.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {
				paymentReceipt = receiptData.getData().toPaymentReceipt();
				logger.info(" getPaymentStatus :: paymentReceipt  :" + paymentReceipt.toString());
			}

			Map<String, Object> wrapper = new HashMap<String, Object>();
			Map<String, Object> model = new HashMap<String, Object>();
			ArrayList<Map> dataList = new ArrayList<>();

			model.put(DetailsConstants.applicationId, paymentReceipt.getApplicationId());
			model.put(DetailsConstants.customerId, paymentReceipt.getCustomerId());
			model.put(DetailsConstants.paymentDate, paymentReceipt.getPaymentDate());
			model.put(DetailsConstants.paymentMode, paymentReceipt.getPaymentMode());
			model.put(DetailsConstants.amountPaidNumber,
					Utility.getAmountInCurrency(paymentReceipt.getAmountPaidNumber(),
							metaService.getTenantProfile().getDecplc(), metaService.getTenantProfile().getCurrency()));
			model.put(DetailsConstants.amountPaidWord, paymentReceipt.getAmountPaidWord());
			model.put(DetailsConstants.paymentId, paymentReceipt.getPaymentId());
			model.put(DetailsConstants.customerName, paymentReceipt.getCustomerName());
			model.put(DetailsConstants.civilId, paymentReceipt.getCivilId());
			model.put(DetailsConstants.mobileNumber, paymentReceipt.getMobileNumber());
			model.put(DetailsConstants.emialId, paymentReceipt.getEmialId());
			model.put(DetailsConstants.policyDuration, (paymentReceipt.getPolicyDuration() + " Year"));
			model.put(DetailsConstants.governate, paymentReceipt.getGovernate());
			model.put(DetailsConstants.areaDesc, paymentReceipt.getAreaDesc());
			model.put(DetailsConstants.address, paymentReceipt.getAddress());
			model.put(DetailsConstants.make, paymentReceipt.getMake());
			model.put(DetailsConstants.subMake, paymentReceipt.getSubMake());
			model.put(DetailsConstants.ktNumber, paymentReceipt.getKtNumber());
			model.put(DetailsConstants.chasisNumber, paymentReceipt.getChasisNumber());
			model.put(DetailsConstants.modelYear, paymentReceipt.getModelYear());
			model.put(DetailsConstants.trnsReceiptRef, paymentReceipt.getTrnsReceiptRef());
			model.put(DetailsConstants.policyNumber, paymentReceipt.getPolicyNumber());
			model.put(DetailsConstants.policyIssueDate, paymentReceipt.getPolicyIssueDate());
			model.put(DetailsConstants.policyFromDate, paymentReceipt.getPolicyFromDate());
			model.put(DetailsConstants.policyDueDate, paymentReceipt.getPolicyDueDate());
			model.put(DetailsConstants.additionalCoverage, paymentReceipt.getAdditionalCoverage());
			model.put(DetailsConstants.insuranceCo, paymentReceipt.getInsuranceCo());
			model.put(DetailsConstants.vehicleValue, paymentReceipt.getVehicleValue());
			model.put(DetailsConstants.purpose, paymentReceipt.getPurpose());
			model.put(DetailsConstants.colour, paymentReceipt.getColour());
			model.put(DetailsConstants.shape, paymentReceipt.getShape());
			model.put(DetailsConstants.capacity, paymentReceipt.getCapacity());
			model.put(DetailsConstants.fuelType, paymentReceipt.getFuelType());
			model.put(DetailsConstants.vehicleCondition, paymentReceipt.getVehicleCondition());
			model.put(DetailsConstants.insuredName, paymentReceipt.getCustomerName());
			model.put(DetailsConstants.insuredAddress, paymentReceipt.getAddress());
			model.put(DetailsConstants.insuredMobileNo, paymentReceipt.getMobileNumber());
			model.put(DetailsConstants.policyContribution, paymentReceipt.getPolicyContribution());
			model.put(DetailsConstants.supervisionFees, paymentReceipt.getSupervisionFees());
			model.put(DetailsConstants.issueFees, paymentReceipt.getIssueFees());
			model.put(DetailsConstants.endorsMentFees, paymentReceipt.getEndorsementFees());
			model.put(DetailsConstants.discountAmount, paymentReceipt.getDiscountAmount());
			
			
			dataList.add(model);
			wrapper.put("results", dataList);
			
			
			for (Map.Entry<String, Object> entry : wrapper.entrySet()) {
				logger.info("Key = " + entry.getKey() + ", Value = " + entry.getValue());
			}

			file = postManService.processTemplate(new File(TemplatesIB.POLICY_RECEIPT, wrapper, File.Type.PDF))
					.getResult();
			file.create(response, true);
		} catch (Exception e) {
			logger.error("paymentReceiptDataExt", e);
		}
		return null;
	}
	
	
	@ApiOperation(value = "Validate payment link")
	@ApiWebAppStatus({WebAppStatusCodes.SUCCESS , WebAppStatusCodes.TECHNICAL_ERROR})
	@RequestMapping(value="/pub/payment-validatelink", method= {RequestMethod.POST})
	public AmxApiResponse<CustomizeQuoteModel, Object> validatePaymentLink(@RequestParam BigDecimal linkId, @RequestParam String verifyCode, @RequestParam BigDecimal languageId,HttpServletRequest request){
		CustomizeQuoteModel customizeQuoteModel = customizeQuoteService.validatePaymentLink(linkId,verifyCode,languageId,request);
		return AmxApiResponse.build(customizeQuoteModel);
		
	}

	private void setMetaData() {

		logger.info("CustomizeQuoteService :: setMetaData :: getLanguageId  :" + userSession.getLanguageId());
		customerRegistrationService.getCompanySetUp();

		/*
		 * if (null == metaService.getTenantProfile().getCountryId() || null ==
		 * metaService.getUserDeviceInfo().getDeviceId()) {
		 * logger.info("CustomizeQuoteService :: setMetaData :: getLanguageId  :" +
		 * userSession.getLanguageId()); userSession.setLanguageId(new BigDecimal(0));
		 * customerRegistrationService.getCompanySetUp(); }
		 */
	}
	
	
}
