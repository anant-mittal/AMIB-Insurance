




package com.insurance.user_registartion.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.transaction.annotation.Transactional;

import com.insurance.base.model.Business;
import com.insurance.response.ApiResponse;
import com.insurance.response.ResponseStatus;
import com.insurance.services.AbstractService;
import com.insurance.user_registartion.model.CustomerPersonalDetail;
import com.insurance.user_registartion.model.CustomerRegistrationDetails;
import oracle.jdbc.OracleTypes;

public class CustomerRegistrationDao extends AbstractService
{
	String TAG = "com.insurance.user_registartion.dao :: CustomerRegistrationDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	CustomerPersonalDetail customerPersonalDetail;

	Connection connection;

	@Override
	public String getModelType()
	{
		return null;
	}

	public ApiResponse addNewCustomer(CustomerPersonalDetail customerPersonalDetail)
	{
		logger.info(TAG + " addNewCustomer :: customerRegistrationDetails :" + customerPersonalDetail.toString());

		ApiResponse response = getBlackApiResponse();

		CallableStatement callableStatement = null;

		List<Business> businessDataArray = new ArrayList<Business>();

		response.getData().setType("business");

		getConnection();

		String callProcedure = "{call IRB_REGISTER_USER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.registerOutParameter(1, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(2, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(10, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(11, java.sql.Types.DATE);
			callableStatement.registerOutParameter(12, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(13, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(14, java.sql.Types.NUMERIC);
			callableStatement.registerOutParameter(15, java.sql.Types.VARCHAR);

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

	@Transactional
	public Map<String, Object> createNewCustomer(CustomerPersonalDetail customerPersonalDetail)
	{
		String callProcedure = "{call IRB_REGISTER_USER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		String civilId = customerPersonalDetail.getCivilId();
		String mobileNumber = "";// = customerPersonalDetail.getMobiileNumber();
		String otp = "";// = customerPersonalDetail.getOtp();

		Map<String, Object> output = null;

		try
		{
			List<SqlParameter> declareInAndOutputParameters = Arrays.asList(new SqlParameter(Types.VARCHAR),
					new SqlParameter(Types.VARCHAR), 
					new SqlParameter(Types.VARCHAR));

			output = jdbcTemplate.call(new CallableStatementCreator()
			{
				@Override
				public CallableStatement createCallableStatement(Connection con) throws SQLException
				{
					CallableStatement cs = con.prepareCall(callProcedure);
					cs.setString(1, civilId);
					cs.setString(2, mobileNumber);
					cs.setString(3, otp);
					cs.executeQuery();
					return cs;
				}

			}, declareInAndOutputParameters);

		}
		catch (Exception e)
		{

		}
		return output;
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
}
