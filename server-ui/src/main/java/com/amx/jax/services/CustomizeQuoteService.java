package com.amx.jax.services;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.AppConfig;
import com.amx.jax.WebConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.dao.CustomizeQuoteDao;
import com.amx.jax.dao.MyQuoteDao;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.models.CustomizeQuoteAddPol;
import com.amx.jax.models.CustomizeQuoteInfo;
import com.amx.jax.models.CustomizeQuoteModel;
import com.amx.jax.models.CustomizeQuoteSave;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.MyQuoteModel;
import com.amx.jax.models.PaymentDetails;
import com.amx.jax.models.PgRedirectUrl;
import com.amx.jax.models.QuotationDetails;
import com.amx.jax.models.QuoteAddPolicyDetails;
import com.amx.jax.models.ResponseInfo;
import com.amx.jax.models.TotalPremium;
import com.amx.jax.models.ResponseInfo;
import com.amx.jax.payg.PayGService;
import com.amx.jax.payg.Payment;
import com.amx.jax.ui.session.UserSession;
import com.amx.jax.utility.CalculateUtility;
import com.amx.jax.utility.Utility;
import com.amx.utils.HttpUtils;

@Service
public class CustomizeQuoteService
{
	String TAG = "com.amx.jax.services :: CustomizeQuoteService :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomizeQuoteService.class);

	@Autowired
	MetaData metaData;
	
	@Autowired
	UserSession userSession;

	@Autowired
	private CustomizeQuoteDao customizeQuoteDao;

	@Autowired
	private MyQuoteDao myQuoteDao;
	
	@Autowired
	private PayGService payGService;

	@Autowired
	PayMentService payMentService;
	
	public AmxApiResponse<?, Object> getCustomizedQuoteDetails(BigDecimal quoteSeqNumber)
	{
		logger.info(TAG + " getCustomizedQuoteDetails :: quoteSeqNumber :" + quoteSeqNumber);
		AmxApiResponse<CustomizeQuoteModel, Object> resp = new AmxApiResponse<CustomizeQuoteModel, Object>();
		CustomizeQuoteModel customizeQuoteModel = new CustomizeQuoteModel();
		CustomizeQuoteInfo customizeQuoteInfo = new CustomizeQuoteInfo();
		ArrayList<QuoteAddPolicyDetails> quoteAddPolicyDetails = null;
		QuotationDetails quotationDetails = new QuotationDetails();
		TotalPremium totalPremium = new TotalPremium();
		HashMap<String, ArrayList> repTypeMap = new HashMap<String, ArrayList>();

		try
		{
			MyQuoteModel myQuoteModel = new MyQuoteModel();
			ArrayList<MyQuoteModel> getUserQuote = myQuoteDao.getUserQuote(userSession.getCustomerSequenceNumber());
			for (int i = 0; i < getUserQuote.size(); i++)
			{
				MyQuoteModel myQuoteModelFromDb = getUserQuote.get(i);
				if (null != quoteSeqNumber && !quoteSeqNumber.toString().equals(""))
				{
					if (null != myQuoteModelFromDb.getQuoteSeqNumber() && myQuoteModelFromDb.getQuoteSeqNumber().equals(quoteSeqNumber))
					{
						myQuoteModel = myQuoteModelFromDb;
						customizeQuoteInfo.setQuoteSeqNumber(quoteSeqNumber);
					}
				}
			}
			
			logger.info(TAG + " getCustomizedQuoteDetails :: myQuoteModel :" + myQuoteModel.toString());
			

			// SET Quotation Details
			quotationDetails.setMakeCode(myQuoteModel.getMakeCode());
			quotationDetails.setMakeDesc(myQuoteModel.getMakeDesc());
			quotationDetails.setSubMakeCode(myQuoteModel.getSubMakeCode());
			quotationDetails.setSubMakeDesc(myQuoteModel.getSubMakeDesc());
			quotationDetails.setCompanyCode(myQuoteModel.getCompanyCode());
			quotationDetails.setCompanyName(myQuoteModel.getCompanyName());
			quotationDetails.setCompanyShortCode(myQuoteModel.getCompanyShortCode());
			quotationDetails.setChassisNumber(myQuoteModel.getChassisNumber());
			quotationDetails.setPolicyDuration(myQuoteModel.getPolicyDuration());
			logger.info(TAG + " getCustomizedQuoteDetails :: quotationDetails :" + quotationDetails.toString());

			
			
			// SET QuoteAddPolicy Details
			quoteAddPolicyDetails = customizeQuoteDao.getQuoteAdditionalPolicy(myQuoteModel.getQuoteSeqNumber(), myQuoteModel.getVerNumber());
			logger.info(TAG + " getCustomizedQuoteDetails :: quoteAddPolicyDetails :" + quoteAddPolicyDetails.toString());

			
			
			// SET TotalPremium Details
			totalPremium.setBasicPremium(Utility.getNumericValue(myQuoteModel.getBasicPremium()));
			totalPremium.setAddCoveragePremium(Utility.getNumericValue(myQuoteModel.getAddCoveragePremium()));
			totalPremium.setIssueFee(Utility.getNumericValue(myQuoteModel.getIssueFee()));
			totalPremium.setSupervisionFees(Utility.getNumericValue(myQuoteModel.getSupervisionFees()));
			totalPremium.setTotalAmount(Utility.getNumericValue(myQuoteModel.getNetAmount()));
			logger.info(TAG + " getCustomizedQuoteDetails :: totalPremium :" + totalPremium.toString());

			
			
			// SET Replacement Type List Meta
			for (int i = 0; i < quoteAddPolicyDetails.size(); i++)
			{
				String polType = quoteAddPolicyDetails.get(i).getAddPolicyTypeCode();
				Date quoteDate = DateFormats.setDbSqlFormatDate(myQuoteModel.getQuoteDate());
				ArrayList repTypeArray = customizeQuoteDao.getReplacementTypeList(polType, quoteDate);
				if (null != repTypeArray)
				{
					repTypeMap.put(polType, repTypeArray);
				}
			}

			customizeQuoteModel.setCustomizeQuoteInfo(customizeQuoteInfo);
			customizeQuoteModel.setQuotationDetails(quotationDetails);
			customizeQuoteModel.setQuoteAddPolicyDetails(quoteAddPolicyDetails);
			customizeQuoteModel.setTotalPremium(totalPremium);

			
			AmxApiResponse<?, Object> calculatedQuoteResp = calculateCutomizeQuote(customizeQuoteModel);
			CustomizeQuoteModel calculatedQuote = (CustomizeQuoteModel) calculatedQuoteResp.getData();
			
			resp.setData(calculatedQuote);
			resp.setMeta(repTypeMap);
			resp.setStatusKey(ApiConstants.SUCCESS);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getQuoteSeqList()
	{
		AmxApiResponse<String, Object> resp = new AmxApiResponse<String, Object>();
		try
		{
			ArrayList<String> allQuotes = new ArrayList<String>();

			ArrayList<MyQuoteModel> getUserQuote = myQuoteDao.getUserQuote(userSession.getCustomerSequenceNumber());
			for (int i = 0; i < getUserQuote.size(); i++)
			{
				MyQuoteModel myQuoteModelFromDb = getUserQuote.get(i);
				if (null != myQuoteModelFromDb.getQuoteSeqNumber())
				{
					allQuotes.add(myQuoteModelFromDb.getQuoteSeqNumber().toString());
				}
			}
			resp.setMeta(allQuotes);
			resp.setStatusKey(ApiConstants.SUCCESS);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> calculateCutomizeQuote(CustomizeQuoteModel customizeQuoteModel)
	{
		AmxApiResponse<CustomizeQuoteModel, Object> resp = new AmxApiResponse<CustomizeQuoteModel, Object>();
		try
		{
			
			ArrayList<QuoteAddPolicyDetails> quoteAddPolicyDetails = null;
			HashMap<String, ArrayList> repTypeMap = new HashMap<String, ArrayList>();
			CustomizeQuoteModel customizeQuoteCalculated = CalculateUtility.calculateCustomizeQuote(customizeQuoteModel);
			CustomizeQuoteInfo customizeQuoteInfo = customizeQuoteCalculated.getCustomizeQuoteInfo();
			BigDecimal quoteSeqNumber = customizeQuoteInfo.getQuoteSeqNumber();
			
			MyQuoteModel myQuoteModel = new MyQuoteModel();
			ArrayList<MyQuoteModel> getUserQuote = myQuoteDao.getUserQuote(userSession.getCustomerSequenceNumber());
			for (int i = 0; i < getUserQuote.size(); i++)
			{
				MyQuoteModel myQuoteModelFromDb = getUserQuote.get(i);
				if (null != quoteSeqNumber && !quoteSeqNumber.toString().equals(""))
				{
					if (null != myQuoteModelFromDb.getQuoteSeqNumber() && myQuoteModelFromDb.getQuoteSeqNumber().equals(quoteSeqNumber))
					{
						myQuoteModel = myQuoteModelFromDb;
						customizeQuoteInfo.setQuoteSeqNumber(quoteSeqNumber);
					}
				}
			}
			
			quoteAddPolicyDetails = customizeQuoteCalculated.getQuoteAddPolicyDetails();
			for (int i = 0; i < quoteAddPolicyDetails.size(); i++)
			{
				String polType = quoteAddPolicyDetails.get(i).getAddPolicyTypeCode();
				Date quoteDate = DateFormats.setDbSqlFormatDate(myQuoteModel.getQuoteDate());
				ArrayList repTypeArray = customizeQuoteDao.getReplacementTypeList(polType, quoteDate);
				if (null != repTypeArray)
				{
					repTypeMap.put(polType, repTypeArray);
				}
			}
			
			resp.setData(CalculateUtility.calculateCustomizeQuote(customizeQuoteModel));
			resp.setStatusKey(ApiConstants.SUCCESS);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getTermsAndCondition()
	{
		AmxApiResponse<String, Object> resp = new AmxApiResponse<String, Object>();
		try
		{
			resp.setStatusKey(ApiConstants.SUCCESS);
			resp.setResults(customizeQuoteDao.getTermsAndCondition());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}
	
	
	public TreeMap<Integer,String> getTermsAndConditionTest()
	{
		return customizeQuoteDao.getTermsAndConditionTest();
	}

	public AmxApiResponse<?, Object> saveCustomizeQuote(CustomizeQuoteModel customizeQuoteModel , HttpServletRequest request)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			CustomizeQuoteInfo customizeQuoteInfo = customizeQuoteModel.getCustomizeQuoteInfo();
			BigDecimal quoteSeqNumber = customizeQuoteInfo.getQuoteSeqNumber();

			MyQuoteModel myQuoteModel = new MyQuoteModel();
			ArrayList<MyQuoteModel> getUserQuote = myQuoteDao.getUserQuote(userSession.getCustomerSequenceNumber());
			for (int i = 0; i < getUserQuote.size(); i++)
			{
				MyQuoteModel myQuoteModelFromDb = getUserQuote.get(i);
				if (null != myQuoteModelFromDb.getQuoteSeqNumber() && myQuoteModelFromDb.getQuoteSeqNumber().equals(quoteSeqNumber))
				{
					myQuoteModel = myQuoteModelFromDb;
				}
			}

			AmxApiResponse<?, Object> respQuoteAddModel = saveCustomizeQuoteAddPol(customizeQuoteModel, myQuoteModel);
			if (respQuoteAddModel.getStatus().equals(ApiConstants.FAILURE))
			{
				return respQuoteAddModel;
			}
			
			return saveCustomizeQuoteDetails(customizeQuoteModel, myQuoteModel ,customizeQuoteInfo ,request);
		
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> saveCustomizeQuoteDetails(CustomizeQuoteModel customizeQuoteModel, MyQuoteModel myQuoteModel ,CustomizeQuoteInfo customizeQuoteInfo , HttpServletRequest request)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			if (null != customizeQuoteModel)
			{
				TotalPremium totalPremium = customizeQuoteModel.getTotalPremium();
				CustomizeQuoteSave customizeQuoteSave = new CustomizeQuoteSave();
				customizeQuoteSave.setQuotSeqNumber(myQuoteModel.getQuoteSeqNumber());
				customizeQuoteSave.setVerNumber(myQuoteModel.getVerNumber());
				customizeQuoteSave.setBasicPremium(totalPremium.getBasicPremium());
				customizeQuoteSave.setSupervisionFees(totalPremium.getSupervisionFees());
				customizeQuoteSave.setIssueFee(totalPremium.getIssueFee());
				customizeQuoteSave.setDisscountAmt(new BigDecimal(0));
				customizeQuoteSave.setAddCoveragePremium(totalPremium.getAddCoveragePremium());
				customizeQuoteSave.setTotalAmount(totalPremium.getTotalAmount());
				
				logger.info(TAG + " saveCustomizeQuoteAddPol :: customizeQuoteSave :" + customizeQuoteSave.toString());
				ResponseInfo validate = customizeQuoteDao.saveCustomizeQuote(customizeQuoteSave , userSession.getCivilId());
				logger.info(TAG + " saveCustomizeQuoteAddPol :: getErrorCode() :" + validate.getErrorCode());
				logger.info(TAG + " saveCustomizeQuoteAddPol :: getErrorMessage() :" + validate.getErrorMessage());
				
				
				if (validate.getErrorCode() == null)
				{
					resp.setStatusKey(ApiConstants.SUCCESS);
					
					/******************************************************PAYMENT GATEWAY***********************************************************/
					
					logger.info(TAG + " saveCustomizeQuoteDetails :: getQuoteSeqNumber :" + customizeQuoteInfo.getQuoteSeqNumber());
					logger.info(TAG + " saveCustomizeQuoteDetails :: getTotalAmount    :" + totalPremium.getTotalAmount());
					logger.info(TAG + " saveCustomizeQuoteDetails :: getTotalAmount    :" + totalPremium.getTotalAmount());
					
					AmxApiResponse<PaymentDetails, Object> respInsertPayment = payMentService.insertPaymentDetals(customizeQuoteInfo.getQuoteSeqNumber(),totalPremium.getTotalAmount());
					PaymentDetails paymentDetails = respInsertPayment.getData();
					
					logger.info(TAG + " saveCustomizeQuoteDetails :: paymentDetails :" + paymentDetails.toString());
					
					Payment payment = new Payment();
					payment.setDocFinYear(1123);
					payment.setDocNo(paymentDetails.getPaySeqNum().toString());// PaySeqNum
					payment.setMerchantTrackId(paymentDetails.getPaySeqNum().toString());// PaySeqNum
					payment.setNetPayableAmount(totalPremium.getTotalAmount());
					payment.setPgCode(PayGServiceCode.KNET);
					
					logger.info(TAG + " saveCustomizeQuoteDetails :: request.getSession().getId() :" + request.getSession().getId());
					//String redirctUrl = payGService.getPaymentUrl(payment , HttpUtils.getServerName(request)+"/app/landing/myquotes/payment");
					String redirctUrl = payGService.getPaymentUrl(payment , HttpUtils.getServerName(request)+"/app/landing/myquotes/quote");
					logger.info(TAG + " saveCustomizeQuoteDetails :: redirctUrl :" + redirctUrl);
					
					resp.setRedirectUrl(redirctUrl);
					
					/******************************************************PAYMENT GATEWAY***********************************************************/
					
				}
				else
				{
					resp.setStatusKey(ApiConstants.FAILURE);
					resp.setMessageKey(validate.getErrorCode());
				}
				
				return resp;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	

	private AmxApiResponse<?, Object> saveCustomizeQuoteAddPol(CustomizeQuoteModel customizeQuoteModel, MyQuoteModel myQuoteModel)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		if (null != customizeQuoteModel)
		{

			ArrayList<QuoteAddPolicyDetails> quoteAddPolicyDetailsArray = customizeQuoteModel.getQuoteAddPolicyDetails();
			QuotationDetails quotationDetails = customizeQuoteModel.getQuotationDetails();

			for (int i = 0; i < quoteAddPolicyDetailsArray.size(); i++)
			{
				BigDecimal yearMultiplePremium = new BigDecimal(0);
				CustomizeQuoteAddPol customizeQuoteAddPol = new CustomizeQuoteAddPol();
				QuoteAddPolicyDetails quoteAddPolicyDetails = quoteAddPolicyDetailsArray.get(i);

				customizeQuoteAddPol.setQuoteSeqNumber(myQuoteModel.getQuoteSeqNumber());
				customizeQuoteAddPol.setVerNumber(myQuoteModel.getVerNumber());
				customizeQuoteAddPol.setAddPolicyTypeCode(quoteAddPolicyDetails.getAddPolicyTypeCode());
				customizeQuoteAddPol.setYearlyPremium(quoteAddPolicyDetails.getYearlyPremium());
				if (quoteAddPolicyDetails.getAddPolicyTypeEnable())
				{
					yearMultiplePremium = quoteAddPolicyDetails.getYearlyPremium().multiply(quotationDetails.getPolicyDuration());
					customizeQuoteAddPol.setOptIndex("Y");
					customizeQuoteAddPol.setYearMultiplePremium(yearMultiplePremium);
				}
				else
				{
					customizeQuoteAddPol.setOptIndex("N");
					customizeQuoteAddPol.setYearMultiplePremium(yearMultiplePremium);
				}
				customizeQuoteAddPol.setReplacementTypeCode(quoteAddPolicyDetails.getReplacementTypeCode());
				logger.info(TAG + " saveCustomizeQuoteAddPol :: customizeQuoteAddPol :" + customizeQuoteAddPol.toString());

				ResponseInfo validate = customizeQuoteDao.saveCustomizeQuoteAddPol(customizeQuoteAddPol , userSession.getCivilId());
				if (validate.getErrorCode() != null)
				{
					resp.setMessageKey(validate.getErrorCode());
					resp.setStatusKey(ApiConstants.FAILURE);
					return resp;
				}
			}
			resp.setStatusKey(ApiConstants.SUCCESS);
			return resp;
		}
		return null;
	}
}
