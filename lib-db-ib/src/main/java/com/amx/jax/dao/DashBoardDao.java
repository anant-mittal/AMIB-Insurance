package com.amx.jax.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.amx.jax.constants.ApiConstants;
import com.amx.jax.meta.IMetaService;
import com.amx.jax.models.IncompleteApplModel;

@Component
public class DashBoardDao
{
	String TAG = "RequestQuoteDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(RequestQuoteDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	Connection connection;

	@Autowired
	IMetaService metaService;
	
	
	public IncompleteApplModel getIncompleteApplication(String civilId, String userType , BigDecimal custSeqNum)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_CHECK_INCOMPLETE_APPL(?,?,?,?,?,?,?,?,?)}";
		IncompleteApplModel incompleteApplModel = new IncompleteApplModel();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(3, userType);
			callableStatement.setString(4, civilId);
			callableStatement.setBigDecimal(5, custSeqNum);
			callableStatement.registerOutParameter(6, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			incompleteApplModel.setAppSeqNumber(callableStatement.getBigDecimal(6));
			incompleteApplModel.setAppStage(callableStatement.getString(7));
			incompleteApplModel.setErrorCode(callableStatement.getString(8));
			incompleteApplModel.setErrorMessage(callableStatement.getString(9));

			logger.info(TAG + " getIncompleteApplication :: getBigDecimal(6) :" + callableStatement.getBigDecimal(6));

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
			incompleteApplModel.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			incompleteApplModel.setErrorMessage(e.toString());
			logger.info(TAG+"getIncompleteApplication :: exception :" + e);
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
			if (null == connection || connection.isClosed())
			{
				connection = jdbcTemplate.getDataSource().getConnection();
			}
			return connection;
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
