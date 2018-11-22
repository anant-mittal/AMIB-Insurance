package com.amx.jax.utility;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Utility
{
	static String TAG = "com.amx.jax.services :: CalculateUtil :: ";

	private static final Logger logger = LoggerFactory.getLogger(Utility.class);

	public static BigDecimal round(BigDecimal inValue , BigDecimal upToDecimal)
	{
		if (null != inValue && null != upToDecimal && upToDecimal.compareTo(BigDecimal.ZERO) > 0)
		{
			int tillDecimalPlace = upToDecimal.intValue();
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
}
