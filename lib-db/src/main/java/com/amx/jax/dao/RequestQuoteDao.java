package com.amx.jax.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.amx.jax.models.ActivePolicyModel;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.IncompleteApplModel;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.VehicleSession;
import org.springframework.beans.factory.annotation.Autowired;
import oracle.jdbc.OracleTypes;

@Repository
public class RequestQuoteDao
{
	String TAG = "com.amx.jax.dao.RequestQuoteDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(ActivePolicyDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	MetaData metaData;
	
	@Autowired
	VehicleSession vehicleSession;

	Connection connection;
	
	public IncompleteApplModel getIncompleteApplication()
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_CHECK_INCOMPLETE_APPL(?,?,?,?,?,?,?,?,?)}";
		IncompleteApplModel incompleteApplModel = new IncompleteApplModel();
		
		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setString(3, metaData.getUserType());
			callableStatement.setString(4, metaData.getCivilId());
			callableStatement.setBigDecimal(5, metaData.getCustomerSequenceNumber());
			callableStatement.registerOutParameter(6, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			incompleteApplModel.setAppSeqNumber(callableStatement.getBigDecimal(6));
			logger.info(TAG + " getIncompleteApplication :: callableStatement.getBigDecimal(6) :" + callableStatement.getBigDecimal(6));
			vehicleSession.setAppSeqNumber(callableStatement.getBigDecimal(6));
			incompleteApplModel.setAppStage(callableStatement.getString(7));
			incompleteApplModel.setErrorCode(callableStatement.getString(8));
			incompleteApplModel.setErrorMessage(callableStatement.getString(9));

			if (callableStatement.getString(8) == null)
			{
				incompleteApplModel.setStatus(true);
			}
			else
			{
				incompleteApplModel.setStatus(false);
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
		return incompleteApplModel;
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

	private void CloseConnection(Statement statement, Connection connection)
	{
		try
		{
			if (statement != null)
			{
				statement.close();
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
