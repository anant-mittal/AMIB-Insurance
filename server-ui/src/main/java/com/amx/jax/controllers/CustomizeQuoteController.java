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

	@ApiOperation(value = "returns the list of quotation sequence number")
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

	@ApiOperation(value = "submits the calculated quote and calls payment gateway")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/api/customize-quote/submit-quote", method = RequestMethod.POST)
	public AmxApiResponse<?, Object> saveCustomizeQuote(@RequestBody CustomizeQuoteModel customizeQuoteModel,
			HttpServletRequest request) {
		return customizeQuoteService.saveCustomizeQuote(customizeQuoteModel, request);
	}

	@ApiOperation(value = "submits payment info to server")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@RequestMapping(value = "/remit/save-remittance", method = { RequestMethod.POST })
	public PaymentResponseDto onPaymentCallback(@RequestBody PaymentResponseDto paymentResponse) {
		try {
			setMetaData();

			logger.info("onPaymentCallback :: getPostDate  :" + paymentResponse.getPostDate());
			PaymentDetails paymentDetails = new PaymentDetails();
			paymentDetails.setPaymentId(paymentResponse.getPaymentId());
			paymentDetails.setApprovalNo(paymentResponse.getAuth_appNo());
			paymentDetails.setApprovalDate(null);
			paymentDetails.setResultCd(paymentResponse.getResultCode());
			paymentDetails.setTransId(paymentResponse.getTransactionId());
			paymentDetails.setRefId(paymentResponse.getReferenceId());

			if (null != paymentResponse.getTrackId()) {
				BigDecimal paySeqNumber = new BigDecimal(paymentResponse.getTrackId().toString());
				logger.info(" onPaymentCallback :: paySeqNumber  :" + paySeqNumber);
				paymentDetails.setPaySeqNum(paySeqNumber);
				paymentDetails.setPaymentToken(paySeqNumber.toString());
			} else {
				paymentDetails.setPaySeqNum(null);
			}

			PaymentDetails updateStatus = payMentService.updatePaymentDetals(paymentDetails);
			logger.info(" onPaymentCallback :: updateStatus  :" + updateStatus.toString());

		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return paymentResponse;
	}

	@ApiOperation(value = "return the payment status after payment through payment gateway", notes = "once this api is called and payment "
			+ "is success it will trigger an email to the customer of successfull payment transaction with transaction receipt pdf")
	@ApiWebAppStatus({ WebAppStatusCodes.TECHNICAL_ERROR, WebAppStatusCodes.SUCCESS })
	@ApiMockParam(example = "123", value = "pay sequence number of transaction")
	@RequestMapping(value = "/api/payment-status", method = { RequestMethod.POST })
	public AmxApiResponse<?, Object> getPaymentStatus(@RequestParam String paySeqNum) {
		logger.info("getPaymentStatus :: paySeqNum  :" + paySeqNum);
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

			AmxApiResponse<?, Object> receiptData = payMentService.paymentReceiptData(paySeqNumDet);
			if (receiptData.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS)) {
				paymentReceipt = (PaymentReceipt) receiptData.getData();
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

			dataList.add(model);
			wrapper.put("results", dataList);

			file = postManService.processTemplate(new File(TemplatesIB.TRNX_RECEIPT, wrapper, File.Type.PDF))
					.getResult();
			file.create(response, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setMetaData() {
		if (null == metaService.getTenantProfile().getCountryId()
				|| null == metaService.getUserDeviceInfo().getDeviceId()) {
			if (httpService.getLanguage().toString().equalsIgnoreCase("EN")) {
				metaService.getTenantProfile().setLanguageId(new BigDecimal(0));
			}
			customerRegistrationService.getCompanySetUp();
		}
	}
}
