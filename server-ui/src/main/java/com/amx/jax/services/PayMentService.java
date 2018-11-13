package com.amx.jax.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.HardCodedValues;
import com.amx.jax.dao.MyQuoteDao;
import com.amx.jax.dao.PayMentDao;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.MyQuoteModel;
import com.amx.jax.models.PaymentDetails;
import com.amx.jax.models.PaymentReceiptModel;
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
		try
		{
			
			ArrayResponseModel arrayResponseModel = payMentDao.getPaymentStatus(paySeqNum);
			logger.info(TAG + " getPaymentStatus :: arrayResponseModel  :" + arrayResponseModel.toString());
			if(null == arrayResponseModel.getErrorCode())
			{
				PaymentStatus paymentStatus = (PaymentStatus) arrayResponseModel.getObject();
				logger.info(TAG + " getPaymentStatus :: paymentStatus  :" + paymentStatus.toString());
				if(paymentStatus.getPaymentStatus().equalsIgnoreCase("CAPTURED"))
				{
					AmxApiResponse<? , Object> createAmibResp = payMentService.cretaeAmibCust();
					if (createAmibResp.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
					{
						//return createAmibResp;
					}
					else
					{
						AmxApiResponse<? , Object> processTeceiptResp = payMentService.processReceipt(paySeqNum);
						if (processTeceiptResp.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
						{
							//return processTeceiptResp;
						}
						else
						{
							AmxApiResponse<? , Object> createAmibPolicyResp = payMentService.createAmibPolicy(paySeqNum);
							if (createAmibPolicyResp.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
							{
								//return createAmibPolicyResp;
							}
							else
							{
								AmxApiResponse<? , Object> preparePrintData = payMentService.preparePrintData(paySeqNum);
								if (preparePrintData.getStatusKey().equalsIgnoreCase(ApiConstants.FAILURE))
								{
									//return preparePrintData;
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
				//Send Mail
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
	
	
	public AmxApiResponse<?, Object> paymentReceiptData(BigDecimal paySeqNum)
	{
		AmxApiResponse<PaymentReceiptModel, Object> resp = new AmxApiResponse<PaymentReceiptModel, Object>();
		try
		{
			PaymentReceiptModel paymentReceiptModel = payMentDao.paymentReceiptData(paySeqNum);
			
			logger.info(TAG + " paymentReceiptData :: paymentReceiptModel  :" + paymentReceiptModel.toString());
			
			if(null == paymentReceiptModel.getErrorCode())
			{
				resp.setData(paymentReceiptModel);
				resp.setStatusKey(ApiConstants.SUCCESS);
			}
			else
			{
				resp.setStatusKey(ApiConstants.FAILURE);
				resp.setMessageKey(paymentReceiptModel.getErrorCode());
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
