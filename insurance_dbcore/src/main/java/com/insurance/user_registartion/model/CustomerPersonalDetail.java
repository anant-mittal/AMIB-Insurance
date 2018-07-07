




package com.insurance.user_registartion.model;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;
import org.springframework.format.annotation.NumberFormat;
import com.insurance.model.AbstractModel;

public class CustomerPersonalDetail extends AbstractModel
{
	private static final long serialVersionUID = 1L;

	@NotNull
	private BigDecimal countryId;

	@NotNull
	private BigDecimal nationalityId;

	@NotNull
	@NumberFormat
	private String identityInt;

	@NotNull
	private String title;

	@NotNull
	@Size(min = 1)
	private String firstName;

	@NotNull
	@Size(min = 1)
	private String lastName;

	@NotNull
	@Email
	private String email;

	@NotNull
	@Size(min = 1)
	@NumberFormat
	private String mobile;

	@NotNull
	@NumberFormat
	private String telPrefix;

	public BigDecimal getCountryId()
	{
		return countryId;
	}

	public void setCountryId(BigDecimal countryId)
	{
		this.countryId = countryId;
	}

	public BigDecimal getNationalityId()
	{
		return nationalityId;
	}

	public void setNationalityId(BigDecimal nationalityId)
	{
		this.nationalityId = nationalityId;
	}

	public String getIdentityInt()
	{
		return identityInt;
	}

	public void setIdentityInt(String identityInt)
	{
		this.identityInt = identityInt;
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getMobile()
	{
		return mobile;
	}

	public void setMobile(String mobile)
	{
		this.mobile = mobile;
	}

	public String getTelPrefix()
	{
		return telPrefix;
	}

	public void setTelPrefix(String telPrefix)
	{
		this.telPrefix = telPrefix;
	}

	@Override
	public String toString()
	{
		return "CustomerPersonalDetail [countryId=" + countryId + ", nationalityId=" + nationalityId + ", identityInt=" + identityInt + ", title=" + title + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", mobile=" + mobile + ", telPrefix=" + telPrefix + "]";
	}

}
