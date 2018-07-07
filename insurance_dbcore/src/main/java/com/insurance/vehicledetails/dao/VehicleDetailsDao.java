package com.insurance.vehicledetails.dao;

import com.insurance.response.ApiResponse;
import com.insurance.services.AbstractService;

public class VehicleDetailsDao extends AbstractService
{
	public ApiResponse getMake()
	{
		ApiResponse response = getBlackApiResponse();
		return response;
	}

	@Override
	public String getModelType()
	{
		return null;
	}
}
