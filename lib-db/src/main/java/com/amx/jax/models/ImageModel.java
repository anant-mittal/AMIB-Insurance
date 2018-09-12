package com.amx.jax.models;

import java.util.ArrayList;

public class ImageModel
{
	ArrayList<ImageInitInfo> imageInfoArray;

	private VehicleImageDetails vehicleImageDetails;
	
	private String errorMessage;

	private String errorCode;

	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	public VehicleImageDetails getVehicleImageDetails()
	{
		return vehicleImageDetails;
	}

	public void setVehicleImageDetails(VehicleImageDetails vehicleImageDetails)
	{
		this.vehicleImageDetails = vehicleImageDetails;
	}

	public ArrayList<ImageInitInfo> getImageInfoArray()
	{
		return imageInfoArray;
	}

	public void setImageInfoArray(ArrayList<ImageInitInfo> imageInfoArray)
	{
		this.imageInfoArray = imageInfoArray;
	}
}
