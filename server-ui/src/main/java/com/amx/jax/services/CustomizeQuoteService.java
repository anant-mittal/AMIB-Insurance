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

import com.amx.jax.WebAppStatus.WebAppStatusCodes;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.constants.Message;
import com.amx.jax.dao.CustomizeQuoteDao;
import com.amx.jax.dao.MyQuoteDao;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.CustomizeQuoteAddPol;
import com.amx.jax.models.CustomizeQuoteInfo;
import com.amx.jax.models.CustomizeQuoteModel;
import com.amx.jax.models.CustomizeQuoteSave;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.MyQuoteModel;
import com.amx.jax.models.PaymentDetails;
import com.amx.jax.models.QuotationDetails;
import com.amx.jax.models.QuoteAddPolicyDetails;
import com.amx.jax.models.ResponseInfo;
import com.amx.jax.models.TotalPremium;
import com.amx.jax.payg.PayGParams;
import com.amx.jax.payg.PayGService;
import com.amx.jax.payg.Payment;
import com.amx.jax.paymentlink.dao.PaymentLinkDao;
import com.amx.jax.ui.session.UserSession;
import com.amx.jax.utility.CalculateUtility;
import com.amx.jax.utility.Utility;
import com.amx.utils.HttpUtils;

@Service
public class CustomizeQuoteService
{
	private static final Logger logger = LoggerFactory.getLogger(CustomizeQuoteService.class);

	String TAG = "CustomizeQuoteService :: ";
	
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
	
	@Autowired
	PaymentLinkDao paymentLinkDao;
	
	public AmxApiResponse<CustomizeQuoteModel, Object> getCustomizedQuoteDetails(BigDecimal quoteSeqNumber)
	{
		boolean quoteAvailableToCustomer = false;
		BigDecimal custId = userSession.getCustomerSequenceNumber();
		
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
			ArrayResponseModel arrayResponseUserQuote = myQuoteDao.getUserQuote(userSession.getCustomerSequenceNumber(), userSession.getLanguageId());
			if(arrayResponseUserQuote.getErrorCode() == null)
			{
				ArrayList<MyQuoteModel> getUserQuote = arrayResponseUserQuote.getDataArray();
				for (int i = 0; i < getUserQuote.size(); i++)
				{
					MyQuoteModel myQuoteModelFromDb = getUserQuote.get(i);
					if (null != quoteSeqNumber && !quoteSeqNumber.toString().equals("") && !myQuoteModelFromDb.getPaymentProcessError().equalsIgnoreCase("Y"))
					{
						logger.info("Quote seq no "+myQuoteModelFromDb.getQuoteSeqNumber());
						if (null != myQuoteModelFromDb.getQuoteSeqNumber() && myQuoteModelFromDb.getQuoteSeqNumber().equals(quoteSeqNumber))
						{
							myQuoteModel = myQuoteModelFromDb;
							customizeQuoteInfo.setQuoteSeqNumber(quoteSeqNumber);
							quoteAvailableToCustomer = true;
							logger.info("flag is true");
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
			
			
			if(!quoteAvailableToCustomer)
			{
				resp.setMessage(Message.NO_QUOTE_AVAILABLE);
				resp.setStatusKey(WebAppStatusCodes.NO_QUOTE_AVAILABLE.toString());
				resp.setMessageKey(WebAppStatusCodes.NO_QUOTE_AVAILABLE.toString());
				return resp;
			}
			
			
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
			
			
			// SET QuoteAddPolicy Details
			ArrayResponseModel arrayResponseModel = customizeQuoteDao.getQuoteAdditionalPolicy(myQuoteModel.getQuoteSeqNumber(), myQuoteModel.getVerNumber(), userSession.getLanguageId());
			if(arrayResponseModel.getErrorCode() == null)
			{
				quoteAddPolicyDetails = arrayResponseModel.getDataArray();
			}
			else
			{
				resp.setMessage(arrayResponseModel.getErrorMessage());
				resp.setMessageKey(arrayResponseModel.getErrorCode());
				return resp;
			}
			
			
			
			
			// SET TotalPremium Details
			totalPremium.setBasicPremium(Utility.getNumericValue(myQuoteModel.getBasicPremium()));
			totalPremium.setAddCoveragePremium(Utility.getNumericValue(myQuoteModel.getAddCoveragePremium()));
			totalPremium.setIssueFee(Utility.getNumericValue(myQuoteModel.getIssueFee()));
			totalPremium.setSupervisionFees(Utility.getNumericValue(myQuoteModel.getSupervisionFees()));
			totalPremium.setTotalAmount(Utility.getNumericValue(myQuoteModel.getNetAmount()));

			
			
			// SET Replacement Type List Meta
			for (int i = 0; i < quoteAddPolicyDetails.size(); i++)
			{
				String polType = quoteAddPolicyDetails.get(i).getAddPolicyTypeCode();
				Date quoteDate = DateFormats.setDbSqlFormatDate(myQuoteModel.getQuoteDate());
				
				ArrayResponseModel arrayResponseModelRep = customizeQuoteDao.getReplacementTypeList(polType, quoteDate, userSession.getLanguageId());
				if(null != arrayResponseModelRep)
				{
					if(null == arrayResponseModelRep.getErrorCode())
					{
						ArrayList repTypeArray = arrayResponseModelRep.getDataArray();
						if (null != repTypeArray)
						{
							repTypeMap.put(polType, repTypeArray);
						}
					}
					else
					{
						resp.setMessage(arrayResponseModelRep.getErrorMessage());
						resp.setMessageKey(arrayResponseModelRep.getErrorCode());
						return resp;
					}
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
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);

		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG+"getCustomizedQuoteDetails :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getQuoteSeqList()
	{
		AmxApiResponse<String, Object> resp = new AmxApiResponse<String, Object>();
		try
		{
			ArrayList<String> allQuotes = new ArrayList<String>();
			
			ArrayResponseModel arrayResponseUserQuote = myQuoteDao.getUserQuote(userSession.getCustomerSequenceNumber(), userSession.getLanguageId());
			if(arrayResponseUserQuote.getErrorCode() == null)
			{
				ArrayList<MyQuoteModel> getUserQuote = arrayResponseUserQuote.getDataArray();
				for (int i = 0; i < getUserQuote.size(); i++)
				{
					MyQuoteModel myQuoteModelFromDb = getUserQuote.get(i);
					if (null != myQuoteModelFromDb.getQuoteSeqNumber() && null != myQuoteModelFromDb.getPaymentProcessError() && !myQuoteModelFromDb.getPaymentProcessError().equalsIgnoreCase("Y"))
					{
						allQuotes.add(myQuoteModelFromDb.getQuoteSeqNumber().toString());
					}
				}
				resp.setMeta(allQuotes);
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			}
			else
			{
				resp.setMessageKey(arrayResponseUserQuote.getErrorCode());
				resp.setMessage(arrayResponseUserQuote.getErrorMessage());
			}
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG+"getQuoteSeqList :: exception :" + e);
			e.printStackTrace();
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
			ArrayResponseModel arrayResponseUserQuote = myQuoteDao.getUserQuote(userSession.getCustomerSequenceNumber(), userSession.getLanguageId());
			if(arrayResponseUserQuote.getErrorCode() == null)
			{
				ArrayList<MyQuoteModel> getUserQuote = arrayResponseUserQuote.getDataArray();
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
			}
			else
			{
				resp.setMessageKey(arrayResponseUserQuote.getErrorCode());
				resp.setMessage(arrayResponseUserQuote.getErrorMessage());
				return resp;
			}
			
			
			quoteAddPolicyDetails = customizeQuoteCalculated.getQuoteAddPolicyDetails();
			for (int i = 0; i < quoteAddPolicyDetails.size(); i++)
			{
				String polType = quoteAddPolicyDetails.get(i).getAddPolicyTypeCode();
				Date quoteDate = DateFormats.setDbSqlFormatDate(myQuoteModel.getQuoteDate());
				
				ArrayResponseModel arrayResponseModelRep = customizeQuoteDao.getReplacementTypeList(polType, quoteDate, userSession.getLanguageId());
				if(null != arrayResponseModelRep)
				{
					if(null == arrayResponseModelRep.getErrorCode())
					{
						ArrayList repTypeArray = arrayResponseModelRep.getDataArray();
						if (null != repTypeArray)
						{
							repTypeMap.put(polType, repTypeArray);
						}
					}
					else
					{
						resp.setMessage(arrayResponseModelRep.getErrorMessage());
						resp.setMessageKey(arrayResponseModelRep.getErrorCode());
						return resp;
					}
				}
				
			}
			
			resp.setData(CalculateUtility.calculateCustomizeQuote(customizeQuoteModel));
			resp.setStatusEnum(WebAppStatusCodes.SUCCESS);

		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG+"calculateCutomizeQuote :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}

	public AmxApiResponse<?, Object> getTermsAndCondition()
	{
		AmxApiResponse<String, Object> resp = new AmxApiResponse<String, Object>();
		try
		{
			ArrayResponseModel arrayResponseModelRep = customizeQuoteDao.getTermsAndCondition(userSession.getLanguageId());
			if(arrayResponseModelRep.getErrorCode() == null)
			{
				resp.setResults(arrayResponseModelRep.getDataArray());
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			}
			else
			{
				resp.setMessage(arrayResponseModelRep.getErrorMessage());
				resp.setMessageKey(arrayResponseModelRep.getErrorCode());
				return resp;
			}
			
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG+"getTermsAndCondition :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}
	
	
	public TreeMap<Integer,String> getTermsAndConditionTest()
	{
		return customizeQuoteDao.getTermsAndConditionTest(userSession.getLanguageId());
	}

	public AmxApiResponse<?, Object> saveCustomizeQuote(CustomizeQuoteModel customizeQuoteModel , HttpServletRequest request)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			CustomizeQuoteInfo customizeQuoteInfo = customizeQuoteModel.getCustomizeQuoteInfo();
			BigDecimal quoteSeqNumber = customizeQuoteInfo.getQuoteSeqNumber();

			MyQuoteModel myQuoteModel = new MyQuoteModel();
			ArrayResponseModel arrayResponseUserQuote = myQuoteDao.getUserQuote(userSession.getCustomerSequenceNumber(), userSession.getLanguageId());
			if(arrayResponseUserQuote.getErrorCode() == null)
			{
				ArrayList<MyQuoteModel> getUserQuote = arrayResponseUserQuote.getDataArray();
				for (int i = 0; i < getUserQuote.size(); i++)
				{
					MyQuoteModel myQuoteModelFromDb = getUserQuote.get(i);
					if (null != myQuoteModelFromDb.getQuoteSeqNumber() && myQuoteModelFromDb.getQuoteSeqNumber().equals(quoteSeqNumber))
					{
						myQuoteModel = myQuoteModelFromDb;
					}
				}
			}
			else
			{
				resp.setMessageKey(arrayResponseUserQuote.getErrorCode());
				resp.setMessage(arrayResponseUserQuote.getErrorMessage());
				return resp;
			}
			
			AmxApiResponse<?, Object> respQuoteAddModel = saveCustomizeQuoteAddPol(customizeQuoteModel, myQuoteModel);
			if (!respQuoteAddModel.getStatusKey().equals(ApiConstants.SUCCESS))
			{
				return respQuoteAddModel;
			}
			
			return saveCustomizeQuoteDetails(customizeQuoteModel, myQuoteModel ,customizeQuoteInfo ,request);
		
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG+"saveCustomizeQuote :: exception :" + e);
			e.printStackTrace();
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
				
				ResponseInfo validate = customizeQuoteDao.saveCustomizeQuote(customizeQuoteSave , userSession.getCivilId());
				logger.info("saveCustomizeQuoteDetails :: getErrorCode :" + validate.getErrorCode());
				if (validate.getErrorCode() == null)
				{
					resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
					
					/******************************************************PAYMENT GATEWAY***********************************************************/
					
					logger.info("saveCustomizeQuoteDetails :: getQuoteSeqNumber :" + customizeQuoteInfo.getQuoteSeqNumber());
					
					AmxApiResponse<PaymentDetails, Object> respInsertPayment = payMentService.insertPaymentDetals(customizeQuoteInfo.getQuoteSeqNumber(),totalPremium.getTotalAmount());
					PaymentDetails paymentDetails = respInsertPayment.getData();
					logger.info("saveCustomizeQuoteDetails :: getPaySeqNum :" + paymentDetails.getPaySeqNum());
					
					/*Payment payment = new Payment();
					payment.setDocFinYear(userSession.getCivilId().toString());//Civil Id Added
					payment.setDocNo(paymentDetails.getPaySeqNum().toString());// PaySeqNum
					payment.setMerchantTrackId(paymentDetails.getPaySeqNum().toString());// PaySeqNum
					payment.setNetPayableAmount(totalPremium.getTotalAmount());
					payment.setPgCode(PayGServiceCode.KNET);
					String redirctUrl = payGService.getPaymentUrl(payment , HttpUtils.getServerName(request)+"/app/landing/myquotes/quote");
					logger.info("saveCustomizeQuoteDetails :: redirctUrl :" + redirctUrl);*/
					
					
					PayGParams payment = new PayGParams();
					payment.setDocFy(userSession.getCivilId());//Civil Id Added
					payment.setDocNo(paymentDetails.getPaySeqNum().toString());// PaySeqNum
					payment.setTrackId(paymentDetails.getPaySeqNum().toString());// PaySeqNum
					payment.setAmount(totalPremium.getTotalAmount().toString());
					//payment.setAmountObject(totalPremium.getTotalAmount());
					payment.setProduct("QUOTE");//Language Selected
					payment.setServiceCode(PayGServiceCode.KNET);
					
					String redirctUrl = payGService.getPaymentUrl(payment , HttpUtils.getServerName(request)+"/app/landing/myquotes/quote");
					logger.info("saveCustomizeQuoteDetails :: redirctUrl :" + redirctUrl);
					
					resp.setRedirectUrl(redirctUrl);
					
					/******************************************************PAYMENT GATEWAY***********************************************************/
					
				}
				else
				{
					resp.setStatusKey(validate.getErrorCode());
					resp.setMessageKey(validate.getErrorCode());
				}
			}
		}
		catch (Exception e)
		{
			resp.setMessageKey(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG+"saveCustomizeQuoteDetails :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}
	

	private AmxApiResponse<?, Object> saveCustomizeQuoteAddPol(CustomizeQuoteModel customizeQuoteModel, MyQuoteModel myQuoteModel)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		try 
		{
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
					
					ResponseInfo validate = customizeQuoteDao.saveCustomizeQuoteAddPol(customizeQuoteAddPol , userSession.getCivilId());
					if (validate.getErrorCode() != null)
					{
						resp.setMessageKey(validate.getErrorCode());
						resp.setStatusKey(validate.getErrorCode());
						return resp;
					}
				}
				resp.setStatusEnum(WebAppStatusCodes.SUCCESS);
			}
		}
		catch (Exception e) 
		{
			resp.setMessageKey(ApiConstants.TECHNICAL_ERROR_ON_SERVER);
			resp.setMessage(e.toString());
			logger.info(TAG + "getCompanySetUp :: exception :" + e);
			e.printStackTrace();
		}
		return resp;
	}
	
	public CustomizeQuoteModel validatePaymentLink(BigDecimal linkId , String verifyCode, BigDecimal languageId){
		CustomizeQuoteModel customizeQuoteModel = paymentLinkDao.validatePaymentLink(linkId, verifyCode,languageId);
		return customizeQuoteModel;
		
	}
}
