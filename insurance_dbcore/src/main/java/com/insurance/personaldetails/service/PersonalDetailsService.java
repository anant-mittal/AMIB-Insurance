




package com.insurance.personaldetails.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.insurance.personaldetails.dao.PersonalDetailsDao;
import com.insurance.response.ApiResponse;

@Service
public class PersonalDetailsService
{
	@Autowired
	public PersonalDetailsDao personalDetailsDao;

	public ApiResponse getBusiness()
	{
		ApiResponse apiResponse = null;
		try
		{
			apiResponse = personalDetailsDao.getBusiness();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return apiResponse;
	}
	
	public ApiResponse getNationality()
	{
		ApiResponse apiResponse = null;
		try
		{
			apiResponse = personalDetailsDao.getNationality();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return apiResponse;
	}
	
	public ApiResponse getGovernorates()
	{
		ApiResponse apiResponse = null;
		try
		{
			apiResponse = personalDetailsDao.getGovernorates();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return apiResponse;
	}
	
	public ApiResponse getArea(String gov)
	{
		ApiResponse apiResponse = null;
		try
		{
			apiResponse = personalDetailsDao.getArea(gov);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return apiResponse;
	}
}
