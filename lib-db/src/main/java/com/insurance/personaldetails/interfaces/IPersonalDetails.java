package com.insurance.personaldetails.interfaces;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.RequestParam;

public interface IPersonalDetails
{
	public ArrayList getBusiness();
	
	public ArrayList getNationality();
	
	public ArrayList getGovernorates();
	
	public ArrayList getArea(@RequestParam("gov") String gov);
}
