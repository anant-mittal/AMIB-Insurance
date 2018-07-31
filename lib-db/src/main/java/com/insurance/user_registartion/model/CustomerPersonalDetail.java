




package com.insurance.user_registartion.model;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.NumberFormat;


public class CustomerPersonalDetail
{
	private static final long serialVersionUID = 1L;

	@NotNull
	private int countryId;
	
	@NotNull
	private int compCd;
	
	@NotNull
	private String userType;
	
	@NotNull
	private String civilId;
	
	@NotNull
	private String password;
	
	@NotNull
	@Size(min = 1)
	@NumberFormat
	private String mobile;
	
	@NotNull
	private String mobVerificationCode;
	
	@NotNull
	@Email
	private String email;
	
	@NotNull
	private String emailVerificationCode;
	
	@NotNull
	private int languageId;
	
	@NotNull
	private String deviceType;

	@NotNull
	private String createdDeviceId;
	
	@NotNull
	private int userSeqNumber;
	
	public int getCountryId()
	{
		return countryId;
	}

	public void setCountryId(int countryId)
	{
		this.countryId = countryId;
	}

	public int getCompCd()
	{
		return compCd;
	}

	public void setCompCd(int compCd)
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

	public String getCivilId()
	{
		return civilId;
	}

	public void setCivilId(String civilId)
	{
		this.civilId = civilId;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getMobVerificationCode()
	{
		return mobVerificationCode;
	}

	public void setMobVerificationCode(String mobVerificationCode)
	{
		this.mobVerificationCode = mobVerificationCode;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getEmailVerificationCode()
	{
		return emailVerificationCode;
	}

	public void setEmailVerificationCode(String emailVerificationCode)
	{
		this.emailVerificationCode = emailVerificationCode;
	}

	public int getLanguageId()
	{
		return languageId;
	}

	public void setLanguageId(int languageId)
	{
		this.languageId = languageId;
	}

	public String getDeviceType()
	{
		return deviceType;
	}

	public void setDeviceType(String deviceType)
	{
		this.deviceType = deviceType;
	}

	public String getCreatedDeviceId()
	{
		return createdDeviceId;
	}

	public void setCreatedDeviceId(String createdDeviceId)
	{
		this.createdDeviceId = createdDeviceId;
	}

	public int getUserSeqNumber()
	{
		return userSeqNumber;
	}

	public void setUserSeqNumber(int userSeqNumber)
	{
		this.userSeqNumber = userSeqNumber;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	
	@Override
	public String toString()
	{
		return "CustomerPersonalDetail [countryId=" + countryId + ", compCd=" + compCd + ", userType=" + userType + ", civilId=" + civilId + ", password=" + password + ", mobile=" + mobile + ", mobVerificationCode=" + mobVerificationCode + ", email=" + email + ", emailVerificationCode="
				+ emailVerificationCode + ", languageId=" + languageId + ", deviceType=" + deviceType + ", createdDeviceId=" + createdDeviceId + ", userSeqNumber=" + userSeqNumber + "]";
	}
	
}
