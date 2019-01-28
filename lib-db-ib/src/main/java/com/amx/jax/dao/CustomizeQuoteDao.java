package com.amx.jax.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.amx.jax.constants.ApiConstants;
import com.amx.jax.meta.IMetaService;
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.CustomizeQuoteAddPol;
import com.amx.jax.models.CustomizeQuoteSave;
import com.amx.jax.models.QuoteAddPolicyDetails;
import com.amx.jax.models.ReplacementTypeList;
import com.amx.jax.models.TermsCondition;
import com.amx.jax.models.ResponseInfo;
import com.amx.jax.utility.Utility;

import oracle.jdbc.OracleTypes;

@Repository
public class CustomizeQuoteDao
{
	String TAG = "com.amx.jax.dao.CustomizeQuoteDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(CustomizeQuoteDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	IMetaService metaService;

	Connection connection;

	public ArrayResponseModel getQuoteAdditionalPolicy(BigDecimal quotSeqNumber, BigDecimal verNumber , BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		String callProcedure = "{call IRB_GET_QUOTE_ADDLPOL(?,?,?,?,?,?,?,?)}";
		ArrayList<QuoteAddPolicyDetails> activePolicyArray = new ArrayList<QuoteAddPolicyDetails>();
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, quotSeqNumber);
			callableStatement.setBigDecimal(4, verNumber);
			callableStatement.setBigDecimal(5, languageId);
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
				arrayResponseModel.setDataArray(activePolicyArray);
				
			}
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getQuoteAdditionalPolicy :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	//public ArrayList getReplacementTypeList(String policyTypeCode, Date policyDate)// V10,28-09-2018
	public ArrayResponseModel getReplacementTypeList(String policyTypeCode, Date policyDate , BigDecimal languageId)// V10,28-09-2018
	{
		getConnection();
		CallableStatement callableStatement = null;
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		String callProcedure = "{call IRB_GET_REPLPOLTYPE_LIST(?,?,?,?,?,?,?,?)}";
		ArrayList<ReplacementTypeList> repTypeListArray = new ArrayList<ReplacementTypeList>();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(3, policyTypeCode);
			callableStatement.setDate(4, policyDate);
			callableStatement.setBigDecimal(5, languageId);
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
				//logger.info("getCustomizedQuoteDetails :: rs.getString(1) :" + rs.getString(1));
				replacementTypeList.setReplacementTypeCode(rs.getString(1));
				replacementTypeList.setReplacementTypeDesc(rs.getString(2));
				replacementTypeList.setYearlyPremium(rs.getBigDecimal(3));
				repTypeListArray.add(replacementTypeList);
			}
			//logger.info("getCustomizedQuoteDetails :: repTypeListArray :" + repTypeListArray);
			arrayResponseModel.setDataArray(repTypeListArray);

		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getReplacementTypeList :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
	}

	//public ArrayList getTermsAndCondition()
	public ArrayResponseModel getTermsAndCondition(BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		
		String callProcedure = "{call IRB_GET_TERMSCON(?,?,?,?,?,?,?)}";
		ArrayList<TermsCondition> termsConditionArray = new ArrayList<TermsCondition>();
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(3, "PAY");
			callableStatement.setBigDecimal(4, languageId);
			callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			ResultSet rs = (ResultSet) callableStatement.getObject(5);

			while (rs.next())
			{
				TermsCondition termsCondition = new TermsCondition();
				termsCondition.setTermsAndCondition(rs.getString(1));
				termsCondition.setId(rs.getBigDecimal(2));
				termsConditionArray.add(termsCondition);
			}
			arrayResponseModel.setDataArray(termsConditionArray);
		}
		catch (Exception e)
		{
			arrayResponseModel.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			arrayResponseModel.setErrorMessage(e.toString());
			logger.info(TAG+"getTermsAndCondition :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}

		return arrayResponseModel;
	}

	public TreeMap<Integer, String> getTermsAndConditionTest(BigDecimal languageId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_TERMSCON(?,?,?,?,?,?,?)}";
		TreeMap<Integer, String> data = new TreeMap<Integer, String>();
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setString(3, "PAY");
			callableStatement.setBigDecimal(4, languageId);
			callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			ResultSet rs = (ResultSet) callableStatement.getObject(5);

			while (rs.next())
			{
				data.put(rs.getInt(2), rs.getString(1));
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

		return data;
	}

	public ResponseInfo saveCustomizeQuote(CustomizeQuoteSave customizeQuoteSave , String civilId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		ResponseInfo validate = new ResponseInfo();
		String callProcedure = "{call IRB_SAVE_QUOTE_SUMMARY(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, customizeQuoteSave.getQuotSeqNumber());
			callableStatement.setBigDecimal(4, customizeQuoteSave.getVerNumber());
			callableStatement.setBigDecimal(5, Utility.round(customizeQuoteSave.getBasicPremium(), metaService.getTenantProfile().getDecplc()));
			callableStatement.setBigDecimal(6, Utility.round(customizeQuoteSave.getSupervisionFees(), metaService.getTenantProfile().getDecplc()));
			callableStatement.setBigDecimal(7, Utility.round(customizeQuoteSave.getIssueFee(), metaService.getTenantProfile().getDecplc()));
			callableStatement.setBigDecimal(8, Utility.round(customizeQuoteSave.getDisscountAmt(), metaService.getTenantProfile().getDecplc()));
			callableStatement.setBigDecimal(9, Utility.round(customizeQuoteSave.getAddCoveragePremium(), metaService.getTenantProfile().getDecplc()));
			callableStatement.setBigDecimal(10,Utility.round(customizeQuoteSave.getTotalAmount(), metaService.getTenantProfile().getDecplc()));
			callableStatement.setString(11, metaService.getUserDeviceInfo().getDeviceType());
			callableStatement.setString(12, metaService.getUserDeviceInfo().getDeviceId());
			callableStatement.setString(13, civilId);
			callableStatement.registerOutParameter(14, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(15, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			validate.setErrorCode(callableStatement.getString(14));
			validate.setErrorMessage(callableStatement.getString(15));

		}
		catch (Exception e)
		{
			validate.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			validate.setErrorMessage(e.toString());
			logger.info(TAG+"saveCustomizeQuote :: exception :" + e);
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return validate;
	}

	public ResponseInfo saveCustomizeQuoteAddPol(CustomizeQuoteAddPol customizeQuoteAddPol , String civilId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		ResponseInfo validate = new ResponseInfo();
		String callProcedure = "{call IRB_CUSTOMIZE_QUOTE_ADDLPOL(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaService.getTenantProfile().getCountryId());
			callableStatement.setBigDecimal(2, metaService.getTenantProfile().getCompCd());
			callableStatement.setBigDecimal(3, customizeQuoteAddPol.getQuoteSeqNumber());
			callableStatement.setBigDecimal(4, customizeQuoteAddPol.getVerNumber());
			callableStatement.setString(5, customizeQuoteAddPol.getAddPolicyTypeCode());
			callableStatement.setBigDecimal(6, customizeQuoteAddPol.getYearlyPremium());
			callableStatement.setString(7, customizeQuoteAddPol.getOptIndex());
			callableStatement.setBigDecimal(8, Utility.round(customizeQuoteAddPol.getYearMultiplePremium(), metaService.getTenantProfile().getDecplc()));
			callableStatement.setString(9, customizeQuoteAddPol.getReplacementTypeCode());
			callableStatement.setString(10, metaService.getUserDeviceInfo().getDeviceType());
			callableStatement.setString(11, metaService.getUserDeviceInfo().getDeviceId());
			callableStatement.setString(12, civilId);
			callableStatement.registerOutParameter(13, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(14, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			
			//logger.info(TAG + " saveCustomizeQuoteAddPol :: getErrorCode() :" + callableStatement.getString(13));
			//logger.info(TAG + " saveCustomizeQuoteAddPol :: getErrorMessage() :" + callableStatement.getString(14));
			
			validate.setErrorCode(callableStatement.getString(13));
			validate.setErrorMessage(callableStatement.getString(14));
		}
		catch (Exception e)
		{
			validate.setErrorCode(ApiConstants.ERROR_OCCURRED_ON_SERVER);
			validate.setErrorMessage(e.toString());
			logger.info(TAG+"saveCustomizeQuote :: exception :" + e);
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
