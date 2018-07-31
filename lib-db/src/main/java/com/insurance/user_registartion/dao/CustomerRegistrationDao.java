




package com.insurance.user_registartion.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.constants.ApiConstants;
import com.insurance.user_registartion.interfaces.ICustomerRegistration;
import com.insurance.user_registartion.model.CompanySetUp;
import com.insurance.user_registartion.model.CustomerPersonalDetail;

import oracle.jdbc.OracleTypes;

@Repository
public class CustomerRegistrationDao implements ICustomerRegistration
{
	String TAG = "com.insurance.user_registartion.dao :: CustomerRegistrationDao :: ";
	private static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationDao.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	Connection connection;

	public boolean isValidCivilId(String civilid)
	{
		logger.info(TAG + " isValidCivilId :: civilid :" + civilid);

		getConnection();
		CallableStatement callableStatement = null;
		String callFunction = "{ ? = call O_VALIDATE_CIVILID(?)}";

		try
		{
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
			callableStatement.setString(2, civilid);
			callableStatement.executeUpdate();
			String result = callableStatement.getString(1);
			logger.info(TAG + " isValidCivilId :: result :" + result);

			if (null == result)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public boolean isCivilIdExist(String civilid)
	{
		logger.info(TAG + " isCivilIdExist :: civilid :" + civilid);

		getConnection();
		CallableStatement callableStatement = null;
		String callFunction = "{ ? = call IRB_IF_ONLINE_USEREXIST(?,?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callFunction);
			callableStatement.registerOutParameter(1, java.sql.Types.VARCHAR);
			callableStatement.setInt(2, 1);
			callableStatement.setInt(3, 10);
			callableStatement.setString(4, "D");
			callableStatement.setString(5, civilid);
			callableStatement.executeUpdate();
			String result = callableStatement.getString(1);
			logger.info(TAG + " isCivilIdExist :: result :" + result);

			if (result.equalsIgnoreCase("Y"))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public boolean isValidMobileNumber(String mobileNumber)
	{
		logger.info(TAG + " isValidMobileNumber :: civilid :" + mobileNumber);

		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call O_VALIDATE_TELEPHONE(?,?,?)}";

		try
		{
			callableStatement = connection.prepareCall(callProcedure);
			callableStatement.setString(1, mobileNumber);
			callableStatement.setString(2, "M");
			callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();
			String result = callableStatement.getString(3);
			logger.info(TAG + " isValidMobileNumber :: result :" + result);

			if (null == result)
			{
				return true;
			}
			else
			{
				return false;
			}

		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public AmxApiResponse<CustomerPersonalDetail, Object> addNewCustomer(CustomerPersonalDetail customerPersonalDetail)
	{
		logger.info(TAG + " addNewCustomer :: customerRegistrationDetails :" + customerPersonalDetail.toString());

		AmxApiResponse<CustomerPersonalDetail, Object> resp = new AmxApiResponse<CustomerPersonalDetail, Object>();
		
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_REGISTER_USER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

		int countyId = customerPersonalDetail.getCountryId();
		int compCd = customerPersonalDetail.getCompCd();
		String userType = customerPersonalDetail.getUserType();
		String userCivilId = customerPersonalDetail.getCivilId();
		String userPassword = customerPersonalDetail.getPassword();
		String mobileNumber = customerPersonalDetail.getMobile();
		String emailId = customerPersonalDetail.getEmail();
		String mobVerificationCode = customerPersonalDetail.getMobVerificationCode();
		String emailVerificationCode = customerPersonalDetail.getEmailVerificationCode();
		String deviceType = customerPersonalDetail.getDeviceType();
		String createdDeviceId = customerPersonalDetail.getCreatedDeviceId();
		String createdBy = customerPersonalDetail.getCivilId();

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, countyId);
			callableStatement.setInt(2, compCd);
			callableStatement.setString(3, userType);
			callableStatement.setString(4, userCivilId);
			callableStatement.setString(5, userPassword);
			callableStatement.setString(6, mobileNumber);
			callableStatement.setString(7, mobVerificationCode);
			callableStatement.setString(8, emailId);
			callableStatement.setString(9, emailVerificationCode);
			callableStatement.setInt(10, 0);
			callableStatement.setString(11, deviceType);
			callableStatement.setDate(12, getCurrentDate());
			callableStatement.setString(13, createdDeviceId);
			callableStatement.setString(14, createdBy);
			
			callableStatement.registerOutParameter(15, java.sql.Types.INTEGER);
			callableStatement.registerOutParameter(16, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(17, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			int userSequenceNumber = callableStatement.getInt(15);
			String recd = callableStatement.getString(16);
			String error = callableStatement.getString(17);

			customerPersonalDetail.setUserSeqNumber(userSequenceNumber);
			
			if(error == null)
			{
				resp.setData(customerPersonalDetail);
				resp.setError(error);
				resp.setMessage("User Registration SuccessFull");
				resp.setStatus(ApiConstants.Success);
			}
			else
			{
				resp.setData(null);
				resp.setError(error);
				resp.setMessage("User Registration Fail Kindly Try After Some Time...");
				resp.setStatus(ApiConstants.Failure);
			}
			
			logger.info(TAG + " addNewCustomer :: userSequenceNumber :" + userSequenceNumber);
			logger.info(TAG + " addNewCustomer :: recd :" + recd);
			logger.info(TAG + " addNewCustomer :: error :" + error);

		}
		catch (Exception e)
		{
			resp.setData(null);
			resp.setStatus(ApiConstants.Failure);
			resp.setMessage("User Registration Fail Some Error Occured On Server Kindly Try After Some Time...");
			resp.setException(e.toString());
			e.printStackTrace();
		}
		return resp;
	}

	public ArrayList getCompanySetUp(int langId)
	{
		getConnection();
		CallableStatement callableStatement = null;
		String callProcedure = "{call IRB_GET_COMPANY_SETUP(?,?,?,?)}";
		ArrayList<CompanySetUp> companySetUpArray = new ArrayList<CompanySetUp>();

		logger.info(TAG + " getCompanySetUp ::");

		try
		{
			callableStatement = connection.prepareCall(callProcedure);

			callableStatement.setInt(1, langId);
			callableStatement.registerOutParameter(2, OracleTypes.CURSOR);
			callableStatement.registerOutParameter(3, java.sql.Types.VARCHAR);
			callableStatement.registerOutParameter(4, java.sql.Types.VARCHAR);
			callableStatement.executeUpdate();

			ResultSet rs = (ResultSet) callableStatement.getObject(2);

			while (rs.next())
			{
				CompanySetUp companySetUp = new CompanySetUp();

				companySetUp.setCntryCd(rs.getInt(1));
				companySetUp.setCompCd(rs.getInt(2));
				companySetUp.setCompanyName(rs.getString(3));
				companySetUp.setCbox(rs.getString(4));
				companySetUp.setCpo(rs.getString(5));
				companySetUp.setTeli(rs.getString(6));
				companySetUp.setTeli2(rs.getString(7));
				companySetUp.setFax(rs.getString(8));
				companySetUp.setEmail(rs.getString(9));
				companySetUp.setRegNumber(rs.getString(10));
				companySetUp.setMainAct(rs.getInt(11));
				companySetUp.setMainActCenter(rs.getInt(12));
				companySetUp.setHeading(rs.getString(13));
				companySetUp.setDecplc(rs.getInt(14));
				companySetUp.setCurrency(rs.getString(15));
				companySetUp.setLangId(rs.getInt(16));
				companySetUp.setAppName(rs.getString(17));
				companySetUp.setSmsSenderId(rs.getString(18));

				companySetUpArray.add(companySetUp);
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
		return companySetUpArray;
	}

	private static java.sql.Date getCurrentDate()
	{
		java.util.Date today = new java.util.Date();
		return new java.sql.Date(today.getTime());
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
