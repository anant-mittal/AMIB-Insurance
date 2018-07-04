




package com.insurance.base.dao;

import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import oracle.jdbc.OracleTypes;
import java.sql.CallableStatement;
import com.insurance.base.model.Business;
import com.insurance.base.model.Nationality;
import com.insurance.response.ApiResponse;
import com.insurance.response.ResponseStatus;
import com.insurance.services.AbstractService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataDao extends AbstractService
{
	String TAG = "com.insurance.base.dao.DataDao";

	@Autowired
	JdbcTemplate jdbcTemplate;

	Connection connection;

	public List<Business> getBusinessData()
	{
		System.out.println("\n\n===========================================BUSINESS==========================================");

		getConnection();

		List<Business> businessDataArray = new ArrayList<Business>();
		CallableStatement callableStatement = null;

		String getDBUSERByUserIdSql = "{call IRB_GET_BUSINESS(?,?)}";

		try
		{
			callableStatement = connection.prepareCall(getDBUSERByUserIdSql);
			callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
			callableStatement.execute();
			ResultSet rs = (ResultSet) callableStatement.getObject(1);

			while (rs.next())
			{
				Business business = new Business();
				business.setCntrycd(rs.getInt(1));
				business.setCompcd(rs.getInt(2));
				business.setNationality(rs.getString(3));
				business.setDesc(rs.getString(4));
				System.out.println("DataDao :: getBusinessData :: rs.getString(3) :" + rs.getString(3));
				businessDataArray.add(business);
			}
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return businessDataArray;
	}

	public ApiResponse getBusinessDataDemo()
	{
		System.out.println("\n\n===========================================BUSINESS==========================================");

		ApiResponse response = getBlackApiResponse();
		response.getData().setType("business");

		getConnection();

		List<Business> businessDataArray = new ArrayList<Business>();
		CallableStatement callableStatement = null;

		String getDBUSERByUserIdSql = "{call IRB_GET_BUSINESS(?,?)}";

		try
		{
			callableStatement = connection.prepareCall(getDBUSERByUserIdSql);
			callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
			callableStatement.execute();
			ResultSet rs = (ResultSet) callableStatement.getObject(1);

			while (rs.next())
			{
				Business business = new Business();
				business.setCntrycd(rs.getInt(1));
				business.setCompcd(rs.getInt(2));
				business.setNationality(rs.getString(3));
				business.setDesc((rs.getString(4)));
				System.out.println("DataDao :: getBusinessData :: rs.getString(3) :" + rs.getString(3));
				businessDataArray.add(business);
			}

			System.out.println("DataDao :: getBusinessData :: businessDataArray :" + businessDataArray);

			response.getData().getValues().addAll(businessDataArray);
			response.setResponseStatus(ResponseStatus.OK);

		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
			response.setResponseStatus(ResponseStatus.INTERNAL_ERROR);
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return response;
	}

	public List<Nationality> getNationalityData()
	{
		System.out.println("\n\n===========================================Nationality==========================================");

		getConnection();

		List<Nationality> nationalityDataArray = new ArrayList<Nationality>();
		CallableStatement callableStatement = null;

		String getDBUSERByUserIdSql = "{call IRB_GET_NATIONALITIES(?,?)}";

		try
		{
			callableStatement = connection.prepareCall(getDBUSERByUserIdSql);
			callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(2, java.sql.Types.VARCHAR);
			callableStatement.execute();
			ResultSet rs = (ResultSet) callableStatement.getObject(1);

			while (rs.next())
			{
				Nationality nationality = new Nationality();
				nationality.setNationality(rs.getString(1));
				nationality.setDescription(rs.getString(2));
				nationalityDataArray.add(nationality);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return nationalityDataArray;
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
