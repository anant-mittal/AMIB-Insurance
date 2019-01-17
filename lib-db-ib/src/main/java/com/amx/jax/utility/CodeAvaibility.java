package com.amx.jax.utility;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.amx.jax.models.Purpose;

public class CodeAvaibility 
{
	private static final Logger logger = LoggerFactory.getLogger(CodeAvaibility.class);
	
	public static String purposeCodeCheck(String key , List<?> list)
	{
		//logger.info("KeyAvaibility :: purposeKeyAvaibility :: key :" + key);
		
		if(null != key && !key.equals("") && list.size() > 0)
		{
			ArrayList<Purpose> purposeArray = (ArrayList<Purpose>) list;
			
			for(int i = 0 ; i < list.size() ; i++)
			{
				Purpose purpose = purposeArray.get(i);
				
				//logger.info("KeyAvaibility :: purposeKeyAvaibility :: getPurposeCode :" + purpose.getPurposeCode());
				
				if(key.equalsIgnoreCase(purpose.getPurposeCode()))
				{
					//logger.info("KeyAvaibility :: purposeKeyAvaibility :: return PurposeCode :" + purpose.getPurposeCode());
					return purpose.getPurposeCode();
				}
			}
		}
		return null;
	}
}
