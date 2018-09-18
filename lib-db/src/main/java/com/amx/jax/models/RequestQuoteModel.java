package com.amx.jax.models;

import java.util.List;

public class RequestQuoteModel
{
	private RequestQuoteInfo requestQuoteInfo;

	private VehicleDetails vehicleDetails;

	private PersonalDetails personalDetails;

	private List<?> vehicleImageDetails;

	public List<?> getVehicleImageDetails()
	{
		return vehicleImageDetails;
	}

	public void setVehicleImageDetails(List<?> list)
	{
		this.vehicleImageDetails = list;
	}

	public PersonalDetails getPersonalDetails()
	{
		return personalDetails;
	}

	public void setPersonalDetails(PersonalDetails personalDetails)
	{
		this.personalDetails = personalDetails;
	}

	public VehicleDetails getVehicleDetails()
	{
		return vehicleDetails;
	}

	public void setVehicleDetails(VehicleDetails vehicleDetails)
	{
		this.vehicleDetails = vehicleDetails;
	}

	public RequestQuoteInfo getRequestQuoteInfo()
	{
		return requestQuoteInfo;
	}

	public void setRequestQuoteInfo(RequestQuoteInfo requestQuoteInfo)
	{
		this.requestQuoteInfo = requestQuoteInfo;
	}
}
