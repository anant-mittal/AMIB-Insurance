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
import com.amx.jax.models.CustomerProfileDetailModel;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.IncompleteApplModel;
import com.amx.jax.models.IncompleteApplResponse;
import com.amx.jax.models.MetaData;
import org.springframework.beans.factory.annotation.Autowired;
import oracle.jdbc.OracleTypes;

@Repository
public class ActivePolicyDao
{
	String TAG = "com.amx.jax.dao.ActivePolicyDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(ActivePolicyDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	MetaData metaData;

	Connection connection;

	public ArrayList<ActivePolicyModel> getUserActivePolicy()
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_ACTIVE_POLICIES(?,?,?,?,?,?,?)}";
		ArrayList<ActivePolicyModel> activePolicyArray = new ArrayList<ActivePolicyModel>();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, metaData.getUserAmibCustRef());
			callableStatement.setBigDecimal(4, metaData.getLanguageId());
			callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			
			ResultSet rs = (ResultSet) callableStatement.getObject(5);
			
			while (rs.next())
			{
				ActivePolicyModel activePolicyModel = new ActivePolicyModel();
				activePolicyModel.setCountryId(rs.getBigDecimal(1));
				activePolicyModel.setCompCd(rs.getBigDecimal(2));
				activePolicyModel.setDocNumber(rs.getBigDecimal(3));
				activePolicyModel.setDocDate(DateFormats.uiFormattedDate(rs.getDate(4)));
				activePolicyModel.setFinance(rs.getBigDecimal(5));
				activePolicyModel.setShowRoom(rs.getBigDecimal(6));
				activePolicyModel.setSalesman(rs.getBigDecimal(7));
				activePolicyModel.setUserAmibCustRef(rs.getBigDecimal(8));
				activePolicyModel.setInsuredEnglishName(rs.getString(9));
				activePolicyModel.setInsuredArabicName(rs.getString(10));
				activePolicyModel.setIdNumber(rs.getBigDecimal(11));
				activePolicyModel.setInsCompanyCode(rs.getBigDecimal(12));
				activePolicyModel.setInsCompanyDesc(rs.getString(13));
				activePolicyModel.setDbcust(rs.getBigDecimal(14));
				activePolicyModel.setMakeCode(rs.getString(15));
				activePolicyModel.setMakeDesc(rs.getString(16));
				activePolicyModel.setModelCode(rs.getString(17));
				activePolicyModel.setModelDesc(rs.getString(18));
				activePolicyModel.setModelYear(rs.getBigDecimal(19));
				activePolicyModel.setShapeCode(rs.getString(20));
				activePolicyModel.setShapeDesc(rs.getString(21));
				activePolicyModel.setColourCode(rs.getString(22));
				activePolicyModel.setColourDesc(rs.getString(23));
				activePolicyModel.setNoPass(rs.getBigDecimal(24));
				activePolicyModel.setChassis(rs.getBigDecimal(25));
				activePolicyModel.setKtNumber(rs.getBigDecimal(26));
				activePolicyModel.setVehicleConditionCode(rs.getString(27));
				activePolicyModel.setVehicleConditionDesc(rs.getString(28));
				activePolicyModel.setPurposeCode(rs.getString(29));
				activePolicyModel.setPurposeDesc(rs.getString(30));
				activePolicyModel.setVehicleSrNumber(rs.getBigDecimal(31));
				activePolicyModel.setPolicyTypeCode(rs.getString(32));
				activePolicyModel.setPolicyTypeDesc(rs.getString(33));
				activePolicyModel.setPolicyNumber(rs.getString(34));
				activePolicyModel.setMaxInsuredAmount(rs.getBigDecimal(35));
				activePolicyModel.setStartDate(DateFormats.uiFormattedDate(rs.getDate(36)));
				activePolicyModel.setEndDate(DateFormats.uiFormattedDate(rs.getDate(37)));
				activePolicyModel.setSupervisionKey(rs.getBigDecimal(38));
				activePolicyModel.setIssueFee(rs.getBigDecimal(39));
				activePolicyModel.setPremium(rs.getBigDecimal(40));
				activePolicyModel.setDiscount(rs.getBigDecimal(41));
				activePolicyModel.setRenewalIndic(rs.getString(42));
				activePolicyArray.add(activePolicyModel);
				logger.info(TAG + " getUserActivePolicy :: activePolicyModel :" + activePolicyModel.toString());
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
