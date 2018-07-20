




package com.insurance.personaldetails.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.insurance.personaldetails.model.Area;
import com.insurance.personaldetails.model.Business;
import com.insurance.personaldetails.model.Governorates;
import com.insurance.personaldetails.model.Nationality;
import com.insurance.response.ApiResponse;
import com.insurance.response.ResponseStatus;
import com.insurance.services.AbstractService;

import oracle.jdbc.OracleTypes;

@Repository
public class PersonalDetailsDao extends AbstractService
{
	String TAG = "com.insurance.personaldetails.dao.PersonalDetailsDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(PersonalDetailsDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	Connection connection;

	public ApiResponse getBusiness()
	{
		getConnection();

		ApiResponse response = getBlackApiResponse();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_BUSINESS(?,?,?,?,?,?)}";
		List<Business> businessArray = new ArrayList<Business>();

		logger.info(TAG + " getBusiness ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, 1);
			callableStatement.setInt(2, 11);
			callableStatement.setInt(3, 1);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				Business business = new Business();
				business.setBusinessCode(rs.getString(1));
				logger.info(TAG + " getBusiness :: Business code :" + rs.getString(1).toString());
				business.setBusinessDesc(rs.getString(2));
				logger.info(TAG + " getBusiness :: Business disc :" + rs.getString(2).toString());
				businessArray.add(business);
			}

			response.getData().getValues().addAll(businessArray);
			response.getData().setType("business");
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
	
	
	
	public ApiResponse getNationality()
	{
		getConnection();

		ApiResponse response = getBlackApiResponse();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_NATIONALITIES(?,?,?,?,?,?)}";
		List<Nationality> nationalityArray = new ArrayList<Nationality>();

		logger.info(TAG + " getNationality ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, 1);
			callableStatement.setInt(2, 11);
			callableStatement.setInt(3, 3);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				Nationality nationality = new Nationality();
				nationality.setNationalityCode(rs.getString(1));
				logger.info(TAG + " getNationality :: Nationality code :" + rs.getString(1));
				nationality.setNationalityDesc(rs.getString(2));
				logger.info(TAG + " getNationality :: Nationality disc :" + rs.getString(2));
				nationalityArray.add(nationality);
			}

			response.getData().getValues().addAll(nationalityArray);
			response.getData().setType("nationality");
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
	
	
	public ApiResponse getGovernorates()
	{
		getConnection();

		ApiResponse response = getBlackApiResponse();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_GOVERNORATES(?,?,?,?,?,?)}";
		List<Governorates> governoratesArray = new ArrayList<Governorates>();

		logger.info(TAG + " getGovernorates ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, 1);
			callableStatement.setInt(2, 11);
			callableStatement.setInt(3, 3);
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				Governorates governorates = new Governorates();
				governorates.setGovCode(rs.getString(1));
				logger.info(TAG + " getNationality :: gov code :" + rs.getString(1));
				governorates.setGovDesc(rs.getString(2));
				logger.info(TAG + " getNationality :: gov disc :" + rs.getString(2));
				governoratesArray.add(governorates);
			}

			response.getData().getValues().addAll(governoratesArray);
			response.getData().setType("governorates");
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
	
	
	
	
	public ApiResponse getArea(String gov)
	{
		getConnection();

		ApiResponse response = getBlackApiResponse();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_AREA(?,?,?,?,?,?,?)}";
		List<Area> areaArray = new ArrayList<Area>();

		logger.info(TAG + " getArea ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, 1);
			callableStatement.setInt(2, 11);
			callableStatement.setString(3,gov);
			callableStatement.setInt(4, 3);
			callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(5);

			while (rs.next())
			{
				Area area = new Area();
				area.setAreaCode(rs.getString(1));
				logger.info(TAG + " getArea :: Area code :" + rs.getString(1).toString());
				area.setAreaDesc(rs.getString(2));
				logger.info(TAG + " getArea :: Area disc :" + rs.getString(2).toString());
				areaArray.add(area);
			}

			response.getData().getValues().addAll(areaArray);
			response.getData().setType("area");
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
