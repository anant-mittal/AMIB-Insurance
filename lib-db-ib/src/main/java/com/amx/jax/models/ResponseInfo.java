
package com.amx.jax.models;

import com.amx.jax.swagger.ApiMockModelProperty;

public class ResponseInfo
{
	@ApiMockModelProperty(example = "TECHNICAL ERROR", value = "technical error")
	public String errorCode;

	@ApiMockModelProperty(example = "AMIB customer not created", value = "")
	public String errorMessage;

	@ApiMockModelProperty(example = "amibmotor@almullagroup.com", value = "AMIB email ID")
	private String contactUsEmail;

	@ApiMockModelProperty(example = "22250888", value = "AMIB helpline number")
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
		return "Validate [errorCode=" + errorCode + ", errorMessage=" + errorMessage
				+ ", contactUsEmail=" + contactUsEmail + ", contactUsHelpLineNumber=" + contactUsHelpLineNumber + "]";
	}

	
}
