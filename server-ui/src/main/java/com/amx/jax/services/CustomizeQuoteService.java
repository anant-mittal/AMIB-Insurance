package com.amx.jax.services;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constants.ApiConstants;
import com.amx.jax.dao.CustomizeQuoteDao;
import com.amx.jax.dao.MyQuoteDao;
import com.amx.jax.models.CustomizeQuoteModel;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.MyQuoteModel;
import com.amx.jax.models.QuotationDetails;
import com.amx.jax.models.QuoteAddPolicyDetails;
import com.amx.jax.models.RegSession;
import com.amx.jax.models.TotalPremium;

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
	
	
	public AmxApiResponse<?, Object> getCustomizedQuoteDetails(BigDecimal appSeqNumber)
	{
		AmxApiResponse<CustomizeQuoteModel, Object> resp = new AmxApiResponse<CustomizeQuoteModel, Object>();

		CustomizeQuoteModel customizeQuoteModel = new CustomizeQuoteModel();
		ArrayList<QuoteAddPolicyDetails> quoteAddPolicyDetails = null;
		QuotationDetails quotationDetails = new QuotationDetails();
		TotalPremium totalPremium = new TotalPremium();
		try
		{
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

			customizeQuoteModel.setQuotationDetails(quotationDetails);
			customizeQuoteModel.setQuoteAddPolicyDetails(quoteAddPolicyDetails);
			customizeQuoteModel.setTotalPremium(totalPremium);

			resp.setData(customizeQuoteModel);
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

	public AmxApiResponse<?, Object> getReplacementTypeList(BigDecimal appSeqNumber, String addPolicyTypeCode)
	{
		AmxApiResponse<?, Object> resp = new AmxApiResponse<Object, Object>();
		MyQuoteModel myQuoteModel = new MyQuoteModel();

		try
		{
			ArrayList<MyQuoteModel> getUserQuote = myQuoteDao.getUserQuote();
			for (int i = 0; i < getUserQuote.size(); i++)
			{
				MyQuoteModel myQuoteModelFromDb = getUserQuote.get(i);
				if (null != myQuoteModelFromDb.getAppSeqNumber() && myQuoteModelFromDb.getAppSeqNumber().equals(appSeqNumber))
				{
					myQuoteModel = myQuoteModelFromDb;
				}
			}
			resp.setResults(customizeQuoteDao.getReplacementTypeList(addPolicyTypeCode, DateFormats.setDbSqlFormatDate(myQuoteModel.getQuoteDate())));
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
}
