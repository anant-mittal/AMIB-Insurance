package com.amx.jax.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateFormats
{
	static String TAG = "com.amx.jax.services :: PersonalDetailsService :: ";

	private static final Logger logger = LoggerFactory.getLogger(DateFormats.class);
	
	public static String uiFormattedDate(java.sql.Date inDateSqlFormat)
	{
		if(null != inDateSqlFormat && !inDateSqlFormat.toString().equals(""))
		{
			try
			{
				String idExpDateStr = formatType3(inDateSqlFormat.toString());
				return idExpDateStr;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	public static String formatType1(String inDate)
	{
		String outDate = "";
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy");
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		try
		{
			java.util.Date date = inputDateFormat.parse(inDate);
			outDate = outputDateFormat.format(date);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return outDate;
	}

	public static String formatType2(String inDate)
	{
		String outDate = "";
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		try
		{
			Date date = inputDateFormat.parse(inDate);
			outDate = outputDateFormat.format(date);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return outDate;
	}
	
	public static String formatType3(String inDate)
	{
		String outDate = "";
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		try
		{
			Date date = inputDateFormat.parse(inDate);
			outDate = outputDateFormat.format(date);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return outDate;
	}
	
	
	public static String formatType4(String inDate)
	{
		String outDate = "";
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try
		{
			Date date = inputDateFormat.parse(inDate);
			outDate = outputDateFormat.format(date);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return outDate;
	}
	
	
	public static String formatType5(String inDate)
	{
		String outDate = "";
		SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		
		try
		{
			Date date = inputDateFormat.parse(inDate);
			outDate = outputDateFormat.format(date);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return outDate;
	}
}
