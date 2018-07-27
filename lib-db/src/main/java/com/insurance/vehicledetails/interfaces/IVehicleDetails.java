package com.insurance.vehicledetails.interfaces;

import java.util.ArrayList;

import com.insurance.vehicledetails.model.Model;
import org.springframework.web.bind.annotation.RequestParam;
import com.amx.jax.api.AmxApiResponse;

public interface IVehicleDetails
{
	public ArrayList getMake();
	
	public AmxApiResponse<Model, Object> getModel(String make);
	
	public ArrayList getFuleType();
	
	public ArrayList getPurpose();
	
	public ArrayList getShape();
	
	public ArrayList getColour();
}
