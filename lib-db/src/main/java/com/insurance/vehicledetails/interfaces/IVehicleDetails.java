package com.insurance.vehicledetails.interfaces;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestParam;

public interface IVehicleDetails
{
	public ArrayList getMake();
	
	public ArrayList getModel(@RequestParam("make") String make);
	
	public ArrayList getFuleType();
	
	public ArrayList getPurpose();
	
	public ArrayList getShape();
	
	public ArrayList getColour();
}
