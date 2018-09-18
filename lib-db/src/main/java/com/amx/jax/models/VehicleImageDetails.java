package com.amx.jax.models;

import java.util.ArrayList;

public class VehicleImageDetails
{
	ArrayList<ImageDetails> imageInfoArray;
	
	ImageUploadDetails imageUploadDetails;
	
	public ImageUploadDetails getImageUploadDetails()
	{
		return imageUploadDetails;
	}

	public void setImageUploadDetails(ImageUploadDetails imageUploadDetails)
	{
		this.imageUploadDetails = imageUploadDetails;
	}

	public ArrayList<ImageDetails> getImageInfoArray()
	{
		return imageInfoArray;
	}

	public void setImageInfoArray(ArrayList<ImageDetails> imageInfoArray)
	{
		this.imageInfoArray = imageInfoArray;
	}
}
