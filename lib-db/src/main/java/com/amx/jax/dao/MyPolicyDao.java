package com.amx.jax.dao;

import java.math.BigDecimal;
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
public class MyPolicyDao
{
	String TAG = "com.amx.jax.dao.ActivePolicyDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(MyPolicyDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	MetaData metaData;

	@Autowired
	VehicleSession vehicleSession;

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
				activePolicyModel.setInsCompanyPrefix(rs.getString(14));
				activePolicyModel.setDbcust(rs.getBigDecimal(15));
				activePolicyModel.setMakeCode(rs.getString(16));
				activePolicyModel.setMakeDesc(rs.getString(17));
				activePolicyModel.setSubMakeCode(rs.getString(18));
				activePolicyModel.setSubMakeDesc(rs.getString(19));
				activePolicyModel.setModelYear(rs.getBigDecimal(20));
				activePolicyModel.setShapeCode(rs.getString(21));
				activePolicyModel.setShapeDesc(rs.getString(22));
				activePolicyModel.setColourCode(rs.getString(23));
				activePolicyModel.setColourDesc(rs.getString(24));
				activePolicyModel.setNoPass(rs.getBigDecimal(25));
				activePolicyModel.setChassis(rs.getBigDecimal(26));
				activePolicyModel.setKtNumber(rs.getBigDecimal(27));
				activePolicyModel.setVehicleConditionCode(rs.getString(28));
				activePolicyModel.setVehicleConditionDesc(rs.getString(29));
				activePolicyModel.setPurposeCode(rs.getString(30));
				activePolicyModel.setPurposeDesc(rs.getString(31));
				activePolicyModel.setVehicleSrNumber(rs.getBigDecimal(32));
				activePolicyModel.setPolicyTypeCode(rs.getString(33));
				activePolicyModel.setPolicyTypeDesc(rs.getString(34));
				activePolicyModel.setPolicyNumber(rs.getString(35));
				activePolicyModel.setMaxInsuredAmount(rs.getBigDecimal(36));
				activePolicyModel.setStartDate(DateFormats.uiFormattedDate(rs.getDate(37)));
				activePolicyModel.setEndDate(DateFormats.uiFormattedDate(rs.getDate(38)));
				activePolicyModel.setSupervisionKey(rs.getBigDecimal(39));
				activePolicyModel.setIssueFee(rs.getBigDecimal(40));
				activePolicyModel.setPremium(rs.getBigDecimal(41));
				activePolicyModel.setDiscount(rs.getBigDecimal(42));
				activePolicyModel.setRenewalIndic(rs.getString(43));
				activePolicyModel.setFuelCode(rs.getString(44));
				activePolicyModel.setFuelDesc(rs.getString(45));
				if (null != rs.getString(43) && rs.getString(43).equalsIgnoreCase("N"))
				{
					activePolicyModel.setRenewableApplCheck("N");
				}
				else
				{
					activePolicyModel.setRenewableApplCheck(checkRenewableApplicable(rs.getBigDecimal(3)));
				}
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

	public String checkRenewableApplicable(BigDecimal oldDocNumber)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_CHECK_RENEW_APPLICABLE(?,?,?,?,?,?,?,?,?)}";
		String renewableApplCheck = null;

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setString(3, metaData.getUserType());
			callableStatement.setString(4, metaData.getCivilId());
			callableStatement.setBigDecimal(5, metaData.getCustomerSequenceNumber());
			callableStatement.setBigDecimal(6, oldDocNumber);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(8, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(9, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			renewableApplCheck = callableStatement.getString(7);

			logger.info(TAG + " checkRenewableApplicable :: oldDocNumber       :" + oldDocNumber);
			logger.info(TAG + " checkRenewableApplicable :: renewableApplCheck :" + renewableApplCheck);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return renewableApplCheck;
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