




package com.amx.jax.personaldetails.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.insurance.personaldetails.dao.PersonalDetailsDao;
import com.insurance.personaldetails.interfaces.IPersonalDetails;

@Service
public class PersonalDetailsService implements IPersonalDetails
{
	@Autowired
	public PersonalDetailsDao personalDetailsDao;

	public ArrayList getBusiness()
	{
		try
		{
			return personalDetailsDao.getBusiness();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList getNationality()
	{
		
		try
		{
			return personalDetailsDao.getNationality();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList getGovernorates()
	{
		
		try
		{
			return personalDetailsDao.getGovernorates();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList getArea(String gov)
	{
		
		try
		{
			return personalDetailsDao.getArea(gov);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
