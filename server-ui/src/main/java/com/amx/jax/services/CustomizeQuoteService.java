package com.amx.jax.services;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.dao.CustomizeQuoteDao;
import com.amx.jax.dao.MyQuoteDao;
import com.amx.jax.models.CustomizeQuoteAddPol;
import com.amx.jax.models.CustomizeQuoteInfo;
import com.amx.jax.models.CustomizeQuoteModel;
import com.amx.jax.models.CustomizeQuoteSave;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.MyQuoteModel;
import com.amx.jax.models.QuotationDetails;
import com.amx.jax.models.QuoteAddPolicyDetails;
import com.amx.jax.models.RegSession;
import com.amx.jax.models.TotalPremium;
import com.amx.jax.models.Validate;
import com.amx.jax.utility.CustomizeQuoteUtility;

@Service
public class CustomizeQuoteService
{
	String TAG = "com.amx.jax.services :: CustomizeQuoteService :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomizeQuoteService.class);

	@Autowired
	RegSession regSession;

	@Autowired
	MetaData metaData;

	@Autowired
	private CustomizeQuoteDao customizeQuoteDao;

	@Autowired
	private MyQuoteDao myQuoteDao;

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
			ArrayList<MyQuoteModel> getUserQuote = myQuoteDao.getUserQuote();
			for (int i = 0; i < getUserQuote.size(); i++)
			{
				MyQuoteModel myQuoteModelFromDb = getUserQuote.get(i);

				if (null != quoteSeqNumber && !quoteSeqNumber.toString().equals(""))
				{
					if (null != myQuoteModelFromDb.getQuoteSeqNumber() && myQuoteModelFromDb.getQuoteSeqNumber().equals(quoteSeqNumber))
					{
						myQuoteModel = myQuoteModelFromDb;
						customizeQuoteInfo.setAppSeqNumber(myQuoteModel.getAppSeqNumber());
						customizeQuoteInfo.setQuoteSeqNumber(quoteSeqNumber);
					}
				}
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
			logger.info(TAG + " getCustomizedQuoteDetails :: quotationDetails :" + quotationDetails.toString());

			// SET QuoteAddPolicy Details
			quoteAddPolicyDetails = customizeQuoteDao.getQuoteAdditionalPolicy(myQuoteModel.getQuoteSeqNumber(), myQuoteModel.getVerNumber());
			logger.info(TAG + " getCustomizedQuoteDetails :: quoteAddPolicyDetails :" + quoteAddPolicyDetails.toString());

			// SET TotalPremium Details
			totalPremium.setBasicPremium(myQuoteModel.getBasicPremium());
			totalPremium.setAddCoveragePremium(myQuoteModel.getAddCoveragePremium());
			totalPremium.setIssueFee(myQuoteModel.getIssueFee());
			totalPremium.setSupervisionFees(myQuoteModel.getSupervisionFees());
			totalPremium.setTotalAmount(myQuoteModel.getNetAmount());
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

			resp.setData(customizeQuoteModel);
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

			ArrayList<MyQuoteModel> getUserQuote = myQuoteDao.getUserQuote();
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
			resp.setData(CustomizeQuoteUtility.calculateCustomizeQuote(customizeQuoteModel));
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
			resp.setData(customizeQuoteDao.getTermsAndCondition());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			resp.setException(e.toString());
			resp.setStatusKey(ApiConstants.FAILURE);
		}
		return resp;
	}

	public AmxApiResponse<?, Object> saveCustomizeQuote(CustomizeQuoteModel customizeQuoteModel)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{

			AmxApiResponse<?, Object> respQuoteAddModel = saveCustomizeQuoteAddPol(customizeQuoteModel);
			if (respQuoteAddModel.getStatus().equals(ApiConstants.FAILURE))
			{
				return respQuoteAddModel;
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

	public AmxApiResponse<?, Object> getustomizeQuoteDataToSave(CustomizeQuoteModel customizeQuoteModel)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();
		try
		{
			if (null != customizeQuoteModel)
			{
				CustomizeQuoteInfo customizeQuoteInfo = customizeQuoteModel.getCustomizeQuoteInfo();
				BigDecimal appSeqNumber = customizeQuoteInfo.getAppSeqNumber();
				MyQuoteModel myQuoteModel = new MyQuoteModel();

				ArrayList<MyQuoteModel> getUserQuote = myQuoteDao.getUserQuote();

				for (int i = 0; i < getUserQuote.size(); i++)
				{
					MyQuoteModel myQuoteModelFromDb = getUserQuote.get(i);
					if (null != myQuoteModelFromDb.getAppSeqNumber() && myQuoteModelFromDb.getAppSeqNumber().equals(appSeqNumber))
					{
						myQuoteModel = myQuoteModelFromDb;
					}
				}

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
				Validate validate = customizeQuoteDao.saveCustomizeQuote(customizeQuoteSave);
				if (validate.getErrorCode() == null)
				{
					resp.setStatusKey(ApiConstants.SUCCESS);
				}
				else
				{
					resp.setStatusKey(ApiConstants.FAILURE);
				}
				resp.setMessageKey(validate.getErrorCode());
				return resp;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	private AmxApiResponse<?, Object> saveCustomizeQuoteAddPol(CustomizeQuoteModel customizeQuoteModel)
	{
		AmxApiResponse<Object, Object> resp = new AmxApiResponse<Object, Object>();

		if (null != customizeQuoteModel)
		{
			CustomizeQuoteInfo customizeQuoteInfo = customizeQuoteModel.getCustomizeQuoteInfo();
			BigDecimal appSeqNumber = customizeQuoteInfo.getAppSeqNumber();

			MyQuoteModel myQuoteModel = new MyQuoteModel();
			ArrayList<MyQuoteModel> getUserQuote = myQuoteDao.getUserQuote();
			for (int i = 0; i < getUserQuote.size(); i++)
			{
				MyQuoteModel myQuoteModelFromDb = getUserQuote.get(i);
				if (null != myQuoteModelFromDb.getAppSeqNumber() && myQuoteModelFromDb.getAppSeqNumber().equals(appSeqNumber))
				{
					myQuoteModel = myQuoteModelFromDb;
				}
			}

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

				Validate validate = customizeQuoteDao.saveCustomizeQuoteAddPol(customizeQuoteAddPol);
				logger.info(TAG + " saveCustomizeQuoteAddPol :: getErrorCode() :" + validate.getErrorCode());
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
