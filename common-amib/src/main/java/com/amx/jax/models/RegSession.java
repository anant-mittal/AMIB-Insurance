
package com.amx.jax.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RegSession implements Serializable
{

	private static final long serialVersionUID = 4265567700694960490L;

	private String motpPrefix;

	private String eotpPrefix;

	private String motp;

	private String eotp;

	private String civilId;

	private String mobileNumber;

	private String emailId;

	private String changePasswordOtp;

	private BigDecimal countryId;

	private BigDecimal compCd;

	private String userType;

	private String deviceType;

	private String deviceId;

	private BigDecimal languageId;

	private BigDecimal userSequenceNumber;

	private String contactUsEmail;

	private String contactUsHelpLineNumber;
	
	private ArrayList<OtpCountSession> userSessionOtpCount;

	public ArrayList<OtpCountSession> getUserSessionOtpCount()
	{
		return userSessionOtpCount;
	}

	public void setUserSessionOtpCount(ArrayList<OtpCountSession> userSessionOtpCount)
	{
		this.userSessionOtpCount = userSessionOtpCount;
	}

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

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public String getChangePasswordOtp()
	{
		return changePasswordOtp;
	}

	public void setChangePasswordOtp(String changePasswordOtp)
	{
		this.changePasswordOtp = changePasswordOtp;
	}

	public String getMotpPrefix()
	{
		return motpPrefix;
	}

	public void setMotpPrefix(String motpPrefix)
	{
		this.motpPrefix = motpPrefix;
	}

	public String getEotpPrefix()
	{
		return eotpPrefix;
	}

	public void setEotpPrefix(String eotpPrefix)
	{
		this.eotpPrefix = eotpPrefix;
	}

	public String getMotp()
	{
		return motp;
	}

	public void setMotp(String motp)
	{
		this.motp = motp;
	}

	public String getEotp()
	{
		return eotp;
	}

	public void setEotp(String eotp)
	{
		this.eotp = eotp;
	}

	public String getCivilId()
	{
		return civilId;
	}

	public void setCivilId(String civilId)
	{
		this.civilId = civilId;
	}

	public String getMobileNumber()
	{
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber)
	{
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId()
	{
		return emailId;
	}

	public void setEmailId(String emailId)
	{
		this.emailId = emailId;
	}

	public BigDecimal getCountryId()
	{
		return countryId;
	}

	public void setCountryId(BigDecimal countryId)
	{
		this.countryId = countryId;
	}

	public BigDecimal getCompCd()
	{
		return compCd;
	}

	public void setCompCd(BigDecimal compCd)
	{
		this.compCd = compCd;
	}

	public String getUserType()
	{
		return userType;
	}

	public void setUserType(String userType)
	{
		this.userType = userType;
	}

	public String getDeviceType()
	{
		return deviceType;
	}

	public void setDeviceType(String deviceType)
	{
		this.deviceType = deviceType;
	}

	public String getDeviceId()
	{
		return deviceId;
	}

	public void setDeviceId(String deviceId)
	{
		this.deviceId = deviceId;
	}

	public BigDecimal getLanguageId()
	{
		return languageId;
	}

	public void setLanguageId(BigDecimal languageId)
	{
		this.languageId = languageId;
	}

	public BigDecimal getUserSequenceNumber()
	{
		return userSequenceNumber;
	}

	public void setUserSequenceNumber(BigDecimal userSequenceNumber)
	{
		this.userSequenceNumber = userSequenceNumber;
	}

}
