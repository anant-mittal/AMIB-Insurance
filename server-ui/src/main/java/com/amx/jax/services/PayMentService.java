package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.DetailsConstants;
import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.dao.MyQuoteDao;
import com.amx.jax.dao.PayMentDao;
import com.amx.jax.meta.IMetaService;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.MyQuoteModel;
import com.amx.jax.models.PaymentDetails;
import com.amx.jax.models.PaymentReceipt;
import com.amx.jax.models.PaymentStatus;
import com.amx.jax.models.ResponseInfo;
import com.amx.jax.ui.session.UserSession;
import com.amx.jax.utility.Utility;

@Service
public class PayMentService
{
	String TAG = "com.amx.jax.services :: PayMentService :: ";

	private static final Logger logger = LoggerFactory.getLogger(PayMentService.class);
	
	@Autowired
	IMetaService metaService;
	
	@Autowired
	PayMentDao payMentDao;
	
	@Autowired
	UserSession userSession;
	
	@Autowired
	private MyQuoteDao myQuoteDao;
	
	@Autowired
	PayMentService payMentService;
	
	@Autowired
	EmailSmsService emailSmsService;
	
	public AmxApiResponse<PaymentDetails, Object> insertPaymentDetals(BigDecimal quoteSeqNum , BigDecimal paymentAmount)
	{
		AmxApiResponse<PaymentDetails, Object> resp = new AmxApiResponse<PaymentDetails, Object>();
		try
		{
			MyQuoteModel myQuoteModel = new MyQuoteModel();
			ArrayResponseModel arrayResponseUserQuote = myQuoteDao.getUserQuote(userSession.getCustomerSequenceNumber(), userSession.getLanguageId());
			if(arrayResponseUserQuote.getErrorCode() == null)
			{
				ArrayList<MyQuoteModel> getUserQuote = arrayResponseUserQuote.getDataArray();
				for (int i = 0; i < getUserQuote.size(); i++)
				{
					MyQuoteModel myQuoteModelFromDb = getUserQuote.get(i);
					if (null != quoteSeqNum && !quoteSeqNum.toString().equals(""))
					{
						if (null != myQuoteModelFromDb.getQuoteSeqNumber() && myQuoteModelFromDb.getQuoteSeqNumber().equals(quoteSeqNum))
						{
							myQuoteModel = myQuoteModelFromDb;
						}
					}
				}
			}
			else
			{
				resp.setMessageKey(arrayResponseUserQuote.getErrorCode());
				resp.setMessage(arrayResponseUserQuote.getErrorMessage());
				return resp;
			}
			
			PaymentDetails insertPaymentDetails = new PaymentDetails();
			insertPaymentDetails.setAppSeqNum(myQuoteModel.getAppSeqNumber());
			insertPaymentDetails.setQuoteSeqNum(quoteSeqNum);
			insertPaymentDetails.setQouteVerNum(myQuoteModel.getVerNumber());
			insertPaymentDetails.setCustSeqNum(userSession.getCustomerSequenceNumber());
			insertPaymentDetails.setPaymentAmount(paymentAmount);
			insertPaymentDetails.setPaymentMethod(HardCodedValues.PAYMENT_METHOD);
			
			PaymentDetails paymentDetails = payMentDao.insertPaymentDetals(insertPaymentDetails , userSession.getCivilId());
			if(null == paymentDetails.getErrorCode())
			{
				resp.setData(paymentDetails);
			}
			else
			{
				resp.setMessageKey(paymentDetails.getErrorCode());
				resp.setMessage(paymentDetails.getErrorMessage());
				resp.setStatusKey(paymentDetails.getErrorCode());
			}
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "insertPaymentDetals :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}
	
	
	public PaymentDetails updatePaymentDetals(PaymentDetails paymentDetails)
	{
		return payMentDao.updatePaymentDetals(paymentDetails , userSession.getCivilId());
	}
	
	
	public AmxApiResponse<?, Object> cretaeAmibCust()
	{
		AmxApiResponse<PaymentDetails, Object> resp = new AmxApiResponse<PaymentDetails, Object>();
		try
		{
			ResponseInfo validate = payMentDao.cretaeAmibCust(userSession.getCustomerSequenceNumber(), userSession.getCivilId());
			
			if(null == validate.getErrorCode())
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			}
			else
			{
				resp.setStatusKey(validate.getErrorMessage());
				resp.setMessageKey(validate.getErrorCode());
				resp.setMessage(validate.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG+"cretaeAmibCust :: exception :" + e);
		}
		return resp;
	}
	
	
	public AmxApiResponse<?, Object> processReceipt(BigDecimal paySeqNum)
	{
		AmxApiResponse<PaymentDetails, Object> resp = new AmxApiResponse<PaymentDetails, Object>();
		try
		{
			logger.info(TAG + " processReceipt :: paySeqNum  :" + paySeqNum);
			ResponseInfo validate = payMentDao.processReceipt(userSession.getCustomerSequenceNumber(), userSession.getCivilId() , paySeqNum);
			if(null == validate.getErrorCode())
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			}
			else
			{
				resp.setStatusKey(validate.getErrorCode());
				resp.setMessageKey(validate.getErrorCode());
				resp.setMessage(validate.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "processReceipt :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}
	
	
	public AmxApiResponse<?, Object> createAmibPolicy(BigDecimal paySeqNum)
	{
		AmxApiResponse<PaymentDetails, Object> resp = new AmxApiResponse<PaymentDetails, Object>();
		try
		{
			logger.info(TAG + " createAmibPolicy :: paySeqNum  :" + paySeqNum);
			ResponseInfo validate = payMentDao.createAmibPolicy(userSession.getCustomerSequenceNumber(), userSession.getCivilId() , paySeqNum);
			if(null == validate.getErrorCode())
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			}
			else
			{
				resp.setStatusKey(validate.getErrorCode());
				resp.setMessageKey(validate.getErrorCode());
				resp.setMessage(validate.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "createAmibPolicy :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}
	
	public AmxApiResponse<?, Object> preparePrintData(BigDecimal paySeqNum)
	{
		AmxApiResponse<PaymentDetails, Object> resp = new AmxApiResponse<PaymentDetails, Object>();
		try
		{
			logger.info(TAG + " preparePrintData :: paySeqNum  :" + paySeqNum);
			ResponseInfo validate = payMentDao.preparePrintData(paySeqNum);
			if(null == validate.getErrorCode())
			{
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			}
			else
			{
				resp.setStatusKey(validate.getErrorCode());
				resp.setMessageKey(validate.getErrorCode());
				resp.setMessage(validate.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "preparePrintData :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}
	
	public AmxApiResponse<?, Object> getPaymentStatus(BigDecimal paySeqNum)
	{
		AmxApiResponse<PaymentStatus, Object> resp = new AmxApiResponse<>();
		PaymentStatus paymentStatus = new PaymentStatus();
		
		try
		{
			ArrayResponseModel arrayResponseModel = payMentDao.getPaymentStatus(paySeqNum);
			logger.info(TAG + " getPaymentStatus :: arrayResponseModel  1 :" + arrayResponseModel.toString());
			paymentStatus.setPaymentProcedureStatus("N");
			
			logger.info(TAG + " getPaymentStatus :: arrayResponseModel 2 :" + arrayResponseModel.getErrorCode());
			
			if(null == arrayResponseModel.getErrorCode())
			{
				paymentStatus = (PaymentStatus) arrayResponseModel.getObject();
				logger.info(TAG + " getPaymentStatus :: paymentStatus 3 :" + paymentStatus.toString());
				
				if(paymentStatus.getPaymentStatus().equalsIgnoreCase("CAPTURED"))
				{
					try
					{
						logger.info(TAG + " getPaymentStatus :: paymentStatus 4 :" + paymentStatus.toString());
						
						AmxApiResponse<? , Object> createAmibResp = payMentService.cretaeAmibCust();
						if (!createAmibResp.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
						{
							logger.info(TAG + " getPaymentStatus :: paymentStatus 5 :" + paymentStatus.toString());
							logger.info(TAG + " getPaymentStatus :: paymentStatus getMessageKey :" + createAmibResp.getMessageKey());
							logger.info(TAG + " getPaymentStatus :: paymentStatus getMessage :" +  createAmibResp.getMessage());
							logger.info(TAG + " getPaymentStatus :: paymentStatus paySeqNum :" +  paySeqNum.toString());
							emailSmsService.failedPGProcedureAfterCapture(paymentStatus , createAmibResp.getMessageKey() , createAmibResp.getMessage() , "CREATE AMIB PROCEDURE" , paySeqNum.toString());
							paymentStatus.setPaymentProcedureStatus("Y");
						}
						else
						{
							logger.info(TAG + " getPaymentStatus :: paymentStatus 6 :" + paymentStatus.toString());
							AmxApiResponse<? , Object> processTeceiptResp = payMentService.processReceipt(paySeqNum);
							if (!processTeceiptResp.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
							{
								logger.info(TAG + " getPaymentStatus :: paymentStatus 7 :" + paymentStatus.toString());
								logger.info(TAG + " getPaymentStatus :: paymentStatus getMessageKey :" + processTeceiptResp.getMessageKey());
								logger.info(TAG + " getPaymentStatus :: paymentStatus getMessage :" +  processTeceiptResp.getMessage());
								logger.info(TAG + " getPaymentStatus :: paymentStatus paySeqNum :" +  paySeqNum.toString());
								
								emailSmsService.failedPGProcedureAfterCapture(paymentStatus , processTeceiptResp.getMessageKey() , processTeceiptResp.getMessage() , "PROCESS RECEIPT PROCEDURE" , paySeqNum.toString());
								paymentStatus.setPaymentProcedureStatus("Y");
							}
							else
							{
								logger.info(TAG + " getPaymentStatus :: paymentStatus 8 :" + paymentStatus.toString());
								AmxApiResponse<? , Object> createAmibPolicyResp = payMentService.createAmibPolicy(paySeqNum);
								if (!createAmibPolicyResp.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
								{
									logger.info(TAG + " getPaymentStatus :: paymentStatus 9 :" + paymentStatus.toString());
									logger.info(TAG + " getPaymentStatus :: paymentStatus getMessageKey :" + createAmibPolicyResp.getMessageKey());
									logger.info(TAG + " getPaymentStatus :: paymentStatus getMessage :" +  createAmibPolicyResp.getMessage());
									logger.info(TAG + " getPaymentStatus :: paymentStatus paySeqNum :" +  paySeqNum.toString());
									emailSmsService.failedPGProcedureAfterCapture(paymentStatus , createAmibPolicyResp.getMessageKey() , createAmibPolicyResp.getMessage() , "CREATE AMIB PLOICY PROCEDURE" , paySeqNum.toString());
									paymentStatus.setPaymentProcedureStatus("Y");
								}
								else
								{
									logger.info(TAG + " getPaymentStatus :: paymentStatus 10 :" + paymentStatus.toString());
									AmxApiResponse<? , Object> preparePrintData = payMentService.preparePrintData(paySeqNum);
									if (!preparePrintData.getStatusKey().equalsIgnoreCase(ApiConstants.SUCCESS))
									{
										logger.info(TAG + " getPaymentStatus :: paymentStatus 11 :" + paymentStatus.toString());
										logger.info(TAG + " getPaymentStatus :: paymentStatus getMessageKey :" + preparePrintData.getMessageKey());
										logger.info(TAG + " getPaymentStatus :: paymentStatus getMessage :" +  preparePrintData.getMessage());
										logger.info(TAG + " getPaymentStatus :: paymentStatus paySeqNum :" +  paySeqNum.toString());
										emailSmsService.failedPGProcedureAfterCapture(paymentStatus , preparePrintData.getMessageKey() , preparePrintData.getMessage() , "PREPARE STATEMENT PROCEDURE" , paySeqNum.toString());
									}
								}
							}
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
					try
					{
						emailSmsService.emialToCustonSuccessPg(paymentStatus.getTotalAmount(),
								paymentStatus.getTransactionId(), paymentStatus.getAppSeqNumber(), receiptData(paySeqNum));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				
				logger.info(TAG + " getPaymentStatus :: paymentStatus 12 :" + paymentStatus.toString());
				resp.setData(paymentStatus);
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(arrayResponseModel.getErrorCode());
				resp.setMessageKey(arrayResponseModel.getErrorCode());
			}
			
			//Check
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "getPaymentStatus :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}
	
	public ArrayList<Map> receiptData(BigDecimal paySeqNum) 
	{
		PaymentReceipt paymentReceipt = null;
		
		logger.info(TAG + " receiptData :: paySeqNum  :" + paySeqNum);
		
		AmxApiResponse<?, Object> receiptData  = payMentService.paymentReceiptData(paySeqNum);
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
		model.put(DetailsConstants.amountPaidNumber, Utility.getAmountInCurrency(paymentReceipt.getAmountPaidNumber(), metaService.getTenantProfile().getDecplc() , metaService.getTenantProfile().getCurrency()));
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
		
		//emailSmsService.emialToCustonSuccessPg(new BigDecimal(100),"11111111111", new BigDecimal(2222) , dataList);
		
		return dataList;
	}
	
	
	public AmxApiResponse<?, Object> paymentReceiptData(BigDecimal paySeqNum)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<>();
		try
		{
			ArrayResponseModel arrayResponseModel = payMentDao.paymentReceiptData(paySeqNum, userSession.getLanguageId());
			if(null == arrayResponseModel.getErrorCode())
			{
				resp.setData(arrayResponseModel.getObject());
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(arrayResponseModel.getErrorCode());
				resp.setMessageKey(arrayResponseModel.getErrorCode());
			}
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "paymentReceiptData :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}
}
