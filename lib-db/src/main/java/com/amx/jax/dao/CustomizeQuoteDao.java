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

import com.amx.jax.models.AdditionalPolicySave;
import com.amx.jax.models.CustomizeQuoteAddPol;
import com.amx.jax.models.CustomizeQuoteSave;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.QuoteAddPolicyDetails;
import com.amx.jax.models.ReplacementTypeList;
import com.amx.jax.models.Validate;

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
					quoteAddPolicyDetails.setReplacTypeVisibility("select");
				}
				else
				{
					quoteAddPolicyDetails.setReplacTypeVisibility("text");
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
				quoteAddPolicyDetails.setReplacementTypeCode(rs.getString(9));
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
				if (null == rs.getString(1) || rs.getString(1).equals(""))
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

	public String getTermsAndCondition()
	{
		getConnection();
		String termsCondition = "";
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_TERMSCON(?,?,?,?,?,?,?)}";
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setString(3, "PAY");
			callableStatement.setBigDecimal(4, metaData.getLanguageId());
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			termsCondition = callableStatement.getString(5);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return termsCondition;
	}

	public Validate saveCustomizeQuote(CustomizeQuoteSave customizeQuoteSave)
	{
		getConnection();
		CallableStatement callableStatement = null;
		Validate validate = new Validate();
		String callProcedure = "{call IRB_SAVE_QUOTE_SUMMARY(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, customizeQuoteSave.getQuotSeqNumber());
			callableStatement.setBigDecimal(4, customizeQuoteSave.getVerNumber());
			callableStatement.setBigDecimal(5, customizeQuoteSave.getBasicPremium());
			callableStatement.setBigDecimal(6, customizeQuoteSave.getSupervisionFees());
			callableStatement.setBigDecimal(7, customizeQuoteSave.getIssueFee());
			callableStatement.setBigDecimal(8, customizeQuoteSave.getDisscountAmt());
			callableStatement.setBigDecimal(9, customizeQuoteSave.getAddCoveragePremium());
			callableStatement.setBigDecimal(10, customizeQuoteSave.getTotalAmount());
			callableStatement.setString(12, metaData.getDeviceType());
			callableStatement.setString(13, metaData.getDeviceId());
			callableStatement.setString(14, metaData.getCivilId());
			callableStatement.registerOutParameter(15, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(16, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			validate.setErrorCode(callableStatement.getString(15));
			validate.setErrorMessage(callableStatement.getString(16));
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return validate;
	}
	
	
	
	public Validate saveCustomizeQuoteAddPol(CustomizeQuoteAddPol customizeQuoteAddPol)
	{
		getConnection();
		CallableStatement callableStatement = null;
		Validate validate = new Validate();
		String callProcedure = "{call IRB_CUSTOMIZE_QUOTE_ADDLPOL(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, customizeQuoteAddPol.getQuoteSeqNumber());
			callableStatement.setBigDecimal(4, customizeQuoteAddPol.getVerNumber());
			callableStatement.setString(5, customizeQuoteAddPol.getAddPolicyTypeCode());
			callableStatement.setBigDecimal(6, customizeQuoteAddPol.getYearlyPremium());
			callableStatement.setString(7, customizeQuoteAddPol.getOptIndex());
			callableStatement.setBigDecimal(8, customizeQuoteAddPol.getYearMultiplePremium());
			callableStatement.setString(9, customizeQuoteAddPol.getReplacementTypeCode());
			callableStatement.setString(10, metaData.getDeviceType());
			callableStatement.setString(11, metaData.getDeviceId());
			callableStatement.setString(12, metaData.getCivilId());
			callableStatement.registerOutParameter(13, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(14, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			validate.setErrorCode(callableStatement.getString(15));
			validate.setErrorMessage(callableStatement.getString(16));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return validate;
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
