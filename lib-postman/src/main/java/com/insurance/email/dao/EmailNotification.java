
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

	public Email sendEmail(String emailFrom, String emailTo, String Subject, String emailData)
	{
		Email email = new Email();

		try
		{

			Thread thread = new Thread()
			{
				public void run()
				{
					SimpleMailMessage mail = new SimpleMailMessage();
					mail.setFrom(emailFrom);
					mail.setTo(emailTo);
					mail.setSubject(Subject);
					mail.setText(emailData);

					logger.info(TAG + " sendEmail :: Sending....");
					javaMailSender.send(mail);
					logger.info(TAG + " sendEmail :: Done!!!!");
				}
			};
			thread.start();
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
