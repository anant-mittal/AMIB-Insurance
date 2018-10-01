package com.amx.jax.utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amx.jax.models.CustomizeQuoteModel;
import com.amx.jax.models.QuotationDetails;
import com.amx.jax.models.QuoteAddPolicyDetails;
import com.amx.jax.models.TotalPremium;

public class CustomizeQuoteUtility
{
	static String TAG = "com.amx.jax.utility :: CustomizeQuoteUtility :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomizeQuoteUtility.class);

	public static CustomizeQuoteModel calculateCustomizeQuote(CustomizeQuoteModel customizeQuoteModel)
	{
		BigDecimal policyDuration;
		BigDecimal yearlyAddCovPremium = null;
		BigDecimal addCovPremium = null;
		BigDecimal totalPremiumAmount = null;

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
					yearlyAddCovPremium = yearlyAddCovPremium.add(yearlyPremiumAmount);
					logger.info(TAG + " calculateCustomizeQuote :: yearlyAddCovPremium :" + yearlyAddCovPremium);
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
			totalPremiumAmount = totalPremiumAmount.add(totalPremium.getBasicPremium());
			logger.info(TAG + " calculateCustomizeQuote :: totalPremiumAmount1 :" + totalPremiumAmount);
			totalPremiumAmount = totalPremiumAmount.add(addCovPremium);
			logger.info(TAG + " calculateCustomizeQuote :: totalPremiumAmount2 :" + totalPremiumAmount);
			totalPremiumAmount = totalPremiumAmount.add(totalPremium.getIssueFee());
			logger.info(TAG + " calculateCustomizeQuote :: totalPremiumAmount3 :" + totalPremiumAmount);
			totalPremiumAmount = totalPremiumAmount.add(totalPremium.getSupervisionFees());
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
