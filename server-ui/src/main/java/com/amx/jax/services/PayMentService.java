package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.DetailsConstants;
import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.dao.MyQuoteDao;
import com.amx.jax.dao.PayMentDao;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.MyQuoteModel;
import com.amx.jax.models.PaymentDetails;
import com.amx.jax.models.PaymentReceipt;
import com.amx.jax.models.PaymentStatus;
import com.amx.jax.models.ResponseInfo;
import com.amx.jax.ui.session.UserSession;

@Service
public class PayMentService
{
	String TAG = "com.amx.jax.services :: PayMentService :: ";

	private static final Logger logger = LoggerFactory.getLogger(PayMentService.class);
	
	@Autowired
	MetaData metaData;
	
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
			ArrayList<MyQuoteModel> getUserQuote = myQuoteDao.getUserQuote(userSession.getCustomerSequenceNumber());
			for (int i = 0; i < getUserQuote.size(); i++)
			{
				MyQuoteModel myQuoteModelFromDb = getUserQuote.get(i);
				logger.info(TAG + " insertPaymentDetals :: myQuoteModelFromDb :" + myQuoteModelFromDb.toString());
				if (null != quoteSeqNum && !quoteSeqNum.toString().equals(""))
				{
					if (null != myQuoteModelFromDb.getQuoteSeqNumber() && myQuoteModelFromDb.getQuoteSeqNumber().equals(quoteSeqNum))
					{
						myQuoteModel = myQuoteModelFromDb;
					}
				}
			}
			
			logger.info(TAG + " insertPaymentDetals :: quoteSeqNum :" + quoteSeqNum);
			logger.info(TAG + " insertPaymentDetals :: getAppSeqNumber :" + myQuoteModel.getAppSeqNumber());
			logger.info(TAG + " insertPaymentDetals :: getVerNumber :" + myQuoteModel.getVerNumber());
			
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
				logger.info(TAG + " insertPaymentDetals :: updatePaymentDetailRet :" + paymentDetails);
			}
			else
			{
				resp.setMessageKey(paymentDetails.getErrorCode());
				resp.setMessage(paymentDetails.getErrorMessage());
				resp.setStatusKey(ApiConstants.FAILURE);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
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
			logger.info(TAG + " cretaeAmibCust :: getCustomerSequenceNumber  :" + userSession.getCustomerSequenceNumber());
			logger.info(TAG + " cretaeAmibCust :: getCivilId  :" + userSession.getCivilId());
			
			ResponseInfo validate = payMentDao.cretaeAmibCust(userSession.getCustomerSequenceNumber(), userSession.getCivilId());
			logger.info(TAG + " cretaeAmibCust :: validate  :" + validate.toString());
			
			if(null == validate.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessageKey(validate.getErrorCode());
				resp.setMessage(validate.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
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
			logger.info(TAG + " processReceipt :: validate  :" + validate.toString());
			
			if(null == validate.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessageKey(validate.getErrorCode());
				resp.setMessage(validate.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
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
			logger.info(TAG + " createAmibPolicy :: validate  :" + validate.toString());
			
			if(null == validate.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessageKey(validate.getErrorCode());
				resp.setMessage(validate.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
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
			logger.info(TAG + " preparePrintData :: validate  :" + validate.toString());
			
			if(null == validate.getErrorCode())
			{
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessageKey(validate.getErrorCode());
				resp.setMessage(validate.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
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
			logger.info(TAG + " getPaymentStatus :: arrayResponseModel  :" + arrayResponseModel.toString());
			if(null == arrayResponseModel.getErrorCode())
			{
				paymentStatus = (PaymentStatus) arrayResponseModel.getObject();
				logger.info(TAG + " getPaymentStatus :: paymentStatus  :" + paymentStatus.toString());
				
				if(paymentStatus.getPaymentStatus().equalsIgnoreCase("CAPTURED"))
				{
					emailSmsService.emialToCustonSuccessPg(paymentStatus.getTotalAmount(),
							paymentStatus.getTransactionId(), paymentStatus.getAppSeqNumber(), receiptData(paySeqNum));

					AmxApiResponse<? , Object> createAmibResp = payMentService.cretaeAmibCust();
					if (createAmibResp.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
					{
						emailSmsService.failedPGProcedureAfterCapture(paymentStatus , createAmibResp.getMessageKey() , createAmibResp.getMessage() , "CREATE AMIB PROCEDURE" , paySeqNum.toString());
					}
					else
					{
						AmxApiResponse<? , Object> processTeceiptResp = payMentService.processReceipt(paySeqNum);
						if (processTeceiptResp.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
						{
							emailSmsService.failedPGProcedureAfterCapture(paymentStatus , processTeceiptResp.getMessageKey() , processTeceiptResp.getMessage() , "PROCESS RECEIPT PROCEDURE" , paySeqNum.toString());
						}
						else
						{
							AmxApiResponse<? , Object> createAmibPolicyResp = payMentService.createAmibPolicy(paySeqNum);
							if (createAmibPolicyResp.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
							{
								emailSmsService.failedPGProcedureAfterCapture(paymentStatus , createAmibPolicyResp.getMessageKey() , createAmibPolicyResp.getMessage() , "CREATE AMIB PLOICY PROCEDURE" , paySeqNum.toString());
							}
							else
							{
								AmxApiResponse<? , Object> preparePrintData = payMentService.preparePrintData(paySeqNum);
								if (preparePrintData.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
								{
									emailSmsService.failedPGProcedureAfterCapture(paymentStatus , preparePrintData.getMessageKey() , preparePrintData.getMessage() , "PREPARE STATEMENT PROCEDURE" , paySeqNum.toString());
								}
							}
						}
					}
				}
				
				resp.setData(paymentStatus);
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessageKey(arrayResponseModel.getErrorCode());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}
	
	private ArrayList<Map> receiptData(BigDecimal paySeqNum) 
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
		model.put(DetailsConstants.amountPaidNumber, paymentReceipt.getAmountPaidNumber());
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
		
		return dataList;
	}
	
	
	public AmxApiResponse<?, Object> paymentReceiptData(BigDecimal paySeqNum)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<>();
		try
		{
			ArrayResponseModel arrayResponseModel = payMentDao.paymentReceiptData(paySeqNum);
			if(null == arrayResponseModel.getErrorCode())
			{
				resp.setData(arrayResponseModel.getObject());
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessageKey(arrayResponseModel.getErrorCode());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}
}
