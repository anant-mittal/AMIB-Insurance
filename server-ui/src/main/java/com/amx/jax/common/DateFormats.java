package com.amx.jax.common;

import java.text.SimpleDateFormat;

public class DateFormats
{
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
			java.util.Date date = inputDateFormat.parse(inDate);
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
	
}
