package com.amx.jax.models;

import java.util.ArrayList;

public class VehicleImageDetails
{
	ArrayList<ImageInitInfo> imageInfoArray;
	
	ImageUploadDetails imageUploadDetails;
	
	public ImageUploadDetails getImageUploadDetails()
	{
		return imageUploadDetails;
	}

	public void setImageUploadDetails(ImageUploadDetails imageUploadDetails)
	{
		this.imageUploadDetails = imageUploadDetails;
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
