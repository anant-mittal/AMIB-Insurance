package com.amx.jax.utility;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.amx.jax.models.MetaData;
import com.amx.jax.models.RegSession;
import com.amx.jax.rest.RestService;

@Component
public class CalculateUtil
{
	@Autowired
	static
	MetaData metaData;

	@Autowired
	static
	RegSession regSession;

	static String TAG = "com.amx.jax.services :: CalculateUtil :: ";

	private static final Logger logger = LoggerFactory.getLogger(CalculateUtil.class);

	public static BigDecimal roundBigDecimalValue(BigDecimal inValue)
	{
		logger.info(TAG + " roundBigDecimalValue :: upToDecimal 1  :" + metaData.getDecplc());
		logger.info(TAG + " roundBigDecimalValue :: upToDecimal 2  :" + regSession.getDecplc());

		BigDecimal upToDecimal = metaData.getDecplc();
		BigDecimal upToDecima2 = regSession.getDecplc();

		if (null != inValue && null != upToDecimal && upToDecimal.compareTo(BigDecimal.ZERO) > 0)
		{
			int tillDecimalPlace = upToDecimal.intValue();
			BigDecimal out = new BigDecimal(inValue.toString()).setScale(tillDecimalPlace, BigDecimal.ROUND_DOWN);
			return out;
		}
		return inValue;
	}
}
