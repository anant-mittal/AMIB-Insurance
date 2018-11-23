package com.amx.jax.utility;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.models.MetaData;

@Component
public class Utility
{
	static String TAG = "com.amx.jax.services :: CalculateUtil :: ";

	private static final Logger logger = LoggerFactory.getLogger(Utility.class);
	
	@Autowired
	static
	MetaData metaData;

	public static BigDecimal round(BigDecimal inValue)
	{
		if (null != inValue && null != metaData.getDecplc() && metaData.getDecplc().compareTo(BigDecimal.ZERO) > 0)
		{
			int tillDecimalPlace = metaData.getDecplc().intValue();
			BigDecimal out = new BigDecimal(inValue.toString()).setScale(tillDecimalPlace, BigDecimal.ROUND_DOWN);
			return out;
		}
		return inValue;
	}
	
	public static BigDecimal getNumericValue(BigDecimal value)
	{
		if(null != value && !value.toString().equals(""))
		{
			return value;
		}
		return new BigDecimal(0);
	}
	
	
	public static String getAmountInCurrency(BigDecimal amount)
	{
		BigDecimal amountValue = getNumericValue(amount);
		BigDecimal amountValueWithDecimal = Utility.round(amountValue);
		String amountValueWithCurrency = metaData.getCurrency()+" "+amountValueWithDecimal.toString();
		return amountValueWithCurrency;
	}
}
