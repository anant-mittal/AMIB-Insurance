
package com.insurance.email.model;

public class Email
{
	String emailIdFrom = "";

	String emailIdTo = "";

	String subject = "";

	String message = "";

	boolean emailSentStatus = false;

	String emailSendingException = "";

	public String getEmailIdFrom()
	{
		return emailIdFrom;
	}

	public void setEmailIdFrom(String emailIdFrom)
	{
		this.emailIdFrom = emailIdFrom;
	}

	public String getEmailIdTo()
	{
		return emailIdTo;
	}

	public void setEmailIdTo(String emailIdTo)
	{
		this.emailIdTo = emailIdTo;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public boolean getEmailSentStatus()
	{
		return emailSentStatus;
	}

	public void setEmailSentStatus(boolean emailSentStatus)
	{
		this.emailSentStatus = emailSentStatus;
	}

	public String getEmailSendingException()
	{
		return emailSendingException;
	}

	public void setEmailSendingException(String emailSendingException)
	{
		this.emailSendingException = emailSendingException;
	}

}
