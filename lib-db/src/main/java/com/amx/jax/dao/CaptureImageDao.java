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
import com.amx.jax.models.ArrayResponseModel;
import com.amx.jax.models.CompanySetUp;
import com.amx.jax.models.DateFormats;
import com.amx.jax.models.ImageMandatoryResponse;
import com.amx.jax.models.IncompleteApplModel;
import com.amx.jax.models.MetaData;
import com.amx.jax.models.VehicleSession;
import org.springframework.beans.factory.annotation.Autowired;
import oracle.jdbc.OracleTypes;

@Repository
public class CaptureImageDao
{
	String TAG = "com.amx.jax.dao.RequestQuoteDao :: ";

	private static final Logger logger = LoggerFactory.getLogger(CaptureImageDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	MetaData metaData;
	
	@Autowired
	VehicleSession vehicleSession;

	Connection connection;
	
	public ArrayResponseModel getMandatoryImage()
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_ONLINE_DOCS(?,?,?,?,?,?)}";
		ArrayList<ImageMandatoryResponse> imageMandatoryArray = new ArrayList<ImageMandatoryResponse>();
		ArrayResponseModel arrayResponseModel = new ArrayResponseModel();
		
		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setBigDecimal(1, metaData.getCountryId());
			callableStatement.setBigDecimal(2, metaData.getCompCd());
			callableStatement.setBigDecimal(3, metaData.getLanguageId());
			callableStatement.registerOutParameter(4, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(5, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			ResultSet rs = (ResultSet) callableStatement.getObject(4);

			while (rs.next())
			{
				ImageMandatoryResponse imageMandatoryModel = new ImageMandatoryResponse();
				imageMandatoryModel.setDocType(rs.getString(1));
				imageMandatoryModel.setDeCode(rs.getString(2));
				imageMandatoryModel.setRequiredCheck(rs.getString(3));
				imageMandatoryModel.setDispOrder(rs.getString(4));
				imageMandatoryModel.setDocStatus(rs.getString(5));
				imageMandatoryArray.add(imageMandatoryModel);
			}
			arrayResponseModel.setDataArray(imageMandatoryArray);
			arrayResponseModel.setErrorCode(callableStatement.getString(4));
			arrayResponseModel.setErrorMessage(callableStatement.getString(5));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			CloseConnection(callableStatement, connection);
		}
		return arrayResponseModel;
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
