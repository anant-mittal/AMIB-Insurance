
package com.amx.jax.models;

public class Validate
{
	public boolean valid;

	public String errorCode;

	public String errorMessage;

	private String contactUsEmail;

	private String contactUsHelpLineNumber;

	public String getContactUsEmail()
	{
		return contactUsEmail;
	}

	public void setContactUsEmail(String contactUsEmail)
	{
		this.contactUsEmail = contactUsEmail;
	}

	public String getContactUsHelpLineNumber()
	{
		return contactUsHelpLineNumber;
	}

	public void setContactUsHelpLineNumber(String contactUsHelpLineNumber)
	{
		this.contactUsHelpLineNumber = contactUsHelpLineNumber;
	}

	public boolean isValid()
	{
		return valid;
	}

	public void setValid(boolean valid)
	{
		this.valid = valid;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "Validate [valid=" + valid + ", errorCode=" + errorCode + ", errorMessage=" + errorMessage
				+ ", contactUsEmail=" + contactUsEmail + ", contactUsHelpLineNumber=" + contactUsHelpLineNumber + "]";
	}

	
}
