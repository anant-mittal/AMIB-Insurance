package com.amx.jax.utility;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amx.jax.models.CustomizeQuoteModel;
import com.amx.jax.models.QuotationDetails;
import com.amx.jax.models.QuoteAddPolicyDetails;
import com.amx.jax.models.TotalPremium;

public class CalculateUtility
{
	static String TAG = "com.amx.jax.utility :: CalculateUtility :: ";

	private static final Logger logger = LoggerFactory.getLogger(CalculateUtility.class);

	public static CustomizeQuoteModel calculateCustomizeQuote(CustomizeQuoteModel customizeQuoteModel)
	{
		BigDecimal policyDuration = new BigDecimal(0);
		BigDecimal yearlyAddCovPremium = new BigDecimal(0);
		BigDecimal addCovPremium = new BigDecimal(0);
		BigDecimal totalPremiumAmount = new BigDecimal(0);

		if (null != customizeQuoteModel)
		{
			QuotationDetails quotationDetails = customizeQuoteModel.getQuotationDetails();
			ArrayList<QuoteAddPolicyDetails> quoteAddPolicyDetailsArray = customizeQuoteModel.getQuoteAddPolicyDetails();
			TotalPremium totalPremium = customizeQuoteModel.getTotalPremium();

			
			
			
			
			/* Get Policy Duration First */
			policyDuration = quotationDetails.getPolicyDuration();
			logger.info(TAG + " calculateCustomizeQuote :: policyDuration :" + policyDuration);

			for (int i = 0; i < quoteAddPolicyDetailsArray.size(); i++)
			{
				QuoteAddPolicyDetails quoteAddPolicyDetails = quoteAddPolicyDetailsArray.get(i);
				if (quoteAddPolicyDetails.getAddPolicyTypeEnable())
				{
					BigDecimal yearlyPremiumAmount = quoteAddPolicyDetails.getYearlyPremium();
					logger.info(TAG + " calculateCustomizeQuote :: yearlyPremiumAmount :" + yearlyPremiumAmount);
					
					if(null != yearlyPremiumAmount)
					{
						yearlyAddCovPremium = yearlyAddCovPremium.add(yearlyPremiumAmount);
						logger.info(TAG + " calculateCustomizeQuote :: yearlyAddCovPremium :" + yearlyAddCovPremium);
					}
				}
			}

			
			
			
			
			
			
			
			
			/*
			 * Additional Coverage Premium Multiplie with Number Of Years Of
			 * Policy Duration
			 */

			addCovPremium = yearlyAddCovPremium.multiply(policyDuration);
			logger.info(TAG + " calculateCustomizeQuote :: addCovPremium :" + addCovPremium);
			totalPremium.setAddCoveragePremium(addCovPremium);
			logger.info(TAG + " calculateCustomizeQuote :: totalPremium :" + totalPremium);

			
			
			
			
			
			
			
			
			/* Update Total Premium Block */
			totalPremiumAmount = totalPremiumAmount.add(Utility.getNumericValue(totalPremium.getBasicPremium()));
			logger.info(TAG + " calculateCustomizeQuote :: totalPremiumAmount1 :" + totalPremiumAmount);
		
			totalPremiumAmount = totalPremiumAmount.add(Utility.getNumericValue(addCovPremium));
			logger.info(TAG + " calculateCustomizeQuote :: totalPremiumAmount2 :" + totalPremiumAmount);
		
			totalPremiumAmount = totalPremiumAmount.add(Utility.getNumericValue(totalPremium.getIssueFee()));
			logger.info(TAG + " calculateCustomizeQuote :: totalPremiumAmount3 :" + totalPremiumAmount);
		
			totalPremiumAmount = totalPremiumAmount.add(Utility.getNumericValue(totalPremium.getSupervisionFees()));
			logger.info(TAG + " calculateCustomizeQuote :: totalPremiumAmount4 :" + totalPremiumAmount);
			
			totalPremium.setTotalAmount(totalPremiumAmount);

			
			
			
			
			
			
			/* Set Details Back To Front End */
			customizeQuoteModel.setQuotationDetails(quotationDetails);
			logger.info(TAG + " calculateCustomizeQuote :: quotationDetails :" + quotationDetails);
			customizeQuoteModel.setQuoteAddPolicyDetails(quoteAddPolicyDetailsArray);
			logger.info(TAG + " calculateCustomizeQuote :: quoteAddPolicyDetailsArray :" + quoteAddPolicyDetailsArray);
			customizeQuoteModel.setTotalPremium(totalPremium);
			logger.info(TAG + " calculateCustomizeQuote :: totalPremium :" + totalPremium);
		}
		return customizeQuoteModel;
	}
}
