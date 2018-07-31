




package com.insurance.generateotp;

import java.security.MessageDigest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class CryptoUtil
{

	private Logger log = Logger.getLogger(CryptoUtil.class);

	public static String getHash(String userid, String key)
	{

		byte[] output = null;
		String salt = null;
		String newpassoword = null;

		try
		{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			salt = new StringBuffer(userid).reverse().toString();
			newpassoword = null;
			salt = salt.substring(0, 4);
			newpassoword = addSalt(key, salt);
			md.update(newpassoword.getBytes());
			output = md.digest();
			newpassoword = bytesToHex(output);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return newpassoword;
	}

	public static String addSalt(String password, String salt)
	{
		final int aLength = password.length();
		final int bLength = salt.length();
		final StringBuilder sb = new StringBuilder(aLength + bLength);
		try
		{
			final int min = Math.min(aLength, bLength);
			for (int i = 0; i < min; i++)
			{
				sb.append(password.charAt(i));
				sb.append(salt.charAt(i));
			}
			if (aLength > bLength)
			{
				sb.append(password, bLength, aLength);
			}
			else if (aLength < bLength)
			{
				sb.append(salt, aLength, bLength);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new NullPointerException("Invalid values : ");
		}
		return sb.toString();
	}

	public static String bytesToHex(byte[] b)
	{
		char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < b.length; j++)
		{
			buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
			buf.append(hexDigit[b[j] & 0x0f]);
		}
		return new String(buf.toString());
	}

	public static void main(String[] args)
	{
		CryptoUtil util = new CryptoUtil();
		System.out.println(util.getHash("293111302791", "test"));
	}
}
