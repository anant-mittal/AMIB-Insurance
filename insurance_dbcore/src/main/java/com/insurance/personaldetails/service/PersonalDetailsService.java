




package com.insurance.personaldetails.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.insurance.personaldetails.dao.PersonalDetailsDao;

@Service
public class PersonalDetailsService
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
