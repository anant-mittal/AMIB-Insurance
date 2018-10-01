package com.amx.jax.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.QuoteAddPolicyDetails;
import com.amx.jax.models.ReplacementTypeList;
import oracle.jdbc.OracleTypes;

@Repository
public class CustomizeQuoteDao
{
	String TAG = "com.amx.jax.dao.CustomizeQuoteDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomizeQuoteDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	MetaData metaData;

	Connection connection;
	
	
	public ArrayList<QuoteAddPolicyDetails> getQuoteAdditionalPolicy(BigDecimal quotSeqNumber, BigDecimal verNumber)
	{
		logger.info(TAG + " getQuoteAdditionalPolicy :: quotSeqNumber :" + quotSeqNumber);
		logger.info(TAG + " getQuoteAdditionalPolicy :: verNumber     :" + verNumber);
		
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_QUOTE_ADDLPOL(?,?,?,?,?,?,?,?)}";
		ArrayList<QuoteAddPolicyDetails> activePolicyArray = new ArrayList<QuoteAddPolicyDetails>();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, quotSeqNumber);
			callableStatement.setBigDecimal(4, verNumber);
			callableStatement.setBigDecimal(5, metaData.getLanguageId());
			callableStatement.registerOutParameter(6, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(6);
			while (rs.next())
			{
				QuoteAddPolicyDetails quoteAddPolicyDetails = new QuoteAddPolicyDetails();
				quoteAddPolicyDetails.setAddPolicyTypeCode(rs.getString(3));
				quoteAddPolicyDetails.setAddPolicyTypeDesc(rs.getString(4));

				if (null != rs.getBigDecimal(5) && rs.getBigDecimal(5).equals(new BigDecimal(1)))
				{
					quoteAddPolicyDetails.setReplacTypeVisibility(true);
				}
				else
				{
					quoteAddPolicyDetails.setReplacTypeVisibility(false);
				}

				if (null != rs.getString(7) && !rs.getString(7).equals("") && rs.getString(7).equalsIgnoreCase("Y"))
				{
					quoteAddPolicyDetails.setAddPolicyTypeEnable(true);
				}
				else
				{
					quoteAddPolicyDetails.setAddPolicyTypeEnable(false);
				}
				quoteAddPolicyDetails.setYearlyPremium(rs.getBigDecimal(6));
				quoteAddPolicyDetails.setReplacementType(rs.getString(9));
				activePolicyArray.add(quoteAddPolicyDetails);
				logger.info(TAG + " getQuoteAdditionalPolicy :: QuoteAddPolicyDetails :" + quoteAddPolicyDetails.toString());
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
		return activePolicyArray;
	}

	public ArrayList getReplacementTypeList(String policyTypeCode, Date policyDate)// V10,28-09-2018
	{
		logger.info(TAG + " getReplacementTypeList :: policyTypeCode     :" + policyTypeCode);
		logger.info(TAG + " getReplacementTypeList :: policyDate         :" + policyDate);

		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_REPLPOLTYPE_LIST(?,?,?,?,?,?,?,?)}";
		ArrayList<ReplacementTypeList> repTypeListArray = new ArrayList<ReplacementTypeList>();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setString(3, policyTypeCode);
			callableStatement.setDate(4, policyDate);
			callableStatement.setBigDecimal(5, metaData.getLanguageId());
			callableStatement.registerOutParameter(6, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(6);
			while (rs.next())
			{
				ReplacementTypeList replacementTypeList = new ReplacementTypeList();
				if(null == rs.getString(1) || rs.getString(1).equals(""))
				{
					return null;
				}
				replacementTypeList.setReplacementTypeCode(rs.getString(1));
				replacementTypeList.setReplacementTypeDesc(rs.getString(2));
				replacementTypeList.setYearlyPremium(rs.getBigDecimal(3));
				repTypeListArray.add(replacementTypeList);
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
		return repTypeListArray;
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
