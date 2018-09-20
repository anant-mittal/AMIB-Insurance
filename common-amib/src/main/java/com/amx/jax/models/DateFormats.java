package com.amx.jax.models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateFormats
{
	static String TAG = "com.amx.jax.services :: PersonalDetailsService :: ";

	private static final Logger logger = LoggerFactory.getLogger(DateFormats.class);

	public static String uiFormattedDate(java.sql.Date inDateSqlFormat)
	{
		if (null != inDateSqlFormat && !inDateSqlFormat.toString().equals(""))
		{
			try
			{
				String idExpDateStr = formatType3(inDateSqlFormat.toString());
				return idExpDateStr;
			}
			catch (Exception e)
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
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd-MMM-yy");
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

	public static boolean checkExpiryDate(String idExpiryDate)
	{
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			java.util.Date todays = sdf.parse(DateFormats.formatType1(new java.util.Date().toString()));
			java.util.Date idExpDateFormatted = sdf.parse(DateFormats.formatType1(new java.util.Date(idExpiryDate).toString()));
			if (idExpDateFormatted.before(todays))
			{
				return true;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static java.sql.Date setExpiryDateToDb(String idExpiryDate)
	{
		if (null != idExpiryDate && !idExpiryDate.equals(""))
		{
			try
			{
				String idExpDateStr = formatType4(idExpiryDate);
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				java.util.Date date = format.parse(idExpDateStr);
				return new java.sql.Date(date.getTime());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	public static String convertTimeStampToEpoc(String inDate)
	{
		//System.out.println("convertTimeStampToEpoc :: inDate1 :"+inDate);
		if (null != inDate && !inDate.equals(""))
		{
			//System.out.println("convertTimeStampToEpoc :: inDate2 :"+inDate);
			try
			{
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
				LocalDateTime dt = LocalDateTime.parse(inDate, dtf);
				Instant instant = dt.toInstant(ZoneOffset.UTC);
				long epochLong = instant.toEpochMilli();
				
				//System.out.println("convertTimeStampToEpoc :: epochLong :"+epochLong);
				//System.out.println("convertTimeStampToEpoc :: String.valueOf(epochLong) :"+String.valueOf(epochLong));
				return String.valueOf(epochLong);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
}
