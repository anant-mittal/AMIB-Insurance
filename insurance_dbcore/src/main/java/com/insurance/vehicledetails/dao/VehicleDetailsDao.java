package com.insurance.vehicledetails.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import com.insurance.response.ApiResponse;
import com.insurance.response.ResponseStatus;
import com.insurance.services.AbstractService;

public class VehicleDetailsDao extends AbstractService
{
	@Autowired
	JdbcTemplate jdbcTemplate;

	Connection connection;
	
	public ApiResponse getMake()
	{
		getConnection();
		
		ApiResponse response = getBlackApiResponse();
		CallableStatement callableStatement = null;
		String getDBUSERByUserIdSql = "{call IRB_GET_BUSINESS(?,?)}";
		
		try
		{
			callableStatement = connection.prepareCall(getDBUSERByUserIdSql);
			
			
			response.getData().setType("make");
			response.setResponseStatus(ResponseStatus.OK);
		}
		catch (Exception e) 
		{
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			e.printStackTrace();
		}
		
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		
		return response;
	}
	
	
	
	
	
	
	
	
	public ApiResponse getModel(String make)
	{
		getConnection();
		
		ApiResponse response = getBlackApiResponse();
		CallableStatement callableStatement = null;
		String getDBUSERByUserIdSql = "{call IRB_GET_BUSINESS(?,?)}";
		
		try
		{
			callableStatement = connection.prepareCall(getDBUSERByUserIdSql);
			
			
			
			response.getData().setType("model");
			response.setResponseStatus(ResponseStatus.OK);
		}
		catch (Exception e) 
		{
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
			e.printStackTrace();
		}
		
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		
		return response;
	}
	

	
	private Connection getConnection()
	{
		try
		{
			connection = jdbcTemplate.getDataSource().getConnection();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return connection;
	}

	private void CloseConnection(CallableStatement callableStatement, Connection connection)
	{
		try
		{
			if (callableStatement != null)
			{
				callableStatement.close();
			}

			if (connection != null)
			{
				connection.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public String getModelType()
	{
		return null;
	}
}
