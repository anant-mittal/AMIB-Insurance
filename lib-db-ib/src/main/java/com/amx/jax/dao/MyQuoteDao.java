package com.amx.jax.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.MyQuoteModel;
import com.amx.jax.utility.Calc;
import oracle.jdbc.OracleTypes;

@Repository
public class MyQuoteDao
{
	String TAG = "com.amx.jax.dao.MyQuoteDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(MyQuoteDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	MetaData metaData;

	Connection connection;

	public ArrayList<MyQuoteModel> getUserQuote(BigDecimal customerSeqNum)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_MYQUOTES(?,?,?,?,?,?,?)}";//
		ArrayList<MyQuoteModel> activePolicyArray = new ArrayList<MyQuoteModel>();

		try
		{
			logger.info(TAG + " getUserQuote :: metaData :" + metaData.toString());
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, customerSeqNum);
			callableStatement.setBigDecimal(4, metaData.getLanguageId());
			callableStatement.registerOutParameter(5, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(7, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(5);
			while (rs.next())
			{
				MyQuoteModel myQuoteModel = new MyQuoteModel();
				myQuoteModel.setCountryId(rs.getBigDecimal(1));
				myQuoteModel.setCompCd(rs.getBigDecimal(2));
				logger.info(TAG + " getUserQuote :: getAppSeqNumber :" + rs.getBigDecimal(3));
				myQuoteModel.setAppSeqNumber(rs.getBigDecimal(3));
				myQuoteModel.setAppDate(DateFormats.uiFormattedDate(rs.getDate(4)));
				myQuoteModel.setAppType(rs.getString(5));
				myQuoteModel.setPolicyDuration(rs.getBigDecimal(6));
				myQuoteModel.setAppStatus(rs.getString(7));
				myQuoteModel.setStatus(rs.getString(8));
				myQuoteModel.setQuoteDate(DateFormats.uiFormattedDate(rs.getDate(9)));
				logger.info(TAG + " getUserQuote :: getQuoteSeqNumber :" + rs.getBigDecimal(10));
				myQuoteModel.setQuoteSeqNumber(rs.getBigDecimal(10));
				myQuoteModel.setVerNumber(rs.getBigDecimal(11));
				myQuoteModel.setCompanyCode(rs.getBigDecimal(12));
				myQuoteModel.setCompanyName(rs.getString(13));
				myQuoteModel.setCompanyShortCode(rs.getString(14));
				myQuoteModel.setMakeCode(rs.getString(15));
				myQuoteModel.setMakeDesc(rs.getString(16));
				myQuoteModel.setSubMakeCode(rs.getString(17));
				myQuoteModel.setSubMakeDesc(rs.getString(18));
				myQuoteModel.setModelYear(rs.getBigDecimal(19));
				myQuoteModel.setShapeCode(rs.getString(20));
				myQuoteModel.setShapeDesc(rs.getString(21));
				myQuoteModel.setColourCode(rs.getString(22));
				myQuoteModel.setColourDesc(rs.getString(23));
				myQuoteModel.setNumberOfPassenger(rs.getBigDecimal(24));
				myQuoteModel.setChassisNumber(rs.getString(25));
				myQuoteModel.setKtNumber(rs.getString(26));
				myQuoteModel.setVehicleConditionCode(rs.getString(27));
				myQuoteModel.setVehicleConditionDesc(rs.getString(28));
				myQuoteModel.setPurposeCode(rs.getString(29));
				myQuoteModel.setPurposeDesc(rs.getString(30));
				myQuoteModel.setFuelCode(rs.getString(31));
				myQuoteModel.setFuelDesc(rs.getString(32));
				myQuoteModel.setVehicleValue(rs.getBigDecimal(33));
				myQuoteModel.setBasicPremium(rs.getBigDecimal(34));
				myQuoteModel.setSupervisionFees(rs.getBigDecimal(35));
				myQuoteModel.setIssueFee(Calc.round(rs.getBigDecimal(36), metaData.getDecplc()));
				myQuoteModel.setDiscount(Calc.round(rs.getBigDecimal(37), metaData.getDecplc()));
				myQuoteModel.setAddCoveragePremium(rs.getBigDecimal(38));
				myQuoteModel.setNetAmount(Calc.round(rs.getBigDecimal(39), metaData.getDecplc()));
				myQuoteModel.setPolCondition(rs.getString(40));
				myQuoteModel.setVehicleType(rs.getString(41));
				myQuoteModel.setPaymentProcessError(rs.getString(42));
				activePolicyArray.add(myQuoteModel);
				
				logger.info(TAG + " getUserQuote :: myQuoteModel :" + myQuoteModel.toString());
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
