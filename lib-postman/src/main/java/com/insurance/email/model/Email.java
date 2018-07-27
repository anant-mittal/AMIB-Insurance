package com.insurance.email.model;

public class Email
{
	String emailIdFrom = "";
	String emailIdTo = "";
	String subject = "";
	String message = "";
	
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
	
}
