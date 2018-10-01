package com.amx.jax.utility;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.amx.jax.models.CustomizeQuoteModel;
import com.amx.jax.models.QuotationDetails;
import com.amx.jax.models.QuoteAddPolicyDetails;
import com.amx.jax.models.TotalPremium;

public class CustomizeQuoteUtility
{
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

			for (int i = 0; i < quoteAddPolicyDetailsArray.size(); i++)
			{
				QuoteAddPolicyDetails quoteAddPolicyDetails = quoteAddPolicyDetailsArray.get(i);
				if (quoteAddPolicyDetails.getAddPolicyTypeEnable())
				{
					BigDecimal yearlyPremiumAmount = quoteAddPolicyDetails.getYearlyPremium();
					yearlyAddCovPremium = yearlyAddCovPremium.add(yearlyPremiumAmount);
				}
			}

			/*
			 * Additional Coverage Premium Multiplie with Number Of Years Of
			 * Policy Duration
			 */

			addCovPremium = yearlyAddCovPremium.multiply(policyDuration);
			totalPremium.setAddCoveragePremium(addCovPremium);

			/* Update Total Premium Block */
			totalPremiumAmount = totalPremiumAmount.add(totalPremium.getBasicPremium());
			totalPremiumAmount = totalPremiumAmount.add(addCovPremium);
			totalPremiumAmount = totalPremiumAmount.add(totalPremium.getIssueFee());
			totalPremiumAmount = totalPremiumAmount.add(totalPremium.getSupervisionFees());
			totalPremium.setTotalAmount(totalPremiumAmount);

			/* Set Details Back To Front End */
			customizeQuoteModel.setQuotationDetails(quotationDetails);
			customizeQuoteModel.setQuoteAddPolicyDetails(quoteAddPolicyDetailsArray);
			customizeQuoteModel.setTotalPremium(totalPremium);

		}
		return customizeQuoteModel;
	}
}
