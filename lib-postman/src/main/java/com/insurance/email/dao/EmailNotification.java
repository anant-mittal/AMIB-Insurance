




package com.insurance.email.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Repository;
import com.insurance.email.model.Email;

@Repository
public class EmailNotification
{
	String TAG = "com.insurance.email.dao :: EmailNotification :: ";

	private static final Logger logger = LoggerFactory.getLogger(EmailNotification.class);
	
	@Autowired
	JavaMailSender javaMailSender;

	public void sendEmail(Email email)
	{
		logger.info(TAG + " sendEmail :: email :"+email);

		SimpleMailMessage mail = new SimpleMailMessage();

		mail.setFrom(email.getEmailIdFrom());
		mail.setTo(email.getEmailIdTo());
		mail.setSubject(email.getSubject());
		mail.setText(email.getMessage());

		logger.info(TAG + " sendEmail :: Sending....");

		javaMailSender.send(mail);

		logger.info(TAG + " sendEmail :: Done!!!!");
	
	}

}
