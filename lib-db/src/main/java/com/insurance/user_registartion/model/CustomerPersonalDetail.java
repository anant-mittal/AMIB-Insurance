




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
	private int userSeqNumber;

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
	@Email
	private String email;

	@NotNull
	private String mobVerificationCode;
	
	@NotNull
	private String emailVerificationCode;
	
	@NotNull
	private String mobileVerified;
	
	@NotNull
	private String emailVerified;
	
	@NotNull
	private String refAmibcd;

	@NotNull
	private String status;
	
	@NotNull
	private String deviceType;

	@NotNull
	private Date date;
	
	@NotNull
	private String createdDeviceId;
	
	@NotNull
	private String createdBy;
	
	@NotNull
	private Date updateOn;
	
	@NotNull
	private String updateDeviceId;
	
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
	
	public int getUserSeqNumber()
	{
		return userSeqNumber;
	}

	public void setUserSeqNumber(int userSeqNumber)
	{
		this.userSeqNumber = userSeqNumber;
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
	
	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public String getMobVerificationCode()
	{
		return mobVerificationCode;
	}

	public void setMobVerificationCode(String mobVerificationCode)
	{
		this.mobVerificationCode = mobVerificationCode;
	}

	public String getEmailVerificationCode()
	{
		return emailVerificationCode;
	}

	public void setEmailVerificationCode(String emailVerificationCode)
	{
		this.emailVerificationCode = emailVerificationCode;
	}
	
	public String getMobileVerified()
	{
		return mobileVerified;
	}

	public void setMobileVerified(String mobileVerified)
	{
		this.mobileVerified = mobileVerified;
	}

	public String getEmailVerified()
	{
		return emailVerified;
	}

	public void setEmailVerified(String emailVerified)
	{
		this.emailVerified = emailVerified;
	}
	
	public String getRefAmibcd()
	{
		return refAmibcd;
	}

	public void setRefAmibcd(String refAmibcd)
	{
		this.refAmibcd = refAmibcd;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String getDeviceType()
	{
		return deviceType;
	}

	public void setDeviceType(String deviceType)
	{
		this.deviceType = deviceType;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getCreatedDeviceId()
	{
		return createdDeviceId;
	}

	public void setCreatedDeviceId(String createdDeviceId)
	{
		this.createdDeviceId = createdDeviceId;
	}

	public String getCreatedBy()
	{
		return createdBy;
	}

	public void setCreatedBy(String createdBy)
	{
		this.createdBy = createdBy;
	}

	public Date getUpdateOn()
	{
		return updateOn;
	}

	public void setUpdateOn(Date updateOn)
	{
		this.updateOn = updateOn;
	}

	public String getUpdateDeviceId()
	{
		return updateDeviceId;
	}

	public void setUpdateDeviceId(String updateDeviceId)
	{
		this.updateDeviceId = updateDeviceId;
	}
	
	@Override
	public String toString()
	{
		return "CustomerPersonalDetail [countryId=" + countryId + ", compCd=" + compCd + ", userSeqNumber=" + userSeqNumber + ", userType=" + userType + ", civilId=" + civilId + ", password=" + password + ", mobile=" + mobile + ", email=" + email + ", mobVerificationCode=" + mobVerificationCode
				+ ", emailVerificationCode=" + emailVerificationCode + ", mobileVerified=" + mobileVerified + ", emailVerified=" + emailVerified + ", refAmibcd=" + refAmibcd + ", status=" + status + ", deviceType=" + deviceType + ", date=" + date + ", createdDeviceId=" + createdDeviceId
				+ ", createdBy=" + createdBy + ", updateOn=" + updateOn + ", updateDeviceId=" + updateDeviceId + "]";
	}



}
