package com.amx.jax.models;

import java.util.ArrayList;

public class VehicleImageDetails
{
	ArrayList<ImageInitInfo> imageInfoArray;
	
	ImageUploadDetails uploadImageDetails;

	public ImageUploadDetails getUploadImageDetails()
	{
		return uploadImageDetails;
	}

	public void setUploadImageDetails(ImageUploadDetails uploadImageDetails)
	{
		this.uploadImageDetails = uploadImageDetails;
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
