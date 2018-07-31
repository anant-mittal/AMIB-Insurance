




package com.insurance.email.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Repository;
import com.insurance.email.model.Email;
import com.insurance.generateotp.RequestOtpModel;
import com.insurance.generateotp.ResponseOtpModel;

@Repository
public class EmailNotification
{
	String TAG = "com.insurance.email.dao :: EmailNotification :: ";

	private static final Logger logger = LoggerFactory.getLogger(EmailNotification.class);

	@Autowired
	JavaMailSender javaMailSender;

	
	private String username;

	public Email sendEmail(RequestOtpModel requestOtpModel, ResponseOtpModel responseOtpModel)
	{
		Email email = new Email();

		try
		{

			String emailId = requestOtpModel.getEmailId();
			String actualOtp = responseOtpModel.getOtpPrefix() + "-" + responseOtpModel.getOtp();

			logger.info(TAG + " sendEmail :: emailId :" + emailId);
			logger.info(TAG + " sendEmail :: actualOtp :" + actualOtp);
			logger.info(TAG + " sendEmail :: configureOuremail :" + this.username);

			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setFrom("almulla.insurance.1427@gmail.com");
			mail.setTo(emailId);
			mail.setSubject("Almulla Insurance Registartion Otp");
			mail.setText("Your OTP Generted For Registration of Almulla Insurance is : " + actualOtp);

			logger.info(TAG + " sendEmail :: Sending....");

			javaMailSender.send(mail);

			logger.info(TAG + " sendEmail :: Done!!!!");

			email.setEmailSentStatus(true);
		}
		catch (Exception e)
		{
			email.setEmailSentStatus(false);
			email.setEmailSendingException(e.toString());
			e.printStackTrace();
		}
		return email;
	}
}
