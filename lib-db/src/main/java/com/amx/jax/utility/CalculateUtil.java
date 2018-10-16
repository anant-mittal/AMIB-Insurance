package com.amx.jax.utility;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amx.jax.models.RegSession;

@Repository
public class CalculateUtil
{
	static String TAG = "com.amx.jax.services :: CalculateUtil :: ";

	private static final Logger logger = LoggerFactory.getLogger(CalculateUtil.class);
	
	public BigDecimal roundBigDecimalValue(BigDecimal inValue , BigDecimal upToDecimal)
	{

		/*BigDecimal d1 = new BigDecimal("10000");
		BigDecimal d2 = new BigDecimal("10000.11111");
		BigDecimal d3 = new BigDecimal("10000.1");
		BigDecimal d4 = new BigDecimal("10000.22");
		
		logger.info(TAG + " isCivilIdExist :: d1 :" + new CalculateUtil().roundBigDecimalValue(d1,regSession.getDecplc()));
		logger.info(TAG + " isCivilIdExist :: d2 :" + new CalculateUtil().roundBigDecimalValue(d2,regSession.getDecplc()));
		logger.info(TAG + " isCivilIdExist :: d3 :" + new CalculateUtil().roundBigDecimalValue(d3,regSession.getDecplc()));
		logger.info(TAG + " isCivilIdExist :: d4 :" + new CalculateUtil().roundBigDecimalValue(d4,regSession.getDecplc()));*/
		
		logger.info(TAG + " roundBigDecimalValue :: upToDecimal :"+upToDecimal);
		logger.info(TAG + " roundBigDecimalValue :: inValue     :"+inValue);
		
		if(null != inValue && null != upToDecimal && upToDecimal.compareTo(BigDecimal.ZERO) > 0)
		{
			int tillDecimalPlace = upToDecimal.intValue();
			BigDecimal out = new BigDecimal(inValue.toString()).setScale(tillDecimalPlace, BigDecimal.ROUND_DOWN);
			return out;
		}
		return inValue;
	}
}
