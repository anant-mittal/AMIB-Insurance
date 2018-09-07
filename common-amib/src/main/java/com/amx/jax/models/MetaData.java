
package com.amx.jax.models;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MetaData implements Serializable
{

	private static final long serialVersionUID = 4265567700694960490L;

	@Override
	public String toString()
	{
		return "MetaData [metaData=" + metaData + ", motpPrefix=" + motpPrefix + ", eotpPrefix=" + eotpPrefix + ", motp=" + motp + ", eotp=" + eotp + ", civilId=" + civilId + ", mobileNumber=" + mobileNumber + ", emailId=" + emailId + ", changePasswordOtp=" + changePasswordOtp + ", countryId="
				+ countryId + ", compCd=" + compCd + ", userType=" + userType + ", deviceType=" + deviceType + ", deviceId=" + deviceId + ", languageId=" + languageId + ", contactUsEmail=" + contactUsEmail + ", contactUsHelpLineNumber=" + contactUsHelpLineNumber + ", mOtpMobileNumber="
				+ mOtpMobileNumber + ", eOtpEmailId=" + eOtpEmailId + ", emailFromConfigured=" + emailFromConfigured + ", customerSequenceNumber=" + customerSequenceNumber + ", userSequenceNumber=" + userSequenceNumber + ", userAmibCustRef=" + userAmibCustRef + "]";
	}
	
	private MetaData metaData;

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

	private String contactUsEmail;

	private String contactUsHelpLineNumber;

	private String mOtpMobileNumber = "";

	private String eOtpEmailId = "";

	String emailFromConfigured;
	
	
	
	/**************************ASK************************/

	private BigDecimal customerSequenceNumber; //[Get:- Update Profile Details and getUser Details] , [USED :- veh_headre_dtl, get_prof_dtls , set_veh_dtls] 

	private BigDecimal userSequenceNumber; //[Get:- On User Login] , [USED :- get_user_dtls, set_veh_dtls]  

	private BigDecimal userAmibCustRef; // [Get:- On User Login] , [USED :- set_veh_dtls]  
	
	/**************************ASK************************/
	
	
	

	public String getEmailFromConfigured()
	{
		return emailFromConfigured;
	}

	public void setEmailFromConfigured(String emailFromConfigured)
	{
		this.emailFromConfigured = emailFromConfigured;
	}

	public BigDecimal getUserAmibCustRef()
	{
		return userAmibCustRef;
	}

	public void setUserAmibCustRef(BigDecimal userAmibCustRef)
	{
		this.userAmibCustRef = userAmibCustRef;
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

	public BigDecimal getCustomerSequenceNumber()
	{
		return customerSequenceNumber;
	}

	public void setCustomerSequenceNumber(BigDecimal customerSequenceNumber)
	{
		this.customerSequenceNumber = customerSequenceNumber;
	}

	public MetaData getMetaData()
	{
		return metaData;
	}

	public void setMetaData(MetaData metaData)
	{
		this.metaData = metaData;
	}

	public BigDecimal getUserSequenceNumber()
	{
		return userSequenceNumber;
	}

	public void setUserSequenceNumber(BigDecimal userSequenceNumber)
	{
		this.userSequenceNumber = userSequenceNumber;
	}

	public String getmOtpMobileNumber()
	{
		return mOtpMobileNumber;
	}

	public void setmOtpMobileNumber(String mOtpMobileNumber)
	{
		this.mOtpMobileNumber = mOtpMobileNumber;
	}

	public String geteOtpEmailId()
	{
		return eOtpEmailId;
	}

	public void seteOtpEmailId(String eOtpEmailId)
	{
		this.eOtpEmailId = eOtpEmailId;
	}
}
