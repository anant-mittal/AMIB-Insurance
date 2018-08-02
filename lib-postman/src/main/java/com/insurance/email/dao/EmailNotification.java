




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

	public Email sendEmail(String emailOtpToSend, String mobileOtpToSend , String emailId)
	{
		Email email = new Email();

		try
		{

			logger.info(TAG + " sendEmail :: emailOtpToSend  :" + emailOtpToSend);
			logger.info(TAG + " sendEmail :: mobileOtpToSend :" + mobileOtpToSend);
			
			SimpleMailMessage mail = new SimpleMailMessage();
			mail.setFrom("almulla.insurance.1427@gmail.com");
			mail.setTo(emailId);
			mail.setSubject("Almulla Insurance Registartion Otp");
			mail.setText("Your Email OTP Generted For Registration of Almulla Insurance is : " + emailOtpToSend + "          And Mobile Otp is :"+mobileOtpToSend);

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
