




package com.insurance.generateotp;

import java.security.MessageDigest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CreateOtpToken
{
	String TAG = "com.insurance.generateotp :: CreateOtpToken :: ";

	private static final Logger logger = LoggerFactory.getLogger(CreateOtpToken.class);

	public ResponseOtpModel generateToken(String civilId)
	{
		ResponseOtpModel responseOtpModel = new ResponseOtpModel();

		String randOtp = createRandomPassword(6);
		String hashedOtp = getHash(civilId, randOtp);
		responseOtpModel.setCivilId(civilId);
		responseOtpModel.setEotpPrefix(Random.randomAlpha(3));

		return responseOtpModel;
	}

	public String createRandomPassword(int length)
	{
		String validChars = "1234567890";
		String password = "";
		for (int i = 0; i < length; i++)
		{
			password = password + String.valueOf(validChars.charAt((int) (Math.random() * validChars.length())));
		}
		return password;
	}

	public String getHash(String userid, String key)
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
			newpassoword = CryptoUtil.addSalt(key, salt);
			md.update(newpassoword.getBytes());
			output = md.digest();
			newpassoword = CryptoUtil.bytesToHex(output);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return newpassoword;
	}

}
